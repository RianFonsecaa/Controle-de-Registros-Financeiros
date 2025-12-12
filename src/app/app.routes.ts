import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { Cobrancas } from './pages/registros/cobrancas/cobrancas';
import { Pix } from './pages/registros/pix/pix';
import { Vales } from './pages/registros/vales/vales';
import { Relatorios } from './pages/relatorios/relatorios';
import { Cadastros } from './pages/cadastros/cadastros';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

  {
    path: 'login',
    loadComponent: () => import('./pages/login/login').then((m) => m.Login),
  },

  {
    path: 'dashboard',
    loadComponent: () =>
      import('./pages/dashboard/dashboard').then((m) => m.Dashboard),
  },
  {
    path: 'cobrancas',
    loadComponent: () =>
      import('./pages/registros/cobrancas/cobrancas').then((m) => m.Cobrancas),
  },
  {
    path: 'pix',
    loadComponent: () => import('./pages/registros/pix/pix').then((m) => m.Pix),
  },
  {
    path: 'vales',
    loadComponent: () =>
      import('./pages/registros/vales/vales').then((m) => m.Vales),
  },
  {
    path: 'relatorios',
    loadComponent: () =>
      import('./pages/relatorios/relatorios').then((m) => m.Relatorios),
  },
  {
    path: 'cadastros',
    loadComponent: () =>
      import('./pages/cadastros/cadastros').then((m) => m.Cadastros),
  },
];
