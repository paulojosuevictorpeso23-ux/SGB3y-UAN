import { Routes } from '@angular/router';
import { TerminalPontoComponent } from './components/terminal-ponto/terminal-ponto.component';
import { LoginComponent } from './components/login/login.component';

export const routes: Routes = [
  { path: '', redirectTo: 'ponto', pathMatch: 'full' }, // Redireciona a raiz para /ponto
  { path: 'ponto', component: TerminalPontoComponent },
  { path: 'login', component: LoginComponent }
];

