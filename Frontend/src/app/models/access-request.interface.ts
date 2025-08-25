export interface AccessRequest {
  medicoId: number;
  status: 'pendente' | 'aprovado' | 'rejeitado';
  dataSolicitacao?: string;
  medicoName?: string;
  medicoSpecialty?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
}

export interface Patient {
  id: number;
  name: string;
  email: string;
  avatar?: string;
}