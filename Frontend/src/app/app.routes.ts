import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { InicialComponent } from './pages/inicial/inicial.component';
import { DashboardComponent } from './pages/inicial/dashboard/dashboard.component';
import { PerfilComponent } from './pages/inicial/perfil/perfil.component';
import { PacientesComponent } from './pages/inicial/pacientes/pacientes.component';
import { RelatoriosComponent } from './pages/inicial/relatorios/relatorios.component';
import { MensagemComponent } from './pages/inicial/mensagem/mensagem.component';
import { NotificacoesComponent } from './pages/inicial/notificacoes/notificacoes.component';


export const routes: Routes = [

    {path: '',component: AppComponent},
    {path: 'login', component: LoginComponent},
    {
    path: 'inicio',
    component: InicialComponent,
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'perfil', component: PerfilComponent },
      { path: 'pacientes', component: PacientesComponent },
      { path: 'relatorios', component: RelatoriosComponent },
      { path: 'mensagem', component: MensagemComponent },
      { path: 'notificacoes', component: NotificacoesComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

];
