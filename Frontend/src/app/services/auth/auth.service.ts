import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'authToken';
  private apiUrl = 'http://localhost:8080/authenticate'; // Pode ser /auth/login ou /api/authenticate

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: { email: string; password: string }): Observable<any> {
    // JWT não precisa de userType
    const loginData = { email: credentials.email, password: credentials.password };
    
    return this.http.post<any>(this.apiUrl, loginData)
      .pipe(
        tap(response => {
          console.log('Resposta JWT:', response);
          
          if (response && response.token) {
            this.setToken(response.token);
            this.extractUserDataFromJWT(response.token);
          }
        })
      );
  }

  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  private extractUserDataFromJWT(token: string): void {
    try {
      console.log('🔍 Token recebido:', token);
      console.log('🔍 Tipo do token:', typeof token);
      console.log('🔍 Comprimento do token:', token.length);
      
      // Verificar se é um JWT válido (deve ter 3 partes separadas por .)
      const parts = token.split('.');
      console.log('🔍 Partes do token:', parts.length);
      
      if (parts.length !== 3) {
        console.error('❌ Token não é um JWT válido (deve ter 3 partes)');
        return;
      }
      
      const payload = JSON.parse(atob(parts[1]));
      
      localStorage.setItem('userId', payload.userId || payload.sub || '');
      localStorage.setItem('userName', payload.fullname || payload.name || '');
      localStorage.setItem('userEmail', payload.email || '');
      localStorage.setItem('userType', payload.role || payload.authorities || '');
      
      console.log('✅ Dados extraídos do JWT:', payload);
    } catch (error) {
      console.error('❌ Erro ao decodificar JWT:', error);
      console.error('❌ Token problemático:', token);
    }
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  private redirectToDashboard(): void {
    const userType = localStorage.getItem('userType');
    console.log('🔄 Redirecionando. UserType:', userType);
    if (userType === 'MEDICO') {
      console.log('👨‍⚕️ Redirecionando para médico dashboard');
      this.router.navigate(['/medico/dashboard']);
    } else {
      console.log('👤 Redirecionando para paciente dashboard');
      this.router.navigate(['/paciente/dashboard']);
    }
  }

  isMedico(): boolean {
    return localStorage.getItem('userType') === 'MEDICO';
  }

  isPaciente(): boolean {
    return localStorage.getItem('userType') === 'PACIENTE';
  }
  
  isUser(): boolean {
    return localStorage.getItem('userType') === 'USER';
  }

  registrarUsuario(userData: any, userType: 'medico' | 'paciente'): Observable<any> {
    return this.http.post(`http://localhost:8080/register/${userType}`, userData);
  }

  getCurrentUser(): any {
    return {
      id: localStorage.getItem('userId'),
      name: localStorage.getItem('userName'),
      email: localStorage.getItem('userEmail'),
      type: localStorage.getItem('userType')
    };
  }

  testarLogin(): void {
    console.log('🔍 === TESTE DE LOGIN ===');
    console.log('Token no localStorage:', localStorage.getItem('authToken'));
    console.log('UserType no localStorage:', localStorage.getItem('userType'));
    console.log('Está logado?', this.isLoggedIn());
    console.log('É médico?', this.isMedico());
    console.log('É paciente?', this.isPaciente());
    console.log('======================');
  }
}