import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MensagensService } from '../../../services/mensagens/mensagem.service';
import { Message, Conversation } from '../../../models/message.interface';

interface Paciente {
  id: number;
  name: string;
  avatar?: string;
}

interface PacienteComMensagens {
  paciente: Paciente;
  mensagens: Message[];
  ultimaMensagem?: Message;
}

@Component({
  selector: 'app-mensagem',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './mensagem.component.html',
  styleUrls: ['./mensagem.component.css']
})
export class MensagemComponent implements OnInit, AfterViewChecked {
  @ViewChild('messagesContainer') messagesContainer!: ElementRef;
  
  medicoId = 1; // ID do médico logado
  pacientesComMensagens: PacienteComMensagens[] = [];
  conversas: Conversation[] = [];
  pacienteSelecionado: PacienteComMensagens | null = null;
  novaMensagem = '';
  loading = false;
  shouldScrollToBottom = false;

  constructor(
    private http: HttpClient, 
    private cdr: ChangeDetectorRef,
    private mensagensService: MensagensService
  ) {}

  ngOnInit() {
    // Busca id do médico logado do localStorage
    const medicoLogado = localStorage.getItem('medicoId') || localStorage.getItem('userId');
    this.medicoId = medicoLogado ? Number(medicoLogado) : 1;
    
    this.carregarPacientesEMensagens();
  }

