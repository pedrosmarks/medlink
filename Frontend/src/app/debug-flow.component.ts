import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccessRequestsService } from './services/access-requests/access-requests.service';

@Component({
  selector: 'app-debug-flow',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mt-4">
      <h3>🔍 Debug Fluxo Completo</h3>
      
      <div class="row">
        <div class="col-md-4">
          <div class="card">
            <div class="card-header">1. Enviar Requisição</div>
            <div class="card-body">
              <button (click)="step1()" class="btn btn-primary">
                POST /api/patients/2/access-request
              </button>
              <div class="mt-2" [ngClass]="step1Status.class">{{ step1Status.message }}</div>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card">
            <div class="card-header">2. Verificar Pendentes</div>
            <div class="card-body">
              <button (click)="step2()" class="btn btn-info">
                GET /api/patients/2/pending-requests
              </button>
              <div class="mt-2" [ngClass]="step2Status.class">{{ step2Status.message }}</div>
              <pre *ngIf="pendingData">{{ pendingData | json }}</pre>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card">
            <div class="card-header">3. Aprovar</div>
            <div class="card-body">
              <button (click)="step3()" class="btn btn-success">
                PUT /api/patients/2/access-request/1?action=approve
              </button>
              <div class="mt-2" [ngClass]="step3Status.class">{{ step3Status.message }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="mt-4">
        <button (click)="runFullFlow()" class="btn btn-warning btn-lg">
          🚀 Executar Fluxo Completo
        </button>
      </div>
    </div>
  `
})
export class DebugFlowComponent {
  step1Status = { message: 'Não executado', class: 'text-muted' };
  step2Status = { message: 'Não executado', class: 'text-muted' };
  step3Status = { message: 'Não executado', class: 'text-muted' };
  pendingData: any = null;

  constructor(private accessRequestsService: AccessRequestsService) {}

  step1() {
    this.step1Status = { message: 'Enviando...', class: 'text-info' };
    this.accessRequestsService.sendAccessRequest(2, 1).subscribe({
      next: (response) => {
        this.step1Status = { message: '✅ Requisição enviada', class: 'text-success' };
        console.log('Step 1 response:', response);
      },
      error: (error) => {
        this.step1Status = { message: `❌ Erro: ${error.status}`, class: 'text-danger' };
        console.error('Step 1 error:', error);
      }
    });
  }

  step2() {
    this.step2Status = { message: 'Verificando...', class: 'text-info' };
    this.accessRequestsService.getPendingRequests(2).subscribe({
      next: (response) => {
        this.pendingData = response;
        const count = response.data?.length || 0;
        this.step2Status = { message: `✅ ${count} requisição(s) pendente(s)`, class: 'text-success' };
        console.log('Step 2 response:', response);
      },
      error: (error) => {
        this.step2Status = { message: `❌ Erro: ${error.status}`, class: 'text-danger' };
        console.error('Step 2 error:', error);
      }
    });
  }

  step3() {
    this.step3Status = { message: 'Aprovando...', class: 'text-info' };
    this.accessRequestsService.approveRequest(2, 1).subscribe({
      next: (response) => {
        this.step3Status = { message: '✅ Aprovado com sucesso', class: 'text-success' };
        console.log('Step 3 response:', response);
      },
      error: (error) => {
        this.step3Status = { message: `❌ Erro: ${error.status}`, class: 'text-danger' };
        console.error('Step 3 error:', error);
      }
    });
  }

  runFullFlow() {
    console.log('🚀 Iniciando fluxo completo...');
    
    // Step 1: Enviar requisição
    this.step1();
    
    // Step 2: Aguardar 1s e verificar pendentes
    setTimeout(() => {
      this.step2();
      
      // Step 3: Aguardar mais 1s e aprovar
      setTimeout(() => {
        this.step3();
      }, 1000);
    }, 1000);
  }
}