import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBell, faCircleUser, faDesktop, faFileInvoice, faHospitalUser, faMessage } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-paciente-inicial',
  standalone: true,
  imports:[

    FontAwesomeModule,
    RouterModule

  ],
  templateUrl: './inicial.html',
  styleUrls: ['./inicial.css']
})
export class InicialComponent {


  faDesktop = faDesktop;
    faCircleUser = faCircleUser;
    faHospitalUser = faHospitalUser;
    faFileInvoice = faFileInvoice;
    faMessage = faMessage;
    faBell = faBell;

}