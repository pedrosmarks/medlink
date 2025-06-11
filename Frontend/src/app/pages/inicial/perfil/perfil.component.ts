import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PerfilReadService } from '../../../services/perfil/perfil-read.service';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {
  perfil: any = null;

  constructor(private perfilReadService: PerfilReadService) {}

  ngOnInit(): void {
    this.perfilReadService.getPerfil().subscribe(data => {
      this.perfil = data[0]; // Supondo que só existe um perfil
    });
  }
}