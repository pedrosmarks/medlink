export interface Mensagem {
  id?: string;
  senderId: string;
  senderType: 'MEDICO' | 'PACIENTE';
  recipientId: string;
  recipientType: 'MEDICO' | 'PACIENTE';
  content: string;
  timestamp?: string;
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
