import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientesReadService } from '../../../../../../services/pacientes/pacientes-read.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-cirurgias',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './cirurgias.html',
  styleUrls: ['./cirurgias.css']
})
export class Cirurgias implements OnInit {
  cirurgias: any[] = [];
  pacienteNome: string = '';
  pacienteId: string = '';
  novaCirurgiaNome: string = '';
  novaCirurgiaData: string = '';
  novaCirurgiaDescricao: string = '';

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
        this.cirurgias = paciente.cirurgias || [];
        this.pacienteNome = paciente.nome;
      }
    });
  }

  adicionarCirurgia() {
    if (!this.novaCirurgiaNome.trim() || !this.novaCirurgiaData || !this.novaCirurgiaDescricao.trim()) return;
    const nova = {
      nome: this.novaCirurgiaNome.trim(),
      data: this.novaCirurgiaData,
      descricao: this.novaCirurgiaDescricao.trim()
    };
    this.http.patch<any>(`http://localhost:3000/pacientes/${this.pacienteId}`, {
      cirurgias: [...this.cirurgias, nova]
    }).subscribe(() => {
      this.cirurgias.push(nova);
      this.novaCirurgiaNome = '';
      this.novaCirurgiaData = '';
      this.novaCirurgiaDescricao = '';
    });
  }

  removerCirurgia(index: number) {
    const novasCirurgias = this.cirurgias.slice();
    novasCirurgias.splice(index, 1);
    this.http.patch<any>(`http://localhost:3000/pacientes/${this.pacienteId}`, {
      cirurgias: novasCirurgias
    }).subscribe(() => {
      this.cirurgias = novasCirurgias;
    });
  }
}