import { Component } from '@angular/core';
import { LoginBaseComponent } from './login-base/login-base.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [LoginBaseComponent, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  aba: 'paciente' | 'medico' = 'paciente';
}