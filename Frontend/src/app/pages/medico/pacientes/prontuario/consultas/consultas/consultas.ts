import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { HttpClient } from '@angular/common/http';

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
    private http: HttpClient
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
    
    // Buscar consultas do novo endpoint
    this.http.get<any>(`http://localhost:8080/api/patients/${this.pacienteId}/consultations`).subscribe({
      next: (response) => {
        console.log('Response consultas:', response);
        this.consultas = response.data || response || [];
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

    // Adicionar localmente (implementar endpoint POST se necessário)
    this.consultas = [...this.consultas, nova];
    this.novaConsultaDate = '';
    this.novaConsultaReason = '';
    this.novaConsultaNotes = '';
    
    console.log('Consulta adicionada localmente. Implementar endpoint POST se necessário.');
  }
removerConsulta(index: number) {
  const novasConsultas = this.consultas.slice();
  novasConsultas.splice(index, 1);
  
  // Atualizar no backend (implementar endpoint específico se necessário)
  this.consultas = novasConsultas;
  console.log('Consulta removida. Implementar endpoint de remoção se necessário.');
}
}