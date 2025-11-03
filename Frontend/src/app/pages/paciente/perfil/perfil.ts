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
    const pacienteId = localStorage.getItem('pacienteId') || localStorage.getItem('userId');
    const userType = localStorage.getItem('userType');
    
    console.log('👤 Carregando perfil do paciente...');
    console.log('🔑 PacienteId:', pacienteId);
    console.log('👨⚕️ UserType:', userType);
    
    if (pacienteId && userType === 'PACIENTE') {
      this.perfilReadService.getPerfilPacienteById(pacienteId).subscribe({
        next: (response) => {
          console.log('✅ Perfil carregado:', response);
          this.perfil = response.data || response;
        },
        error: (error) => {
          console.error('❌ Erro ao carregar perfil:', error);
        }
      });
    } else {
      console.log('❌ Condições não atendidas para carregar perfil');
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
      },
      error: () => {
        // Tratar erro silenciosamente ou mostrar mensagem ao usuário
      }
    });
  }

  cancelar() {
    this.perfil = { ...this.perfilBackup };
    this.editando = false;
  }

  // Getters e setters para campos de endereço
  get addressStreet() { return this.perfil?.address?.street || ''; }
  set addressStreet(value: string) { 
    if (!this.perfil.address) this.perfil.address = {};
    this.perfil.address.street = value;
  }

  get addressNumber() { return this.perfil?.address?.number || ''; }
  set addressNumber(value: string) { 
    if (!this.perfil.address) this.perfil.address = {};
    this.perfil.address.number = value;
  }

  get addressComplement() { return this.perfil?.address?.complement || ''; }
  set addressComplement(value: string) { 
    if (!this.perfil.address) this.perfil.address = {};
    this.perfil.address.complement = value;
  }

  get addressNeighborhood() { return this.perfil?.address?.neighborhood || ''; }
  set addressNeighborhood(value: string) { 
    if (!this.perfil.address) this.perfil.address = {};
    this.perfil.address.neighborhood = value;
  }

  get addressCity() { return this.perfil?.address?.city || ''; }
  set addressCity(value: string) { 
    if (!this.perfil.address) this.perfil.address = {};
    this.perfil.address.city = value;
  }

  get addressState() { return this.perfil?.address?.state || ''; }
  set addressState(value: string) { 
    if (!this.perfil.address) this.perfil.address = {};
    this.perfil.address.state = value;
  }

  get addressZipCode() { return this.perfil?.address?.zipCode || ''; }
  set addressZipCode(value: string) { 
    if (!this.perfil.address) this.perfil.address = {};
    this.perfil.address.zipCode = value;
  }
}