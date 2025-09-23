import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProntuarioService } from '../../../../../services/prontuario/prontuario.service';

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

  constructor(private prontuarioService: ProntuarioService) {}

  ngOnInit(): void {
    this.pacienteId = localStorage.getItem('userId') || '';
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    this.carregarCirurgias();
  }

  carregarCirurgias(): void {
    this.prontuarioService.getCirurgiasPaciente(this.pacienteId)
      .subscribe({
        next: (data) => {
          this.cirurgias = data;
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
