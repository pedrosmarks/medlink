import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginBaseComponent } from './login-base/login-base.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [LoginBaseComponent, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  aba: 'paciente' | 'medico' = 'paciente';

  constructor(private router: Router) {}

  ngOnInit(): void {
    // Verifica se já está logado e redireciona
    const pacienteId = localStorage.getItem('pacienteId');
    const medicoId = localStorage.getItem('medicoId');

    if (medicoId) {
      this.router.navigate(['/medico/dashboard']);
      return;
    }
    if (pacienteId) {
      this.router.navigate(['/paciente/dashboard']);
      return;
    }
  }
}