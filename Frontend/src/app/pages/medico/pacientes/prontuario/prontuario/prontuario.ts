import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PacientesReadService } from '../../../../../services/pacientes/pacientes-read.service';

@Component({
  selector: 'app-prontuario',
  standalone: true,
  imports: [CommonModule,
    RouterModule
  ],
  templateUrl: './prontuario.html',
  styleUrls: ['./prontuario.css'] // <-- Corrija aqui (era styleUrl)
})
export class Prontuario implements OnInit {
  paciente: any;

  constructor(
    private route: ActivatedRoute,
    private pacientesReadService: PacientesReadService
  ) {}

  ngOnInit(): void {
    // Busca ID do paciente da rota
    this.route.paramMap.subscribe(params => {
      let id = params.get('id');
      console.log('🎯 ID capturado no prontuário:', id);
      
      if (id && id !== 'undefined') {
        // Busca paciente diretamente por ID ao invés de listar todos
        this.pacientesReadService.getPacienteById(id).subscribe({
          next: (response) => {
            console.log('✅ Paciente carregado:', response);
            this.paciente = response.data || response;
          },
          error: (error) => {
            console.error('❌ Erro ao carregar paciente:', error);
          }
        });
      } else {
        console.error('❌ ID inválido para prontuário:', id);
      }
    });
  }
}