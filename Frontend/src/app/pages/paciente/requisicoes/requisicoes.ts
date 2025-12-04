import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccessRequestsService } from '../../../services/access-requests/access-requests.service';
import { AccessRequest } from '../../../domain/models/access-request.interface';

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
    
    this.accessRequestsService.getPendingRequests(this.pacienteId).subscribe({
      next: (response) => {
        this.requisicoes = (response.data || []).map(req => ({
          ...req,
          status: (['pendente', 'aprovado', 'rejeitado'].includes(req.status?.toLowerCase())
            ? req.status.toLowerCase()
            : 'pendente') as 'pendente' | 'aprovado' | 'rejeitado'
        }));
        this.loading = false;
      },
      error: () => {
        // Tratar erro silenciosamente ou mostrar mensagem ao usuário
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

    
    // Remove imediatamente da lista local para melhor UX
    this.requisicoes = this.requisicoes.filter(req => req.medicoId !== request.medicoId);
    
    this.accessRequestsService.approveRequest(this.pacienteId, request.medicoId).subscribe({
      next: (response) => {

        alert(response.message + '\n\nO médico agora aparece na sua lista de médicos!');
        // Recarrega para garantir sincronização com o backend
        this.carregarRequisicoes();
        // Notifica outros componentes que um médico foi aprovado
        window.dispatchEvent(new CustomEvent('medico-aprovado'));
      },
      error: () => {
        // Em caso de erro, recarrega a lista para voltar ao estado original
        this.carregarRequisicoes();
        alert('Erro ao aprovar a solicitação. Por favor, tente novamente.');
      }
    });
  }

  recusar(request: AccessRequest): void {

    
    // Remove imediatamente da lista local para melhor UX
    this.requisicoes = this.requisicoes.filter(req => req.medicoId !== request.medicoId);
    
    this.accessRequestsService.rejectRequest(this.pacienteId, request.medicoId).subscribe({
      next: (response) => {
        alert(response.message);
        // Recarrega para garantir sincronização com o backend
        this.carregarRequisicoes();
      },
      error: () => {
        // Em caso de erro, recarrega a lista para voltar ao estado original
        this.carregarRequisicoes();
        alert('Erro ao rejeitar a solicitação. Por favor, tente novamente.');
      }
    });
  }
}