import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-cirurgias',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './cirurgias.html',
  styleUrls: ['./cirurgias.css']
})
export class Cirurgias implements OnInit {
  cirurgias: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  carregando: boolean = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.pacienteId = localStorage.getItem('userId') || '';
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    this.carregarCirurgias();
  }

  carregarCirurgias(): void {
    console.log('Carregando cirurgias para paciente ID:', this.pacienteId);
    this.http.get<any>(`http://localhost:8080/api/patients/${this.pacienteId}/surgeries`)
      .subscribe({
        next: (response) => {
          console.log('Cirurgias carregadas:', response);
          this.cirurgias = response.data || [];
          this.carregando = false;
        },
        error: (error) => {
          console.error('Erro ao carregar cirurgias:', error);
          this.cirurgias = [];
          this.carregando = false;
        }
      });
  }
}
