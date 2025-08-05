// src/app/pages/mensagens/mensagens.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MensagensService } from '../../../services/mensagens/mensagem.service';

@Component({
  selector: 'app-mensagem',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mensagem.component.html',
  styleUrls: ['./mensagem.component.css'],
  
})
export class MensagemComponent implements OnInit {
  mensagens: any[] = [];
  conversas: any[] = [];
  novaMensagem: string = '';
  destinatarioSelecionado: any = null;
  medicos: any[] = [];
  usuarioAtual = { id: '1', tipo: 'paciente', nome: 'João da Silva' }; // Simular usuário logado

  constructor(private mensagensService: MensagensService) {}

  ngOnInit(): void {
    this.carregarConversas();
    this.carregarMedicos();
  }

  carregarConversas(): void {
    this.mensagensService.getMensagens().subscribe(mensagens => {
      // Filtrar mensagens do usuário atual
      this.mensagens = mensagens.filter(m => 
        m.remetenteId === this.usuarioAtual.id || 
        m.destinatarioId === this.usuarioAtual.id
      );
      this.organizarConversas();
    });
  }

  carregarMedicos(): void {
    this.mensagensService.getMedicos().subscribe(medicos => {
      this.medicos = medicos;
    });
  }

  organizarConversas(): void {
    const conversasMap = new Map();
    
    this.mensagens.forEach(mensagem => {
      const outroUsuario = mensagem.remetenteId === this.usuarioAtual.id ? 
        { id: mensagem.destinatarioId, nome: mensagem.destinatarioNome, tipo: mensagem.destinatarioTipo } :
        { id: mensagem.remetenteId, nome: mensagem.remetenteNome, tipo: mensagem.remetenteTipo };
      
      if (!conversasMap.has(outroUsuario.id)) {
        conversasMap.set(outroUsuario.id, {
          usuario: outroUsuario,
          ultimaMensagem: mensagem,
          mensagens: []
        });
      }
      
      conversasMap.get(outroUsuario.id).mensagens.push(mensagem);
      if (new Date(mensagem.data) > new Date(conversasMap.get(outroUsuario.id).ultimaMensagem.data)) {
        conversasMap.get(outroUsuario.id).ultimaMensagem = mensagem;
      }
    });
    
    this.conversas = Array.from(conversasMap.values()).sort((a, b) => 
      new Date(b.ultimaMensagem.data).getTime() - new Date(a.ultimaMensagem.data).getTime()
    );
  }

  selecionarConversa(conversa: any): void {
    this.destinatarioSelecionado = conversa;
  }

 enviarMensagem(): void {
  if (!this.novaMensagem.trim() || !this.destinatarioSelecionado) return;

  const mensagem = {
    remetenteId: this.usuarioAtual.id,
    remetenteTipo: this.usuarioAtual.tipo,
    remetenteNome: this.usuarioAtual.nome,
    destinatarioId: this.destinatarioSelecionado.usuario.id,
    destinatarioTipo: this.destinatarioSelecionado.usuario.tipo,
    destinatarioNome: this.destinatarioSelecionado.usuario.nome,
    texto: this.novaMensagem.trim(),
    data: new Date().toISOString(),
    lida: false
  };

  this.mensagensService.enviarMensagem(mensagem).subscribe(() => {
    this.novaMensagem = '';
    
    // SALVAR O ID DO DESTINATÁRIO ATUAL
    const destinatarioAtual = this.destinatarioSelecionado.usuario.id;
    
    // RECARREGAR AS CONVERSAS
    this.carregarConversas();
    
    // DEPOIS DE RECARREGAR, RESELECIONAR A CONVERSA
    setTimeout(() => {
      const conversaAtual = this.conversas.find(c => c.usuario.id === destinatarioAtual);
      if (conversaAtual) {
        this.destinatarioSelecionado = conversaAtual;
      }
    }, 100);
  });
}

  iniciarNovaConversa(medico: any): void {
    this.destinatarioSelecionado = {
      usuario: { id: medico.id, nome: medico.nome, tipo: 'medico' },
      mensagens: []
    };
  }
}