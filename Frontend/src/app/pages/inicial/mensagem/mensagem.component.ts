import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { MensagemReadService } from '../../../services/mensagens/mensagens-read.service';

@Component({
  selector: 'app-mensagem',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './mensagem.component.html',
  styleUrls: ['./mensagem.component.css']
})
export class MensagemComponent implements OnInit {
  mensagens: any[] = [];

  constructor(private mensagemReadService: MensagemReadService) {}

  ngOnInit(): void {
    this.mensagemReadService.getMensagens().subscribe(data => {
      this.mensagens = data;
    });
  }
}