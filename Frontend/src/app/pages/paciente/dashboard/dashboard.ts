import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardReadService } from '../../../services/dashboard/dashboard-read.service';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'],
  providers: [DashboardReadService]
})
export class Dashboard implements OnInit {
  cards: any[] = [];

  constructor(private dashboardreadService: DashboardReadService) {}

  ngOnInit(): void {
    this.dashboardreadService.getDashboard().subscribe((data: any[]) => {
      this.cards = data;
    });
  }
}