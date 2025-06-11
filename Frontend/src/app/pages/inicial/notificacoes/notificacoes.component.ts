import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificacoesReadService } from '../../../services/notificacoes/notificacoes-read.service';

@Component({
  selector: 'app-notificacoes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notificacoes.component.html',
  styleUrls: ['./notificacoes.component.css']
})
export class NotificacoesComponent implements OnInit {
  notificacoes: any[] = [];

  constructor(private notificacoesReadService: NotificacoesReadService) {}

  ngOnInit(): void {
    this.notificacoesReadService.getNotificacoes().subscribe(data => {
      this.notificacoes = data;
    });
  }
}