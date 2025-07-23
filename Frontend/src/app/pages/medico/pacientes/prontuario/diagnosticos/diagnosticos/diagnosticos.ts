import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-diagnosticos',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './diagnosticos.html',
  styleUrls: ['./diagnosticos.css']
})
export class Diagnosticos implements OnInit {
  diagnosticos: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  novoDiagnosticoNome: string = '';
  novoDiagnosticoData: string = '';

  constructor(
    private route: ActivatedRoute,
    private pacientesReadService: PacientesReadService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const id = this.route.parent?.snapshot.paramMap.get('id');
    this.pacienteId = id ?? '';
    this.pacientesReadService.getPacientes().subscribe(pacientes => {
      const paciente = pacientes.find((p: any) => String(p.id) === String(id));
      if (paciente) {
        this.diagnosticos = paciente.diagnosticos || [];
        this.pacienteNome = paciente.nome;
      }
    });
  }

  adicionarDiagnostico() {
    if (!this.novoDiagnosticoNome.trim() || !this.novoDiagnosticoData) return;
    const novo = {
      nome: this.novoDiagnosticoNome.trim(),
      data: this.novoDiagnosticoData
    };
    this.http.patch<any>(`http://localhost:3000/pacientes/${this.pacienteId}`, {
      diagnosticos: [...this.diagnosticos, novo]
    }).subscribe(() => {
      this.diagnosticos.push(novo);
      this.novoDiagnosticoNome = '';
      this.novoDiagnosticoData = '';
    });
  }

  removerDiagnostico(index: number) {
    const novosDiagnosticos = this.diagnosticos.slice();
    novosDiagnosticos.splice(index, 1);
    this.http.patch<any>(`http://localhost:3000/pacientes/${this.pacienteId}`, {
      diagnosticos: novosDiagnosticos
    }).subscribe(() => {
      this.diagnosticos = novosDiagnosticos;
    });
  }
}