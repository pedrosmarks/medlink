import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { ProntuarioService } from '../../../../../../services/prontuario/prontuario.service';

@Component({
  selector: 'app-consultas',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './consultas.html',
  styleUrls: ['./consultas.css']
})
export class Consultas implements OnInit {
  consultas: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  novaConsultaDate: string = '';
  novaConsultaReason: string = '';
  novaConsultaNotes: string = '';

  constructor(
    private route: ActivatedRoute,
    private pacientesReadService: PacientesReadService,
    private prontuarioService: ProntuarioService
  ) {}

  ngOnInit(): void {
    const id = this.route.parent?.snapshot.paramMap.get('id');
    console.log('ID recebido:', id); 
    this.pacienteId = id ?? '';
    // Buscar dados do paciente
    this.pacientesReadService.getPacienteById(this.pacienteId).subscribe({
      next: (response) => {
        const paciente = response.data || response;
        this.pacienteNome = paciente.nome || paciente.name || 'Paciente';
      },
      error: (error) => {
        console.error('Erro ao carregar paciente:', error);
      }
    });
    
    // Buscar consultas do paciente
    this.prontuarioService.getConsultasPaciente(this.pacienteId).subscribe({
      next: (data) => {
        console.log('Response consultas:', data);
        this.consultas = data;
        console.log('Consultas carregadas:', this.consultas);
      },
      error: (error) => {
        console.error('Erro ao carregar consultas:', error);
        this.consultas = [];
      }
    });
  }

  adicionarConsulta() {
    if (!this.novaConsultaDate || !this.novaConsultaReason.trim()) return;
    
    const nova = {
      date: this.novaConsultaDate,
      reason: this.novaConsultaReason.trim(),
      notes: this.novaConsultaNotes.trim()
    };

    this.prontuarioService.adicionarConsulta(this.pacienteId, nova).subscribe({
      next: (response) => {
        console.log('Consulta adicionada:', response);
        // Recarregar as consultas para atualizar a lista
        this.prontuarioService.getConsultasPaciente(this.pacienteId).subscribe(data => {
          this.consultas = data;
        });
        this.novaConsultaDate = '';
        this.novaConsultaReason = '';
        this.novaConsultaNotes = '';
      },
      error: (error) => {
        console.error('Erro ao adicionar consulta:', error);
        // Adicionar localmente se o backend falhar
        this.consultas = [...this.consultas, nova];
        this.novaConsultaDate = '';
        this.novaConsultaReason = '';
        this.novaConsultaNotes = '';
      }
    });
  }
removerConsulta(index: number) {
  const consulta = this.consultas[index];
  if (!consulta || !consulta.id) {
    // Se não tem ID, apenas remove localmente
    const novasConsultas = this.consultas.slice();
    novasConsultas.splice(index, 1);
    this.consultas = novasConsultas;
    console.log('Consulta removida localmente (não estava salva no backend)');
    return;
  }
  
  this.prontuarioService.removerConsulta(this.pacienteId, consulta.id).subscribe({
    next: (response) => {
      console.log('Consulta removida:', response);
      // Recarregar as consultas para atualizar a lista
      this.prontuarioService.getConsultasPaciente(this.pacienteId).subscribe(data => {
        this.consultas = data;
      });
    },
    error: (error) => {
      console.error('Erro ao remover consulta:', error);
      // Remove localmente se o backend falhar
      const novasConsultas = this.consultas.slice();
      novasConsultas.splice(index, 1);
      this.consultas = novasConsultas;
    }
  });
}
}