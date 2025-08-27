import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { HttpClient } from '@angular/common/http';

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
    private http: HttpClient
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
    const url = `http://localhost:8080/patients/${this.pacienteId}/vaccines`;
    console.log('Carregando vacinas de:', url);
    
    this.http.get<any>(url).subscribe({
      next: (response) => {
        console.log('Vacinas recebidas:', response);
        this.vacinas = response.data || response;
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
      date: this.novaVacinaData,
      status: 'Aplicada'
    };
    
    const url = `http://localhost:8080/api/patients/${this.pacienteId}/vaccines`;
    console.log('Adicionando vacina:', url, novaVacina);
    
    this.http.post<any>(url, novaVacina).subscribe({
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
      console.error('Vacina sem ID para remoção');
      return;
    }
    
    const url = `http://localhost:8080/api/patients/${this.pacienteId}/vaccines/${vacina.id}`;
    console.log('Removendo vacina:', url);
    
    this.http.delete<any>(url).subscribe({
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