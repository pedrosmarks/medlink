import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PerfilUpdateService {
  private apiUrl = 'http://localhost:8080/api/medic';

  constructor(private http: HttpClient) {}

  updatePerfil(perfil: any): Observable<any> {
    // Converter data para formato brasileiro dd/MM/yyyy
    const perfilFormatado = { ...perfil };
    if (perfilFormatado.birthdate && perfilFormatado.birthdate.includes('-')) {
      const [year, month, day] = perfilFormatado.birthdate.split('-');
      perfilFormatado.birthdate = `${day}/${month}/${year}`;
    }
    
    return this.http.put(`${this.apiUrl}/${perfil.id}`, perfilFormatado);
  }

  updatePerfilPaciente(perfil: any): Observable<any> {
    return this.http.put(`http://localhost:8080/api/patients/${perfil.id}`, perfil);
  }
}