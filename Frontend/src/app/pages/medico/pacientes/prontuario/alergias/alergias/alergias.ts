import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { ProntuarioService } from '../../../../../../services/prontuario/prontuario.service';

@Component({
  selector: 'app-alergias',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './alergias.html',
  styleUrls: ['./alergias.css']
})
export class Alergias implements OnInit {
  alergias: any[] = [];
  pacienteNome: string = '';
  novaAlergia: string = '';
  pacienteId: string = '';

  constructor(
    private route: ActivatedRoute,
    private pacientesReadService: PacientesReadService,
    private prontuarioService: ProntuarioService
  ) {}

  ngOnInit(): void {
    // Captura ID do paciente da rota pai (/medico/pacientes/:id/prontuario)
    let id = this.route.snapshot.paramMap.get('id');
    
    // Se não encontrou na rota atual, busca na rota pai
    if (!id && this.route.parent) {
      id = this.route.parent.snapshot.paramMap.get('id');
    }
    
    // Se ainda não encontrou, busca na rota avô (dois níveis acima)
    if (!id && this.route.parent?.parent) {
      id = this.route.parent.parent.snapshot.paramMap.get('id');
    }
    
    console.log('🔍 ID capturado (alergias):', id);
    console.log('🔍 Estrutura da rota:', {
      atual: this.route.snapshot.paramMap.keys,
      pai: this.route.parent?.snapshot.paramMap.keys,
      avo: this.route.parent?.parent?.snapshot.paramMap.keys
    });
    
    if (id && id !== 'undefined') {
      this.pacienteId = id;
      this.carregarAlergias();
      this.carregarDadosPaciente();
    } else {
      console.error('❌ ID do paciente não encontrado em nenhuma rota');
    }
  }

  carregarAlergias() {
    console.log('Carregando alergias para paciente:', this.pacienteId);
    
    this.prontuarioService.getAlergiasPaciente(this.pacienteId).subscribe({
      next: (data) => {
        console.log('Alergias recebidas:', data);
        this.alergias = data;
        
        // Log detalhado das alergias
        this.alergias.forEach((alergia, index) => {
          console.log(`💊 ALERGIA ${index + 1}:`, alergia);
          console.log('Campos disponíveis:', Object.keys(alergia));
        });
      },
      error: (error) => {
        console.error('Erro ao carregar alergias:', error);
        this.alergias = [];
      }
    });
  }

  carregarDadosPaciente() {
    this.pacientesReadService.getPacienteById(this.pacienteId).subscribe({
      next: (response) => {
        const paciente = response.data || response;
        this.pacienteNome = paciente.name || paciente.nome || 'Paciente';
        console.log('Nome do paciente:', this.pacienteNome);
      },
      error: (error) => {
        console.error('Erro ao carregar dados do paciente:', error);
      }
    });
  }

  adicionarAlergia() {
    if (!this.novaAlergia.trim()) return;
    
    const novaAlergia = {
      name: this.novaAlergia.trim(),
      substance: this.novaAlergia.trim(),
      reaction: 'Não especificada',
      severity: 'Moderada'
    };
    
    console.log('Adicionando alergia para paciente:', this.pacienteId, novaAlergia);
    
    this.prontuarioService.adicionarAlergia(this.pacienteId, novaAlergia).subscribe({
      next: (response) => {
        console.log('Alergia adicionada:', response);
        this.carregarAlergias(); // Recarrega a lista
        this.novaAlergia = '';
      },
      error: (error) => {
        console.error('Erro ao adicionar alergia:', error);
        // Adiciona localmente se o backend falhar
        this.alergias.push(novaAlergia);
        this.novaAlergia = '';
        console.log('Alergia adicionada localmente.');
      }
    });
  }

  removerAlergia(index: number) {
    const alergia = this.alergias[index];
    if (!alergia) {
      console.error('Alergia não encontrada no índice:', index);
      return;
    }
    
    // Verifica se a alergia tem ID para fazer DELETE no backend
    if (alergia.id !== undefined && alergia.id !== null) {
      console.log('Removendo alergia via backend:', alergia.id);
      
      this.prontuarioService.removerAlergia(this.pacienteId, alergia.id).subscribe({
        next: (response) => {
          console.log('Alergia removida com sucesso:', response);
          this.carregarAlergias(); // Recarrega a lista do backend
        },
        error: (error) => {
          console.error('Erro ao remover alergia:', error);
          // Se der erro, remove localmente como fallback
          this.alergias.splice(index, 1);
        }
      });
    } else {
      // Se não tem ID, remove apenas localmente
      console.log('Alergia sem ID, removendo localmente');
      this.alergias.splice(index, 1);
    }
  }
}