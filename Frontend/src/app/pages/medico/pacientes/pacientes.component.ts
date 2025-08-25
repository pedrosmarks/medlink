import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PacientesReadService } from '../../../services/pacientes/pacientes-read.service';
import { PacientesUpdateService } from '../../../services/pacientes/pacientes-update';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-pacientes',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './pacientes.component.html',
  styleUrls: ['./pacientes.component.css']
})
export class PacientesComponent implements OnInit {
  pacientes: any[] = [];
  todosPacientes: any[] = [];
  medicoId: number = 1;

  // Modal e busca
  modalAberto = false;
  buscaNome = '';
  resultadosBusca: any[] = [];

  constructor(private pacientesReadService: PacientesReadService, private pacientesUpdateService: PacientesUpdateService) {}

  ngOnInit(): void {
    this.medicoId = parseInt(localStorage.getItem('userId') || '1');
    console.log('🚀 CARREGANDO PACIENTES REAIS - Médico ID:', this.medicoId);
    
    this.pacientesReadService.getPacientes().subscribe({
      next: (response: any) => {
        console.log('✅ Response do backend:', response);
        console.log('✅ Dados recebidos:', response.data);
        
        // LOG DETALHADO DOS CAMPOS
        response.data.forEach((paciente: any, index: number) => {
          console.log(`📋 PACIENTE ${index + 1}:`);
          console.log('Todos os campos:', Object.keys(paciente));
          console.log('Dados completos:', paciente);
        });
        
        // Processa dados e adiciona avatar padrão se não existir
        this.pacientes = response.data.map((paciente: any) => ({
          ...paciente,
          avatar: paciente.avatar || 'https://cdn-icons-png.flaticon.com/512/921/921347.png'
        }));
        this.todosPacientes = this.pacientes;
        
        console.log('🎯 PACIENTES FINAIS:', this.pacientes);
        console.log('🎯 QUANTIDADE FINAL:', this.pacientes.length);
      },
      error: (error) => {
        console.error('❌ Erro ao carregar:', error);
      }
    });
  }

  abrirModalAdicionar() {
    this.modalAberto = true;
    this.buscaNome = '';
    this.resultadosBusca = [];
  }

  fecharModalAdicionar() {
    this.modalAberto = false;
  }

  pesquisarPacientes() {
  const termo = this.buscaNome.trim().toLowerCase();
  if (termo.length === 0) {
    this.resultadosBusca = [];
    return;
  }
  this.resultadosBusca = this.todosPacientes.filter(p =>
  p.nome && p.nome.toLowerCase().includes(termo) &&
  !p.especialistasAutorizados.includes(this.medicoId) &&
  !(p.requisicoesAcesso || []).some((req: any) => req.medicoId === this.medicoId && req.status === 'pendente')
);
}

  adicionarEspecialista(paciente: any) {
  if (!paciente.requisicoesAcesso) paciente.requisicoesAcesso = [];
  const jaSolicitado = paciente.requisicoesAcesso.some((req: any) => req.medicoId === this.medicoId && req.status === 'pendente');
  if (!jaSolicitado) {
    paciente.requisicoesAcesso.push({ medicoId: this.medicoId, status: 'pendente' });

    // Persiste no backend
    this.pacientesUpdateService.updatePaciente(paciente.id, {
      requisicoesAcesso: paciente.requisicoesAcesso
    }).subscribe();
  }
  this.resultadosBusca = this.resultadosBusca.filter(p => p.id !== paciente.id);
}
}