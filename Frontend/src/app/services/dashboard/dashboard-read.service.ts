import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardReadService {

  private apiUrl = 'http://localhost:3000/dashboard'; // URL do endpoint do json-server

  constructor(private http: HttpClient) { }

  getDashboard(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}