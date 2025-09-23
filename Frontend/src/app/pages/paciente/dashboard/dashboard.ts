import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MedicosService } from '../../../services/medicos/medicos.service';
import { MensagensService } from '../../../services/mensagens/mensagem.service';

@Component({
  selector: 'app-paciente-dashboard',
  standalone: true,
  imports: [CommonModule],
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

  constructor(private medicosService: MedicosService, private mensagensService: MensagensService) {}

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
      const mensagensCount = await this.mensagensService.countMensagensNaoLidas(this.pacienteId, 'PATIENT').toPromise();
      this.mensagensNaoLidas = mensagensCount || 0;

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