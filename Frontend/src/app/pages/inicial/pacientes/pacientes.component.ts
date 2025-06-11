import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PacientesReadService } from '../../../services/pacientes/pacientes-read.service';

@Component({
  selector: 'app-pacientes',
  standalone: true,
  imports: [CommonModule],
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