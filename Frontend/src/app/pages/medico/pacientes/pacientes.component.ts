import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PacientesReadService } from '../../../services/pacientes/pacientes-read.service';
import { PacientesUpdateService } from '../../../services/pacientes/pacientes-update';
import { AccessRequestsService } from '../../../services/access-requests/access-requests.service';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Patient } from '../../../models/access-request.interface';

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
  pacientesFiltrados: any[] = [];
  medicoId: number = 1;

  // Busca de pacientes
  buscaNome = '';
  resultadosBusca: Patient[] = [];
  buscandoPacientes = false;

  constructor(
    private pacientesReadService: PacientesReadService,
    private pacientesUpdateService: PacientesUpdateService,
    private accessRequestsService: AccessRequestsService
  ) {}

  ngOnInit(): void {
    this.medicoId = parseInt(localStorage.getItem('userId') || '1');
    console.log('🚀 CARREGANDO PACIENTES REAIS - Médico ID:', this.medicoId);
    
    this.pacientesReadService.getPacientes().subscribe({
      next: (response: any) => {
        console.log('✅ Response do backend:', response);
        console.log('✅ Dados recebidos:', response.data);
        
        // LOG DETALHADO DOS CAMPOS
        response.data.forEach((paciente: any, index: number) => {
          console.log(`📋 PACIENTE ${index + 1}:`);
          console.log('Todos os campos:', Object.keys(paciente));
          console.log('Dados completos:', paciente);
        });
        
        // Processa dados e adiciona avatar padrão se não existir
        this.pacientes = response.data.map((paciente: any) => ({
          ...paciente,
          avatar: paciente.avatar || 'https://cdn-icons-png.flaticon.com/512/921/921347.png'
        }));
        this.todosPacientes = this.pacientes;
        this.pacientesFiltrados = this.pacientes;
        
        console.log('🎯 PACIENTES FINAIS:', this.pacientes);
        console.log('🎯 QUANTIDADE FINAL:', this.pacientes.length);
      },
      error: (error) => {
        console.error('❌ Erro ao carregar:', error);
      }
    });
  }

  pesquisarPacientes() {
    const termo = this.buscaNome.trim();
    if (termo.length < 2) {
      this.resultadosBusca = [];
      return;
    }

    this.buscandoPacientes = true;
    this.accessRequestsService.searchPatients(termo).subscribe({
      next: (response) => {
        this.resultadosBusca = response.data || [];
        this.buscandoPacientes = false;
      },
      error: (error) => {
        console.error('Erro ao buscar pacientes:', error);
        this.buscandoPacientes = false;
      }
    });
  }

  solicitarAcesso(paciente: Patient) {
    console.log('🚀 Solicitando acesso:', {
      pacienteId: paciente.id,
      medicoId: this.medicoId,
      endpoint: `POST /api/patients/${paciente.id}/access-request`
    });
    
    this.accessRequestsService.sendAccessRequest(paciente.id, this.medicoId).subscribe({
      next: (response) => {
        console.log('✅ Requisição enviada com sucesso:', response);
        alert(response.message);
        this.resultadosBusca = this.resultadosBusca.filter(p => p.id !== paciente.id);
      },
      error: (error) => {
        console.error('❌ Erro ao solicitar acesso:', {
          status: error.status,
          statusText: error.statusText,
          error: error.error,
          url: error.url
        });
        alert(`Erro ao solicitar acesso: ${error.status} - ${error.statusText}`);
      }
    });
  }

  limparBusca() {
    this.buscaNome = '';
    this.resultadosBusca = [];
  }
}