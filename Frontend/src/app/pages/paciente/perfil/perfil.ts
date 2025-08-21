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
      console.log('Fazendo chamada para:', `http://localhost:8080/api/pacientes/${pacienteId}`);
      this.perfilReadService.getPerfilPacienteById(pacienteId).subscribe({
        next: (data) => {
          console.log('Perfil do paciente recebido:', data);
          console.log('Estrutura dos dados:', JSON.stringify(data, null, 2));
          this.perfil = data;
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