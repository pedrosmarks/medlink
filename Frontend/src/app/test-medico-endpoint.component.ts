import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-test-medico',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mt-4">
      <h3>🔍 Teste Endpoint Médico</h3>
      
      <button (click)="testMedico()" class="btn btn-primary">
        Testar GET /medic/1
      </button>
      
      <div class="mt-3" *ngIf="result">
        <h5>Resultado:</h5>
        <pre>{{ result | json }}</pre>
      </div>
      
      <div class="mt-3" *ngIf="error">
        <div class="alert alert-danger">
          <strong>Erro:</strong> {{ error }}
        </div>
      </div>
    </div>
  `
})
export class TestMedicoEndpointComponent {
  result: any = null;
  error: string = '';

  constructor(private http: HttpClient) {}

  testMedico() {
    this.result = null;
    this.error = '';
    
    this.http.get('http://localhost:8080/medic/1').subscribe({
      next: (response) => {
        this.result = response;
      },
      error: (error) => {
        this.error = `Status ${error.status}: ${error.statusText}`;
      }
    });
  }
}