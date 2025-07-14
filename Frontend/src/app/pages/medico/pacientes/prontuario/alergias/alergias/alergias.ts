import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';

@Component({
  selector: 'app-alergias',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './alergias.html',
  styleUrls: ['./alergias.css']
})
export class Alergias implements OnInit {
  alergias: any[] = [];
  pacienteNome: string = '';

  constructor(
    private route: ActivatedRoute,
    private pacientesReadService: PacientesReadService
  ) {}

  ngOnInit(): void {
    const id = this.route.parent?.snapshot.paramMap.get('id');
    this.pacientesReadService.getPacientes().subscribe(pacientes => {
      const paciente = pacientes.find((p: any) => String(p.id) === String(id));
      if (paciente) {
        this.alergias = paciente.alergias || [];
        this.pacienteNome = paciente.nome;
      }
    });
  }
}