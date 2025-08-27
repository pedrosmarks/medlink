import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Mensagem {
  id?: string;
  senderId: string;
  senderType: string;
  receiverId: string;
  receiverType: string;
  conteudo: string;
  lida?: boolean;
  dataEnvio?: string;
}

@Injectable({ providedIn: 'root' })
export class MensagemService {
  private apiUrl = 'http://localhost:8080/messages';

  constructor(private http: HttpClient) {}

  getAll(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  getConversations(senderId: string, senderType: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}?senderId=${senderId}&senderType=${senderType}`);
  }

  sendMessage(mensagem: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, mensagem);
  }

  markAsRead(id: string): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/${id}`, {});
  }
}
