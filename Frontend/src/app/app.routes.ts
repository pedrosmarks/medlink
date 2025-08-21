import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { InicialComponent as MedicoInicialComponent } from './pages/medico/inicial.component';
import { DashboardComponent } from './pages/medico/dashboard/dashboard.component';
import { PerfilComponent } from './pages/medico/perfil/perfil.component';
import { PacientesComponent } from './pages/medico/pacientes/pacientes.component';
import { RelatoriosComponent } from './pages/medico/relatorios/relatorios.component';
import { MensagemComponent } from './pages/medico/mensagem/mensagem.component';
import { NotificacoesComponent } from './pages/medico/notificacoes/notificacoes.component';

import { InicialComponent as PacienteInicialComponent } from './pages/paciente/inicial';
import { Dashboard as PacienteDashboardComponent } from './pages/paciente/dashboard/dashboard';
import { Perfil as PacientePerfilComponent } from './pages/paciente/perfil/perfil';
import { Prontuario as PacienteProntuarioComponent } from './pages/paciente/prontuario/prontuario';
import { Mensagem as PacienteMensagemComponent } from './pages/paciente/mensagem/mensagem';
import { Requisicoes as PacienteRequisicoesComponent } from './pages/paciente/requisicoes/requisicoes';
import { Medico as PacienteMedicoComponent } from './pages/paciente/medico/medico';


export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'login', component: LoginComponent },

  // Rotas do médico
  {
    path: 'medico',
    component: MedicoInicialComponent,
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
  },

  // Rotas do paciente
  {
    path: 'paciente',
    component: PacienteInicialComponent,
    children: [
      { path: 'dashboard', component: PacienteDashboardComponent },
      { path: 'perfil', component: PacientePerfilComponent },
      { path: 'medico', component: PacienteMedicoComponent},
      { path: 'mensagem', component: PacienteMensagemComponent },
      { path: 'notificacoes', component: NotificacoesComponent },
      {
        path: 'prontuario',
        loadComponent: () =>
          import('./pages/paciente/prontuario/prontuario').then(m => m.Prontuario),
        children: [
          {
            path: '',
            pathMatch: 'full',
            loadComponent: () =>
              import('./pages/paciente/prontuario/prontuario-cards/prontuario-cards').then(m => m.ProntuarioCards)
          },
          {
            path: 'consultas',
            loadComponent: () =>
              import('./pages/paciente/prontuario/consultas/consultas/consultas').then(m => m.Consultas)
          },
          {
            path: 'vacinas',
            loadComponent: () =>
              import('./pages/paciente/prontuario/vacinas/vacinas/vacinas').then(m => m.Vacinas)
          },
          {
            path: 'medicamentos',
            loadComponent: () =>
              import('./pages/paciente/prontuario/medicamentos/medicamentos/medicamentos').then(m => m.Medicamentos)
          },
          {
            path: 'cirurgias',
            loadComponent: () =>
              import('./pages/paciente/prontuario/cirurgias/cirurgias/cirurgias').then(m => m.Cirurgias)
          },
          {
            path: 'diagnosticos',
            loadComponent: () =>
              import('./pages/paciente/prontuario/diagnosticos/diagnosticos/diagnosticos').then(m => m.Diagnosticos)
          },
          {
            path: 'alergias',
            loadComponent: () =>
              import('./pages/paciente/prontuario/alergias/alergias/alergias').then(m => m.Alergias)
          }
        ]
      },
      { path: 'requisicoes', component: PacienteRequisicoesComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  }
];