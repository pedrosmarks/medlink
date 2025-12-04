import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../../../services/login/login';
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
    // Componente inicializado
  }
  
  // Controle do modal de cadastro
  mostrarModalCadastro = false;
  
  // FontAwesome icons
  faUser = faUser;
  faLock = faLock;
  faSignInAlt = faSignInAlt;
  faExclamationTriangle = faExclamationTriangle;

  constructor(private loginService: LoginService, private router: Router) {}

  login() {
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
    
    this.loginService.login(this.usuario, this.senha)
      .subscribe({
        next: (response: any) => {
          console.log('✅ RESPOSTA DO BACKEND:', response);
          console.log('🔍 Tipo da resposta:', typeof response);
          console.log('🔍 Chaves:', Object.keys(response || {}));
          
          // Backend retorna apenas { token: "..." }
          if (response?.token) {
            console.log('🔑 Token recebido, decodificando...');
            
            // Decodificar JWT para extrair dados
            try {
              const payload = JSON.parse(atob(response.token.split('.')[1]));
              console.log('🔍 Payload do JWT:', payload);
              
              // Salvar dados do JWT
              localStorage.setItem('userId', payload.userId || payload.pacienteId || payload.sub);
              localStorage.setItem('userName', payload.fullname || 'Usuário');
              localStorage.setItem('userEmail', payload.email);
              localStorage.setItem('userType', payload.role);
              
              // Redirecionar baseado no role do JWT
              if (payload.role === 'MEDICO') {
                localStorage.setItem('medicoId', payload.userId || payload.medicoId);
                localStorage.removeItem('pacienteId');
                this.router.navigate(['/medico/dashboard']);
              } else if (payload.role === 'PACIENTE') {
                localStorage.setItem('pacienteId', payload.pacienteId || payload.userId);
                localStorage.removeItem('medicoId');
                this.router.navigate(['/paciente/dashboard']);
              } else {
                this.erro = 'Tipo de usuário inválido!';
              }
              
            } catch (error) {
              this.erro = 'Erro ao processar token!';
            }
          } else {
            this.erro = 'Token não recebido do servidor!';
          }
        },
        error: (error) => {
          console.log('❌ ERRO DETALHADO:', error);
          console.log('🔍 Status:', error.status);
          console.log('🔍 Mensagem:', error.message);
          console.log('🔍 Body:', error.error);
          
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