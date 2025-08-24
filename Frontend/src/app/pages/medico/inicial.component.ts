import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faDesktop, faCircleUser, faHospitalUser, faFileInvoice, faMessage, faBell, faSignOutAlt, faUserMd } from '@fortawesome/free-solid-svg-icons';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';

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
  faUserMd = faUserMd;
  faSignOutAlt = faSignOutAlt;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    this.medicoNome = currentUser?.name || 'Médico';
    this.medicoEmail = currentUser?.email || 'medico@exemplo.com';
  }

  logout(): void {
    this.authService.logout();
  }
}
