import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { InicialComponent } from './pages/inicial/inicial.component';

export const routes: Routes = [

    {path: '',component: AppComponent},
    {path: 'login', component: LoginComponent},
    {path: 'inicio',component:InicialComponent}

];
