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
    return this.http.get<any[]>(`${this.apiUrl}/api/patients/${pacienteId}/authorized-doctors`);
  }

  // Buscar pacientes autorizados para um médico específico
  getPacientesAutorizados(medicoId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/api/medic/${medicoId}/patients`);
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
