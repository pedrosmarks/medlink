import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MedicosService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // Buscar médicos autorizados para um paciente específico
  getMedicosAutorizados(pacienteId: string): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8080/patient/${pacienteId}/authorized-doctors`);
  }

  // Buscar todos os médicos
  getMedicos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/medicos`);
  }

  // Buscar médico específico
  getMedico(medicoId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/medicos/${medicoId}`);
  }

  // Buscar dados do perfil do médico (caso seja diferente dos dados básicos)
  getPerfilMedico(medicoId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/medicos/${medicoId}/perfil`);
  }
}
