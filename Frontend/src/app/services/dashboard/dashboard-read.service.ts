import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardReadService {

  private apiUrl = 'http://localhost:8080/dashboard';

  constructor(private http: HttpClient) { }

  getDashboard(): Observable<any[]> {
    return this.http.get<any>(this.apiUrl).pipe(
      map(response => response.data) // pega só o array de cards
    );
  }
}