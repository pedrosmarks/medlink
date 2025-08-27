import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { HttpClient } from '@angular/common/http';

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
    private http: HttpClient
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
    const url = `http://localhost:8080/patients/${this.pacienteId}/allergies`;
    console.log('Carregando alergias de:', url);
    
    this.http.get<any>(url).subscribe({
      next: (response) => {
        console.log('Alergias recebidas:', response);
        this.alergias = response.data || response;
        
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
    
    const url = `http://localhost:8080/api/patients/${this.pacienteId}/allergies`;
    console.log('Adicionando alergia:', url, novaAlergia);
    
    this.http.post<any>(url, novaAlergia).subscribe({
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
    
    // Como não temos ID, vamos usar o nome da alergia para identificação
    const url = `http://localhost:8080/api/patients/${this.pacienteId}/allergies`;
    console.log('Removendo alergia:', alergia.name, 'do paciente:', this.pacienteId);
    
    // Por enquanto, remove apenas localmente até o backend implementar remoção por ID
    this.alergias.splice(index, 1);
    console.log('Alergia removida localmente. Backend precisa implementar DELETE com ID.');
    
    // TODO: Quando backend adicionar campo 'id', usar:
    // this.http.delete(`${url}/${alergia.id}`).subscribe(...);
  }
}