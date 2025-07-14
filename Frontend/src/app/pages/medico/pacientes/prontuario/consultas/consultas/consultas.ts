import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';

@Component({
  selector: 'app-consultas',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './consultas.html',
  styleUrls: ['./consultas.css']
})
export class Consultas implements OnInit {
  consultas: any[] = [];
  pacienteNome: string = '';

  constructor(
    private route: ActivatedRoute,
    private pacientesReadService: PacientesReadService
  ) {}

  ngOnInit(): void {
  // Pegue o id da rota PAI
  const id = this.route.parent?.snapshot.paramMap.get('id');
  this.pacientesReadService.getPacientes().subscribe(pacientes => {
    const paciente = pacientes.find((p: any) => String(p.id) === String(id));
    if (paciente) {
      this.consultas = paciente.consultas || [];
      this.pacienteNome = paciente.nome;
    }
  });
}
}