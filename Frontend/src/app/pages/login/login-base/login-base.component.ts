import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faUser, faLock, faSignInAlt, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { CadastroModalComponent } from '../cadastro-modal/cadastro-modal.component';

@Component({
  selector: 'app-login-base',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule, CadastroModalComponent],
  templateUrl: './login-base.component.html',
  styleUrls: ['./login-base.component.css']
})
export class LoginBaseComponent implements OnInit {
  @Input() perfil: string = '';
  usuario = '';
  senha = '';
  erro = '';
  
  ngOnInit() {
    alert('COMPONENTE CARREGOU!');
    console.log('🔧 LoginBaseComponent inicializado');
    console.log('👤 Perfil recebido:', this.perfil);
  }
  
  // Controle do modal de cadastro
  mostrarModalCadastro = false;
  
  // FontAwesome icons
  faUser = faUser;
  faLock = faLock;
  faSignInAlt = faSignInAlt;
  faExclamationTriangle = faExclamationTriangle;

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    alert('LOGIN CHAMADO!');
    console.log('🚀 MÉTODO LOGIN CHAMADO!');
    console.log('👤 Usuário:', this.usuario);
    console.log('🔒 Senha:', this.senha ? '***' : 'vazia');
    console.log('👨‍⚕️ Perfil:', this.perfil);
    
    if (!this.usuario || !this.senha) {
      this.erro = 'Preencha todos os campos!';
      console.log('❌ Campos vazios!');
      return;
    }

    const userType = this.perfil === 'medico' ? 'MEDICO' : 'PACIENTE';
    this.authService.login({ email: this.usuario, password: this.senha, userType })
      .subscribe({
        next: (response: any) => {
          console.log('✅ Login realizado com sucesso:', response);
          
          // Redirecionar baseado no tipo de usuário
          if (userType === 'MEDICO') {
            this.router.navigate(['/medico/dashboard']);
          } else {
            this.router.navigate(['/paciente/dashboard']);
          }
        },
        error: (error) => {
          console.log('ERRO no login:', error);
          
          if (error.status === 400 && error.error) {
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

  irParaCadastro(event: Event) {
    event.preventDefault();
    this.mostrarModalCadastro = true;
  }

  fecharModalCadastro() {
    this.mostrarModalCadastro = false;
  }
}