import { Component, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faShieldAlt, faCheckCircle, faTimes, faFileContract } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-consentimento-lgpd',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './consentimento-lgpd.component.html',
  styleUrls: ['./consentimento-lgpd.component.css']
})
export class ConsentimentoLgpdComponent {
  @Input() isOpen = false;
  @Output() aceitar = new EventEmitter<void>();
  @Output() recusar = new EventEmitter<void>();

  // Checkboxes do consentimento
  checkboxes = {
    leuCompreendeu: false,
    autorizaTratamento: false,
    consentiuCompartilhamento: false,
    cienteDireitos: false,
    concordaPolitica: false
  };

  // FontAwesome icons
  faShieldAlt = faShieldAlt;
  faCheckCircle = faCheckCircle;
  faTimes = faTimes;
  faFileContract = faFileContract;

  constructor() {}

  // Verifica se todos os checkboxes foram marcados
  todosCheckboxesMarcados(): boolean {
    return Object.values(this.checkboxes).every(valor => valor === true);
  }

  // Manipula a mudança nos checkboxes
  onCheckboxChange() {
    // Força a detecção de mudanças
  }

  // Aceita o consentimento
  aceitarConsentimento() {
    if (this.todosCheckboxesMarcados()) {
      this.aceitar.emit();
    }
  }

  // Recusa o consentimento
  recusarConsentimento() {
    this.recusar.emit();
  }

  // Limpa todos os checkboxes
  limparCheckboxes() {
    this.checkboxes = {
      leuCompreendeu: false,
      autorizaTratamento: false,
      consentiuCompartilhamento: false,
      cienteDireitos: false,
      concordaPolitica: false
    };
  }
}