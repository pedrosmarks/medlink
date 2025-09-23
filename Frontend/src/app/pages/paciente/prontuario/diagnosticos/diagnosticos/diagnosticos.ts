import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProntuarioService } from '../../../../../services/prontuario/prontuario.service';

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

  constructor(private prontuarioService: ProntuarioService) {}

  ngOnInit(): void {
    this.pacienteId = localStorage.getItem('userId') || '';
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    this.carregarDiagnosticos();
  }

  carregarDiagnosticos(): void {
    this.prontuarioService.getDiagnosticosPaciente(this.pacienteId)
      .subscribe({
        next: (data: any[]) => {
          this.diagnosticos = data;
          this.carregando = false;
        },
        error: (error: any) => {
          console.error('Erro ao carregar diagnósticos:', error);
          this.diagnosticos = [];
          this.carregando = false;
        }
      });
  }
}
