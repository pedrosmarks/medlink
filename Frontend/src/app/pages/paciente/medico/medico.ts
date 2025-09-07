import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MedicosService } from '../../../services/medicos/medicos.service';
import { AuthService } from '../../../services/auth/auth.service';
import { Medico as MedicoInterface } from '../../../models/medico.interface';

@Component({
  selector: 'app-medico',
  imports: [CommonModule],
  templateUrl: './medico.html',
  styleUrl: './medico.css'
})
export class Medico implements OnInit {
  medicosAutorizados: any[] = [];
  loading = true;
  error = '';

  constructor(
    private medicosService: MedicosService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.carregarMedicosAutorizados();
    
    // Escutar evento de médico aprovado
    window.addEventListener('medico-aprovado', () => {
      console.log('🔄 Recarregando lista de médicos após aprovação...');
      this.carregarMedicosAutorizados();
    });
  }

  carregarMedicosAutorizados() {
    const pacienteId = localStorage.getItem('userId');
    if (!pacienteId) {
      this.error = 'ID do paciente não encontrado';
      this.loading = false;
      return;
    }
    this.medicosService.getMedicosAutorizadosCompletos(pacienteId).subscribe({
      next: (medicos: any[]) => {
        this.medicosAutorizados = medicos || [];
        console.log('Medicos autorizados recebidos:', this.medicosAutorizados);
        this.loading = false;
      },
      error: (error) => {
        // Tratamento específico para diferentes tipos de erro
        if (error.status === 404) {
          this.error = 'Endpoint não encontrado. Verifique se o backend está rodando.';
        } else if (error.status === 401) {
          this.error = 'Não autorizado a acessar estes dados';
        } else if (error.status === 500) {
          this.error = 'Erro interno do servidor';
        } else if (error.status === 0) {
          this.error = 'Não foi possível conectar ao servidor. Verifique se o backend está rodando.';
        } else {
          this.error = `Erro ${error.status}: ${error.statusText || 'Erro desconhecido'}`;
        }
        this.loading = false;
      }
    });
  }

  formatarTelefone(telefone: string): string {
    // Remove caracteres especiais e formata o telefone
    return telefone?.replace(/\D/g, '').replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3') || 'Não informado';
  }

  iniciarConversa(medicoId: string) {
    // Navega para a página de mensagens com o médico selecionado
    this.router.navigate(['/paciente/mensagem'], { 
      queryParams: { medicoId: medicoId }
    });
  }

  recarregarDados() {
    this.loading = true;
    this.error = '';
    this.carregarMedicosAutorizados();
  }

  verDetalhes(medicoId: string) {
    // Implementar modal ou página de detalhes do médico
    console.log('Ver detalhes do médico:', medicoId);
  }
}
