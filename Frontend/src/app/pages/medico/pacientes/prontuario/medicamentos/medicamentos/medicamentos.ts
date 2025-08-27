import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-medicamentos',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './medicamentos.html',
  styleUrls: ['./medicamentos.css']
})
export class Medicamentos implements OnInit {
  medicamentos: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  novoMedicamentoName: string = '';
  novoMedicamentoDosage: string = '';
  novoMedicamentoFrequency: string = '';

  constructor(
    private route: ActivatedRoute,
    private pacientesReadService: PacientesReadService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const id = this.route.parent?.snapshot.paramMap.get('id');
    this.pacienteId = id ?? '';
    // Buscar dados do paciente
    this.pacientesReadService.getPacienteById(this.pacienteId).subscribe({
      next: (response) => {
        const paciente = response.data || response;
        this.pacienteNome = paciente.nome || paciente.name || 'Paciente';
      },
      error: (error) => console.error('Erro ao carregar paciente:', error)
    });
    
    // Buscar medicamentos do novo endpoint
    this.http.get<any>(`http://localhost:8080/patients/${this.pacienteId}/medications`).subscribe({
      next: (response) => {
        this.medicamentos = response.data || response || [];
      },
      error: (error) => {
        console.error('Erro ao carregar medicamentos:', error);
        this.medicamentos = [];
      }
    });
  }

  adicionarMedicamento() {
    if (!this.novoMedicamentoName.trim() || !this.novoMedicamentoDosage.trim() || !this.novoMedicamentoFrequency.trim()) return;
    
    const novo = {
      name: this.novoMedicamentoName.trim(),
      dosage: this.novoMedicamentoDosage.trim(),
      frequency: this.novoMedicamentoFrequency.trim()
    };

    // Adicionar localmente
    this.medicamentos = [...this.medicamentos, novo];
    this.novoMedicamentoName = '';
    this.novoMedicamentoDosage = '';
    this.novoMedicamentoFrequency = '';
    
    console.log('Medicamento adicionado localmente. Implementar endpoint POST se necessário.');
  }

  removerMedicamento(index: number) {
    const novosMedicamentos = this.medicamentos.slice();
    novosMedicamentos.splice(index, 1);
    this.medicamentos = novosMedicamentos;
    
    console.log('Medicamento removido localmente. Implementar endpoint DELETE se necessário.');
  }
}