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
  { path: 'login', component: Login },
  { path: 'dashboard', component: Dashboard },
  { path: 'cobrancas', component: Cobrancas },
  { path: 'pix', component: Pix },
  { path: 'vales', component: Vales },
  { path: 'relatorios', component: Relatorios },
  { path: 'cadastros', component: Cadastros },
];
