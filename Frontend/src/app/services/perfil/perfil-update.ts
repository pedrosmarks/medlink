import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PerfilUpdateService {
  private apiUrl = 'http://localhost:8080/api/medic';

  constructor(private http: HttpClient) {}

  updatePerfil(perfil: any): Observable<any> {
    // Mapear campos do frontend para o formato esperado pelo backend
    const perfilFormatado = {
      id: perfil.id,
      name: perfil.nome,
      cpf: perfil.cpf,
      gender: perfil.gender,
      birthDate: perfil.birthdate, 
      phoneNumber: perfil.telefone,
      address: perfil.address,
      crm: perfil.crm,
      specialty: perfil.especialidade,
      email: perfil.email,
      active: perfil.active
    };

    // Converter data para formato brasileiro dd/MM/yyyy se necessário
    if (perfilFormatado.birthDate && perfilFormatado.birthDate.includes('-')) {
      const [year, month, day] = perfilFormatado.birthDate.split('-');
      perfilFormatado.birthDate = `${day}/${month}/${year}`;
    }

    console.log('Perfil formatado para envio:', perfilFormatado);
    
    return this.http.put(`${this.apiUrl}/${perfil.id}`, perfilFormatado);
  }

  updatePerfilPaciente(perfil: any): Observable<any> {
    return this.http.put(`http://localhost:8080/api/patients/${perfil.id}`, perfil);
  }
}