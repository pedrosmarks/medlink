import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PacientesReadService {

  private apiUrl = 'http://localhost:8080/api/pacientes';

  constructor(private http: HttpClient) { }

  getPacientes(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  updatePaciente(id: number, changes: any): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${id}`, changes);
  }
}