import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PerfilReadService } from '../../../services/perfil/perfil-read.service';
import { PerfilUpdateService } from '../../../services/perfil/perfil-update';

@Component({
  selector: 'app-paciente-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil.html',
  styleUrls: ['./perfil.css']
})
export class Perfil implements OnInit {
  perfil: any = null;
  editando = false;
  perfilBackup: any = {};

  constructor(
    private perfilReadService: PerfilReadService,
    private perfilUpdateService: PerfilUpdateService
  ) {}

  ngOnInit(): void {
    const pacienteId = localStorage.getItem('userId');
    const userProfile = localStorage.getItem('userProfile');
    
    console.log('ID do usuário:', pacienteId);
    console.log('Perfil do usuário:', userProfile);
    
    if (pacienteId && userProfile === 'PATIENT') {
      console.log('Fazendo chamada para:', `http://localhost:8080/api/patients/${pacienteId}`);
      this.perfilReadService.getPerfilPacienteById(pacienteId).subscribe({
        next: (response) => {
          console.log('Response completo:', response);
          console.log('response.data:', response.data);
          console.log('Estrutura dos dados:', JSON.stringify(response, null, 2));
          
          // Verifica se tem endereço
          console.log('Tem endereço?', response.data?.address);
          console.log('Endereço completo:', JSON.stringify(response.data?.address, null, 2));
          
          // Processa response.data (formato ApiResponse)
          this.perfil = response.data || response;
          console.log('Perfil final:', this.perfil);
          console.log('Endereço no perfil final:', this.perfil?.address);
        },
        error: (error) => {
          console.error('Erro ao buscar perfil do paciente:', error);
        }
      });
    }
  }

  editar() {
    this.editando = true;
    this.perfilBackup = { ...this.perfil };
  }

  salvar() {
    this.perfilUpdateService.updatePerfilPaciente(this.perfil).subscribe({
      next: () => {
        this.editando = false;
        console.log('Perfil atualizado com sucesso');
      },
      error: (error) => {
        console.error('Erro ao atualizar perfil:', error);
      }
    });
  }

  cancelar() {
    this.perfil = { ...this.perfilBackup };
    this.editando = false;
  }
}