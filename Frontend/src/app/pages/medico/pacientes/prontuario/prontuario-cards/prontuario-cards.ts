import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-prontuario-cards',
  imports: [CommonModule,
    RouterModule
  ],
  templateUrl: './prontuario-cards.html',
  styleUrl: './prontuario-cards.css'
})
export class ProntuarioCards {

}
