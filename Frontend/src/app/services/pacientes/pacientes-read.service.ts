import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PacientesReadService {

  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getPacientes(): Observable<any[]> {
    // Busca ID do médico logado
    const medicoId = localStorage.getItem('userId');
    
    if (!medicoId) {
      throw new Error('ID do médico não encontrado');
    }
    
    const url = `${this.baseUrl}/medic/${medicoId}/patients`;
    console.log('Buscando pacientes em:', url);
    
    return this.http.get<any[]>(url);
  }

  getPacienteById(id: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/api/patients/${id}`);
  }

  updatePaciente(id: number, changes: any): Observable<any> {
    return this.http.patch(`${this.baseUrl}/api/patients/${id}`, changes);
  }

  buscarTodosPacientes(termo: string): Observable<any> {
    // Endpoint para buscar TODOS os pacientes do sistema
    const url = `${this.baseUrl}/api/patients/search?name=${encodeURIComponent(termo)}`;
    console.log('Buscando todos os pacientes:', url);
    return this.http.get<any>(url);
  }
}