import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('🔍 INTERCEPTOR EXECUTADO para:', req.url);
    
    // Adiciona token se existir
    const token = localStorage.getItem('authToken');
    console.log('🔑 Token encontrado:', token ? 'SIM' : 'NÃO');
    
    let authReq = req;
    
    if (token) {
      authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      console.log('✅ Header Authorization adicionado');
    } else {
      console.log('❌ Nenhum token encontrado, requisição sem Authorization');
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        // Se receber erro 401 (não autorizado), faz logout automático
        if (error.status === 401) {
          this.clearAuth();
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }

  private clearAuth(): void {
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userProfile');
    localStorage.removeItem('medicoId');
    localStorage.removeItem('pacienteId');
    localStorage.removeItem('userType');
    localStorage.removeItem('authToken');
  }
}
