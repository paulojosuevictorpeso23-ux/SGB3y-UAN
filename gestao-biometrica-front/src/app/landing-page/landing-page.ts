import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-landing-page',
  imports: [CommonModule],
  template: `
    <div style="font-family: 'Segoe UI', Roboto, sans-serif; background: #fafafa; min-height: 100vh; color: #1e293b; margin: 0; padding: 0;">
      
      <nav style="background: white; border-bottom: 1px solid #e2e8f0; padding: 15px 40px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 1px 3px rgba(0,0,0,0.02);">
        <div style="display: flex; align-items: center; gap: 12px;">
          <div style="background: #0d47a1; color: white; width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 16px; box-shadow: 0 2px 4px rgba(13, 71, 161, 0.2);">
            UAN
          </div>
          <div>
            <h4 style="margin: 0; color: #0d47a1; font-size: 15px; font-weight: 700; letter-spacing: 0.5px;">UNIVERSIDADE AGOSTINHO NETO</h4>
            <p style="margin: 0; font-size: 11px; color: #64748b; font-weight: 500;">Faculdade de Ciências</p>
          </div>
        </div>
        
        <button (click)="aoClicarEntrar.emit()" 
                style="padding: 10px 20px; background-color: #0d47a1; color: white; border: none; border-radius: 6px; font-weight: 600; font-size: 13px; cursor: pointer; transition: all 0.2s; box-shadow: 0 2px 8px rgba(13, 71, 161, 0.15);">
          Aceder ao Portal 🔐
        </button>
      </nav>

      <header style="background: linear-gradient(135deg, #0d47a1 0%, #1565c0 100%); color: white; padding: 80px 20px; text-align: center; position: relative; overflow: hidden;">
        <div style="position: absolute; top: -50px; right: -50px; width: 300px; height: 300px; background: rgba(255,255,255,0.03); border-radius: 50%;"></div>
        <div style="position: absolute; bottom: -80px; left: -80px; width: 400px; height: 400px; background: rgba(255,255,255,0.02); border-radius: 50%;"></div>

        <div style="max-width: 800px; margin: 0 auto; position: relative; z-index: 2;">
          <span style="background: rgba(255,255,255,0.15); padding: 6px 14px; border-radius: 20px; font-size: 12px; font-weight: 600; letter-spacing: 1px; text-transform: uppercase;">
            Inovação Tecnológica Campus UAN
          </span>
          <h1 style="margin: 25px 0 15px 0; font-size: 42px; font-weight: 800; line-height: 1.2; letter-spacing: -0.5px;">
            Sistema de Gestão & Monitorização Biométrica
          </h1>
          <p style="margin: 0 auto 35px auto; font-size: 16px; opacity: 0.9; max-width: 600px; line-height: 1.6;">
            Modernização do ecossistema académico. Controlo rigoroso de assiduidade, fluxos de acessos em tempo real e segurança para estudantes, docentes e funcionários.
          </p>
          
          <div style="display: flex; gap: 15px; justify-content: center;">
            <button (click)="aoClicarEntrar.emit()" 
                    style="padding: 15px 30px; background-color: #ffffff; color: #0d47a1; border: none; border-radius: 8px; font-weight: bold; font-size: 15px; cursor: pointer; box-shadow: 0 4px 12px rgba(0,0,0,0.1); transition: transform 0.2s;">
              Entrar no Sistema
            </button>
            <a href="#funcionalidades" style="padding: 15px 25px; background-color: rgba(255,255,255,0.1); color: white; border: 1px solid rgba(255,255,255,0.2); border-radius: 8px; font-weight: 600; font-size: 14px; text-decoration: none; transition: background 0.2s;">
              Saber Mais 👇
            </a>
          </div>
        </div>
      </header>

      <section id="funcionalidades" style="max-width: 1100px; margin: -40px auto 60px auto; padding: 0 20px; position: relative; z-index: 10;">
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 25px;">
          
          <div style="background: white; border-radius: 12px; padding: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.04); border: 1px solid #eef2f6;">
            <div style="font-size: 32px; margin-bottom: 15px;">🛡️</div>
            <h3 style="margin: 0 0 10px 0; font-size: 18px; color: #0d47a1; font-weight: 700;">Segurança Biométrica</h3>
            <p style="margin: 0; color: #64748b; font-size: 13.5px; line-height: 1.6;">
              Eliminação de fraudes em assinaturas manuais de folhas de presença. Validação única e intransmissível através da leitura da impressão digital.
            </p>
          </div>

          <div style="background: white; border-radius: 12px; padding: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.04); border: 1px solid #eef2f6;">
            <div style="font-size: 32px; margin-bottom: 15px;">📊</div>
            <h3 style="margin: 0 0 10px 0; font-size: 18px; color: #0d47a1; font-weight: 700;">Relatórios em Tempo Real</h3>
            <p style="margin: 0; color: #64748b; font-size: 13.5px; line-height: 1.6;">
              O administrador consegue monitorizar de forma centralizada quem entrou ou saiu do campus ou de salas específicas a cada segundo.
            </p>
          </div>

          <div style="background: white; border-radius: 12px; padding: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.04); border: 1px solid #eef2f6;">
            <div style="font-size: 32px; margin-bottom: 15px;">🎓</div>
            <h3 style="margin: 0 0 10px 0; font-size: 18px; color: #0d47a1; font-weight: 700;">Transparência Estudantil</h3>
            <p style="margin: 0; color: #64748b; font-size: 13.5px; line-height: 1.6;">
              Os estudantes ganham um portal individual onde podem consultar com total transparência o seu histórico completo de assiduidade académica.
            </p>
          </div>

        </div>
      </section>

      <footer style="text-align: center; padding: 40px 20px; background-color: #1e293b; color: #94a3b8; font-size: 13px; border-top: 1px solid #334155;">
        <p style="margin: 0;">© 2026 Universidade Agostinho Neto. Todos os direitos reservados.</p>
        <p style="margin: 6px 0 0 0; color: #64748b; font-size: 11px;">Desenvolvido para o Controlo de Assiduidade Automatizado da UAN.</p>
      </footer>

    </div>
  `,
  styles: []
})
export class LandingPageComponent {
  @Output() aoClicarEntrar = new EventEmitter<void>();
}
