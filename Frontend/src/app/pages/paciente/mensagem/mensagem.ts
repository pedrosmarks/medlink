import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MensagemService } from '../../../mensagem.service';
import { MedicosService } from '../../../services/medicos/medicos.service';
import { MensagensService } from '../../../services/mensagens/mensagem.service';
import { Message, Conversation } from '../../../models/message.interface';

export interface Medico {
  id: number;
  name: string;
  specialty?: string;
  crm?: string;
}

export interface ConversaComMedico {
  medico: Medico;
  mensagens: Message[];
  ultimaMensagem?: Message;
}

@Component({
  selector: 'app-mensagem',
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './mensagem.html',
  styleUrls: ['./mensagem.css']
})
export class Mensagem implements OnInit, AfterViewChecked {
  @ViewChild('messagesContainer') messagesContainer!: ElementRef;
  
  mensagens: Message[] = [];
  medicosComMensagens: ConversaComMedico[] = [];
  conversas: Conversation[] = [];
  novaMensagemConteudo = '';
  senderId = '';
  senderType = '';
  receiverId = '';
  receiverType = '';
  conversaSelecionada: ConversaComMedico | null = null;
  loading = false;
  shouldScrollToBottom = false;

  constructor(
    private mensagemService: MensagemService, 
    private http: HttpClient, 
    private cdr: ChangeDetectorRef, 
    private medicosService: MedicosService,
    private mensagensService: MensagensService
  ) {}

  ngOnInit() {
    // Busca id do paciente logado do localStorage
    const pacienteLogado = localStorage.getItem('userId');
    this.senderId = pacienteLogado ? pacienteLogado : '';
    this.senderType = 'paciente';
    this.carregarMedicosEMensagens();
  }

