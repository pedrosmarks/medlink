import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MedicosService } from '../../../services/medicos/medicos.service';
import { MensagensService } from '../../../services/mensagens/mensagem.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
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

  constructor(
    private medicosService: MedicosService,
    private mensagensService: MensagensService
  ) {}

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
      const pacientesResponse = await this.medicosService.getPacientesAutorizados(String(this.medicoId)).toPromise();
      const pacientes = pacientesResponse || [];
      this.totalPacientesAutorizados = pacientes.length;

      // Buscar mensagens não lidas
      const mensagensCount = await this.mensagensService.countMensagensNaoLidas(String(this.medicoId), 'MEDIC').toPromise();
      this.mensagensNaoLidas = mensagensCount || 0;

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