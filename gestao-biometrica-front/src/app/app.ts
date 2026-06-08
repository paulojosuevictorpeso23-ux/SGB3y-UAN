import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

// Importamos todas as 6 telas do ecossistema
import { LandingPageComponent } from './landing-page/landing-page';
import { LoginComponent } from './login/login';
import { RegistarPontoComponent } from './registar-ponto/registar-ponto';
import { RegistarUtilizadorComponent } from './registar-utilizador/registar-utilizador';
import { ConsultarAssiduidadeComponent } from './consultar-assiduidade/consultar-assiduidade';
import { DashboardAdminComponent } from './dashboard-admin/dashboard-admin';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet, 
    LandingPageComponent, 
    LoginComponent, 
    RegistarPontoComponent, 
    RegistarUtilizadorComponent, 
    ConsultarAssiduidadeComponent,
    DashboardAdminComponent
  ],
  template: `
    @if (!usuarioLogado()) {
      
      @if (!exibirLoginForm()) {
        <app-landing-page (aoClicarEntrar)="exibirLoginForm.set(true)"></app-landing-page>
      } 
      @else {
        <div style="position: relative;">
          <button (click)="exibirLoginForm.set(false)" 
                  style="position: absolute; top: 15px; left: 15px; padding: 6px 12px; background: #f1f5f9; border: 1px solid #cbd5e1; border-radius: 4px; cursor: pointer; font-size: 11px; font-weight: bold; color: #475569; z-index: 999;">
            ⬅️ Voltar ao Início
          </button>
          
          <app-login (loginSucesso)="aoLogarComSucesso($event)"></app-login>
        </div>
      }

    } @else if (usuarioLogado().cargo?.nome === 'ADMIN') {
      <div style="font-family: sans-serif; background-color: #fafafa; min-height: 100vh; padding-bottom: 40px; margin: 0; animation: iniciarPainel 0.3s ease-out;">
        
        <div style="background-color: #0d47a1; color: white; padding: 20px; text-align: center; position: relative; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
          <h1 style="margin: 0; font-size: 24px; letter-spacing: 0.5px;">Sistema de Gestão Biométrica - UAN</h1>
          <p style="margin: 6px 0 0 0; opacity: 0.8; font-size: 14px;">Painel do Administrador | Olá, {{ usuarioLogado().nome }}</p>
          
          <button (click)="fazerLogout()" 
                  style="position: absolute; right: 20px; top: 25px; padding: 8px 14px; background-color: #c5221f; color: white; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; font-size: 12px;">
            🚪 Sair
          </button>
        </div>

        <div style="display: flex; justify-content: center; gap: 15px; margin: 25px 0; flex-wrap: wrap; padding: 0 10px;">
          <button (click)="mudarTela('dashboard')"
                  [style.background-color]="telaAtual() === 'dashboard' ? '#1565c0' : '#e0e0e0'"
                  [style.color]="telaAtual() === 'dashboard' ? 'white' : '#333'"
                  style="padding: 12px 20px; font-size: 14px; font-weight: bold; border: none; border-radius: 4px; cursor: pointer; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
            🏠 Dashboard
          </button>

          <button (click)="mudarTela('utilizadores')"
                  [style.background-color]="telaAtual() === 'utilizadores' ? '#1565c0' : '#e0e0e0'"
                  [style.color]="telaAtual() === 'utilizadores' ? 'white' : '#333'"
                  style="padding: 12px 20px; font-size: 14px; font-weight: bold; border: none; border-radius: 4px; cursor: pointer; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
            👥 Gerir Utilizadores
          </button>
          
          <button (click)="mudarTela('ponto')"
                  [style.background-color]="telaAtual() === 'ponto' ? '#1565c0' : '#e0e0e0'"
                  [style.color]="telaAtual() === 'ponto' ? 'white' : '#333'"
                  style="padding: 12px 20px; font-size: 14px; font-weight: bold; border: none; border-radius: 4px; cursor: pointer; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
            ⏱️ Registar Assiduidade
          </button>

          <button (click)="mudarTela('relatorios')"
                  [style.background-color]="telaAtual() === 'relatorios' ? '#1565c0' : '#e0e0e0'"
                  [style.color]="telaAtual() === 'relatorios' ? 'white' : '#333'"
                  style="padding: 12px 20px; font-size: 14px; font-weight: bold; border: none; border-radius: 4px; cursor: pointer; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
            📊 Ver Relatórios
          </button>
        </div>

        <hr style="border: 0; border-top: 1px solid #ddd; width: 85%; margin: 0 auto 30px auto;">

        <div style="animation: surgir 0.25s ease-out; max-width: 1200px; margin: 0 auto; padding: 0 20px;">
          @if (telaAtual() === 'dashboard') {
            <app-dashboard-admin [usuarioLogado]="usuarioLogado()"></app-dashboard-admin>
          } @else if (telaAtual() === 'utilizadores') {
            <app-registar-utilizador></app-registar-utilizador>
          } @else if (telaAtual() === 'ponto') {
            <app-registar-ponto></app-registar-ponto>
          } @else if (telaAtual() === 'relatorios') {
            <div style="max-width: 1000px; margin: 0 auto;">
               <app-consultar-assiduidade [usuarioLogado]="usuarioLogado()"></app-consultar-assiduidade>
            </div>
          }
        </div>
      </div>
    } @else if (usuarioLogado().cargo?.nome === 'ESTUDANTE') {
      <div style="font-family: sans-serif; background-color: #fafafa; min-height: 100vh; padding-bottom: 40px; margin: 0; animation: iniciarPainel 0.3s ease-out;">
        
        <div style="background-color: #1b5e20; color: white; padding: 20px; text-align: center; position: relative; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
          <h1 style="margin: 0; font-size: 24px; letter-spacing: 0.5px;">Portal do Estudante - UAN</h1>
          <p style="margin: 6px 0 0 0; opacity: 0.8; font-size: 14px;">Bem-vindo, {{ usuarioLogado().nome }}</p>
          
          <button (click)="fazerLogout()" 
                  style="position: absolute; right: 20px; top: 25px; padding: 8px 14px; background-color: #c5221f; color: white; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; font-size: 12px;">
            🚪 Sair
          </button>
        </div>

        <div style="max-width: 1000px; margin: 30px auto; padding: 0 20px; animation: surgir 0.25s ease-out;">
          <app-consultar-assiduidade [usuarioLogado]="usuarioLogado()"></app-consultar-assiduidade>
        </div>
      </div>
    } @else {
      <div style="font-family: sans-serif; padding: 40px; text-align: center; background: white; max-width: 500px; margin: 80px auto; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); border-top: 4px solid #c5221f;">
        <h2 style="color: #c5221f; margin-top: 0;">⚠️ Perfil Não Mapeado</h2>
        <p>O login foi efetuado com sucesso, mas o Angular não encontrou um painel para este cargo.</p>
        <p style="background: #f1f5f9; padding: 12px; border-radius: 4px; font-family: monospace; font-weight: bold;">
          Cargo recebido: "{{ usuarioLogado()?.cargo?.nome }}"
        </p>
        <p style="font-size: 13px; color: #666;">Abre a consola do teu navegador (F12) para ver a resposta completa da API.</p>
        <button (click)="fazerLogout()" style="padding: 10px 20px; background-color: #0d47a1; color: white; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; margin-top: 10px;">
          Voltar para o Login
        </button>
      </div>
    }

    <router-outlet />
  `,
  styles: [`
    @keyframes iniciarPainel { from { opacity: 0; } to { opacity: 1; } }
    @keyframes surgir { from { opacity: 0; transform: translateY(8px); } to { opacity: 1; transform: translateY(0); } }
  `],
})
export class App {
  protected readonly title = signal('gestao-biometrica-front');
  protected usuarioLogado = signal<any>(null);
  protected telaAtual = signal('dashboard');
  protected exibirLoginForm = signal<boolean>(false);

  aoLogarComSucesso(usuario: any) {
    console.log('=== DADOS RECEBIDOS DO BACKEND ===');
    console.log(usuario);
    this.usuarioLogado.set(usuario);
  }

  mudarTela(nomeDaTela: string) {
    this.telaAtual.set(nomeDaTela);
  }

  fazerLogout() {
    this.usuarioLogado.set(null);
    this.exibirLoginForm.set(false);
  }
}
