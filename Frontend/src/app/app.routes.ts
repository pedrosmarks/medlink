import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { InicialComponent } from './pages/medico/inicial.component';
import { DashboardComponent } from './pages/medico/dashboard/dashboard.component';
import { PerfilComponent } from './pages/medico/perfil/perfil.component';
import { PacientesComponent } from './pages/medico/pacientes/pacientes.component';
import { RelatoriosComponent } from './pages/medico/relatorios/relatorios.component';
import { MensagemComponent } from './pages/medico/mensagem/mensagem.component';
import { NotificacoesComponent } from './pages/medico/notificacoes/notificacoes.component';


export const routes: Routes = [

    {path: '',component: AppComponent},
    {path: 'login', component: LoginComponent},
    {
    path: 'medico',
    component: InicialComponent,
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'perfil', component: PerfilComponent },
      { path: 'pacientes', component: PacientesComponent },
      { path: 'relatorios', component: RelatoriosComponent },
      { path: 'mensagem', component: MensagemComponent },
      { path: 'notificacoes', component: NotificacoesComponent },
      {
        path: 'pacientes/:id/prontuario',
        loadComponent: () =>
        import('./pages/medico/pacientes/prontuario/prontuario/prontuario').then(m => m.Prontuario)
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

];
