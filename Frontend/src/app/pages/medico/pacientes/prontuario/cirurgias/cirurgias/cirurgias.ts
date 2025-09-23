import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { ProntuarioService } from '../../../../../../services/prontuario/prontuario.service';

@Component({
  selector: 'app-cirurgias',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './cirurgias.html',
  styleUrls: ['./cirurgias.css']
})
export class Cirurgias implements OnInit {
  cirurgias: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  novaCirurgiaNome: string = '';
  novaCirurgiaData: string = '';
  novaCirurgiaDescricao: string = '';

  constructor(
    private route: ActivatedRoute,
    private pacientesReadService: PacientesReadService,
    private prontuarioService: ProntuarioService
  ) {}

  ngOnInit(): void {
    // Captura ID do paciente da rota pai
    let id = this.route.snapshot.paramMap.get('id');
    
    if (!id && this.route.parent) {
      id = this.route.parent.snapshot.paramMap.get('id');
    }
    
    if (!id && this.route.parent?.parent) {
      id = this.route.parent.parent.snapshot.paramMap.get('id');
    }
    
    if (id && id !== 'undefined') {
      this.pacienteId = id;
      this.carregarCirurgias();
      this.carregarDadosPaciente();
    } else {
      console.error('ID do paciente não encontrado para cirurgias');
    }
  }

  carregarCirurgias() {
    this.prontuarioService.getCirurgiasPaciente(this.pacienteId).subscribe({
      next: (data) => {
        this.cirurgias = data;
      },
      error: (error) => {
        console.error('Erro ao carregar cirurgias:', error);
        this.cirurgias = [];
      }
    });
  }

  carregarDadosPaciente() {
    this.pacientesReadService.getPacienteById(this.pacienteId).subscribe({
      next: (response) => {
        const paciente = response.data || response;
        this.pacienteNome = paciente.name || paciente.nome || 'Paciente';
      },
      error: (error) => {
        console.error('Erro ao carregar dados do paciente:', error);
      }
    });
  }

  adicionarCirurgia() {
    if (!this.novaCirurgiaNome.trim() || !this.novaCirurgiaData) return;
    
    const novaCirurgia = {
      name: this.novaCirurgiaNome.trim(),
      date: this.novaCirurgiaData,
      notes: this.novaCirurgiaDescricao.trim(),
      location: ""
    };
    
    console.log('Adicionando cirurgia para paciente:', this.pacienteId, novaCirurgia);
    
    this.prontuarioService.adicionarCirurgia(this.pacienteId, novaCirurgia).subscribe({
      next: (response) => {
        console.log('Cirurgia adicionada:', response);
        this.carregarCirurgias();
        this.novaCirurgiaNome = '';
        this.novaCirurgiaData = '';
        this.novaCirurgiaDescricao = '';
      },
      error: (error) => {
        console.error('Erro ao adicionar cirurgia:', error);
        // Adiciona localmente se backend falhar
        this.cirurgias.push(novaCirurgia);
        this.novaCirurgiaNome = '';
        this.novaCirurgiaData = '';
        this.novaCirurgiaDescricao = '';
      }
    });
  }

  removerCirurgia(index: number) {
    const cirurgia = this.cirurgias[index];
    if (!cirurgia) {
      console.error('Cirurgia não encontrada no índice:', index);
      return;
    }
    
    if (cirurgia.id) {
      console.log('Removendo cirurgia:', cirurgia.id);
      
      this.prontuarioService.removerCirurgia(this.pacienteId, cirurgia.id).subscribe({
        next: (response) => {
          console.log('Cirurgia removida:', response);
          this.carregarCirurgias();
        },
        error: (error) => {
          console.error('Backend não implementou DELETE. Removendo localmente:', error.status);
          this.cirurgias.splice(index, 1);
        }
      });
    } else {
      // Remove localmente se não tem ID
      console.log('Removendo cirurgia localmente (sem ID)');
      this.cirurgias.splice(index, 1);
    }
  }
}