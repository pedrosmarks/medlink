import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Observable, from } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private router: Router, private http: HttpClient) {}

  /**
   * Verifica se o usuário está logado
   */
  isLoggedIn(): boolean {
    const medicoId = localStorage.getItem('medicoId');
    const pacienteId = localStorage.getItem('pacienteId');
    return !!(medicoId || pacienteId);
  }

  /**
   * Retorna o tipo de usuário logado
   */
  getUserType(): 'medico' | 'paciente' | null {
    return localStorage.getItem('userType') as 'medico' | 'paciente' | null;
  }

  /**
   * Retorna os dados do usuário logado
   */
  getCurrentUser(): any {
    if (!this.isLoggedIn()) return null;

    return {
      id: localStorage.getItem('userId'),
      name: localStorage.getItem('userName'),
      email: localStorage.getItem('userEmail'),
      profile: localStorage.getItem('userProfile'),
      type: this.getUserType()
    };
  }

  /**
   * Verifica se é médico
   */
  isMedico(): boolean {
    return this.getUserType() === 'medico' && !!localStorage.getItem('medicoId');
  }

  /**
   * Verifica se é paciente
   */
  isPaciente(): boolean {
    return this.getUserType() === 'paciente' && !!localStorage.getItem('pacienteId');
  }

  /**
   * Faz logout completo
   */
  logout(): void {
    // Remove todos os dados de autenticação
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userProfile');
    localStorage.removeItem('medicoId');
    localStorage.removeItem('pacienteId');
    localStorage.removeItem('userType');
    localStorage.removeItem('authToken');
    
    // Redireciona para login
    this.router.navigate(['/login']);
  }

  /**
   * Redireciona para a área correta baseado no tipo de usuário
   */
  redirectToUserArea(): void {
    if (this.isMedico()) {
      this.router.navigate(['/medico/dashboard']);
    } else if (this.isPaciente()) {
      this.router.navigate(['/paciente/dashboard']);
    } else {
      this.router.navigate(['/login']);
    }
  }

  /**
   * Registra um novo usuário (médico ou paciente)
   * @param userData Dados do usuário a ser cadastrado
   * @param perfil Tipo de usuário ('medico' ou 'paciente')
   */
  registrarUsuario(userData: any, perfil: 'medico' | 'paciente'): Observable<any> {
    const endpoint = perfil === 'medico' ? 
      `${this.apiUrl}/medic` : 
      `${this.apiUrl}/patients`;

    return from(this.http.post(endpoint, userData, {
      headers: { 'Content-Type': 'application/json' }
    }).toPromise());
  }
}
