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
    this.http.patch<any>(`http://localhost:3000/pacientes/${this.pacienteId}`, {
      medicamentos: [...this.medicamentos, novo]
    }).subscribe(() => {
      this.medicamentos.push(novo);
      this.novoMedicamentoNome = '';
      this.novoMedicamentoDosagem = '';
      this.novoMedicamentoInicio = '';
    });
  }

  removerMedicamento(index: number) {
    const novosMedicamentos = this.medicamentos.slice();
    novosMedicamentos.splice(index, 1);
    this.http.patch<any>(`http://localhost:3000/pacientes/${this.pacienteId}`, {
      medicamentos: novosMedicamentos
    }).subscribe(() => {
      this.medicamentos = novosMedicamentos;
    });
  }
}