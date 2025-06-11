import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PerfilReadService {

  private apiUrl = 'http://localhost:3000/perfil';

  constructor(private http: HttpClient) { }

  getPerfil(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}