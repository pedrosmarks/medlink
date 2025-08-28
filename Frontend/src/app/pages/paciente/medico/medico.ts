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
    
    console.log('🔍 Carregando médicos autorizados para paciente:', pacienteId);
    console.log('🌐 URL da requisição:', `http://localhost:8080/api/patients/${pacienteId}/authorized-doctors`);

    this.medicosService.getMedicosAutorizados(pacienteId).subscribe({
      next: (response: any) => {
        console.log('✅ Response completo:', response);
        console.log('✅ Tipo da response:', typeof response);
        console.log('✅ É array?', Array.isArray(response));
        console.log('✅ response.data:', response.data);
        console.log('✅ response.data é array?', Array.isArray(response.data));
        
        // Tenta diferentes estruturas de resposta
        let medicos = [];
        if (Array.isArray(response)) {
          medicos = response;
        } else if (response.data && Array.isArray(response.data)) {
          medicos = response.data;
        } else if (response.result && Array.isArray(response.result)) {
          medicos = response.result;
        }
        
        this.medicosAutorizados = medicos;
        this.loading = false;
        
        console.log('📊 Médicos processados:', this.medicosAutorizados);
        console.log('📊 Total de médicos autorizados:', this.medicosAutorizados.length);
        
        if (this.medicosAutorizados.length > 0) {
          const medico = this.medicosAutorizados[0];
          console.log('🔍 PRIMEIRO MÉDICO:', JSON.stringify(medico, null, 2));
          console.log('🔍 CAMPOS:', Object.keys(medico));
          console.log('🔍 medico.name:', medico.name);
          console.log('🔍 medico.nome:', medico.nome);
          console.log('🔍 medico.fullName:', medico.fullName);
          console.log('🔍 medico.doctorName:', medico.doctorName);
        }
        
        // Log detalhado de cada médico
        this.medicosAutorizados.forEach((medico, index) => {
          console.log(`👨‍⚕️ MÉDICO ${index + 1}:`, medico);
          console.log('Campos disponíveis:', Object.keys(medico));
          console.log('Nome:', medico.nome || medico.name);
          console.log('Especialidade:', medico.especialidade || medico.specialty);
        });
        
        if (this.medicosAutorizados.length === 0) {
          console.log('⚠️ Nenhum médico autorizado encontrado para o paciente:', pacienteId);
          this.error = 'Você ainda não possui médicos autorizados. Aguarde a aprovação das requisições.';
        }
      },
      error: (error) => {
        console.error('❌ Erro completo:', error);
        console.error('❌ Status:', error.status);
        console.error('❌ StatusText:', error.statusText);
        console.error('❌ URL:', error.url);
        console.error('❌ Error body:', error.error);
        
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
