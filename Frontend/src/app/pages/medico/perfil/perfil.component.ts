import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PerfilReadService } from '../../../services/perfil/perfil-read.service';
import { PerfilUpdateService } from '../../../services/perfil/perfil-update';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {
  perfil: any = null;
  editando = false;
  perfilBackup: any = {};

  constructor(
    private perfilReadService: PerfilReadService,
    private perfilUpdateService: PerfilUpdateService
  ) {}

  ngOnInit(): void {
  console.log('=== INICIANDO CARREGAMENTO DO PERFIL ===');
  const medicoId = localStorage.getItem('userId');
  console.log('ID do médico:', medicoId);
 
  if (medicoId) {
    console.log('Fazendo chamada para:', `http://localhost:8080/medic/${medicoId}`);
    this.perfilReadService.getPerfilById(medicoId).subscribe({
      next: (response) => {
        console.log('=== SUCESSO NA CHAMADA ===');
        console.log('Response completo:', response);
        console.log('response.data:', response.data);
        console.log('response.data.data:', response.data?.data);
        console.log('Tipo do response:', typeof response);
        
        // Novo formato ApiResponse: response.data.data
        this.perfil = response.data?.data || response.data || response;
        console.log('Perfil final:', this.perfil);
      },
      error: (error) => {
        console.log('=== ERRO NA CHAMADA ===');
        console.log('Erro completo:', error);
        console.log('Status:', error.status);
        console.log('Mensagem:', error.message);
        console.log('URL chamada:', error.url);
      }
    });
  } else {
    console.log('ERRO: ID do médico não encontrado no localStorage!');
  }
}

  editar() {
    this.editando = true;
    this.perfilBackup = { ...this.perfil };
  }

  salvar() {
    this.perfilUpdateService.updatePerfil(this.perfil).subscribe(response => {
      console.log('Update response:', response);
      console.log('Update response.data:', response.data);
      console.log('Update response.data.data:', response.data?.data);
      
      // Novo formato ApiResponse: response.data.data
      if (response.data?.data) {
        this.perfil = response.data.data;
      } else if (response.data) {
        this.perfil = response.data;
      }
      this.editando = false;
    });
  }

  cancelar() {
    this.perfil = { ...this.perfilBackup };
    this.editando = false;
  }
}