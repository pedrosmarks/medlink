import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-relatorios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './relatorios.component.html',
  styleUrls: ['./relatorios.component.css']
})
export class RelatoriosComponent {
  relatorios = [
    {
      titulo: 'Relatório de Consultas',
      descricao: 'Resumo das consultas realizadas no período.',
      data: '01/06/2025',
      icone: 'https://cdn-icons-png.flaticon.com/512/3135/3135715.png'
    },
    {
      titulo: 'Relatório de Exames',
      descricao: 'Exames solicitados e resultados recebidos.',
      data: '28/05/2025',
      icone: 'https://cdn-icons-png.flaticon.com/512/3135/3135789.png'
    },
    {
      titulo: 'Relatório Financeiro',
      descricao: 'Resumo financeiro do mês.',
      data: '01/06/2025',
      icone: 'https://cdn-icons-png.flaticon.com/512/3135/3135768.png'
    }
  ];
}