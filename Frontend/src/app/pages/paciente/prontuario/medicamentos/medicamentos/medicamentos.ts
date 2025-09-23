import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProntuarioService } from '../../../../../services/prontuario/prontuario.service';

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

  constructor(private prontuarioService: ProntuarioService) {}

  ngOnInit(): void {
    this.pacienteId = localStorage.getItem('userId') || '';
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    this.carregarMedicamentos();
  }

  carregarMedicamentos(): void {
    this.prontuarioService.getMedicamentosPaciente(this.pacienteId)
      .subscribe({
        next: (data: any[]) => {
          this.medicamentos = data;
          this.carregando = false;
        },
        error: (error: any) => {
          console.error('Erro ao carregar medicamentos:', error);
          this.medicamentos = [];
          this.carregando = false;
        }
      });
  }
}
