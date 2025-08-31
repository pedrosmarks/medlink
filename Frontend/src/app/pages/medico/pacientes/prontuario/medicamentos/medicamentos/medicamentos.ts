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
    
    this.carregarMedicamentos();
  }

  carregarMedicamentos() {
    this.http.get<any>(`http://localhost:8080/api/patients/${this.pacienteId}/medications`).subscribe({
      next: (response) => {
        this.medicamentos = response.data || response || [];
        console.log('Medicamentos carregados:', this.medicamentos.length);
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

    const url = `http://localhost:8080/api/patients/${this.pacienteId}/medications`;
    console.log('Adicionando medicamento:', url, novo);
    
    this.http.post<any>(url, novo).subscribe({
      next: (response) => {
        console.log('Medicamento adicionado:', response);
        this.carregarMedicamentos(); // Recarrega a lista
        this.novoMedicamentoName = '';
        this.novoMedicamentoDosage = '';
        this.novoMedicamentoFrequency = '';
      },
      error: (error) => {
        console.error('Erro ao adicionar medicamento:', error);
        // Fallback: adiciona localmente se backend falhar
        this.medicamentos = [...this.medicamentos, novo];
        this.novoMedicamentoName = '';
        this.novoMedicamentoDosage = '';
        this.novoMedicamentoFrequency = '';
        console.log('Medicamento adicionado localmente (fallback).');
      }
    });
  }

  removerMedicamento(index: number) {
    const medicamento = this.medicamentos[index];
    if (!medicamento || !medicamento.name) {
      console.error('Medicamento não encontrado ou sem nome');
      return;
    }
    
    const url = `http://localhost:8080/api/patients/${this.pacienteId}/medications/${encodeURIComponent(medicamento.name)}`;
    console.log('Removendo medicamento:', url);
    
    this.http.delete<any>(url).subscribe({
      next: (response) => {
        console.log('Medicamento removido:', response);
        this.carregarMedicamentos(); // Recarrega a lista
      },
      error: (error) => {
        console.error('Erro ao remover medicamento:', error);
        // Fallback: remove localmente se backend falhar
        const novosMedicamentos = this.medicamentos.slice();
        novosMedicamentos.splice(index, 1);
        this.medicamentos = novosMedicamentos;
        console.log('Medicamento removido localmente (fallback).');
      }
    });
  }
}