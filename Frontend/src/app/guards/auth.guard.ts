import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private authService: AuthService) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    // Se não está logado, redireciona para login
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return false;
    }

    // Verifica se está tentando acessar área correspondente ao tipo de usuário
    const routePath = route.routeConfig?.path || '';

    if (routePath.startsWith('medico')) {
      // Tentando acessar área do médico
      if (this.authService.isMedico()) {
        return true;
      } else {
        // Se é paciente tentando acessar área do médico
        this.router.navigate(['/paciente/dashboard']);
        return false;
      }
    }

    if (routePath.startsWith('paciente')) {
      // Tentando acessar área do paciente
      if (this.authService.isPaciente()) {
        return true;
      } else {
        // Se é médico tentando acessar área do paciente
        this.router.navigate(['/medico/dashboard']);
        return false;
      }
    }

    // Caso padrão - redireciona para login
    this.router.navigate(['/login']);
    return false;
  }
}
