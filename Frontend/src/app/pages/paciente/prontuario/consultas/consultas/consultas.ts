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
    this.pacienteId = localStorage.getItem('userId') || '';
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    this.carregarConsultas();
  }

  carregarConsultas(): void {
    // Chama a API para buscar consultas do paciente logado
    this.http.get<any[]>(`http://localhost:8080/consultas?pacienteId=${this.pacienteId}`)
      .subscribe({
        next: (consultas) => {
          this.consultas = consultas || [];
          this.carregando = false;
        },
        error: (error) => {
          console.error('Erro ao carregar consultas:', error);
          this.consultas = [];
          this.carregando = false;
        }
      });
  }
}