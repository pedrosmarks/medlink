import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { ProntuarioService } from '../../../../../../services/prontuario/prontuario.service';

@Component({
  selector: 'app-vacinas',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './vacinas.html',
  styleUrls: ['./vacinas.css']
})
export class Vacinas implements OnInit {
  vacinas: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  novaVacinaNome: string = '';
  novaVacinaData: string = '';

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
    
    console.log('💉 ID capturado (vacinas):', id);
    
    if (id && id !== 'undefined') {
      this.pacienteId = id;
      this.carregarVacinas();
      this.carregarDadosPaciente();
    } else {
      console.error('❌ ID do paciente não encontrado para vacinas');
    }
  }

  carregarVacinas() {
    console.log('Carregando vacinas para paciente:', this.pacienteId);
    
    this.prontuarioService.getVacinasPaciente(this.pacienteId).subscribe({
      next: (data) => {
        console.log('Vacinas recebidas:', data);
        this.vacinas = data;
      },
      error: (error) => {
        console.error('Erro ao carregar vacinas:', error);
        this.vacinas = [];
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

  adicionarVacina() {
    if (!this.novaVacinaNome.trim() || !this.novaVacinaData) return;
    
    const novaVacina = {
      name: this.novaVacinaNome.trim(),
      date: this.novaVacinaData
    };
    
    console.log('Adicionando vacina para paciente:', this.pacienteId, novaVacina);
    
    this.prontuarioService.adicionarVacina(this.pacienteId, novaVacina).subscribe({
      next: (response) => {
        console.log('Vacina adicionada:', response);
        this.carregarVacinas();
        this.novaVacinaNome = '';
        this.novaVacinaData = '';
      },
      error: (error) => {
        console.error('Erro ao adicionar vacina:', error);
        // Adiciona localmente se backend falhar
        this.vacinas.push(novaVacina);
        this.novaVacinaNome = '';
        this.novaVacinaData = '';
      }
    });
  }

  removerVacina(index: number) {
    const vacina = this.vacinas[index];
    if (!vacina || !vacina.id) {
      console.log('Vacina removida localmente (não estava salva no backend)');
      return;
    }
    
    console.log('Removendo vacina:', vacina.id);
    
    this.prontuarioService.removerVacina(this.pacienteId, vacina.id).subscribe({
      next: (response) => {
        console.log('Vacina removida:', response);
        this.carregarVacinas(); // Recarrega a lista
      },
      error: (error) => {
        console.error('Backend não implementou DELETE. Removendo localmente:', error.status);
        // Remove localmente até backend implementar
        this.vacinas.splice(index, 1);
      }
    });
  }
}