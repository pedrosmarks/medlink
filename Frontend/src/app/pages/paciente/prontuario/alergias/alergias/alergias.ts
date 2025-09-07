import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../services/pacientes/pacientes-read.service';
import { HttpClient } from '@angular/common/http';

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
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.pacienteId = localStorage.getItem('userId') || '';
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
    this.carregarAlergias();
  }

  carregarAlergias(): void {
    console.log('Carregando alergias para paciente ID:', this.pacienteId);
    this.http.get<any>(`http://localhost:8080/api/patients/${this.pacienteId}/allergies`)
      .subscribe({
        next: (response) => {
          console.log('Alergias carregadas:', response);
          this.alergias = response.data || [];
          this.carregando = false;
        },
        error: (error) => {
          console.error('Erro ao carregar alergias:', error);
          this.alergias = [];
          this.carregando = false;
        }
      });
  }

  adicionarAlergia() {
    if (!this.novaAlergia.trim()) return;
    const nova = { descricao: this.novaAlergia.trim() };

    // Buscar paciente completo antes de atualizar
    this.pacientesReadService.getPacienteById(this.pacienteId).subscribe(paciente => {
      const alergiasAtualizadas = [...(paciente.alergias || []), nova];
      const pacienteAtualizado = { ...paciente, alergias: alergiasAtualizadas };

      this.http.put<any>(`http://localhost:8080/api/patients/${this.pacienteId}`, pacienteAtualizado)
        .subscribe(() => {
          this.alergias = alergiasAtualizadas;
          this.novaAlergia = '';
        });
    });
  }

  removerAlergia(index: number) {
    this.pacientesReadService.getPacienteById(this.pacienteId).subscribe(paciente => {
      const novasAlergias = paciente.alergias.slice();
      novasAlergias.splice(index, 1);
      const pacienteAtualizado = { ...paciente, alergias: novasAlergias };

      this.http.put<any>(`http://localhost:8080/api/patients/${this.pacienteId}`, pacienteAtualizado)
        .subscribe(() => {
          this.alergias = novasAlergias;
        });
    });
  }
}