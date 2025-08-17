import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-vacinas',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './vacinas.html',
  styleUrls: ['./vacinas.css']
})
export class Vacinas implements OnInit {
  vacinas: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  novaVacinaNome: string = '';
  novaVacinaData: string = '';

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
        this.vacinas = paciente.vacinas || [];
        this.pacienteNome = paciente.nome;
      }
    });
  }

  adicionarVacina() {
    if (!this.novaVacinaNome.trim() || !this.novaVacinaData) return;
    const nova = {
      nome: this.novaVacinaNome.trim(),
      data: this.novaVacinaData
    };

    // Buscar paciente completo
    this.pacientesReadService.getPacienteById(this.pacienteId).subscribe(paciente => {
      const vacinasAtualizadas = [...(paciente.vacinas || []), nova];
      const pacienteAtualizado = { ...paciente, vacinas: vacinasAtualizadas };

      this.http.put<any>(`http://localhost:8080/api/pacientes/${this.pacienteId}`, pacienteAtualizado)
        .subscribe(() => {
          this.vacinas = vacinasAtualizadas;
          this.novaVacinaNome = '';
          this.novaVacinaData = '';
        });
    });
  }

  removerVacina(index: number) {
    this.pacientesReadService.getPacienteById(this.pacienteId).subscribe(paciente => {
      const novasVacinas = paciente.vacinas.slice();
      novasVacinas.splice(index, 1);
      const pacienteAtualizado = { ...paciente, vacinas: novasVacinas };

      this.http.put<any>(`http://localhost:8080/api/pacientes/${this.pacienteId}`, pacienteAtualizado)
        .subscribe(() => {
          this.vacinas = novasVacinas;
        });
    });
  }
}