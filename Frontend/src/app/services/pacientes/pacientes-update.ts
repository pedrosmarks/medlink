
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PacientesUpdateService {
  buscarPaciente(paciente: any) {
    throw new Error('Method not implemented.');
  }
  private apiUrl = 'http://localhost:8080/api/patients';

  constructor(private http: HttpClient) {}

  updatePaciente(id: number, changes: any): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${id}`, changes);
  }
}