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
    const medicoId = localStorage.getItem('medicoId') || localStorage.getItem('userId');
    const userType = localStorage.getItem('userType');
    
    console.log('👨⚕️ Carregando perfil do médico...');
    console.log('🔑 MedicoId:', medicoId);
    console.log('👨⚕️ UserType:', userType);
    
    if (medicoId && userType === 'MEDICO') {
      this.perfilReadService.getPerfilById(medicoId).subscribe({
        next: (response) => {
          console.log('✅ Perfil carregado:', response);
          this.perfil = response.data?.data || response.data || response;
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
    
    // Inicializar address se não existir
    if (!this.perfil.address) {
      this.perfil.address = {
        street: '',
        number: '',
        complement: '',
        neighborhood: '',
        city: '',
        state: '',
        zipCode: ''
      };
    }
  }

  salvar() {
    const medicData = {
      ...this.perfil,
      cpf: this.formatCpf(this.perfil.cpf),
      crm: this.formatCrm(this.perfil.crm)
    };
    
    this.perfilUpdateService.updatePerfil(medicData).subscribe(response => {
      console.log('Update response:', response);
      console.log('Update response.data:', response.data);
      
      // Mapear resposta do backend para formato do frontend
      if (response.data) {
        const backendData = response.data;
        this.perfil = {
          id: backendData.id,
          nome: backendData.name,
          cpf: backendData.cpf,
          gender: backendData.gender,
          birthdate: backendData.birthDate,
          telefone: backendData.phoneNumber,
          address: backendData.address,
          crm: backendData.crm,
          especialidade: backendData.specialty,
          email: backendData.email,
          active: backendData.active
        };
      }
      this.editando = false;
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

  // Funções de formatação
  formatCpf(cpf: string): string {
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  formatCrm(crm: string): string {
    return crm.replace('-', '/');
  }
}