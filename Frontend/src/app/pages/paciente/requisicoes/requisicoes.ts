import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-requisicoes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './requisicoes.html',
  styleUrls: ['./requisicoes.css']
})
export class Requisicoes implements OnInit {
  paciente: any = null;
  requisicoes: any[] = [];
  medicos: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const pacienteId = localStorage.getItem('pacienteId') || '2'; // ajuste para o id real do paciente logado
    this.http.get<any>(`http://localhost:3000/pacientes/${pacienteId}`).subscribe(paciente => {
      this.paciente = paciente;
      this.requisicoes = paciente.requisicoesAcesso || [];
      this.carregarMedicos();
    });
  }

  carregarMedicos() {
  this.http.get<any[]>(`http://localhost:3000/perfil`).subscribe(medicos => {
    this.medicos = medicos;
  });
}

  getNomeMedico(medicoId: number | string): string {
    const medico = this.medicos.find(m => String(m.id) === String(medicoId));
    return medico ? medico.nome : 'Médico desconhecido';
  }

  aprovar(medicoId: number) {
    this.atualizarStatus(medicoId, 'aprovado');
  }

  recusar(medicoId: number) {
    this.atualizarStatus(medicoId, 'recusado');
  }

 atualizarStatus(medicoId: number, status: string) {
  const novasRequisicoes = this.requisicoes.map(req =>
    req.medicoId === medicoId ? { ...req, status } : req
  );

  // Se aprovado, adiciona o médico aos especialistasAutorizados (se ainda não estiver)
  let novosEspecialistas = this.paciente.especialistasAutorizados || [];
  if (status === 'aprovado' && !novosEspecialistas.includes(medicoId)) {
    novosEspecialistas = [...novosEspecialistas, medicoId];
  }

  this.http.patch<any>(`http://localhost:3000/pacientes/${this.paciente.id}`, {
    requisicoesAcesso: novasRequisicoes,
    especialistasAutorizados: novosEspecialistas
  }).subscribe(() => {
    this.requisicoes = novasRequisicoes;
    this.paciente.especialistasAutorizados = novosEspecialistas;
  });
}
}