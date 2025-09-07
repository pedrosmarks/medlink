import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MedicosService } from '../../../services/medicos/medicos.service';

@Component({
  selector: 'app-paciente-dashboard',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class Dashboard implements OnInit {
  pacienteNome: string = '';
  pacienteId: string = '';
  proximasConsultas: number = 0;
  medicosAutorizados: number = 0;
  mensagensNaoLidas: number = 0;
  consultasRealizadas: number = 0;
  loading: boolean = false;

  constructor(private http: HttpClient, private medicosService: MedicosService) {}

  async ngOnInit(): Promise<void> {
    // Busca dados do paciente do localStorage
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    this.pacienteId = localStorage.getItem('pacienteId') || '1';
    
    await this.carregarDadosDashboard();
  }

  async carregarDadosDashboard() {
    this.loading = true;
    
    try {
      // Buscar médicos autorizados
  const medicos = await this.medicosService.getMedicosAutorizadosCompletos(this.pacienteId).toPromise().catch(() => []);
  this.medicosAutorizados = Array.isArray(medicos) ? medicos.length : 0;

      // Buscar mensagens não lidas
      const mensagensResponse = await this.http.get<any>('http://localhost:8080/messages').toPromise();
      const todasMensagens = mensagensResponse.data || mensagensResponse || [];
      this.mensagensNaoLidas = todasMensagens.filter((m: any) => 
        !m.lida && m.destinatarioId === this.pacienteId && m.destinatarioTipo === 'paciente'
      ).length;

      // Dados simulados mas baseados na estrutura real
      this.proximasConsultas = Math.floor(Math.random() * 3) + 1;
      this.consultasRealizadas = Math.floor(Math.random() * 10) + 5;
      
    } catch (error) {
      console.error('Erro ao carregar dados do dashboard:', error);
    } finally {
      this.loading = false;
    }
  }
}