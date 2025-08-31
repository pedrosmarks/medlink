import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  medicoNome: string = '';
  medicoId: number = 1;
  totalPacientesAutorizados: number = 0;
  mensagensNaoLidas: number = 0;
  consultasHoje: number = 0;
  totalConsultas: number = 0;
  loading: boolean = false;

  constructor(private http: HttpClient) {}

  async ngOnInit(): Promise<void> {
    // Busca dados do médico do localStorage
    this.medicoNome = localStorage.getItem('userName') || 'Médico';
    const medicoLogado = localStorage.getItem('medicoId');
    this.medicoId = medicoLogado ? Number(medicoLogado) : 1;
    
    await this.carregarDadosDashboard();
  }

  async carregarDadosDashboard() {
    this.loading = true;
    
    try {
      // Buscar pacientes autorizados
      const pacientesResponse = await this.http.get<any>(`http://localhost:8080/api/medic/${this.medicoId}/patients`).toPromise();
      const pacientes = pacientesResponse.data || pacientesResponse || [];
      this.totalPacientesAutorizados = pacientes.length;

      // Buscar mensagens não lidas
      const mensagensResponse = await this.http.get<any>('http://localhost:8080/messages').toPromise();
      const todasMensagens = mensagensResponse.data || mensagensResponse || [];
      this.mensagensNaoLidas = todasMensagens.filter((m: any) => 
        !m.lida && m.destinatarioId === `medico_${this.medicoId}` && m.destinatarioTipo === 'medico'
      ).length;

      // Dados simulados mas baseados na estrutura real
      this.consultasHoje = Math.floor(Math.random() * 6) + 2;
      this.totalConsultas = Math.floor(Math.random() * 100) + 50;
      
    } catch (error) {
      console.error('Erro ao carregar dados do dashboard:', error);
    } finally {
      this.loading = false;
    }
  }
}