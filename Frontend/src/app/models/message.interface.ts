export interface Message {
  id: string;
  senderId: string;
  senderType: 'MEDIC' | 'PATIENT';
  senderName: string;
  recipientId: string;
  recipientType: 'MEDIC' | 'PATIENT';
  recipientName: string;
  text: string;
  date: string;
  read: boolean;
}

export interface MessagesResponse {
  message: string;
  data: Message[];
}

export interface Conversation {
  participantId: string;
  participantName: string;
  participantType: 'MEDIC' | 'PATIENT';
  messages: Message[];
  lastMessage?: Message;
  unreadCount: number;
}