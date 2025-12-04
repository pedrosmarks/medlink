export interface Mensagem {
  id?: string;
  senderId: string;
  senderType: 'MEDICO' | 'PACIENTE';
  recipientId: string;
  recipientType: 'MEDICO' | 'PACIENTE';
  text: string;
  senderName?: string;
  recipientName?: string;
  date?: string;
  read?: boolean;
}

export interface ApiResponse<T> {
  message: string;
  data: T;
}

export interface Conversa {
  usuario: {
    id: string;
    nome: string;
    tipo: 'medico' | 'paciente';
  };
  mensagens: Mensagem[];
  ultimaMensagem: Mensagem | null;
}
