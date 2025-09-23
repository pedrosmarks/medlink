import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../services/pacientes/pacientes-read.service';
import { ProntuarioService } from '../../../../../services/prontuario/prontuario.service';

@Component({
  selector: 'app-alergias',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  providers: [PacientesReadService],
  templateUrl: './alergias.html',
  styleUrls: ['./alergias.css']
})
export class Alergias implements OnInit {
  alergias: any[] = [];
  pacienteNome: string = '';
  novaAlergia: string = '';
  pacienteId: string = '';
carregando: any;

  constructor(
    private route: ActivatedRoute,
    private pacientesReadService: PacientesReadService,
    private prontuarioService: ProntuarioService
  ) {}

  ngOnInit(): void {
    this.pacienteId = localStorage.getItem('userId') || '';
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    this.carregarAlergias();
  }

  carregarAlergias(): void {
    this.prontuarioService.getAlergiasPaciente(this.pacienteId)
      .subscribe({
        next: (data: any[]) => {
          this.alergias = data;
          this.carregando = false;
        },
        error: (error: any) => {
          console.error('Erro ao carregar alergias:', error);
          this.alergias = [];
          this.carregando = false;
        }
      });
  }

  adicionarAlergia() {
    if (!this.novaAlergia.trim()) return;
    const nova = { descricao: this.novaAlergia.trim() };

    this.prontuarioService.adicionarAlergia(this.pacienteId, nova)
      .subscribe(() => {
        // Recarregar alergias após adicionar
        this.carregarAlergias();
        this.novaAlergia = '';
      });
  }

  removerAlergia(index: number) {
    const alergiaId = this.alergias[index].id;
    if (!alergiaId) {
      console.error('Não foi possível encontrar o ID da alergia para remover');
      return;
    }

    this.prontuarioService.removerAlergia(this.pacienteId, alergiaId)
      .subscribe(() => {
        // Recarregar alergias após remover
        this.carregarAlergias();
      });
  }
}