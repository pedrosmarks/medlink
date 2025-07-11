import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PerfilUpdateService {
  private apiUrl = 'http://localhost:3000/perfil'; // ajuste se necessário

  constructor(private http: HttpClient) {}

  updatePerfil(perfil: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${perfil.id}`, perfil);
  }
}