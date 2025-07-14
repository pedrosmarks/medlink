import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';

@Component({
  selector: 'app-cirurgias',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './cirurgias.html',
  styleUrls: ['./cirurgias.css']
})
export class Cirurgias implements OnInit {
  cirurgias: any[] = [];
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
        this.cirurgias = paciente.cirurgias || [];
        this.pacienteNome = paciente.nome;
      }
    });
  }
}