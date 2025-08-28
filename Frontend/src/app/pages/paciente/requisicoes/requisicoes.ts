import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccessRequestsService } from '../../../services/access-requests/access-requests.service';
import { AccessRequest } from '../../../models/access-request.interface';

@Component({
  selector: 'app-requisicoes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './requisicoes.html',
  styleUrls: ['./requisicoes.css']
})
export class Requisicoes implements OnInit, OnDestroy {
  requisicoes: AccessRequest[] = [];
  pacienteId: number = 0;
  loading = false;
  private refreshInterval: any;

  constructor(private accessRequestsService: AccessRequestsService) {}

  ngOnInit(): void {
    this.pacienteId = parseInt(localStorage.getItem('userId') || '0');
    this.carregarRequisicoes();
    this.startAutoRefresh();
  }

  ngOnDestroy(): void {
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
    }
  }

  private startAutoRefresh(): void {
    this.refreshInterval = setInterval(() => {
      this.carregarRequisicoes();
    }, 15000); // Atualiza a cada 15 segundos
  }

  carregarRequisicoes(): void {
    this.loading = true;
    console.log('🔍 Carregando requisições para paciente:', this.pacienteId);
    
    this.accessRequestsService.getPendingRequests(this.pacienteId).subscribe({
      next: (response) => {
        console.log('✅ Requisições carregadas:', response);
        this.requisicoes = response.data || [];
        console.log('📊 Total de requisições:', this.requisicoes.length);
        this.loading = false;
      },
      error: (error) => {
        console.warn('❌ Erro ao carregar requisições:', {
          status: error.status,
          statusText: error.statusText,
          url: error.url
        });
        this.requisicoes = [];
        this.loading = false;
      }
    });
  }

  getMedicoInfo(request: AccessRequest): string {
    if (request.medicoName && request.medicoSpecialty) {
      return `${request.medicoName} - ${request.medicoSpecialty}`;
    }
    return `Médico ID: ${request.medicoId}`;
  }

  getMedicoMessage(request: AccessRequest): string {
    if (request.medicoName && request.medicoSpecialty) {
      return `${request.medicoName} - ${request.medicoSpecialty} está solicitando acesso ao seu prontuário médico.`;
    }
    return 'Este médico está solicitando acesso ao seu prontuário médico.';
  }

  aprovar(request: AccessRequest): void {
    console.log('✅ Aprovando requisição:', {
      pacienteId: this.pacienteId,
      medicoId: request.medicoId,
      endpoint: `PUT /api/patients/${this.pacienteId}/access-request/${request.medicoId}?action=approve`
    });
    
    this.accessRequestsService.approveRequest(this.pacienteId, request.medicoId).subscribe({
      next: (response) => {
        console.log('✅ Requisição aprovada:', response);
        alert(response.message + '\n\nO médico agora aparece na sua lista de médicos!');
        this.carregarRequisicoes();
        
        // Forçar atualização da lista de médicos
        setTimeout(() => {
          window.dispatchEvent(new CustomEvent('medico-aprovado'));
        }, 500);
      },
      error: (error) => {
        console.error('❌ Erro ao aprovar:', {
          status: error.status,
          statusText: error.statusText,
          error: error.error,
          url: error.url
        });
        alert(`Erro ao aprovar: ${error.status} - ${error.statusText}`);
      }
    });
  }

  recusar(request: AccessRequest): void {
    console.log('❌ Rejeitando requisição:', {
      pacienteId: this.pacienteId,
      medicoId: request.medicoId,
      endpoint: `PUT /api/patients/${this.pacienteId}/access-request/${request.medicoId}?action=reject`
    });
    
    this.accessRequestsService.rejectRequest(this.pacienteId, request.medicoId).subscribe({
      next: (response) => {
        console.log('❌ Requisição rejeitada:', response);
        alert(response.message);
        this.carregarRequisicoes();
      },
      error: (error) => {
        console.error('❌ Erro ao rejeitar:', {
          status: error.status,
          statusText: error.statusText,
          error: error.error,
          url: error.url
        });
        alert(`Erro ao rejeitar: ${error.status} - ${error.statusText}`);
      }
    });
  }
}