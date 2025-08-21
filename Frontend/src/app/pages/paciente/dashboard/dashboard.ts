import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardReadService } from '../../../services/dashboard/dashboard-read.service';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-paciente-dashboard',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'],
  providers: [DashboardReadService]
})
export class Dashboard implements OnInit {
  cards: any[] = [];
  pacienteNome: string = '';
  proximasConsultas: number = 0;
  vacinasAtualizadas: number = 0;

  constructor(private dashboardreadService: DashboardReadService) {}

  ngOnInit(): void {
    // Busca nome do paciente do localStorage
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    
    // Busca dados do dashboard
    this.dashboardreadService.getDashboard().subscribe((data: any[]) => {
      this.cards = data;
    });
    
    // Simulação de estatísticas (pode ser substituído por chamadas reais da API)
    this.proximasConsultas = 2;
    this.vacinasAtualizadas = 12;
  }
}