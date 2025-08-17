import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-prontuario',
  imports: [CommonModule, RouterModule],
  templateUrl: './prontuario.html',
  styleUrl: './prontuario.css'
})
export class Prontuario implements OnInit {
  usuarioId: string | null = null;
  pacienteNome: string | null = null;

  ngOnInit(): void {
    this.usuarioId = localStorage.getItem('userId');
    this.pacienteNome = localStorage.getItem('userName') || 'Paciente';
  }
}
