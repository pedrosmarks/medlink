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
  medicoId = 1; // Defina aqui o ID do médico logado

  // Modal e busca
  modalAberto = false;
  buscaNome = '';
  resultadosBusca: any[] = [];

  constructor(private pacientesReadService: PacientesReadService, private pacientesUpdateService: PacientesUpdateService) {}

  ngOnInit(): void {
    this.pacientesReadService.getPacientes().subscribe((pacientes: any[]) => {
      this.todosPacientes = pacientes;
      this.pacientes = pacientes.filter(paciente =>
        paciente.especialistasAutorizados.includes(this.medicoId)
      );
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