import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-prontuario-cards',
  imports: [CommonModule, RouterModule],
  templateUrl: './prontuario-cards.html',
  styleUrl: './prontuario-cards.css'
})
export class ProntuarioCards implements OnInit {
  pacienteNome: string = '';
  pacienteId: string = '';
  medicoNome: string = '';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.pacienteId = this.route.parent?.snapshot.paramMap.get('id') || '';
    this.medicoNome = localStorage.getItem('userName') || 'Médico';
    this.carregarPaciente();
  }

  carregarPaciente(): void {
    if (this.pacienteId) {
      this.http.get<any>(`http://localhost:8080/pacientes/${this.pacienteId}`)
        .subscribe({
          next: (paciente) => {
            this.pacienteNome = paciente?.nome || 'Paciente';
          },
          error: (error) => {
            console.error('Erro ao carregar paciente:', error);
            this.pacienteNome = 'Paciente';
          }
        });
    }
  }
}
