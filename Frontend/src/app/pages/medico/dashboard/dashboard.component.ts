import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardReadService } from '../../../services/dashboard/dashboard-read.service';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  providers: [DashboardReadService]
})
export class DashboardComponent implements OnInit {
  cards: any[] = [];
  medicoNome: string = '';
  totalPacientes: number = 0;
  consultasHoje: number = 0;

  constructor(private dashboardreadService: DashboardReadService) {}

  ngOnInit(): void {
    // Busca nome do médico do localStorage
    this.medicoNome = localStorage.getItem('userName') || 'Médico';
    
    // Busca dados do dashboard
    this.dashboardreadService.getDashboard().subscribe((data: any[]) => {
      this.cards = data;
    });
    
    // Simulação de estatísticas (pode ser substituído por chamadas reais da API)
    this.totalPacientes = 45;
    this.consultasHoje = 8;
  }
}