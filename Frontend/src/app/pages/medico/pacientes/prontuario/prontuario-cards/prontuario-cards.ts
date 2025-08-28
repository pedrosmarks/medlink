import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-prontuario-cards',
  imports: [CommonModule, RouterModule],
  templateUrl: './prontuario-cards.html',
  styleUrl: './prontuario-cards.css'
})
export class ProntuarioCards implements OnInit {
  pacienteNome: string = '';
  pacienteId: string = '';
  medicoNome: string = '';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.medicoNome = localStorage.getItem('userName') || 'Médico';
    
    // Tenta capturar ID de várias formas
    this.route.paramMap.subscribe(params => {
      let id = params.get('id');
      console.log('🔍 ID da rota atual:', id);
      
      if (!id && this.route.parent) {
        id = this.route.parent.snapshot.paramMap.get('id');
        console.log('🔍 ID da rota pai:', id);
      }
      
      if (id && id !== 'undefined') {
        this.pacienteId = id;
        console.log('✅ ID válido encontrado:', this.pacienteId);
        this.carregarPaciente();
      } else {
        console.error('❌ Nenhum ID válido encontrado');
      }
    });
  }

  carregarPaciente(): void {
    const url = `http://localhost:8080/api/patients/${this.pacienteId}`;
    console.log('Carregando paciente de:', url);
    
    this.http.get<any>(url).subscribe({
      next: (response) => {
        console.log('Paciente carregado:', response);
        const paciente = response.data || response;
        this.pacienteNome = paciente?.name || paciente?.nome || 'Paciente';
      },
      error: (error) => {
        console.error('Erro ao carregar paciente:', error);
        this.pacienteNome = 'Paciente';
      }
    });
  }
}
