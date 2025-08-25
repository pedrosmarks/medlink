import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccessRequestsService } from '../../../services/access-requests/access-requests.service';
import { AccessRequest } from '../../../models/access-request.interface';

@Component({
  selector: 'app-notificacoes',
  imports: [CommonModule],
  templateUrl: './notificacoes.html',
  styleUrl: './notificacoes.css'
})
export class Notificacoes implements OnInit {
  pendingRequests: AccessRequest[] = [];
  pacienteId: number = 0;
  loading = false;
  @Output() notificationUpdated = new EventEmitter<void>();

  constructor(private accessRequestsService: AccessRequestsService) {}

  ngOnInit(): void {
    this.pacienteId = parseInt(localStorage.getItem('userId') || '0');
    this.loadPendingRequests();
  }

  loadPendingRequests(): void {
    this.loading = true;
    this.accessRequestsService.getPendingRequests(this.pacienteId).subscribe({
      next: (response) => {
        this.pendingRequests = response.data || [];
        this.loading = false;
      },
      error: (error) => {
        console.warn('Backend não implementado ainda:', error.status);
        this.pendingRequests = [];
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

  approveRequest(request: AccessRequest): void {
    this.accessRequestsService.approveRequest(this.pacienteId, request.medicoId).subscribe({
      next: (response) => {
        alert(response.message);
        this.loadPendingRequests();
        this.notificationUpdated.emit();
      },
      error: (error) => {
        console.error('Erro ao aprovar requisição:', error);
        alert('Erro ao aprovar requisição');
      }
    });
  }

  rejectRequest(request: AccessRequest): void {
    this.accessRequestsService.rejectRequest(this.pacienteId, request.medicoId).subscribe({
      next: (response) => {
        alert(response.message);
        this.loadPendingRequests();
        this.notificationUpdated.emit();
      },
      error: (error) => {
        console.error('Erro ao rejeitar requisição:', error);
        alert('Erro ao rejeitar requisição');
      }
    });
  }
}
