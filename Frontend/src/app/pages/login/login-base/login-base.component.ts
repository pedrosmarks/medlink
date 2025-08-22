import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../../../services/login/login';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faUser, faLock, faSignInAlt, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-login-base',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './login-base.component.html',
  styleUrls: ['./login-base.component.css']
})
export class LoginBaseComponent {
  @Input() perfil: string = '';
  usuario = '';
  senha = '';
  erro = '';
  
  // FontAwesome icons
  faUser = faUser;
  faLock = faLock;
  faSignInAlt = faSignInAlt;
  faExclamationTriangle = faExclamationTriangle;

  constructor(private loginService: LoginService, private router: Router) {}

  login() {
    if (!this.usuario || !this.senha) {
      this.erro = 'Preencha todos os campos!';
      return;
    }

    this.loginService.login(this.usuario, this.senha)
  .subscribe({
    next: (response) => {
      // O backend retorna { data: { id, name, email, profile } }
      if (response?.data?.id && response?.data?.profile) {
        localStorage.setItem('userId', response.data.id);
        localStorage.setItem('userName', response.data.name || 'Usuário');
        localStorage.setItem('userEmail', response.data.email || this.usuario);
        localStorage.setItem('userProfile', response.data.profile);

        // Salva informações específicas por tipo de usuário
        if (response.data.profile === 'MEDIC') {
          localStorage.setItem('medicoId', response.data.id);
          localStorage.setItem('userType', 'medico');
          localStorage.removeItem('pacienteId'); // Remove dados de paciente se existir
          this.router.navigate(['/medico/dashboard']);
        } else if (response.data.profile === 'PATIENT') {
          localStorage.setItem('pacienteId', response.data.id);
          localStorage.setItem('userType', 'paciente');
          localStorage.removeItem('medicoId'); // Remove dados de médico se existir
          this.router.navigate(['/paciente/dashboard']);
        } else {
          this.erro = 'Tipo de usuário desconhecido!';
        }
      } else {
        this.erro = 'Usuário ou senha inválidos!';
      }
    },
    error: (error) => {
      console.log('Erro no login:', error);
      
      if (error.status === 400 && error.error) {
        // Erros de validação HTTP 400
        const validationErrors = error.error;
        
        if (validationErrors.email) {
          this.erro = validationErrors.email;
        } else if (validationErrors.password) {
          this.erro = validationErrors.password;
        } else if (typeof validationErrors === 'string') {
          this.erro = validationErrors;
        } else {
          this.erro = 'Dados inválidos. Verifique os campos!';
        }
      } else if (error.status === 401) {
        this.erro = 'Usuário ou senha inválidos!';
      } else if (error.status === 0) {
        this.erro = 'Erro de conexão. Verifique sua internet!';
      } else {
        this.erro = 'Erro no servidor. Tente novamente!';
      }
    }
  });
  }
}