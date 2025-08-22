import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private apiUrl = 'http://localhost:8080/auth/login';

  constructor(private http: HttpClient) {}

  login(usuario: string, senha: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    
    const body = {
      email: usuario,
      password: senha
    };
    
    return this.http.post<any>(this.apiUrl, body, { headers });
  }
}