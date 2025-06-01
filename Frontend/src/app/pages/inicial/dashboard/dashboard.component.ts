import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  cards = [
    { title: 'Card 1', text: 'Conteúdo do card 1.', size: 'col-md-10' },
    { title: 'Card 2', text: 'Conteúdo do card 2.', size: 'col-md-5' },
    { title: 'Card 3', text: 'Conteúdo do card 3.', size: 'col-md-5' }
  ];
}
