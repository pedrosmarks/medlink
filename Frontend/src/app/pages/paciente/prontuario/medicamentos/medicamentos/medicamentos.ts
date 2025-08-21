import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-medicamentos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './medicamentos.html',
  styleUrls: ['./medicamentos.css']
})
export class Medicamentos implements OnInit {
  medicamentos: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  carregando: boolean = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.pacienteId = localStorage.getItem('userId') || '';
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    this.carregarMedicamentos();
  }

  carregarMedicamentos(): void {
    this.http.get<any>(`http://localhost:8080/api/pacientes/${this.pacienteId}/medicamentos`)
      .subscribe({
        next: (response) => {
          this.medicamentos = response.data || [];
          this.carregando = false;
        },
        error: (error) => {
          console.error('Erro ao carregar medicamentos:', error);
          this.medicamentos = [];
          this.carregando = false;
        }
      });
  }
}
