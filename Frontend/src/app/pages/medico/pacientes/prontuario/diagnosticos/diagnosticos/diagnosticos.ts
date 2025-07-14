import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';

@Component({
  selector: 'app-diagnosticos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './diagnosticos.html',
  styleUrls: ['./diagnosticos.css']
})
export class Diagnosticos implements OnInit {
  diagnosticos: any[] = [];
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
        this.diagnosticos = paciente.diagnosticos || [];
        this.pacienteNome = paciente.nome;
      }
    });
  }
}