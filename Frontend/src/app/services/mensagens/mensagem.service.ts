import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MensagensService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // Buscar todas as mensagens
  getMensagens(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mensagens`);
  }

  // Buscar conversas de um usuário específico
  getConversas(userId: string, userTipo: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mensagens?remetenteId=${userId}&remetenteTipo=${userTipo}`);
  }

  // Enviar nova mensagem
  enviarMensagem(mensagem: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/mensagens`, mensagem);
    
  }

  // Marcar mensagem como lida
  marcarComoLida(mensagemId: number): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/mensagens/${mensagemId}`, { lida: true });
  }

  // Buscar médicos
  getMedicos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/medicos`);
  }
  getPacientes(): Observable<any[]> {
  return this.http.get<any[]>(`${this.apiUrl}/pacientes`);
 } 
}