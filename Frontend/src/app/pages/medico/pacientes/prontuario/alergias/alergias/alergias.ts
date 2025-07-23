import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-alergias',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './alergias.html',
  styleUrls: ['./alergias.css']
})
export class Alergias implements OnInit {
  alergias: any[] = [];
  pacienteNome: string = '';
  novaAlergia: string = '';
  pacienteId: string = '';

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
        this.alergias = paciente.alergias || [];
        this.pacienteNome = paciente.nome;
      }
    });
  }

  adicionarAlergia() {
    if (!this.novaAlergia.trim()) return;
    const nova = { descricao: this.novaAlergia.trim() };
    // Atualiza no backend (json-server)
    this.http.patch<any>(`http://localhost:3000/pacientes/${this.pacienteId}`, {
      alergias: [...this.alergias, nova]
    }).subscribe(() => {
      this.alergias.push(nova);
      this.novaAlergia = '';
    });
  }

  removerAlergia(index: number) {
  const novasAlergias = this.alergias.slice();
  novasAlergias.splice(index, 1);
  this.http.patch<any>(`http://localhost:3000/pacientes/${this.pacienteId}`, {
    alergias: novasAlergias
  }).subscribe(() => {
    this.alergias = novasAlergias;
  });
}
}