import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-notificacoes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notificacoes.component.html',
  styleUrls: ['./notificacoes.component.css']
})
export class NotificacoesComponent {
  notificacoes = [
    {
      titulo: 'Consulta agendada',
      descricao: 'Você tem uma consulta marcada para amanhã às 14h.',
      data: '01/06/2025',
      icone: 'https://cdn-icons-png.flaticon.com/512/1827/1827392.png'
    },
    {
      titulo: 'Novo exame disponível',
      descricao: 'O resultado do exame de sangue já está disponível.',
      data: '30/05/2025',
      icone: 'https://cdn-icons-png.flaticon.com/512/1827/1827392.png'
    },
    {
      titulo: 'Mensagem recebida',
      descricao: 'Você recebeu uma nova mensagem do paciente Maria de Souza.',
      data: '29/05/2025',
      icone: 'https://cdn-icons-png.flaticon.com/512/1827/1827392.png'
    }
  ];
}