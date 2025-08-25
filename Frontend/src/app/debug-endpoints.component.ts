import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-debug-endpoints',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mt-4">
      <h3>🔍 Debug dos Endpoints</h3>
      
      <div class="alert alert-info">
        <strong>Status dos Endpoints:</strong>
      </div>

      <div class="row">
        <div class="col-md-6">
          <div class="card">
            <div class="card-header">1. Buscar Pacientes</div>
            <div class="card-body">
              <button (click)="testSearch()" class="btn btn-primary">Testar GET /api/patients/search?name=m</button>
              <div class="mt-2" [ngClass]="searchStatus.class">{{ searchStatus.message }}</div>
            </div>
          </div>
        </div>

        <div class="col-md-6">
          <div class="card">
            <div class="card-header">2. Notificações Pendentes</div>
            <div class="card-body">
              <button (click)="testNotifications()" class="btn btn-info">Testar GET /api/patients/2/pending-requests</button>
              <div class="mt-2" [ngClass]="notificationStatus.class">{{ notificationStatus.message }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="row mt-3">
        <div class="col-md-6">
          <div class="card">
            <div class="card-header">3. Enviar Requisição</div>
            <div class="card-body">
              <button (click)="testSendRequest()" class="btn btn-success">Testar POST /api/patients/2/access-request</button>
              <div class="mt-2" [ngClass]="sendStatus.class">{{ sendStatus.message }}</div>
            </div>
          </div>
        </div>

        <div class="col-md-6">
          <div class="card">
            <div class="card-header">4. Aprovar Requisição</div>
            <div class="card-body">
              <button (click)="testApprove()" class="btn btn-warning">Testar PUT /api/patients/2/access-request/1?action=approve</button>
              <div class="mt-2" [ngClass]="approveStatus.class">{{ approveStatus.message }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class DebugEndpointsComponent {
  searchStatus = { message: 'Não testado', class: 'text-muted' };
  notificationStatus = { message: 'Não testado', class: 'text-muted' };
  sendStatus = { message: 'Não testado', class: 'text-muted' };
  approveStatus = { message: 'Não testado', class: 'text-muted' };

  constructor(private http: HttpClient) {}

  testSearch() {
    this.searchStatus = { message: 'Testando...', class: 'text-info' };
    this.http.get('http://localhost:8080/api/patients/search?name=m').subscribe({
      next: (response) => {
        this.searchStatus = { message: '✅ Funcionando', class: 'text-success' };
      },
      error: (error) => {
        this.searchStatus = { 
          message: `❌ Erro ${error.status}: ${error.statusText}`, 
          class: 'text-danger' 
        };
      }
    });
  }

  testNotifications() {
    this.notificationStatus = { message: 'Testando...', class: 'text-info' };
    this.http.get('http://localhost:8080/api/patients/2/pending-requests').subscribe({
      next: (response) => {
        this.notificationStatus = { message: '✅ Funcionando', class: 'text-success' };
      },
      error: (error) => {
        this.notificationStatus = { 
          message: `❌ Erro ${error.status}: ${error.statusText}`, 
          class: 'text-danger' 
        };
      }
    });
  }

  testSendRequest() {
    this.sendStatus = { message: 'Testando...', class: 'text-info' };
    this.http.post('http://localhost:8080/api/patients/2/access-request', {
      medicoId: 1,
      status: 'pendente'
    }).subscribe({
      next: (response) => {
        this.sendStatus = { message: '✅ Funcionando', class: 'text-success' };
      },
      error: (error) => {
        this.sendStatus = { 
          message: `❌ Erro ${error.status}: ${error.statusText}`, 
          class: 'text-danger' 
        };
      }
    });
  }

  testApprove() {
    this.approveStatus = { message: 'Testando...', class: 'text-info' };
    this.http.put('http://localhost:8080/api/patients/2/access-request/1?action=approve', {}).subscribe({
      next: (response) => {
        this.approveStatus = { message: '✅ Funcionando', class: 'text-success' };
      },
      error: (error) => {
        this.approveStatus = { 
          message: `❌ Erro ${error.status}: ${error.statusText}`, 
          class: 'text-danger' 
        };
      }
    });
  }
}