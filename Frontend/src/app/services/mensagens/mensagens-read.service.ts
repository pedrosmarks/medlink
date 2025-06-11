import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MensagemReadService {

  private apiUrl = 'http://localhost:3000/mensagens';

  constructor(private http: HttpClient) { }

  getMensagens(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}