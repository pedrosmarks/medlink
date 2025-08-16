
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PacientesUpdateService {
  private apiUrl = 'http://localhost:8080/pacientes';

  constructor(private http: HttpClient) {}

  updatePaciente(id: number, changes: any): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${id}`, changes);
  }
}