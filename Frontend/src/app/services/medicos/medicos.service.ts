import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MedicosService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // Buscar médicos autorizados para um paciente específico
  getMedicosAutorizados(pacienteId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/patients/${pacienteId}/authorized-doctors`);
  }

  // Buscar pacientes autorizados para um médico específico (versão simplificada)
  getPacientesAutorizados(medicoId: string): Observable<any[]> {
    // Por enquanto, dados simulados até termos a API correta
    return new Observable(observer => {
      const pacientesSimulados = [
        {
          id: 1,
          nome: 'Ana Silva',
          idade: 34,
          avatar: 'https://via.placeholder.com/40/3498db/ffffff?text=AS'
        },
        {
          id: 2,
          nome: 'João Santos',
          idade: 45,
          avatar: 'https://via.placeholder.com/40/e74c3c/ffffff?text=JS'
        }
      ];
      
      setTimeout(() => {
        observer.next(pacientesSimulados);
        observer.complete();
      }, 500);
    });
  }

  // Buscar todos os médicos
  getMedicos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/medic`);
  }

  // Buscar médico específico
  getMedico(medicoId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/medic/${medicoId}`);
  }

  // Buscar dados do perfil do médico (caso seja diferente dos dados básicos)
  getPerfilMedico(medicoId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/medic/${medicoId}/profile`);
  }
}
