import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RelatoriosReadService {

  private apiUrl = 'http://localhost:8080/report/relatorios';

  constructor(private http: HttpClient) { }

  getRelatorios(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}