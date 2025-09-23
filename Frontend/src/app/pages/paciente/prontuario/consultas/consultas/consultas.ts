import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProntuarioService } from '../../../../../services/prontuario/prontuario.service';

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

  constructor(private prontuarioService: ProntuarioService) {}

  ngOnInit(): void {
    this.pacienteId = localStorage.getItem('userId') || '';
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    
    if (!this.pacienteId) {
      console.error('ERRO: ID do paciente não encontrado no localStorage!');
      return;
    }
    
    this.carregarConsultas();
  }

  carregarConsultas(): void {
    this.prontuarioService.getConsultasPaciente(this.pacienteId)
      .subscribe({
        next: (data: any[]) => {
          this.consultas = data;
          this.carregando = false;
        },
        error: (error: any) => {
          console.error('Erro ao carregar consultas:', error);
          console.error('Status do erro:', error.status);
          console.error('Mensagem do erro:', error.message);
          this.consultas = [];
          this.carregando = false;
        }
      });
  }
}