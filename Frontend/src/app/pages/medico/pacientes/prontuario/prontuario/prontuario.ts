import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PacientesReadService } from '../../../../../services/pacientes/pacientes-read.service';

@Component({
  selector: 'app-prontuario',
  standalone: true,
  imports: [CommonModule,
    RouterModule
  ],
  templateUrl: './prontuario.html',
  styleUrls: ['./prontuario.css'] // <-- Corrija aqui (era styleUrl)
})
export class Prontuario implements OnInit {
  paciente: any;

  constructor(
    private route: ActivatedRoute,
    private pacientesReadService: PacientesReadService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.pacientesReadService.getPacientes().subscribe(pacientes => {
      this.paciente = pacientes.find((p: any) => String(p.id) === String(id));
    });
  }
}