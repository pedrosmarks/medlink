import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-diagnosticos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './diagnosticos.html',
  styleUrls: ['./diagnosticos.css']
})
export class Diagnosticos implements OnInit {
  diagnosticos: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  carregando: boolean = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.pacienteId = localStorage.getItem('userId') || '';
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    this.carregarDiagnosticos();
  }

  carregarDiagnosticos(): void {
    this.http.get<any>(`http://localhost:8080/api/pacientes/${this.pacienteId}/diagnosticos`)
      .subscribe({
        next: (response) => {
          this.diagnosticos = response.data || [];
          this.carregando = false;
        },
        error: (error) => {
          console.error('Erro ao carregar diagnósticos:', error);
          this.diagnosticos = [];
          this.carregando = false;
        }
      });
  }
}
