import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccessRequest, ApiResponse, Patient } from '../../models/access-request.interface';

@Injectable({
  providedIn: 'root'
})
export class AccessRequestsService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // Buscar pacientes por nome
  searchPatients(name: string): Observable<ApiResponse<Patient[]>> {
    return this.http.get<ApiResponse<Patient[]>>(`${this.baseUrl}/patients/search?name=${encodeURIComponent(name)}`);
  }

  // Enviar requisição de acesso
  sendAccessRequest(patientId: number, medicoId: number): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/patients/${patientId}/access-request`, {
      medicoId,
      status: 'pendente'
    });
  }

  // Verificar notificações pendentes
  getPendingRequests(patientId: number): Observable<ApiResponse<AccessRequest[]>> {
    return this.http.get<ApiResponse<AccessRequest[]>>(`${this.baseUrl}/patients/${patientId}/pending-requests`);
  }



  // Aprovar requisição
  approveRequest(patientId: number, medicId: number): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/patients/${patientId}/access-request/${medicId}?action=approve`, {});
  }

  // Rejeitar requisição
  rejectRequest(patientId: number, medicId: number): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/patients/${patientId}/access-request/${medicId}?action=reject`, {});
  }
}