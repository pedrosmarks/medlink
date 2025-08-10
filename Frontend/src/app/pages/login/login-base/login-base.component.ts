import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login-base',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login-base.component.html',
  styleUrls: ['./login-base.component.css']
})
export class LoginBaseComponent {
  @Input() perfil: string = '';
  usuario = '';
  senha = '';
  erro = '';

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    if (!this.usuario || !this.senha) {
      this.erro = 'Preencha todos os campos!';
      return;
    }

    if (this.perfil === 'medico') {
      this.http.get<any[]>(`http://localhost:3000/medicos?usuario=${this.usuario}&senha=${this.senha}`)
        .subscribe(medicos => {
          if (medicos.length) {
            localStorage.setItem('medicoId', medicos[0].id);
            this.router.navigate(['/medico']);
          } else {
            this.erro = 'Usuário ou senha inválidos!';
          }
        });
    } else if (this.perfil === 'paciente') {
      this.http.get<any[]>(`http://localhost:3000/pacientes?usuario=${this.usuario}&senha=${this.senha}`)
        .subscribe(pacientes => {
          if (pacientes.length) {
            localStorage.setItem('pacienteId', pacientes[0].id);
            this.router.navigate(['/paciente']);
          } else {
            this.erro = 'Usuário ou senha inválidos!';
          }
        });
    }
  }
}