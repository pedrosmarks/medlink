import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PerfilUpdateService {
  private apiUrl = 'http://localhost:8080/perfil'; // ajuste se necessário

  constructor(private http: HttpClient) {}

  updatePerfil(perfil: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${perfil.id}`, perfil);
  }

  updatePerfilPaciente(perfil: any): Observable<any> {
    return this.http.put(`http://localhost:8080/api/pacientes/${perfil.id}`, perfil);
  }
}