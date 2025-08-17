import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-consultas',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './consultas.html',
  styleUrls: ['./consultas.css']
})
export class Consultas implements OnInit {
  consultas: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  novaConsultaData: string = '';
  novaConsultaDescricao: string = '';

  constructor(
    private route: ActivatedRoute,
    private pacientesReadService: PacientesReadService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const id = this.route.parent?.snapshot.paramMap.get('id');
    console.log('ID recebido:', id); 
    this.pacienteId = id ?? '';
    this.pacientesReadService.getPacientes().subscribe(pacientes => {
      const paciente = pacientes.find((p: any) => String(p.id) === String(id));
      if (paciente) {
        this.consultas = paciente.consultas || [];
        this.pacienteNome = paciente.nome;
      }
    });
  }

  adicionarConsulta() {
  if (!this.novaConsultaData || !this.novaConsultaDescricao.trim()) return;
  const nova = {
    data: this.novaConsultaData,
    descricao: this.novaConsultaDescricao.trim()
  };

  // Buscar paciente completo
  this.pacientesReadService.getPacienteById(this.pacienteId).subscribe(paciente => {
    const consultasAtualizadas = [...(paciente.consultas || []), nova];
    const pacienteAtualizado = { ...paciente, consultas: consultasAtualizadas };

    this.http.put<any>(`http://localhost:8080/api/pacientes/${this.pacienteId}`, pacienteAtualizado)
      .subscribe(() => {
        this.consultas = consultasAtualizadas;
        this.novaConsultaData = '';
        this.novaConsultaDescricao = '';
      });
  });
}
removerConsulta(index: number) {
  this.pacientesReadService.getPacienteById(this.pacienteId).subscribe(paciente => {
    const novasConsultas = paciente.consultas.slice();
    novasConsultas.splice(index, 1);
    const pacienteAtualizado = { ...paciente, consultas: novasConsultas };

    this.http.put<any>(`http://localhost:8080/api/pacientes/${this.pacienteId}`, pacienteAtualizado)
      .subscribe(() => {
        this.consultas = novasConsultas;
      });
  });
}
}