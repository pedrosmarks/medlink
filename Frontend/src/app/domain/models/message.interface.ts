export interface Message {
  id?: string;
  senderId: string;
  senderName: string;
  senderType: 'MEDIC' | 'PATIENT';
  recipientId: string;
  recipientName: string;
  recipientType: 'MEDIC' | 'PATIENT';
  content: string;
  date: string;
  read: boolean;
}

export interface MessagesResponse {
  success: boolean;
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