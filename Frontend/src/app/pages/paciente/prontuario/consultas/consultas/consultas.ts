import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-consultas',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './consultas.html',
  styleUrls: ['./consultas.css']
})
export class Consultas implements OnInit {
  consultas: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  carregando: boolean = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    console.log('=== INICIANDO COMPONENTE CONSULTAS ===');
    this.pacienteId = localStorage.getItem('userId') || '';
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    console.log('Dados do localStorage:');
    console.log('- userId:', this.pacienteId);
    console.log('- userName:', this.pacienteNome);
    console.log('- userProfile:', localStorage.getItem('userProfile'));
    
    if (!this.pacienteId) {
      console.error('ERRO: ID do paciente não encontrado no localStorage!');
      return;
    }
    
    this.carregarConsultas();
  }

  carregarConsultas(): void {
    console.log('Carregando consultas para paciente ID:', this.pacienteId);
    console.log('URL da chamada:', `http://localhost:8080/api/patients/${this.pacienteId}/consultations`);
    
    this.http.get<any>(`http://localhost:8080/api/patients/${this.pacienteId}/consultations`)
      .subscribe({
        next: (response) => {
          console.log('Response completo:', response);
          console.log('Dados das consultas:', response.data);
          this.consultas = response.data || [];
          this.carregando = false;
        },
        error: (error) => {
          console.error('Erro ao carregar consultas:', error);
          console.error('Status do erro:', error.status);
          console.error('Mensagem do erro:', error.message);
          this.consultas = [];
          this.carregando = false;
        }
      });
  }
}