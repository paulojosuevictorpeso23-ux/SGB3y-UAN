import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importante para o *ngIf
import { TerminalPontoComponent } from './components/terminal-ponto/terminal-ponto.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, TerminalPontoComponent, AdminDashboardComponent],
  template: `
    <nav style="background: #333; padding: 10px; color: white; display: flex; gap: 20px;">
      <button (click)="pagina = 'terminal'">Ir para Terminal</button>
      <button (click)="pagina = 'admin'">Ir para Admin</button>
    </nav>

    <main>
      <app-terminal-ponto *ngIf="pagina === 'terminal'"></app-terminal-ponto>
      <app-admin-dashboard *ngIf="pagina === 'admin'"></app-admin-dashboard>
    </main>
  `
})
export class AppComponent {
  pagina: 'terminal' | 'admin' = 'terminal'; // Página inicial
}
