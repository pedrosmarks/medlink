import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificacoesReadService {
  private apiUrl = 'http://localhost:3000/notificacoes';

  constructor(private http: HttpClient) {}

  getNotificacoes(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}