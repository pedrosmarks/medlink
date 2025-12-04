import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Message, MessagesResponse, Conversation } from '../../domain/models/message.interface';

@Injectable({
  providedIn: 'root'
})
export class MensagensService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // Buscar todas as mensagens
  getMensagens(): Observable<Message[]> {
    return this.http.get<MessagesResponse>(`${this.apiUrl}/messages`).pipe(
      map((response: MessagesResponse) => response.data || [])
    );
  }

  // Organizar mensagens em conversas para um usuário específico
  getConversationsForUser(userId: string, userType: 'MEDIC' | 'PATIENT'): Observable<Conversation[]> {
    return this.getMensagens().pipe(
      map((messages: Message[]) => {
        // Filtrar mensagens onde o usuário é remetente ou destinatário
        const userMessages = messages.filter(msg => 
          (msg.senderId === userId && msg.senderType === userType) ||
          (msg.recipientId === userId && msg.recipientType === userType)
        );

        // Agrupar por conversa
        const conversationMap = new Map<string, Conversation>();

        userMessages.forEach(msg => {
          // Determinar quem é o outro participante da conversa
          const isUserSender = msg.senderId === userId && msg.senderType === userType;
          const otherParticipantId = isUserSender ? msg.recipientId : msg.senderId;
          const otherParticipantName = isUserSender ? msg.recipientName : msg.senderName;
          const otherParticipantType = isUserSender ? msg.recipientType : msg.senderType;

          const conversationKey = `${otherParticipantId}-${otherParticipantType}`;

          if (!conversationMap.has(conversationKey)) {
            conversationMap.set(conversationKey, {
              participantId: otherParticipantId,
              participantName: otherParticipantName,
              participantType: otherParticipantType,
              messages: [],
              lastMessage: undefined,
              unreadCount: 0
            });
          }

          const conversation = conversationMap.get(conversationKey)!;
          conversation.messages.push(msg);
        });

        // Processar cada conversa
        const conversations = Array.from(conversationMap.values()).map(conversation => {
          // Ordenar mensagens por data
          conversation.messages.sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
          
          // Definir última mensagem
          conversation.lastMessage = conversation.messages[conversation.messages.length - 1];
          
          // Contar mensagens não lidas (onde o usuário atual é o destinatário)
          conversation.unreadCount = conversation.messages.filter(msg => 
            !msg.read && 
            msg.recipientId === userId && 
            msg.recipientType === userType
          ).length;

          return conversation;
        });

        // Ordenar conversas por data da última mensagem (mais recente primeiro)
        conversations.sort((a, b) => {
          const dateA = a.lastMessage ? new Date(a.lastMessage.date).getTime() : 0;
          const dateB = b.lastMessage ? new Date(b.lastMessage.date).getTime() : 0;
          return dateB - dateA;
        });

        return conversations;
      })
    );
  }

  // Buscar conversas de um usuário específico (método legado - manter para compatibilidade)
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
  enviarMensagem(mensagem: Message): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/messages`, mensagem).pipe(
      map((response: any) => {
        return response.data || response;
      })
    );
  }

  // Marcar mensagem como lida
  marcarComoLida(mensagemId: string): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/messages/${mensagemId}`, {}).pipe(
      map((response: any) => response.data || response)
    );
  }

  // Marcar múltiplas mensagens como lidas
  marcarMensagensComoLidas(mensagemIds: string[]): Observable<any[]> {
    const requests = mensagemIds.map(id => this.marcarComoLida(id));
    return new Observable(observer => {
      Promise.all(requests.map(req => req.toPromise()))
        .then(results => {
          observer.next(results);
          observer.complete();
        })
        .catch(error => observer.error(error));
    });
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

  // Contar mensagens não lidas para um usuário
  countMensagensNaoLidas(userId: string, userType: 'MEDIC' | 'PATIENT'): Observable<number> {
    return this.getMensagens().pipe(
      map((messages: Message[]) => {
        return messages.filter(msg => 
          msg.recipientId === userId && 
          msg.recipientType === userType &&
          !msg.read
        ).length;
      })
    );
  }
}