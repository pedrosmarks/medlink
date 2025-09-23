import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, switchMap, of, forkJoin, catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MedicosService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // Buscar médicos autorizados para um paciente específico
  getMedicosAutorizados(pacienteId: string): Observable<number[]> {
    // Busca os médicos autorizados via endpoint dedicado
    return this.http.get<any>(`${this.apiUrl}/api/patients/${pacienteId}/authorized-doctors`).pipe(
      map(response => {
        if (response && response.data && Array.isArray(response.data)) {
          // Retorna apenas os IDs dos médicos autorizados
          return response.data.map((medico: any) => medico.id);
        }
        return [];
      })
    );
  }

  /**
   * Retorna os objetos completos dos médicos autorizados para um paciente.
   * - Obtém os IDs via /api/patients/{id} e busca cada médico com /api/medic/{id}.
   */
  getMedicosAutorizadosCompletos(pacienteId: string): Observable<any[]> {
    // Obtém IDs dos médicos autorizados do paciente e busca dados completos de cada médico
    return this.getMedicosAutorizados(pacienteId).pipe(
      switchMap((ids: number[]) => {
        if (!ids || ids.length === 0) return of([]);
        const calls = ids.map(id => this.getMedico(String(id)).pipe(
          map(response => response && response.data ? response.data : response),
          catchError(error => {
            console.error(`Erro ao buscar médico ${id}:`, error);
            return of(null);
          })
        ));
        return forkJoin(calls).pipe(
          map(arr => (arr || []).filter(m => m))
        );
      })
    );
  }

  // Buscar pacientes autorizados para um médico específico
  getPacientesAutorizados(medicoId: string): Observable<any[]> {
    return this.http.get<any>(`${this.apiUrl}/api/medic/${medicoId}/patients`).pipe(
      map(response => {
        // Handle both array responses and object responses with a data property
        if (Array.isArray(response)) {
          return response;
        } else if (response && response.data && Array.isArray(response.data)) {
          return response.data;
        }
        return [];
      })
    );
  }

  // Buscar todos os médicos
  getMedicos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/api/medic`);
  }

  // Buscar médico específico
  getMedico(medicoId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/medic/${medicoId}`);
  }

  // Buscar dados do perfil do médico (caso seja diferente dos dados básicos)
  getPerfilMedico(medicoId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/medic/${medicoId}/profile`);
  }

  // Revogar acesso de um médico aos dados do paciente
  revogarAcesso(pacienteId: string, medicoId: string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/api/patients/${pacienteId}/doctors/${medicoId}/access`);
  }
}
