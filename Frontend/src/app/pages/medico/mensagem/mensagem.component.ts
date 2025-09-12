import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

interface Paciente {
  id: number;
  name: string;
  avatar?: string;
}

interface Mensagem {
  id: string;
  senderId: string;
  senderType: string;
  senderName: string;
  recipientId: string;
  recipientType: string;
  recipientName: string;
  text: string;
  date: string;
  read: boolean;
}

interface PacienteComMensagens {
  paciente: Paciente;
  mensagens: Mensagem[];
  ultimaMensagem?: Mensagem;
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
  pacienteSelecionado: PacienteComMensagens | null = null;
  novaMensagem = '';
  loading = false;
  shouldScrollToBottom = false;

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
  // Busca id do médico logado do localStorage
  const medicoLogado = localStorage.getItem('medicoId');
  this.medicoId = medicoLogado ? Number(medicoLogado) : 1;
  this.carregarPacientesEMensagens();
  }

  async carregarPacientesEMensagens() {
    this.loading = true;
    
    try {
      // 1. Buscar pacientes autorizados para este médico
      const pacientesResponse = await this.http.get<any>(`http://localhost:8080/api/medic/${this.medicoId}/patients`).toPromise();
      const pacientesAutorizados: Paciente[] = pacientesResponse.data || pacientesResponse || [];

      // 2. Buscar todas as mensagens
      const mensagensResponse = await this.http.get<any>('http://localhost:8080/api/messages', { headers: { 'Content-Type': 'application/json' } }).toPromise();

      const todasMensagens: Mensagem[] = mensagensResponse.data || [];

      // Filtra apenas mensagens onde o médico logado está envolvido
      const idMedico = `${this.medicoId}`;
      const mensagensDoMedico = todasMensagens.filter(msg =>
        (msg.senderId === idMedico && msg.senderType === 'MEDIC') ||
        (msg.recipientId === idMedico && msg.recipientType === 'MEDIC')
      );

      // 3. Agrupar mensagens por paciente autorizado
      this.pacientesComMensagens = pacientesAutorizados.map(paciente => {
        const idPaciente = paciente.id.toString();
        const msgsPaciente = mensagensDoMedico.filter(msg =>
          (msg.senderId === idPaciente && msg.senderType === 'PATIENT') ||
          (msg.recipientId === idPaciente && msg.recipientType === 'PATIENT')
        );
        msgsPaciente.sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
        return {
          paciente,
          mensagens: msgsPaciente,
          ultimaMensagem: msgsPaciente[msgsPaciente.length - 1]
        };
      });
      


    } catch (error) {
      console.error('Erro ao carregar dados:', error);
    } finally {
      this.loading = false;
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
    
    // Depois fazer as chamadas para o backend
    for (const mensagem of mensagensNaoLidas) {
      try {
        await this.http.patch(`http://localhost:8080/api/messages/${mensagem.id}`, {}, { headers: { 'Content-Type': 'application/json' } }).toPromise();
      } catch (error) {
        console.error('Erro ao marcar mensagem como lida:', error);
        // Se der erro, reverter o estado local
        mensagem.read = false;
      }
    }
  }

  async enviarMensagem() {
    if (!this.novaMensagem.trim() || !this.pacienteSelecionado) return;

    // Recupera nome do médico logado do localStorage
    const nomeMedico = localStorage.getItem('medicoNome') || `medico_${this.medicoId}`;
    const mensagem = {
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
    


    try {
      await this.http.post('http://localhost:8080/api/messages', mensagem, { headers: { 'Content-Type': 'application/json' } }).toPromise();
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
      console.error('Erro ao enviar mensagem:', error);
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