  async carregarMedicosEMensagens() {
    this.loading = true;
    
    try {
      // 1. Buscar médicos autorizados para este paciente usando o endpoint correto
      const medicosResponse = await this.http.get<any>(`http://localhost:8080/api/patients/${this.senderId}/authorized-doctors`).toPromise();
      const medicosAutorizados: Medico[] = medicosResponse.data || [];
      
      // 2. Buscar conversas do paciente usando o novo service
      this.mensagensService.getConversationsForUser(this.senderId, 'PATIENT').subscribe({
        next: (conversas) => {
          this.conversas = conversas;
          
          // 3. Criar lista combinada: médicos autorizados com suas conversas
          this.medicosComMensagens = medicosAutorizados.map(medico => {
            const conversa = conversas.find(c => 
              c.participantId === `${medico.id}` && c.participantType === 'MEDIC'
            );
            
            return {
              medico: {
                id: medico.id,
                name: medico.name,
                specialty: medico.specialty,
                crm: medico.crm
              },
              mensagens: conversa ? conversa.messages : [],
              ultimaMensagem: conversa ? conversa.lastMessage : undefined
            };
          });

          // 4. Adicionar conversas que não estão na lista de médicos autorizados (caso existam)
          conversas.forEach(conversa => {
            const jaExiste = this.medicosComMensagens.some(m => 
              m.medico.id === parseInt(conversa.participantId)
            );
            
            if (!jaExiste && conversa.participantType === 'MEDIC') {
              this.medicosComMensagens.push({
                medico: {
                  id: parseInt(conversa.participantId),
                  name: conversa.participantName,
                  specialty: 'Especialidade não informada',
                  crm: 'CRM não informado'
                },
                mensagens: conversa.messages,
                ultimaMensagem: conversa.lastMessage
              });
            }
          });

          // 5. Ordenar por última mensagem (mais recente primeiro)
          this.medicosComMensagens.sort((a, b) => {
            const dateA = a.ultimaMensagem ? new Date(a.ultimaMensagem.date).getTime() : 0;
            const dateB = b.ultimaMensagem ? new Date(b.ultimaMensagem.date).getTime() : 0;
            return dateB - dateA;
          });

          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.error('Erro ao carregar conversas:', error);
          // Se der erro na busca de conversas, pelo menos mostra os médicos autorizados
          this.medicosComMensagens = medicosAutorizados.map(medico => ({
            medico: {
              id: medico.id,
              name: medico.name,
              specialty: medico.specialty,
              crm: medico.crm
            },
            mensagens: [],
            ultimaMensagem: undefined
          }));
          this.loading = false;
          this.cdr.detectChanges();
        }
      });

    } catch (error: any) {
      console.error('Erro ao carregar médicos:', error);
      this.medicosComMensagens = [];
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  selecionarConversa(conv: ConversaComMedico) {
    this.conversaSelecionada = conv;
    // Preenche receiverId/receiverType para envio
    this.receiverId = `${conv.medico.id}`;
    this.receiverType = 'MEDIC';
    
    
    // Marcar todas as mensagens não lidas como lidas IMEDIATAMENTE
    const mensagensNaoLidas = conv.mensagens.filter(m => {
      const naoLida = !m.read;
      const ehDestinatario = m.recipientId === this.senderId && m.recipientType === 'PATIENT';
      return naoLida && ehDestinatario;
    });
    
    // Marcar localmente para UI imediata
    mensagensNaoLidas.forEach(m => m.read = true);
    
    // Forçar atualização da UI
    this.medicosComMensagens = [...this.medicosComMensagens];
    this.cdr.detectChanges();
    
    // Marcar no backend de forma assíncrona
    this.marcarMensagensComoLidas(conv);
    
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

  private async marcarMensagensComoLidas(conv: ConversaComMedico) {
    // Encontrar mensagens não lidas onde o paciente é destinatário
    const mensagensNaoLidas = conv.mensagens.filter(m => {
      return !m.read && m.recipientId === this.senderId && m.recipientType === 'PATIENT';
    });
    
    if (mensagensNaoLidas.length === 0) return;

    // Marcar como lidas localmente primeiro
    mensagensNaoLidas.forEach(m => m.read = true);
    this.medicosComMensagens = [...this.medicosComMensagens];
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
    if (!this.novaMensagemConteudo || !this.senderId || !this.senderType || !this.receiverId || !this.receiverType) return;
    
    // Recupera nome do médico selecionado
    const nomeMedico = this.conversaSelecionada?.medico.name || `Dr. ${this.receiverId}`;
    // Recupera nome do paciente logado do localStorage
    const nomePaciente = localStorage.getItem('pacienteNome') || localStorage.getItem('userName') || `Paciente ${this.senderId}`;
    
    const msg: Message = {
      id: '', // Será gerado pelo backend
      senderId: this.senderId,
      senderType: 'PATIENT',
      senderName: nomePaciente,
      recipientId: this.receiverId,
      recipientType: 'MEDIC',
      recipientName: nomeMedico,
      text: this.novaMensagemConteudo.trim(),
      date: new Date().toISOString(),
      read: false
    };
    
    console.log('🤒 PACIENTE enviando mensagem:', msg);
    
    try {
      const response = await this.mensagensService.enviarMensagem(msg).toPromise();
      console.log('✅ Mensagem enviada com sucesso:', response);
      this.novaMensagemConteudo = '';
      
      // Recarregar mensagens
      await this.carregarMedicosEMensagens();
      
      // Manter médico selecionado
      const medicoAtualizado = this.medicosComMensagens.find(m => 
        m.medico.id === this.conversaSelecionada?.medico.id
      );
      if (medicoAtualizado) {
        this.conversaSelecionada = medicoAtualizado;
        // Scroll para baixo após enviar mensagem
        setTimeout(() => {
          this.shouldScrollToBottom = true;
        }, 100);
      }
    } catch (error) {
      console.error('❌ Erro ao enviar mensagem (PACIENTE):', error);
    }
  }

  async marcarComoLida(id: string) {
    try {
      await this.mensagensService.marcarComoLida(id).toPromise();
      await this.carregarMedicosEMensagens();
    } catch (error) {
      console.error('Erro ao marcar como lida:', error);
    }
  }

  getPacienteId(): string {
    return this.senderId;
  }

  temMensagensNaoLidas(conv: ConversaComMedico): boolean {
    return conv.mensagens.some(m => {
      const naoLida = !m.read;
      const ehDestinatario = m.recipientId === this.senderId && m.recipientType === 'PATIENT';
      return naoLida && ehDestinatario;
    });
  }

  contarMensagensNaoLidas(conv: ConversaComMedico): number {
    const count = conv.mensagens.filter(m => {
      const naoLida = !m.read;
      const ehDestinatario = m.recipientId === this.senderId && m.recipientType === 'PATIENT';
      return naoLida && ehDestinatario;
    }).length;
    return count;
  }
} 
