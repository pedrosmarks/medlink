import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Mensagem {
  id?: string;
  senderId: string;
  senderType: string;
  recipientId: string;
  recipientType: string;
  text: string;
  read?: boolean;
  date?: string;
  senderName?: string;
  recipientName?: string;
}

@Injectable({ providedIn: 'root' })
export class MensagemService {
  private apiUrl = 'http://localhost:8080/api/messages';
  private headers = { 'Content-Type': 'application/json' };

  constructor(private http: HttpClient) {}

  getAll(): Observable<any> {
    return this.http.get<any>(this.apiUrl, { headers: this.headers });
  }

  getConversations(senderId: string, senderType: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}?senderId=${senderId}&senderType=${senderType}`, { headers: this.headers });
  }

  sendMessage(mensagem: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, mensagem, { headers: this.headers });
  }

  markAsRead(id: string): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/${id}`, {}, { headers: this.headers });
  }
}
