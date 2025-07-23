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
    this.http.patch<any>(`http://localhost:3000/pacientes/${this.pacienteId}`, {
      vacinas: [...this.vacinas, nova]
    }).subscribe(() => {
      this.vacinas.push(nova);
      this.novaVacinaNome = '';
      this.novaVacinaData = '';
    });
  }

  removerVacina(index: number) {
    const novasVacinas = this.vacinas.slice();
    novasVacinas.splice(index, 1);
    this.http.patch<any>(`http://localhost:3000/pacientes/${this.pacienteId}`, {
      vacinas: novasVacinas
    }).subscribe(() => {
      this.vacinas = novasVacinas;
    });
  }
}