import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pacientes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pacientes.component.html',
  styleUrls: ['./pacientes.component.css']
})
export class PacientesComponent {
  pacientes = [
    {
      nome: 'José Manuel',
      cpf: 'xxx.xxx.xxx-xx',
      idade: 30,
      tipoSanguineo: 'A+',
      telefone: '35 9xxxx-xxxx',
      email: 'joséo@exemplo.com.br',
      observacoes: 'Paciente faz hemodialise',
      avatar: 'https://cdn-icons-png.flaticon.com/512/921/921347.png'
    },
    {
      nome: 'Maria de Souza',
      cpf: 'yyy.yyy.yyy-yy',
      idade: 28,
      tipoSanguineo: 'O-',
      telefone: '35 9yyyy-yyyy',
      email: 'maria@exemplo.com.br',
      observacoes: 'Alergia a penicilina',
      avatar: 'https://cdn-icons-png.flaticon.com/512/921/921342.png'
    }
  ];
}