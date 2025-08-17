import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faDesktop } from '@fortawesome/free-solid-svg-icons';
import { faCircleUser } from '@fortawesome/free-solid-svg-icons';
import { faHospitalUser } from '@fortawesome/free-solid-svg-icons';
import { faFileInvoice } from '@fortawesome/free-solid-svg-icons';
import { faMessage } from '@fortawesome/free-solid-svg-icons';
import { faBell } from '@fortawesome/free-solid-svg-icons';
import { faSignOutAlt } from '@fortawesome/free-solid-svg-icons';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-inicial',
  standalone: true,
  imports: [
    FontAwesomeModule,
    RouterModule,
    CommonModule
  ],
  templateUrl: './inicial.component.html',
  styleUrl: './inicial.component.css'
})
export class InicialComponent implements OnInit {
  medicoNome: string = '';
  medicoEmail: string = '';

  faDesktop = faDesktop;
  faCircleUser = faCircleUser;
  faHospitalUser = faHospitalUser;
  faFileInvoice = faFileInvoice;
  faMessage = faMessage;
  faBell = faBell;
  faSignOutAlt = faSignOutAlt;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.medicoNome = localStorage.getItem('userName') || 'Médico';
    this.medicoEmail = localStorage.getItem('userEmail') || 'medico@exemplo.com';
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
