import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AuthService } from '../../../services/auth/auth.service';
import { ConsentimentoLgpdComponent } from './consentimento-lgpd.component';
import { faUser, faEnvelope, faLock, faTimes, faUserDoctor, faPhone, faIdCard, faMapMarkerAlt, faCalendarAlt, faVenusMars, faTint, faClipboard, faHeart } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-cadastro-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule, ConsentimentoLgpdComponent],
  templateUrl: './cadastro-modal.component.html',
  styleUrls: ['./cadastro-modal.component.css']
})
export class CadastroModalComponent {
  @Input() isOpen = false;
  @Input() perfil: string = 'paciente';
  @Output() close = new EventEmitter<void>();

  // Dados básicos do formulário
  nome = '';
  email = '';
  senha = '';
  confirmarSenha = '';
  cpf = '';
  genero = '';
  dataNascimento = '';
  telefone = '';

  // Endereço
  endereco = {
    rua: '',
    numero: '',
    complemento: '',
    bairro: '',
    cidade: '',
    estado: '',
    cep: ''
  };

  // Específico para médicos
  crm = '';
  especialidade = '';

  // Específico para pacientes
  tipoSanguineo = '';
  observacoes = '';
  plano = '';
  cartaoSUS = '';

  // FontAwesome icons
  faUser = faUser;
  faEnvelope = faEnvelope;
  faLock = faLock;
  faTimes = faTimes;
  faUserDoctor = faUserDoctor;
  faPhone = faPhone;
  faIdCard = faIdCard;
  faMapMarkerAlt = faMapMarkerAlt;
  faCalendarAlt = faCalendarAlt;
  faVenusMars = faVenusMars;
  faTint = faTint;
  faClipboard = faClipboard;
  faHeart = faHeart;

  // Controle de estado
  erro = '';
  sucesso = '';
  carregando = false;
  mostrarConsentimento = false;
  consentimentoAceito = false;

  constructor(private authService: AuthService) {}

  fecharModal() {
    this.close.emit();
    this.limparFormulario();
  }

  limparFormulario() {
    this.nome = '';
    this.email = '';
    this.senha = '';
    this.confirmarSenha = '';
    this.cpf = '';
    this.genero = '';
    this.dataNascimento = '';
    this.telefone = '';
    
    this.endereco = {
      rua: '',
      numero: '',
      complemento: '',
      bairro: '',
      cidade: '',
      estado: '',
      cep: ''
    };

    this.crm = '';
    this.especialidade = '';
    this.tipoSanguineo = '';
    this.observacoes = '';
    this.plano = '';
    this.cartaoSUS = '';
    
    this.erro = '';
    this.sucesso = '';
    this.carregando = false;
    this.mostrarConsentimento = false;
    this.consentimentoAceito = false;
  }

  // Inicia o processo de cadastro mostrando o consentimento
  iniciarCadastro() {
    this.mostrarConsentimento = true;
  }

  // Aceita o consentimento e exibe o formulário
  onConsentimentoAceito() {
    this.consentimentoAceito = true;
    this.mostrarConsentimento = false;
  }

  // Recusa o consentimento e fecha o modal
  onConsentimentoRecusado() {
    this.mostrarConsentimento = false;
    this.fecharModal();
  }

  validarFormulario(): boolean {
    // Validações básicas
    if (!this.nome || !this.email || !this.senha || !this.confirmarSenha || !this.cpf || !this.genero || !this.dataNascimento || !this.telefone) {
      this.erro = 'Todos os campos obrigatórios devem ser preenchidos!';
      return false;
    }

    // Validação do endereço
    if (!this.endereco.rua || !this.endereco.bairro || !this.endereco.cidade || !this.endereco.estado || !this.endereco.cep) {
      this.erro = 'Todos os campos de endereço obrigatórios devem ser preenchidos!';
      return false;
    }

    if (this.senha !== this.confirmarSenha) {
      this.erro = 'As senhas não coincidem!';
      return false;
    }

    if (this.senha.length < 6) {
      this.erro = 'A senha deve ter pelo menos 6 caracteres!';
      return false;
    }

    // Validação específica para médico
    if (this.perfil === 'medico') {
      if (!this.crm || !this.especialidade) {
        this.erro = 'CRM e especialidade são obrigatórios para médicos!';
        return false;
      }
    }

    return true;
  }

