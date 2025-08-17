import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-medicamentos',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './medicamentos.html',
  styleUrls: ['./medicamentos.css']
})
export class Medicamentos implements OnInit {
  medicamentos: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  novoMedicamentoNome: string = '';
  novoMedicamentoDosagem: string = '';
  novoMedicamentoInicio: string = '';

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
        this.medicamentos = paciente.medicamentos || [];
        this.pacienteNome = paciente.nome;
      }
    });
  }

  adicionarMedicamento() {
    if (!this.novoMedicamentoNome.trim() || !this.novoMedicamentoDosagem.trim() || !this.novoMedicamentoInicio) return;
    const novo = {
      nome: this.novoMedicamentoNome.trim(),
      dosagem: this.novoMedicamentoDosagem.trim(),
      inicio: this.novoMedicamentoInicio
    };

    // Buscar paciente completo antes de atualizar
    this.pacientesReadService.getPacienteById(this.pacienteId).subscribe(paciente => {
      const medicamentosAtualizados = [...(paciente.medicamentos || []), novo];
      const pacienteAtualizado = { ...paciente, medicamentos: medicamentosAtualizados };

      this.http.put<any>(`http://localhost:8080/api/pacientes/${this.pacienteId}`, pacienteAtualizado)
        .subscribe(() => {
          this.medicamentos = medicamentosAtualizados;
          this.novoMedicamentoNome = '';
          this.novoMedicamentoDosagem = '';
          this.novoMedicamentoInicio = '';
        });
    });
  }

  removerMedicamento(index: number) {
    this.pacientesReadService.getPacienteById(this.pacienteId).subscribe(paciente => {
      const novosMedicamentos = paciente.medicamentos.slice();
      novosMedicamentos.splice(index, 1);
      const pacienteAtualizado = { ...paciente, medicamentos: novosMedicamentos };

      this.http.put<any>(`http://localhost:8080/api/pacientes/${this.pacienteId}`, pacienteAtualizado)
        .subscribe(() => {
          this.medicamentos = novosMedicamentos;
        });
    });
  }
}