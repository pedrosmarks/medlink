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

    // Buscar paciente completo antes de atualizar
    this.pacientesReadService.getPacienteById(this.pacienteId).subscribe(paciente => {
      const diagnosticosAtualizados = [...(paciente.diagnosticos || []), novo];
      const pacienteAtualizado = { ...paciente, diagnosticos: diagnosticosAtualizados };

      this.http.put<any>(`http://localhost:8080/api/pacientes/${this.pacienteId}`, pacienteAtualizado)
        .subscribe(() => {
          this.diagnosticos = diagnosticosAtualizados;
          this.novoDiagnosticoNome = '';
          this.novoDiagnosticoData = '';
        });
    });
  }

  removerDiagnostico(index: number) {
    this.pacientesReadService.getPacienteById(this.pacienteId).subscribe(paciente => {
      const novosDiagnosticos = paciente.diagnosticos.slice();
      novosDiagnosticos.splice(index, 1);
      const pacienteAtualizado = { ...paciente, diagnosticos: novosDiagnosticos };

      this.http.put<any>(`http://localhost:8080/api/pacientes/${this.pacienteId}`, pacienteAtualizado)
        .subscribe(() => {
          this.diagnosticos = novosDiagnosticos;
        });
    });
  }
}