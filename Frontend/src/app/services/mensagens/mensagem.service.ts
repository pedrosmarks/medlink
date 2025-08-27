import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MensagensService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // Buscar todas as mensagens
  getMensagens(): Observable<any[]> {
    return this.http.get<any>(`${this.apiUrl}/messages`).pipe(
      map((response: any) => response.data || response)
    );
  }

  // Buscar conversas de um usuário específico
  getConversas(senderId: string, senderType: string): Observable<any[]> {
    return this.http.get<any>(`${this.apiUrl}/messages`, {
      params: {
        senderId: senderId,
        senderType: senderType.toUpperCase() // Garantir que seja MEDICO ou PACIENTE
      }
    }).pipe(
      map((response: any) => response.data || response)
    );
  }

  // Enviar nova mensagem
  enviarMensagem(mensagem: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/messages`, mensagem).pipe(
      map((response: any) => response.data || response)
    );
  }

  // Marcar mensagem como lida
  marcarComoLida(mensagemId: string): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/messages/${mensagemId}`, {}).pipe(
      map((response: any) => response.data || response)
    );
  }

  // Buscar médicos (manter para compatibilidade)
  getMedicos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/medic`);
  }

  // Buscar pacientes (manter para compatibilidade)
  getPacientes(): Observable<any[]> {
    return this.http.get<any>(`${this.apiUrl}/patients`).pipe(
      map((response: any) => response.data || response)
    );
  }
}