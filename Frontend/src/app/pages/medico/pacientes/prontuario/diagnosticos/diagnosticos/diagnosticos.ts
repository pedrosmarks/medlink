import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { ProntuarioService } from '../../../../../../services/prontuario/prontuario.service';

@Component({
  selector: 'app-diagnosticos',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './diagnosticos.html',
  styleUrls: ['./diagnosticos.css']
})
export class Diagnosticos implements OnInit {
  diagnosticos: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  novoDiagnosticoNome: string = '';
  novoDiagnosticoData: string = '';

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
    
    console.log('📊 ID capturado (diagnósticos):', id);
    
    if (id && id !== 'undefined') {
      this.pacienteId = id;
      this.carregarDiagnosticos();
      this.carregarDadosPaciente();
    } else {
      console.error('❌ ID do paciente não encontrado para diagnósticos');
    }
  }

  carregarDiagnosticos() {
    console.log('Carregando diagnósticos do paciente:', this.pacienteId);
    
    this.prontuarioService.getDiagnosticosPaciente(this.pacienteId).subscribe({
      next: (data) => {
        console.log('Diagnósticos recebidos:', data);
        this.diagnosticos = data;
      },
      error: (error) => {
        console.error('Erro ao carregar diagnósticos:', error);
        this.diagnosticos = [];
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

  adicionarDiagnostico() {
    if (!this.novoDiagnosticoNome.trim() || !this.novoDiagnosticoData) return;
    
    const novoDiagnostico = {
      description: this.novoDiagnosticoNome.trim(),
      date: this.novoDiagnosticoData
    };
    
    console.log('Adicionando diagnóstico para paciente:', this.pacienteId, novoDiagnostico);
    
    this.prontuarioService.adicionarDiagnostico(this.pacienteId, novoDiagnostico).subscribe({
      next: (response) => {
        console.log('Diagnóstico adicionado:', response);
        this.carregarDiagnosticos();
        this.novoDiagnosticoNome = '';
        this.novoDiagnosticoData = '';
      },
      error: (error) => {
        console.error('Erro ao adicionar diagnóstico:', error);
        // Adiciona localmente se backend falhar
        this.diagnosticos.push(novoDiagnostico);
        this.novoDiagnosticoNome = '';
        this.novoDiagnosticoData = '';
      }
    });
  }

  removerDiagnostico(index: number) {
    const diagnostico = this.diagnosticos[index];
    if (!diagnostico) {
      console.error('Diagnóstico não encontrado no índice:', index);
      return;
    }
    
    if (diagnostico.id) {
      console.log('Removendo diagnóstico:', diagnostico.id);
      
      this.prontuarioService.removerDiagnostico(this.pacienteId, diagnostico.id).subscribe({
        next: (response) => {
          console.log('Diagnóstico removido:', response);
          this.carregarDiagnosticos();
        },
        error: (error) => {
          console.error('Backend não implementou DELETE. Removendo localmente:', error.status);
          this.diagnosticos.splice(index, 1);
        }
      });
    } else {
      // Remove localmente se não tem ID
      console.log('Removendo diagnóstico localmente (sem ID)');
      this.diagnosticos.splice(index, 1);
    }
  }
}