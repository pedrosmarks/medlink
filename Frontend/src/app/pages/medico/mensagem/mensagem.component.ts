import { Component, OnInit, AfterViewChecked, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MensagensService } from '../../../services/mensagens/mensagem.service';

@Component({
  selector: 'app-mensagem',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mensagem.component.html',
  styleUrls: ['./mensagem.component.css']
})
export class MensagemComponent implements OnInit, AfterViewChecked {
  @ViewChild('mensagensContainer') private mensagensContainer!: ElementRef;
  
  medicos: any[] = [];
  pacientes: any[] = [];
  conversas: any[] = [];
  destinatarioSelecionado: any = null;
  usuarioAtual: any = { id: localStorage.getItem('userId'), nome: localStorage.getItem('userName'), tipo: 'medico' };
  novaMensagem: string = '';
  private shouldScrollToBottom = false;

  constructor(private mensagensService: MensagensService) {}

  ngOnInit(): void {
    this.carregarConversas();
  }

  ngAfterViewChecked(): void {
    if (this.shouldScrollToBottom) {
      this.scrollToBottom();
      this.shouldScrollToBottom = false;
    }
  }

  private scrollToBottom(): void {
    try {
      if (this.mensagensContainer) {
        this.mensagensContainer.nativeElement.scrollTop = this.mensagensContainer.nativeElement.scrollHeight;
      }
    } catch(err) {
      console.error('Erro ao fazer scroll:', err);
    }
  }

  carregarConversas() {
    this.mensagensService.getMensagens().subscribe(mensagens => {
      const conversasMap: { [pacienteId: string]: any } = {};

      mensagens.forEach((msg: any) => {
        // Só mostra conversas onde o médico atual está envolvido
        const medicoId = this.usuarioAtual.id;
        const isMedicoRemetente = msg.remetenteId === `medico_${medicoId}` && msg.remetenteTipo === 'medico';
        const isMedicoDestinatario = msg.destinatarioId === `medico_${medicoId}` && msg.destinatarioTipo === 'medico';
        
        if (!isMedicoRemetente && !isMedicoDestinatario) return;

        // Descobre o paciente envolvido
        let pacienteId = '';
        let pacienteNome = '';
        if (msg.remetenteTipo === 'paciente') {
          pacienteId = msg.remetenteId;
          pacienteNome = msg.remetenteNome;
        } else if (msg.destinatarioTipo === 'paciente') {
          pacienteId = msg.destinatarioId;
          pacienteNome = msg.destinatarioNome;
        }
        if (!pacienteId) return;

        if (!conversasMap[pacienteId]) {
          conversasMap[pacienteId] = {
            usuario: { id: pacienteId, nome: pacienteNome },
            mensagens: [],
            ultimaMensagem: null
          };
        }
        conversasMap[pacienteId].mensagens.push(msg);
      });

      // Ordena as mensagens por data e define a última mensagem
      Object.values(conversasMap).forEach((conversa: any) => {
        conversa.mensagens.sort((a: any, b: any) => new Date(a.data).getTime() - new Date(b.data).getTime());
        conversa.ultimaMensagem = conversa.mensagens[conversa.mensagens.length - 1] || {};
      });

      this.conversas = Object.values(conversasMap);
      
      // Extrai lista de pacientes únicos para poder iniciar novas conversas
      const pacientesUnicos: { [id: string]: any } = {};
      mensagens.forEach(msg => {
        if (msg.remetenteTipo === 'paciente' && !pacientesUnicos[msg.remetenteId]) {
          pacientesUnicos[msg.remetenteId] = { id: msg.remetenteId, nome: msg.remetenteNome };
        }
        if (msg.destinatarioTipo === 'paciente' && !pacientesUnicos[msg.destinatarioId]) {
          pacientesUnicos[msg.destinatarioId] = { id: msg.destinatarioId, nome: msg.destinatarioNome };
        }
      });
      this.pacientes = Object.values(pacientesUnicos);
    });
  }

  iniciarNovaConversa(paciente: any) {
    let conversa = this.conversas.find(c => c.usuario.id === paciente.id);
    if (!conversa) {
      conversa = {
        usuario: paciente,
        ultimaMensagem: {},
        mensagens: []
      };
      this.conversas.push(conversa);
    }
    this.selecionarConversa(conversa);
  }

  selecionarConversa(conversa: any) {
    this.destinatarioSelecionado = conversa;
    if (!Array.isArray(this.destinatarioSelecionado.mensagens)) {
      this.destinatarioSelecionado.mensagens = [];
    }
    // Faz scroll para baixo quando seleciona uma conversa
    setTimeout(() => {
      this.shouldScrollToBottom = true;
    }, 100);
  }

  enviarMensagem() {
    if (!this.novaMensagem.trim() || !this.destinatarioSelecionado) return;
    const mensagem = {
      texto: this.novaMensagem.trim(),
      data: new Date(),
      remetenteId: `medico_${this.usuarioAtual.id}`,
      remetenteTipo: 'medico',
      remetenteNome: this.usuarioAtual.nome,
      destinatarioId: this.destinatarioSelecionado.usuario.id,
      destinatarioTipo: 'paciente',
      destinatarioNome: this.destinatarioSelecionado.usuario.nome,
      lida: false
    };
    this.mensagensService.enviarMensagem(mensagem).subscribe(() => {
      this.destinatarioSelecionado.mensagens.push(mensagem);
      this.destinatarioSelecionado.ultimaMensagem = mensagem;
      this.novaMensagem = '';
      this.shouldScrollToBottom = true; // Ativa o scroll automático
      this.carregarConversas();
    });
  }
}