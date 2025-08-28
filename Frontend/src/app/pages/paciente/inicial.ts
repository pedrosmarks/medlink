import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBell, faCircleUser, faDesktop, faFileInvoice, faHospitalUser, faMessage, faSignOutAlt, faUser, faClipboardCheck, faUserInjured } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../../services/auth/auth.service';
import { AccessRequestsService } from '../../services/access-requests/access-requests.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-paciente-inicial',
  standalone: true,
  imports: [
    FontAwesomeModule,
    RouterModule,
    CommonModule
  ],
  templateUrl: './inicial.html',
  styleUrls: ['./inicial.css']
})
export class InicialComponent implements OnInit, OnDestroy {

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
  
  // Notifications
  notificationCount: number = 0;
  private pollingInterval: any;
  private pacienteId: number = 0;

  constructor(
    private router: Router, 
    private authService: AuthService,
    private accessRequestsService: AccessRequestsService
  ) {}

  ngOnInit(): void {
    this.loadUserData();
    this.startNotificationPolling();
  }

  ngOnDestroy(): void {
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval);
    }
  }

  private loadUserData(): void {
    const currentUser = this.authService.getCurrentUser();
    this.userName = currentUser?.name || 'Paciente';
    this.userEmail = currentUser?.email || 'paciente@medlink.com';
    this.pacienteId = parseInt(localStorage.getItem('userId') || '0');
  }

  private startNotificationPolling(): void {
    this.checkNotifications();
    this.pollingInterval = setInterval(() => {
      this.checkNotifications();
    }, 10000); // Verifica a cada 10 segundos
  }

  private checkNotifications(): void {
    if (this.pacienteId > 0) {
      this.accessRequestsService.getPendingRequests(this.pacienteId).subscribe({
        next: (response) => {
          this.notificationCount = response.data?.length || 0;
          console.log('🔔 Notificações pendentes:', this.notificationCount);
        },
        error: (error) => {
          console.warn('⚠️ Erro ao verificar notificações:', error.status);
        }
      });
    }
  }

  onLogout(): void {
    this.authService.logout();
  }
}