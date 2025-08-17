import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBell, faCircleUser, faDesktop, faFileInvoice, faHospitalUser, faMessage, faSignOutAlt, faUser } from '@fortawesome/free-solid-svg-icons';

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
  faUser = faUser;
  faSignOutAlt = faSignOutAlt;

  // User data
  userName: string = '';
  userEmail: string = '';

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.loadUserData();
  }

  private loadUserData(): void {
    // Carrega dados do usuário do localStorage ou serviço
    const userData = localStorage.getItem('userData');
    if (userData) {
      const user = JSON.parse(userData);
      this.userName = user.name || 'Paciente';
      this.userEmail = user.email || 'paciente@medlink.com';
    } else {
      this.userName = 'Paciente';
      this.userEmail = 'paciente@medlink.com';
    }
  }

  onLogout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}