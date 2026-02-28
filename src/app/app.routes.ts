import { Routes } from '@angular/router';
import { authGuard } from './guards/auth-guard';
import { roleGuard } from './guards/role-guard';
import { loginGuard } from './guards/login-guard';
import { Dashboard } from './pages/dashboard/dashboard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  {
    path: 'login',
    canActivate: [loginGuard],
    loadComponent: () => import('./pages/login/login').then((m) => m.Login),
  },

  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/main-layout/main-layout').then((m) => m.MainLayout),
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./pages/dashboard/dashboard').then((m) => m.Dashboard),
      },
      {
        path: 'registros',
        loadComponent: () =>
          import('./pages/registros/registros').then((m) => m.Registros),
      },
      {
        path: 'cadastros',
        canActivate: [roleGuard],
        data: { roles: ['ADMIN'] },
        loadComponent: () =>
          import('./pages/cadastros/cadastros').then((m) => m.Cadastros),
      },
    ],
  },
];
