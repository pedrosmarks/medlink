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
  private apiUrl = 'http://localhost:8080/authenticate';

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: { email: string; password: string; userType: string }): Observable<any> {
    return this.http.post<any>(this.apiUrl, credentials)
      .pipe(
        tap(response => {
          const token = response.token || response.data?.token;
          if (token) {
            this.setToken(token);
            
            // Salvar dados do usuário
            if (response.user || response.data?.user) {
              const user = response.user || response.data.user;
              localStorage.setItem('userId', user.id?.toString() || '');
              localStorage.setItem('userName', user.name || '');
              localStorage.setItem('userEmail', user.email || '');
              localStorage.setItem('userType', credentials.userType);
              
              if (credentials.userType === 'MEDICO' && user.crm) {
                localStorage.setItem('medicoId', user.id?.toString() || '');
              } else if (credentials.userType === 'PACIENTE') {
                localStorage.setItem('pacienteId', user.id?.toString() || '');
              }
            }
          }
        })
      );
  }

  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.clearUserData();
    this.router.navigate(['/login']);
  }

  private clearUserData(): void {
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userProfile');
    localStorage.removeItem('medicoId');
    localStorage.removeItem('pacienteId');
    localStorage.removeItem('userType');
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