import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-vacinas',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './vacinas.html',
  styleUrls: ['./vacinas.css']
})
export class Vacinas implements OnInit {
  vacinas: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  carregando: boolean = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.pacienteId = localStorage.getItem('userId') || '';
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    this.carregarVacinas();
    
  }

  carregarVacinas(): void {
    console.log('Carregando vacinas para paciente ID:', this.pacienteId);
    this.http.get<any>(`http://localhost:8080/api/patients/${this.pacienteId}/vaccines`)
      .subscribe({
        next: (response) => {
          console.log('Vacinas carregadas:', response);
          this.vacinas = response.data || [];
          this.carregando = false;
        
        },
        error: (error) => {
          console.error('Erro ao carregar vacinas:', error);
          this.vacinas = [];
          this.carregando = false;
        }
      });
  }
}
