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
  const medicoId = localStorage.getItem('userId');
 
  if (medicoId) {
    this.perfilReadService.getPerfilById(medicoId).subscribe(data => {
      console.log('Perfil recebido:', data);
      this.perfil = data;
    });
  }
}

  editar() {
    this.editando = true;
    this.perfilBackup = { ...this.perfil };
  }

  salvar() {
    this.perfilUpdateService.updatePerfil(this.perfil).subscribe(() => {
      this.editando = false;
    });
  }

  cancelar() {
    this.perfil = { ...this.perfilBackup };
    this.editando = false;
  }
}