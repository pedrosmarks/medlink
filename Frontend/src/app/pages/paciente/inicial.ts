import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBell, faCircleUser, faDesktop, faFileInvoice, faHospitalUser, faMessage, faSignOutAlt, faUser, faClipboardCheck, faUserInjured } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../../services/auth/auth.service';
import { AccessRequestsService } from '../../services/access-requests/access-requests.service';
import { Subscription, filter } from 'rxjs';

@Component({
  selector: 'app-paciente-inicial',
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule,
    RouterModule
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
  pacienteId: number = 0;
  
  // Requisições
  hasRequests = false;
  requestCount = 0;
  private routerSubscription?: Subscription;

  constructor(
    private router: Router, 
    private authService: AuthService,
    private accessRequestsService: AccessRequestsService
  ) {}

  ngOnInit(): void {
    this.loadUserData();
    this.checkRequests();
    
    // Recarrega requisições quando navegar para página de requisições
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        if (event.url.includes('/paciente/requisicoes')) {
          setTimeout(() => this.checkRequests(), 500);
        }
      });
  }

  ngOnDestroy(): void {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

  private loadUserData(): void {
    const currentUser = this.authService.getCurrentUser();
    this.userName = currentUser?.name || 'Paciente';
    this.userEmail = currentUser?.email || 'paciente@medlink.com';
    this.pacienteId = parseInt(localStorage.getItem('userId') || '0');
  }

  private checkRequests(): void {
    if (this.pacienteId > 0) {
      this.accessRequestsService.getPendingRequests(this.pacienteId).subscribe({
        next: (response) => {
          const requests = response.data || [];
          this.requestCount = requests.length;
          this.hasRequests = this.requestCount > 0;
        },
        error: (error) => {
          console.warn('Backend não implementado ainda ou erro no servidor:', error.status);
          this.requestCount = 0;
          this.hasRequests = false;
        }
      });
    }
  }

  onLogout(): void {
    this.authService.logout();
  }
}