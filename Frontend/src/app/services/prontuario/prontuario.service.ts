import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProntuarioService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  // Cirurgias
  getCirurgiasPaciente(pacienteId: string): Observable<any[]> {
    return this.http.get<any>(`${this.baseUrl}/patients/${pacienteId}/surgeries`)
      .pipe(
        map(response => response.data || response || [])
      );
  }

  adicionarCirurgia(pacienteId: string, cirurgia: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/patients/${pacienteId}/surgeries`, cirurgia);
  }

  removerCirurgia(pacienteId: string, cirurgiaId: string | number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/patients/${pacienteId}/surgeries/${cirurgiaId}`);
  }

  atualizarCirurgia(pacienteId: string, cirurgiaId: string | number, cirurgia: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/patients/${pacienteId}/surgeries/${cirurgiaId}`, cirurgia);
  }

  // Medicamentos
  getMedicamentosPaciente(pacienteId: string): Observable<any[]> {
    return this.http.get<any>(`${this.baseUrl}/patients/${pacienteId}/medications`)
      .pipe(
        map(response => response.data || response || [])
      );
  }

  adicionarMedicamento(pacienteId: string, medicamento: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/patients/${pacienteId}/medications`, medicamento);
  }

  removerMedicamento(pacienteId: string, medicamentoId: string | number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/patients/${pacienteId}/medications/${medicamentoId}`);
  }

  atualizarMedicamento(pacienteId: string, medicamentoId: string | number, medicamento: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/patients/${pacienteId}/medications/${medicamentoId}`, medicamento);
  }

  // Consultas
  getConsultasPaciente(pacienteId: string): Observable<any[]> {
    return this.http.get<any>(`${this.baseUrl}/patients/${pacienteId}/consultations`)
      .pipe(
        map(response => response.data || response || [])
      );
  }

  adicionarConsulta(pacienteId: string, consulta: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/patients/${pacienteId}/consultations`, consulta);
  }

  removerConsulta(pacienteId: string, consultaId: string | number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/patients/${pacienteId}/consultations/${consultaId}`);
  }

  atualizarConsulta(pacienteId: string, consultaId: string | number, consulta: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/patients/${pacienteId}/consultations/${consultaId}`, consulta);
  }

  // Vacinas
  getVacinasPaciente(pacienteId: string): Observable<any[]> {
    return this.http.get<any>(`${this.baseUrl}/patients/${pacienteId}/vaccines`)
      .pipe(
        map(response => response.data || response || [])
      );
  }

  adicionarVacina(pacienteId: string, vacina: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/patients/${pacienteId}/vaccines`, vacina);
  }

  removerVacina(pacienteId: string, vacinaId: string | number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/patients/${pacienteId}/vaccines/${vacinaId}`);
  }

  atualizarVacina(pacienteId: string, vacinaId: string | number, vacina: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/patients/${pacienteId}/vaccines/${vacinaId}`, vacina);
  }

  // Alergias
  getAlergiasPaciente(pacienteId: string): Observable<any[]> {
    return this.http.get<any>(`${this.baseUrl}/patients/${pacienteId}/allergies`)
      .pipe(
        map(response => response.data || response || [])
      );
  }

  adicionarAlergia(pacienteId: string, alergia: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/patients/${pacienteId}/allergies`, alergia);
  }

  removerAlergia(pacienteId: string, alergiaId: string | number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/patients/${pacienteId}/allergies/${alergiaId}`);
  }

  atualizarAlergia(pacienteId: string, alergiaId: string | number, alergia: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/patients/${pacienteId}/allergies/${alergiaId}`, alergia);
  }

  // Diagnósticos
  getDiagnosticosPaciente(pacienteId: string): Observable<any[]> {
    return this.http.get<any>(`${this.baseUrl}/patients/${pacienteId}/diagnoses`)
      .pipe(
        map(response => response.data || response || [])
      );
  }

  adicionarDiagnostico(pacienteId: string, diagnostico: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/patients/${pacienteId}/diagnoses`, diagnostico);
  }

  removerDiagnostico(pacienteId: string, diagnosticoId: string | number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/patients/${pacienteId}/diagnoses/${diagnosticoId}`);
  }

  atualizarDiagnostico(pacienteId: string, diagnosticoId: string | number, diagnostico: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/patients/${pacienteId}/diagnoses/${diagnosticoId}`, diagnostico);
  }
}