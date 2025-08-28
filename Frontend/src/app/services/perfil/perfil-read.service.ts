import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PerfilReadService {

  private apiUrl = 'http://localhost:8080/api/medic';

  constructor(private http: HttpClient) { }

  getPerfilById(id: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  getPerfilPacienteById(id: string): Observable<any> {
    return this.http.get<any>(`http://localhost:8080/api/patients/${id}`);
  }
}