  async carregarPacientesEMensagens() {
    this.loading = true;
    
    try {
      // 1. Buscar pacientes autorizados para este médico
      const pacientesResponse = await this.http.get<any>(`http://localhost:8080/api/medic/${this.medicoId}/authorized-patients`).toPromise();
      const pacientesAutorizados: Paciente[] = pacientesResponse.data || [];
      
      // 2. Buscar conversas do médico usando o novo service
      this.mensagensService.getConversationsForUser(`${this.medicoId}`, 'MEDIC').subscribe({
        next: (conversas) => {
          this.conversas = conversas;
          
          // 3. Criar lista combinada: pacientes autorizados com suas conversas
          this.pacientesComMensagens = pacientesAutorizados.map(paciente => {
            const conversa = conversas.find(c => 
              c.participantId === `${paciente.id}` && c.participantType === 'PATIENT'
            );
            
            return {
              paciente: {
                id: paciente.id,
                name: paciente.name,
                avatar: paciente.avatar || 'https://cdn-icons-png.flaticon.com/512/921/921347.png'
              },
              mensagens: conversa ? conversa.messages : [],
              ultimaMensagem: conversa ? conversa.lastMessage : undefined
            };
          });

          // 4. Adicionar conversas que não estão na lista de pacientes autorizados (caso existam)
          conversas.forEach(conversa => {
            const jaExiste = this.pacientesComMensagens.some(p => 
              p.paciente.id === parseInt(conversa.participantId)
            );
            
            if (!jaExiste && conversa.participantType === 'PATIENT') {
              this.pacientesComMensagens.push({
                paciente: {
                  id: parseInt(conversa.participantId),
                  name: conversa.participantName,
                  avatar: 'https://cdn-icons-png.flaticon.com/512/921/921347.png'
                },
                mensagens: conversa.messages,
                ultimaMensagem: conversa.lastMessage
              });
            }
          });

          // 5. Ordenar por última mensagem (mais recente primeiro)
          this.pacientesComMensagens.sort((a, b) => {
            const dateA = a.ultimaMensagem ? new Date(a.ultimaMensagem.date).getTime() : 0;
            const dateB = b.ultimaMensagem ? new Date(b.ultimaMensagem.date).getTime() : 0;
            return dateB - dateA;
          });

          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.error('Erro ao carregar conversas:', error);
          // Se der erro na busca de conversas, pelo menos mostra os pacientes autorizados
          this.pacientesComMensagens = pacientesAutorizados.map(paciente => ({
            paciente: {
              id: paciente.id,
              name: paciente.name,
              avatar: paciente.avatar || 'https://cdn-icons-png.flaticon.com/512/921/921347.png'
            },
            mensagens: [],
            ultimaMensagem: undefined
          }));
          this.loading = false;
          this.cdr.detectChanges();
        }
      });

    } catch (error: any) {
      console.error('Erro ao carregar pacientes:', error);
      this.pacientesComMensagens = [];
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  selecionarPaciente(pacienteComMensagens: PacienteComMensagens) {
    this.pacienteSelecionado = pacienteComMensagens;
    
    
    // Marcar todas as mensagens não lidas como lidas IMEDIATAMENTE
    const mensagensNaoLidas = pacienteComMensagens.mensagens.filter(m => {
      const naoLida = !m.read;
      const ehDestinatario = m.recipientId === `${this.medicoId}`;
      return naoLida && ehDestinatario;
    });
    
    // Marcar localmente para UI imediata
    mensagensNaoLidas.forEach(m => m.read = true);
    
    // Forçar atualização da UI
    this.pacientesComMensagens = [...this.pacientesComMensagens];
    this.cdr.detectChanges();
    
    // Marcar no backend de forma assíncrona
    this.marcarMensagensComoLidas(pacienteComMensagens);
    
    // Fazer scroll para baixo após selecionar
    setTimeout(() => {
      this.shouldScrollToBottom = true;
    }, 50);
  }

  ngAfterViewChecked() {
    if (this.shouldScrollToBottom) {
      this.shouldScrollToBottom = false;
      setTimeout(() => {
        this.scrollToBottom();
      }, 100);
    }
  }

  private scrollToBottom(): void {
    try {
      if (this.messagesContainer && this.messagesContainer.nativeElement) {
        const element = this.messagesContainer.nativeElement;
        // Força o scroll para o final
        element.scrollTop = element.scrollHeight + 1000;
        
        // Adiciona um segundo timeout para garantir
        setTimeout(() => {
          element.scrollTop = element.scrollHeight + 1000;
        }, 50);
      }
    } catch (err) {
      console.error('Erro ao fazer scroll:', err);
    }
  }

  private async marcarMensagensComoLidas(pacienteComMensagens: PacienteComMensagens) {
    const mensagensNaoLidas = pacienteComMensagens.mensagens.filter(m => 
      !m.read && m.recipientId === `${this.medicoId}`
    );
    
    if (mensagensNaoLidas.length === 0) return;
    
    // Marcar como lidas localmente primeiro para atualização imediata da UI
    mensagensNaoLidas.forEach(m => {
      m.read = true;
      console.log('Médico: Marcando mensagem como lida localmente:', m.id);
    });
    
    // Forçar detecção de mudança
    this.pacientesComMensagens = [...this.pacientesComMensagens];
    this.cdr.detectChanges();
    
    // Usar o service para marcar como lidas no backend
    const mensagemIds = mensagensNaoLidas.map(m => m.id);
    this.mensagensService.marcarMensagensComoLidas(mensagemIds).subscribe({
      next: () => {
        console.log('Mensagens marcadas como lidas no backend');
      },
      error: (error) => {
        console.error('Erro ao marcar mensagens como lidas:', error);
        // Se der erro, reverter o estado local
        mensagensNaoLidas.forEach(m => m.read = false);
        this.cdr.detectChanges();
      }
    });
  }

  async enviarMensagem() {
    if (!this.novaMensagem.trim() || !this.pacienteSelecionado) return;

    // Recupera nome do médico logado do localStorage
    const nomeMedico = localStorage.getItem('medicoNome') || `Dr. Médico ${this.medicoId}`;
    const mensagem: Message = {
      id: '', // Será gerado pelo backend
      senderId: `${this.medicoId}`,
      senderType: 'MEDIC',
      senderName: nomeMedico,
      recipientId: this.pacienteSelecionado.paciente.id.toString(),
      recipientType: 'PATIENT', 
      recipientName: this.pacienteSelecionado.paciente.name,
      text: this.novaMensagem.trim(),
      date: new Date().toISOString(),
      read: false
    };

    console.log('🩺 MÉDICO enviando mensagem:', mensagem);

    try {
      const response = await this.mensagensService.enviarMensagem(mensagem).toPromise();
      console.log('✅ Mensagem enviada com sucesso:', response);
      this.novaMensagem = '';
      
      // Recarregar mensagens
      await this.carregarPacientesEMensagens();
      
      // Manter paciente selecionado
      const pacienteAtualizado = this.pacientesComMensagens.find(p => 
        p.paciente.id === this.pacienteSelecionado?.paciente.id
      );
      if (pacienteAtualizado) {
        this.pacienteSelecionado = pacienteAtualizado;
        // Scroll para baixo após enviar mensagem
        setTimeout(() => {
          this.shouldScrollToBottom = true;
        }, 100);
      }
    } catch (error) {
      console.error('❌ Erro ao enviar mensagem (MÉDICO):', error);
    }
  }

  getMedicoId(): string {
    return `${this.medicoId}`;
  }

  temMensagensNaoLidas(item: PacienteComMensagens): boolean {
    return item.mensagens.some(m => !m.read && m.recipientId === `${this.medicoId}`);
  }

  contarMensagensNaoLidas(item: PacienteComMensagens): number {
    return item.mensagens.filter(m => !m.read && m.recipientId === `${this.medicoId}`).length;
  }
}