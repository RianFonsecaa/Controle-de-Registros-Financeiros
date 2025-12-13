import { Routes } from '@angular/router';
import { authGuard } from './guards/auth-guard';
import { roleGuard } from './guards/role-guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login').then((m) => m.Login),
  },

  {
    path: '',
    loadComponent: () =>
      import('./pages/main-layout/main-layout').then((m) => m.MainLayout),
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./pages/dashboard/dashboard').then((m) => m.Dashboard),
      },
      {
        path: 'cobrancas',
        loadComponent: () =>
          import('./pages/registros/cobrancas/cobrancas').then(
            (m) => m.Cobrancas
          ),
      },
      {
        path: 'pix',
        loadComponent: () =>
          import('./pages/registros/pix/pix').then((m) => m.Pix),
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
        data: { roles: ['ADMIN'] },
        canActivate: [roleGuard],
      },
    ],
  },
];
