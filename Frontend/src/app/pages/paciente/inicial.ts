import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBell, faCircleUser, faDesktop, faFileInvoice, faHospitalUser, faMessage, faSignOutAlt, faUser, faClipboardCheck, faUserInjured } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-paciente-inicial',
  standalone: true,
  imports: [
    FontAwesomeModule,
    RouterModule
  ],
  templateUrl: './inicial.html',
  styleUrls: ['./inicial.css']
})
export class InicialComponent implements OnInit {

  // FontAwesome icons
  faDesktop = faDesktop;
  faCircleUser = faCircleUser;
  faHospitalUser = faHospitalUser;
  faFileInvoice = faFileInvoice;
  faMessage = faMessage;
  faBell = faBell;
  faClipboardCheck = faClipboardCheck;
  faUser = faUser;
  faUserInjured = faUserInjured;
  faSignOutAlt = faSignOutAlt;

  // User data
  userName: string = '';
  userEmail: string = '';

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    this.loadUserData();
  }

  private loadUserData(): void {
    const currentUser = this.authService.getCurrentUser();
    this.userName = currentUser?.name || 'Paciente';
    this.userEmail = currentUser?.email || 'paciente@medlink.com';
  }

  onLogout(): void {
    this.authService.logout();
  }
}