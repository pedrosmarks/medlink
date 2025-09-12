import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MensagemService } from '../../../mensagem.service';
import { MedicosService } from '../../../services/medicos/medicos.service';

export interface Medico {
  id: number;
  name: string;
  specialty?: string;
  crm?: string;
}

export interface MensagemBackend {
  id?: string;
  senderId: string;
  senderType: string;
  receiverId: string;
  receiverType: string;
  conteudo: string;
  lida?: boolean;
  dataEnvio?: string;
}

export interface ConversaComMedico {
  medico: Medico;
  mensagens: any[];
  ultimaMensagem?: any;
}

@Component({
  selector: 'app-mensagem',
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './mensagem.html',
  styleUrls: ['./mensagem.css']
})
export class Mensagem implements OnInit, AfterViewChecked {
  @ViewChild('messagesContainer') messagesContainer!: ElementRef;
  
  mensagens: any[] = [];
  medicosComMensagens: ConversaComMedico[] = [];
  novaMensagemConteudo = '';
  senderId = '';
  senderType = '';
  receiverId = '';
  receiverType = '';
  conversaSelecionada: ConversaComMedico | null = null;
  loading = false;
  shouldScrollToBottom = false;

  constructor(private mensagemService: MensagemService, private http: HttpClient, private cdr: ChangeDetectorRef, private medicosService: MedicosService) {}

  ngOnInit() {
    // Busca id do paciente logado do localStorage
    const pacienteLogado = localStorage.getItem('pacienteId');
    this.senderId = pacienteLogado ? pacienteLogado : '';
    this.senderType = 'paciente';
    this.carregarMedicosEMensagens();
  }

  async carregarMedicosEMensagens() {
    this.loading = true;
    
    try {
  // 1. Buscar médicos autorizados para este paciente (objetos completos) via serviço
  const medicosAutorizados: Medico[] = (await this.medicosService.getMedicosAutorizadosCompletos(this.senderId).toPromise().catch(() => [])) || [];

      // 2. Buscar todas as mensagens
      const mensagensResponse = await this.http.get<any>('http://localhost:8080/api/messages', { headers: { 'Content-Type': 'application/json' } }).toPromise();
      console.log('Response completa:', mensagensResponse);
      console.log('Mensagens:', mensagensResponse.data);
      const todasMensagens: any[] = mensagensResponse.data || [];

      // Filtra apenas mensagens onde o paciente logado está envolvido
      const idPaciente = this.senderId;
      const mensagensDoPaciente = todasMensagens.filter(msg =>
        (msg.senderId === idPaciente && msg.senderType === 'PATIENT') ||
        (msg.recipientId === idPaciente && msg.recipientType === 'PATIENT')
      );

      // 3. Agrupar mensagens por médico autorizado
      this.medicosComMensagens = medicosAutorizados.map(medico => {
        const idMedico = `${medico.id}`;
        const msgsMedico = mensagensDoPaciente.filter(msg =>
          (msg.senderId === idMedico && msg.senderType === 'MEDIC') ||
          (msg.recipientId === idMedico && msg.recipientType === 'MEDIC')
        );
        msgsMedico.sort((a, b) => new Date(a.date || a.dataEnvio).getTime() - new Date(b.date || b.dataEnvio).getTime());
        return {
          medico,
          mensagens: msgsMedico,
          ultimaMensagem: msgsMedico[msgsMedico.length - 1]
        };
      });
      
      console.log('Estado atualizado - medicosComMensagens:', this.medicosComMensagens);
      console.log('Total de médicos com mensagens:', this.medicosComMensagens.length);

    } catch (error) {
      console.error('Erro ao carregar dados:', error);
    } finally {
      this.loading = false;
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
    // Encontrar mensagens que já foram marcadas como lidas localmente mas precisam ser sincronizadas
    const mensagensParaSincronizar = conv.mensagens.filter(m => {
      return m.read && m.recipientId === this.senderId && m.recipientType === 'PATIENT';
    });
    
    // Sincronizar com o backend
    for (const mensagem of mensagensParaSincronizar) {
      try {
        await this.http.patch(`http://localhost:8080/api/messages/${mensagem.id}`, {}, { headers: { 'Content-Type': 'application/json' } }).toPromise();
      } catch (error) {
        console.error('Erro ao sincronizar mensagem com backend:', error);
      }
    }
  }

  async enviarMensagem() {
    if (!this.novaMensagemConteudo || !this.senderId || !this.senderType || !this.receiverId || !this.receiverType) return;
    
    // Recupera nome do médico selecionado
    const nomeMedico = this.conversaSelecionada?.medico.name || this.receiverId;
    // Recupera nome do paciente logado do localStorage
    const nomePaciente = localStorage.getItem('pacienteNome') || this.senderId;
    
    const msg = {
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
    
    try {
      const result = await this.http.post('http://localhost:8080/api/messages', msg, { headers: { 'Content-Type': 'application/json' } }).toPromise();
      console.log('Mensagem enviada:', result);
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
      console.error('Erro ao enviar mensagem:', error);
    }
  }

  async marcarComoLida(id: string) {
    try {
      await this.http.patch(`http://localhost:8080/api/messages/${id}`, {}, { headers: { 'Content-Type': 'application/json' } }).toPromise();
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
