import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AccessRequestsService } from './services/access-requests/access-requests.service';

@Component({
  selector: 'app-test-endpoints',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container mt-4">
      <h3>Teste dos Endpoints</h3>
      
      <div class="card mb-3">
        <div class="card-header">1. Buscar Pacientes</div>
        <div class="card-body">
          <input [(ngModel)]="searchName" placeholder="Nome do paciente" class="form-control mb-2">
          <button (click)="testSearchPatients()" class="btn btn-primary">Buscar</button>
          <pre *ngIf="searchResult">{{ searchResult | json }}</pre>
        </div>
      </div>

      <div class="card mb-3">
        <div class="card-header">2. Enviar Requisição</div>
        <div class="card-body">
          <input [(ngModel)]="patientId" placeholder="ID do Paciente" type="number" class="form-control mb-2">
          <input [(ngModel)]="medicoId" placeholder="ID do Médico" type="number" class="form-control mb-2">
          <button (click)="testSendRequest()" class="btn btn-success">Enviar Requisição</button>
          <pre *ngIf="sendResult">{{ sendResult | json }}</pre>
        </div>
      </div>

      <div class="card mb-3">
        <div class="card-header">3. Verificar Notificações</div>
        <div class="card-body">
          <input [(ngModel)]="checkPatientId" placeholder="ID do Paciente" type="number" class="form-control mb-2">
          <button (click)="testPendingRequests()" class="btn btn-info">Verificar</button>
          <pre *ngIf="pendingResult">{{ pendingResult | json }}</pre>
        </div>
      </div>

      <div class="card mb-3">
        <div class="card-header">4. Aprovar/Rejeitar</div>
        <div class="card-body">
          <input [(ngModel)]="actionPatientId" placeholder="ID do Paciente" type="number" class="form-control mb-2">
          <input [(ngModel)]="actionMedicoId" placeholder="ID do Médico" type="number" class="form-control mb-2">
          <button (click)="testApprove()" class="btn btn-success me-2">Aprovar</button>
          <button (click)="testReject()" class="btn btn-danger">Rejeitar</button>
          <pre *ngIf="actionResult">{{ actionResult | json }}</pre>
        </div>
      </div>
    </div>
  `
})
export class TestEndpointsComponent {
  searchName = 'm';
  searchResult: any;
  
  patientId = 2;
  medicoId = 1;
  sendResult: any;
  
  checkPatientId = 2;
  pendingResult: any;
  
  actionPatientId = 2;
  actionMedicoId = 1;
  actionResult: any;

  constructor(private accessRequestsService: AccessRequestsService) {}

  testSearchPatients() {
    this.accessRequestsService.searchPatients(this.searchName).subscribe({
      next: (result) => this.searchResult = result,
      error: (error) => this.searchResult = { error: error.message }
    });
  }

  testSendRequest() {
    this.accessRequestsService.sendAccessRequest(this.patientId, this.medicoId).subscribe({
      next: (result) => this.sendResult = result,
      error: (error) => this.sendResult = { error: error.message }
    });
  }

  testPendingRequests() {
    this.accessRequestsService.getPendingRequests(this.checkPatientId).subscribe({
      next: (result) => this.pendingResult = result,
      error: (error) => this.pendingResult = { error: error.message }
    });
  }

  testApprove() {
    this.accessRequestsService.approveRequest(this.actionPatientId, this.actionMedicoId).subscribe({
      next: (result) => this.actionResult = result,
      error: (error) => this.actionResult = { error: error.message }
    });
  }

  testReject() {
    this.accessRequestsService.rejectRequest(this.actionPatientId, this.actionMedicoId).subscribe({
      next: (result) => this.actionResult = result,
      error: (error) => this.actionResult = { error: error.message }
    });
  }
}