import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../../../services/login/login';
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

  constructor(private loginService: LoginService, private router: Router) {}

  login() {
    if (!this.usuario || !this.senha) {
      this.erro = 'Preencha todos os campos!';
      return;
    }

    this.loginService.login(this.usuario, this.senha)
  .subscribe({
    next: (response) => {
      // O backend retorna { data: { id, name, profile } }
      if (response?.data?.id && response?.data?.profile) {
        localStorage.setItem('userId', response.data.id);
        localStorage.setItem('userProfile', response.data.profile);

        if (response.data.profile === 'MEDIC') {
          this.router.navigate(['/medico/dashboard']);
        } else if (response.data.profile === 'PATIENT') {
          this.router.navigate(['/paciente/dashboard']);
        } else {
          this.erro = 'Tipo de usuário desconhecido!';
        }
      } else {
        this.erro = 'Usuário ou senha inválidos!';
      }
    },
    error: () => {
      this.erro = 'Usuário ou senha inválidos!';
    }
  });
  }
}