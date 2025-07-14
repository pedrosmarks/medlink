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
  { path: '', component: AppComponent },
  { path: 'login', component: LoginComponent },
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
    import('./pages/medico/pacientes/prontuario/prontuario/prontuario').then(m => m.Prontuario),
  children: [
    {
      path: '',
      pathMatch: 'full',
      loadComponent: () =>
        import('./pages/medico/pacientes/prontuario/prontuario-cards/prontuario-cards').then(m => m.ProntuarioCards)
    },
    {
      path: 'consultas',
      loadComponent: () =>
        import('./pages/medico/pacientes/prontuario/consultas/consultas/consultas').then(m => m.Consultas)
    },
    {
      path: 'vacinas',
      loadComponent: () =>
        import('./pages/medico/pacientes/prontuario/vacinas/vacinas/vacinas').then(m => m.Vacinas)
    },
    {
      path: 'medicamentos',
      loadComponent: () =>
        import('./pages/medico/pacientes/prontuario/medicamentos/medicamentos/medicamentos').then(m => m.Medicamentos)
    },
    {
      path: 'cirurgias',
      loadComponent: () =>
        import('./pages/medico/pacientes/prontuario/cirurgias/cirurgias/cirurgias').then(m => m.Cirurgias)
    },
    {
      path: 'diagnosticos',
      loadComponent: () =>
        import('./pages/medico/pacientes/prontuario/diagnosticos/diagnosticos/diagnosticos').then(m => m.Diagnosticos)
    },
    {
      path: 'alergias',
      loadComponent: () =>
        import('./pages/medico/pacientes/prontuario/alergias/alergias/alergias').then(m => m.Alergias)
    }
  ]
}
    ]
  }
];