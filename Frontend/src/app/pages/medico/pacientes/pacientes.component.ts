import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PacientesReadService } from '../../../services/pacientes/pacientes-read.service';
import { PacientesUpdateService } from '../../../services/pacientes/pacientes-update';
import { AccessRequestsService } from '../../../services/access-requests/access-requests.service';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Patient } from '../../../domain/models/access-request.interface';

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
    // Corrigido: usar medicoId específico para médicos
    this.medicoId = parseInt(localStorage.getItem('medicoId') || localStorage.getItem('userId') || '1');
    console.log('🚀 CARREGANDO PACIENTES REAIS - Médico ID:', this.medicoId);
    console.log('🔍 DEBUG localStorage medicoId:', localStorage.getItem('medicoId'));
    console.log('🔍 DEBUG localStorage userId:', localStorage.getItem('userId'));
    console.log('🔍 DEBUG userProfile:', localStorage.getItem('userProfile'));
    console.log('🔍 DEBUG userType:', localStorage.getItem('userType'));
    
    this.carregarPacientes();
    
    // Recarregar quando houver aprovações
    setInterval(() => {
      this.carregarPacientes();
    }, 30000); // A cada 30 segundos
  }

  carregarPacientes(): void {
    console.log('🔄 RECARREGANDO PACIENTES - Médico ID:', this.medicoId);
    console.log('🌐 URL da requisição:', `http://localhost:8080/api/medic/${this.medicoId}/patients`);
    console.log('🔍 localStorage completo:', {
      userId: localStorage.getItem('userId'),
      userProfile: localStorage.getItem('userProfile'),
      medicoNome: localStorage.getItem('medicoNome')
    });
    
    this.pacientesReadService.getPacientes().subscribe({
      next: (response: any) => {
        console.log('✅ RESPONSE COMPLETO DO BACKEND:', response);
        console.log('✅ response.data:', response.data);
        console.log('✅ Quantidade de pacientes retornados:', response.data?.length || 0);
        console.log('✅ Timestamp:', new Date().toLocaleTimeString());
        
        // Log dos IDs dos pacientes retornados
        if (response.data && response.data.length > 0) {
          console.log('📋 IDs dos pacientes retornados:');
          response.data.forEach((p: any, i: number) => {
            console.log(`  Paciente ${i + 1}: ID=${p.id}, Nome=${p.name}`);
          });
        } else {
          console.log('⚠️ NENHUM PACIENTE RETORNADO PELO BACKEND!');
        }
        
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
    console.log('🔍 BUSCANDO PACIENTES:', termo);
    
    if (termo.length < 2) {
      this.resultadosBusca = [];
      return;
    }

    this.buscandoPacientes = true;
    console.log('📡 Fazendo chamada para:', `http://localhost:8080/api/patients/search?name=${encodeURIComponent(termo)}`);
    
    this.accessRequestsService.searchPatients(termo).subscribe({
      next: (response) => {
        console.log('✅ RESPOSTA DA BUSCA:', response);
        console.log('✅ DADOS DA BUSCA:', response.data);
        this.resultadosBusca = response.data || [];
        console.log('✅ RESULTADOS FINAIS:', this.resultadosBusca);
        this.buscandoPacientes = false;
      },
      error: (error) => {
        console.error('❌ ERRO NA BUSCA:', {
          status: error.status,
          statusText: error.statusText,
          message: error.message,
          url: error.url,
          error: error.error
        });
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

  debugPaciente(paciente: any) {
    console.log('🔍 DEBUG PACIENTE:');
    console.log('Paciente completo:', paciente);
    console.log('ID do paciente:', paciente.id);
    console.log('Tipo do ID:', typeof paciente.id);
    console.log('ID é undefined?', paciente.id === undefined);
    console.log('ID é null?', paciente.id === null);
  }
}