import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-mensagem',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mensagem.component.html',
  styleUrls: ['./mensagem.component.css']
})
export class MensagemComponent {
  mensagens = [
    {
      nome: 'João da Silva',
      avatar: 'https://cdn-icons-png.flaticon.com/512/921/921347.png',
      novas: 2,
      resumo: 'Boa tarde, Dr a receita a....'
    },
    {
      nome: 'Maria de Souza',
      avatar: 'https://cdn-icons-png.flaticon.com/512/921/921342.png',
      novas: 0,
      resumo: 'Bom dia o remedio deu reaca...'
    }
  ];
}