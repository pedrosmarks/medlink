import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RelatoriosReadService } from '../../../services/relatorios/relatorios-read.service';

@Component({
  selector: 'app-relatorios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './relatorios.component.html',
  styleUrls: ['./relatorios.component.css']
})
export class RelatoriosComponent implements OnInit {
  relatorios: any[] = [];

  constructor(private relatoriosReadService: RelatoriosReadService) {}

  ngOnInit(): void {
    this.relatoriosReadService.getRelatorios().subscribe(data => {
      this.relatorios = data;
    });
  }
}