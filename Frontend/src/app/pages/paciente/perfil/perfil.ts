import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil.html',
  styleUrls: ['./perfil.css']
})
export class Perfil implements OnInit {
  paciente: any = {};
  editando = false;
  mensagem = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    // Simulação: buscar paciente com id 1 (ajuste para pegar o id do usuário logado)
    this.http.get<any>('http://localhost:3000/pacientes/1').subscribe(paciente => {
      this.paciente = paciente;
    });
  }

  salvar() {
    this.http.patch<any>(`http://localhost:3000/pacientes/${this.paciente.id}`, this.paciente)
      .subscribe(() => {
        this.editando = false;
        this.mensagem = 'Perfil atualizado com sucesso!';
        setTimeout(() => this.mensagem = '', 2000);
      });
  }
}