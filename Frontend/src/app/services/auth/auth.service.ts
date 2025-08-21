import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private router: Router) {}

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
}
