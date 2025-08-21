import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginBaseComponent } from './login-base/login-base.component';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faUser, faUserDoctor } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [LoginBaseComponent, CommonModule, FontAwesomeModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  aba: 'paciente' | 'medico' = 'paciente';
  
  // FontAwesome icons
  faUser = faUser;
  faUserDoctor = faUserDoctor;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    // Verifica se já está logado e redireciona
    if (this.authService.isLoggedIn()) {
      const userType = this.authService.getUserType();
      if (userType === 'paciente') {
        this.router.navigate(['/paciente']);
      } else if (userType === 'medico') {
        this.router.navigate(['/medico']);
      }
    }
  }

  selecionarAba(aba: 'paciente' | 'medico') {
    this.aba = aba;
  }
}
