import { Component } from '@angular/core';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { LoginBaseComponent } from './login-base/login-base.component';

@Component({
  selector: 'app-login', 
  standalone:true,
  imports: [

    TabsModule,
    LoginBaseComponent

  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

}
