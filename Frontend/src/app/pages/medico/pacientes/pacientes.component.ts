import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PacientesReadService } from '../../../services/pacientes/pacientes-read.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-pacientes',
  standalone: true,
  imports: [CommonModule,
    RouterModule
  ],
  templateUrl: './pacientes.component.html',
  styleUrls: ['./pacientes.component.css']
})
export class PacientesComponent implements OnInit {
  pacientes: any[] = [];

  constructor(private pacientesReadService: PacientesReadService) {}

  ngOnInit(): void {
    this.pacientesReadService.getPacientes().subscribe(data => {
      this.pacientes = data;
    });
  }
}