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
  medicosAutorizados: MedicoInterface[] = [];
  loading = true;
  error = '';

  constructor(
    private medicosService: MedicosService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.carregarMedicosAutorizados();
  }

  carregarMedicosAutorizados() {
    const pacienteId = localStorage.getItem('pacienteId');
    
    if (!pacienteId) {
      this.error = 'ID do paciente não encontrado';
      this.loading = false;
      return;
    }

    this.medicosService.getMedicosAutorizados(pacienteId).subscribe({
      next: (medicos) => {
        this.medicosAutorizados = medicos;
        this.loading = false;
        
        if (medicos.length === 0) {
          console.log('Nenhum médico autorizado encontrado para o paciente:', pacienteId);
        }
      },
      error: (error) => {
        console.error('Erro ao carregar médicos autorizados:', error);
        
        // Tratamento específico para diferentes tipos de erro
        if (error.status === 404) {
          this.error = 'Nenhum médico autorizado encontrado';
        } else if (error.status === 401) {
          this.error = 'Não autorizado a acessar estes dados';
        } else if (error.status === 500) {
          this.error = 'Erro interno do servidor';
        } else {
          this.error = 'Erro ao carregar médicos autorizados. Tente novamente.';
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

  agendarConsulta(medicoId: string) {
    // Implementar navegação para agendamento
    console.log('Agendar consulta com médico:', medicoId);
    // Futura implementação: this.router.navigate(['/paciente/agendar-consulta'], { queryParams: { medicoId } });
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
