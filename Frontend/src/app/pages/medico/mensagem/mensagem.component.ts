import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

interface Paciente {
  id: number;
  name: string;
  avatar: string;
  especialistasAutorizados: number[];
}

interface Mensagem {
  id: string;
  remetenteId: string;
  remetenteTipo: string;
  remetenteNome: string;
  destinatarioId: string;
  destinatarioTipo: string;
  destinatarioNome: string;
  texto: string;
  data: string;
  lida: boolean;
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
export class MensagemComponent implements OnInit {
  medicoId = 1; // ID do médico logado
  pacientesComMensagens: PacienteComMensagens[] = [];
  pacienteSelecionado: PacienteComMensagens | null = null;
  novaMensagem = '';
  loading = false;

  constructor(private http: HttpClient) {}

  ngOnInit() {
  // Busca id do médico logado do localStorage
  const medicoLogado = localStorage.getItem('medicoId');
  this.medicoId = medicoLogado ? Number(medicoLogado) : 1;
  this.carregarPacientesEMensagens();
  }

  async carregarPacientesEMensagens() {
    this.loading = true;
    
    try {
      // 1. Buscar todos os pacientes
      const pacientesResponse = await this.http.get<any>('http://localhost:8080/patients').toPromise();
      const todosPacientes: Paciente[] = pacientesResponse.data;

      // 2. Filtrar pacientes que têm o médico autorizado
      const pacientesAutorizados = todosPacientes.filter(paciente => 
        paciente.especialistasAutorizados.includes(this.medicoId)
      );

      // 3. Buscar todas as mensagens
      const mensagensResponse = await this.http.get<any>('http://localhost:8080/messages').toPromise();
      const todasMensagens: Mensagem[] = mensagensResponse.data;

      // Filtra apenas mensagens onde o médico logado está envolvido
      const idMedico = `medico_${this.medicoId}`;
      const mensagensDoMedico = todasMensagens.filter(msg =>
        (msg.remetenteId === idMedico && msg.remetenteTipo === 'medico') ||
        (msg.destinatarioId === idMedico && msg.destinatarioTipo === 'medico')
      );

      // 4. Agrupar mensagens por paciente autorizado
      this.pacientesComMensagens = pacientesAutorizados.map(paciente => {
        const idPaciente = paciente.id.toString();
        const msgsPaciente = mensagensDoMedico.filter(msg =>
          (msg.remetenteId === idPaciente && msg.remetenteTipo === 'paciente') ||
          (msg.destinatarioId === idPaciente && msg.destinatarioTipo === 'paciente')
        );
        msgsPaciente.sort((a, b) => new Date(a.data).getTime() - new Date(b.data).getTime());
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
  }

  async enviarMensagem() {
    if (!this.novaMensagem.trim() || !this.pacienteSelecionado) return;

    // Recupera nome do médico logado do localStorage
    const nomeMedico = localStorage.getItem('medicoNome') || `medico_${this.medicoId}`;
    const mensagem = {
      remetenteId: `medico_${this.medicoId}`,
      remetenteTipo: 'medico',
      remetenteNome: nomeMedico,
      destinatarioId: this.pacienteSelecionado.paciente.id.toString(),
      destinatarioTipo: 'paciente',
      destinatarioNome: '', // paciente não precisa do nome do médico aqui
      texto: this.novaMensagem.trim()
    };

    try {
      await this.http.post('http://localhost:8080/messages', mensagem).toPromise();
      this.novaMensagem = '';
      
      // Recarregar mensagens
      await this.carregarPacientesEMensagens();
      
      // Manter paciente selecionado
      const pacienteAtualizado = this.pacientesComMensagens.find(p => 
        p.paciente.id === this.pacienteSelecionado?.paciente.id
      );
      if (pacienteAtualizado) {
        this.pacienteSelecionado = pacienteAtualizado;
      }
    } catch (error) {
      console.error('Erro ao enviar mensagem:', error);
    }
  }

  async marcarComoLida(mensagemId: string) {
    try {
      await this.http.patch(`http://localhost:8080/messages/${mensagemId}`, {}).toPromise();
      await this.carregarPacientesEMensagens();
    } catch (error) {
      console.error('Erro ao marcar como lida:', error);
    }
  }

  temMensagensNaoLidas(item: PacienteComMensagens): boolean {
    return item.mensagens.some(m => !m.lida && m.destinatarioId === `medico_${this.medicoId}`);
  }

  contarMensagensNaoLidas(item: PacienteComMensagens): number {
    return item.mensagens.filter(m => !m.lida && m.destinatarioId === `medico_${this.medicoId}`).length;
  }

  getMedicoId(): string {
    return `medico_${this.medicoId}`;
  }
}