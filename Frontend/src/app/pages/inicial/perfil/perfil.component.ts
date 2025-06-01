import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent {
  perfil = {
    nome: 'Dr. João da Silva',
    especialidade: 'Cardiologista',
    crm: 'CRM 123456',
    avatar: 'https://cdn-icons-png.flaticon.com/512/921/921347.png',
    descricao: 'Especialista em cardiologia com mais de 15 anos de experiência. Atendimento humanizado e focado no bem-estar do paciente.'
  };
}