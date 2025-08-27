import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MensagemService } from './mensagem.service';

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
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mensagens.component.html',
  styleUrls: ['./mensagens.component.css']
})
export class MensagemComponent implements OnInit {
  mensagens: MensagemBackend[] = [];
  novaMensagemConteudo = '';
  senderId = '';
  senderType = '';
  receiverId = '';
  receiverType = '';

  constructor(private mensagemService: MensagemService) {}

  ngOnInit() {
    // Exemplo: buscar conversas do usuário logado
    // this.listarConversas();
  }

  listarConversas() {
    if (this.senderId && this.senderType) {
      this.mensagemService.getConversations(this.senderId, this.senderType)
        .subscribe(res => this.mensagens = res.data);
    }
  }

  enviarMensagem() {
    if (this.novaMensagemConteudo && this.senderId && this.senderType && this.receiverId && this.receiverType) {
      const msg: MensagemBackend = {
        senderId: this.senderId,
        senderType: this.senderType,
        receiverId: this.receiverId,
        receiverType: this.receiverType,
        conteudo: this.novaMensagemConteudo
      };
      this.mensagemService.sendMessage(msg).subscribe(() => {
        this.novaMensagemConteudo = '';
        this.listarConversas();
      });
    }
  }

  marcarComoLida(id: string) {
    this.mensagemService.markAsRead(id).subscribe(() => {
      this.listarConversas();
    });
  }
}