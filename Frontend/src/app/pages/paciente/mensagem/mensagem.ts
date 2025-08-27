import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MensagemService } from '../../../mensagem.service';

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

@Component({
  selector: 'app-mensagem',
  imports: [CommonModule, FormsModule],
  templateUrl: './mensagem.html',
  styleUrls: ['./mensagem.css']
})
export class Mensagem implements OnInit {
  mensagens: any[] = [];
  conversas: any[] = [];
  novaMensagemConteudo = '';
  senderId = '';
  senderType = '';
  receiverId = '';
  receiverType = '';
  conversaSelecionada: any = null;

  constructor(private mensagemService: MensagemService) {}

  ngOnInit() {
  // Busca id do paciente logado do localStorage
  const pacienteLogado = localStorage.getItem('pacienteId');
  this.senderId = pacienteLogado ? pacienteLogado : '';
  this.senderType = 'paciente';
  this.loadAllMessages();
  }

  loadAllMessages() {
    this.mensagemService.getAll().subscribe(res => {
      this.mensagens = res.data || [];
      this.agruparConversas();
    }, err => {
      console.error('Erro ao carregar mensagens:', err);
    });
  }

  agruparConversas() {
    // Agrupa mensagens por contato (médico)
    const grupos: { [key: string]: any[] } = {};
    for (const msg of this.mensagens) {
      // Só pega mensagens onde o paciente logado está envolvido
      if ((msg.remetenteId === this.senderId && msg.remetenteTipo === 'paciente') ||
          (msg.destinatarioId === this.senderId && msg.destinatarioTipo === 'paciente')) {
        // Identifica o outro participante (médico)
        const contatoId = msg.remetenteId === this.senderId ? msg.destinatarioId : msg.remetenteId;
        // Só agrupa se o outro participante for médico
        if (msg.remetenteId === this.senderId && msg.destinatarioTipo === 'medico') {
          if (!grupos[contatoId]) grupos[contatoId] = [];
          grupos[contatoId].push(msg);
        } else if (msg.destinatarioId === this.senderId && msg.remetenteTipo === 'medico') {
          if (!grupos[contatoId]) grupos[contatoId] = [];
          grupos[contatoId].push(msg);
        }
      }
    }
    this.conversas = Object.keys(grupos).map(contatoId => {
      const msgs = grupos[contatoId].sort((a, b) => new Date(a.data || a.dataEnvio).getTime() - new Date(b.data || b.dataEnvio).getTime());
      // Busca o nome do médico na primeira mensagem da conversa
      let contatoNome = '';
      for (const msg of msgs) {
        if (msg.remetenteId === contatoId && msg.remetenteTipo === 'medico' && msg.remetenteNome) {
          contatoNome = msg.remetenteNome;
          break;
        }
        if (msg.destinatarioId === contatoId && msg.destinatarioTipo === 'medico' && msg.destinatarioNome) {
          contatoNome = msg.destinatarioNome;
          break;
        }
      }
      if (!contatoNome) contatoNome = contatoId;
      return {
        contatoId,
        mensagens: msgs,
        ultimaMensagem: msgs[msgs.length - 1],
        contatoNome
      };
    });
  }

  selecionarConversa(conv: any) {
    this.conversaSelecionada = conv;
    // Preenche receiverId/receiverType para envio
    this.receiverId = conv.contatoId;
    this.receiverType = 'medico';
  }

  enviarMensagem() {
    if (!this.novaMensagemConteudo || !this.senderId || !this.senderType || !this.receiverId || !this.receiverType) return;
    // Recupera nome do médico selecionado (da conversa)
    const nomeMedico = this.conversaSelecionada?.contatoNome || this.receiverId;
    // Recupera nome do paciente logado do localStorage
    const nomePaciente = localStorage.getItem('pacienteNome') || this.senderId;
    const msg = {
      remetenteId: this.senderId,
      remetenteTipo: this.senderType,
      remetenteNome: nomePaciente,
      destinatarioId: this.receiverId,
      destinatarioTipo: this.receiverType,
      destinatarioNome: nomeMedico,
      texto: this.novaMensagemConteudo.trim()
    };
    this.mensagemService.sendMessage(msg).subscribe(() => {
      this.novaMensagemConteudo = '';
      this.loadAllMessages();
    }, err => console.error('Erro ao enviar mensagem:', err));
  }

  marcarComoLida(id: string) {
    this.mensagemService.markAsRead(id).subscribe(() => this.loadAllMessages(), err => console.error(err));
  }
} 