  cadastrar() {
    this.erro = '';
    this.sucesso = '';

    // Verifica se o consentimento foi aceito
    if (!this.consentimentoAceito) {
      this.erro = 'É necessário aceitar o termo de consentimento para prosseguir!';
      return;
    }

    if (!this.validarFormulario()) {
      return;
    }

    this.carregando = true;
    const dados = this.getDadosCadastro();

    this.authService.registrarUsuario(
      dados, 
      this.perfil as 'medico' | 'paciente'
    ).subscribe({
      next: (response) => {
        this.sucesso = `Cadastro de ${this.perfil} criado com sucesso! Bem-vindo(a) ao MedLink!`;
        
        // Fechar modal após 2 segundos
        setTimeout(() => {
          this.fecharModal();
        }, 2000);
        this.carregando = false;
      },
      error: (error) => {
        console.error('❌ Erro no cadastro:', error);
        
        if (error.status === 400 && error.error) {
          // Tratar erros de validação do backend
          const validationErrors = error.error;
          if (typeof validationErrors === 'string') {
            this.erro = validationErrors;
          } else if (validationErrors.message) {
            this.erro = validationErrors.message;
          } else {
            this.erro = 'Dados inválidos. Verifique os campos!';
          }
        } else if (error.status === 409) {
          this.erro = 'Já existe um usuário cadastrado com este email ou CPF!';
        } else if (error.status === 0) {
          this.erro = 'Erro de conexão. Verifique sua internet!';
        } else {
          this.erro = 'Erro no servidor. Tente novamente!';
        }
        this.carregando = false;
      }
    });
  }

  private getDadosCadastro() {
    // Formatar a data para o formato esperado pelo backend (DD/MM/YYYY)
    const dataFormatada = this.formatarDataParaBackend(this.dataNascimento);
    
    // Dados básicos para ambos
    const dadosBase = {
      name: this.nome,
      cpf: this.cpf,
      gender: this.genero,
      birthDate: dataFormatada,
      phoneNumber: this.telefone,
      address: {
        street: this.endereco.rua,
        number: this.endereco.numero,
        complement: this.endereco.complemento,
        neighborhood: this.endereco.bairro,
        city: this.endereco.cidade,
        state: this.endereco.estado,
        zipCode: this.endereco.cep
      },
      email: this.email,
      password: this.senha
    };

    if (this.perfil === 'medico') {
      return {
        ...dadosBase,
        crm: this.crm,
        specialty: this.especialidade
      };
    } else {
      return {
        ...dadosBase,
        bloodType: this.tipoSanguineo,
        observations: this.observacoes,
        plan: this.plano,
        susCard: this.cartaoSUS,
        active: true
      };
    }
  }

  // Método para formatar data para o formato esperado pelo backend Java
  private formatarDataParaBackend(data: string): string {
    if (!data) return '';
    
    // Se a data já está no formato DD/MM/YYYY, retorna como está
    if (data.includes('/')) {
      return data;
    }
    
    // Se está no formato YYYY-MM-DD (do input type="date"), converte
    if (data.includes('-')) {
      // Primeiro tenta o formato ISO (YYYY-MM-DD) para DD/MM/YYYY
      const [ano, mes, dia] = data.split('-');
      
      // Validação básica
      if (ano && mes && dia && ano.length === 4) {
        return `${dia}/${mes}/${ano}`;
      }
    }
    
    // Se não conseguiu formatar, retorna a data original
    console.warn('⚠️ Formato de data não reconhecido:', data);
    return data;
  }

  // Método para fechar o modal clicando no backdrop
  onBackdropClick(event: Event) {
    if (event.target === event.currentTarget) {
      this.fecharModal();
    }
  }
}