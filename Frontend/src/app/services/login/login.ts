import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private authService: AuthService) {}

  login(usuario: string, senha: string): Observable<any> {
    return this.authService.login({
      email: usuario,
      password: senha
    });
  }
}