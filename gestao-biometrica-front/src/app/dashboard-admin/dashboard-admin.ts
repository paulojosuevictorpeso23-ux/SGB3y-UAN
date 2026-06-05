import { Component, inject, OnInit, Input } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard-admin',
  imports: [CommonModule],
  template: `
    <div style="font-family: 'Segoe UI', Roboto, sans-serif; background-color: #f8f9fa; min-height: 100vh; padding: 20px;">
      
      <div style="background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%); color: white; padding: 30px; border-radius: 12px; margin-bottom: 30px; box-shadow: 0 4px 15px rgba(30, 58, 138, 0.15);">
        <h2 style="margin: 0; font-size: 24px; font-weight: 600;">👋 Olá, {{ usuarioLogado?.nome || 'Administrador' }}!</h2>
        <p style="margin: 8px 0 0 0; opacity: 0.9; font-size: 14px;">
          Bem-vindo ao painel de controlo do <b>Sistema de Gestão Biométrica - UAN</b>. Aqui está o resumo das atividades de hoje.
        </p>
      </div>

      <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 20px; margin-bottom: 30px;">
        
        <div style="background: white; padding: 20px; border-radius: 10px; border: 1px solid #eef2f6; box-shadow: 0 2px 4px rgba(0,0,0,0.02); display: flex; align-items: center; justify-content: space-between;">
          <div>
            <p style="margin: 0; color: #64748b; font-size: 12px; font-weight: 600; text-transform: uppercase;">Total Utilizadores</p>
            <h3 style="margin: 8px 0 0 0; color: #1e293b; font-size: 28px; font-weight: 700;">{{ stats.totalUsuarios }}</h3>
          </div>
          <div style="background: #eff6ff; color: #2563eb; width: 45px; height: 45px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 20px;">👥</div>
        </div>

        <div style="background: white; padding: 20px; border-radius: 10px; border: 1px solid #eef2f6; box-shadow: 0 2px 4px rgba(0,0,0,0.02); display: flex; align-items: center; justify-content: space-between;">
          <div>
            <p style="margin: 0; color: #64748b; font-size: 12px; font-weight: 600; text-transform: uppercase;">Presenças Hoje</p>
            <h3 style="margin: 8px 0 0 0; color: #1e293b; font-size: 28px; font-weight: 700;">{{ stats.presencasHoje }}</h3>
          </div>
          <div style="background: #ecfdf5; color: #059669; width: 45px; height: 45px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 20px;">🟢</div>
        </div>

        <div style="background: white; padding: 20px; border-radius: 10px; border: 1px solid #eef2f6; box-shadow: 0 2px 4px rgba(0,0,0,0.02); display: flex; align-items: center; justify-content: space-between;">
          <div>
            <p style="margin: 0; color: #64748b; font-size: 12px; font-weight: 600; text-transform: uppercase;">Estudantes Ativos</p>
            <h3 style="margin: 8px 0 0 0; color: #1e293b; font-size: 28px; font-weight: 700;">{{ stats.estudantesAtivos }}</h3>
          </div>
          <div style="background: #fffbeb; color: #d97706; width: 45px; height: 45px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 20px;">🎓</div>
        </div>

        <div style="background: white; padding: 20px; border-radius: 10px; border: 1px solid #eef2f6; box-shadow: 0 2px 4px rgba(0,0,0,0.02); display: flex; align-items: center; justify-content: space-between;">
          <div>
            <p style="margin: 0; color: #64748b; font-size: 12px; font-weight: 600; text-transform: uppercase;">Taxa de Frequência</p>
            <h3 style="margin: 8px 0 0 0; color: #1e293b; font-size: 28px; font-weight: 700;">{{ stats.taxaFrequencia }}%</h3>
          </div>
          <div style="background: #faf5ff; color: #7c3aed; width: 45px; height: 45px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 20px;">📈</div>
        </div>

      </div>

      <div style="display: grid; grid-template-columns: 1fr 2fr; gap: 25px; flex-wrap: wrap;">
        
        <div style="display: flex; flex-direction: column; gap: 25px;">
          
          <div style="background: white; padding: 25px; border-radius: 10px; border: 1px solid #eef2f6; box-shadow: 0 2px 4px rgba(0,0,0,0.02);">
            <h3 style="margin: 0 0 15px 0; font-size: 16px; color: #1e293b;">Ocupação do Campus</h3>
            <p style="font-size: 12px; color: #64748b; margin-bottom: 8px;">Estudantes com entrada registada neste momento:</p>
            
            <div style="background: #e2e8f0; width: 100%; height: 12px; border-radius: 6px; overflow: hidden; margin-bottom: 15px;">
              <div [style.width.%]="stats.taxaFrequencia" style="background: #3b82f6; height: 100%; border-radius: 6px; transition: width 0.5s ease;"></div>
            </div>
            
            <div style="display: flex; justify-content: space-between; font-size: 12px; font-weight: bold; color: #475569;">
              <span>0%</span>
              <span>Metas Diárias ({{ stats.taxaFrequencia }}%)</span>
              <span>100%</span>
            </div>
          </div>

          <div style="background: white; padding: 25px; border-radius: 10px; border: 1px solid #eef2f6; box-shadow: 0 2px 4px rgba(0,0,0,0.02);">
            <h3 style="margin: 0 0 15px 0; font-size: 16px; color: #1e293b;">⚡ Atalhos Rápidos</h3>
            <div style="display: flex; flex-direction: column; gap: 10px;">
              <button style="padding: 12px; text-align: left; background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 6px; cursor: pointer; font-weight: 500; color: #334155; font-size: 13px; transition: background 0.2s;">
                ➕ Registar Novo Utilizador
              </button>
              <button style="padding: 12px; text-align: left; background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 6px; cursor: pointer; font-weight: 500; color: #334155; font-size: 13px; transition: background 0.2s;">
                📥 Exportar Relatório Diário (PDF)
              </button>
            </div>
          </div>

        </div>

        <div style="background: white; padding: 25px; border-radius: 10px; border: 1px solid #eef2f6; box-shadow: 0 2px 4px rgba(0,0,0,0.02);">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
            <h3 style="margin: 0; font-size: 16px; color: #1e293b;">🛡️ Fluxo Biométrico em Tempo Real</h3>
            <span style="background: #f1f5f9; color: #475569; padding: 4px 10px; border-radius: 12px; font-size: 11px; font-weight: 600;">Atualizado Agora</span>
          </div>

          <div style="display: flex; flex-direction: column; gap: 12px;">
            @for (log of ultimosAcessos; track log.id) {
              <div style="display: flex; align-items: center; justify-content: space-between; padding: 12px; border-bottom: 1px solid #f1f5f9; font-size: 13px;">
                <div style="display: flex; align-items: center; gap: 12px;">
                  <div style="width: 36px; height: 36px; background: #e2e8f0; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-weight: bold; color: #475569;">
                    {{ log.nome.substring(0, 1) }}
                  </div>
                  <div>
                    <div style="font-weight: 600; color: #1e293b;">{{ log.nome }}</div>
                    <div style="font-size: 11px; color: #64748b;">ID Utilizador: #{{ log.userId }} | Cargo: {{ log.cargo }}</div>
                  </div>
                </div>

                <div style="text-align: right;">
                  <span [style.background-color]="log.tipo === 'ENTRADA' ? '#e6f4ea' : '#fce8e6'"
                        [style.color]="log.tipo === 'ENTRADA' ? '#137333' : '#c5221f'"
                        style="padding: 3px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; display: inline-block; margin-bottom: 4px;">
                    {{ log.tipo }}
                  </span>
                  <div style="font-size: 11px; color: #94a3b8; font-family: monospace;">{{ log.hora }}</div>
                </div>
              </div>
            } @empty {
              <div style="text-align: center; color: #94a3b8; padding: 20px; font-style: italic;">
                Nenhuma atividade biométrica intercetada neste turno.
              </div>
            }
          </div>
        </div>

      </div>

    </div>
  `,
  styles: []
})
export class DashboardAdminComponent implements OnInit {
  private http = inject(HttpClient);

  @Input() usuarioLogado: any = null;

  // Estrutura de dados mockada inicialmente para a tela brilhar logo à primeira
  stats = {
    totalUsuarios: 12,
    presencasHoje: 5,
    estudantesAtivos: 9,
    taxaFrequencia: 75
  };

  // Linha do tempo fictícia para povoar o fluxo enquanto a API assiduidade descansa
  ultimosAcessos = [
    { id: 1, userId: 2, nome: 'Paulo Peso', cargo: 'ADMIN', tipo: 'ENTRADA', hora: '23:58:21' },
    { id: 2, userId: 1, nome: 'Administrador do Sistema', cargo: 'ADMIN', tipo: 'ENTRADA', hora: '23:58:10' },
    { id: 3, userId: 4, nome: 'Yhyh', cargo: 'ESTUDANTE', tipo: 'ENTRADA', hora: '23:52:32' },
    { id: 4, userId: 5, nome: 'Miguel', cargo: 'ESTUDANTE', tipo: 'ENTRADA', hora: '23:34:47' }
  ];

  ngOnInit() {
    this.carregarEstatisticasReais();
  }

  carregarEstatisticasReais() {
    // Por agora deixamos os dados visuais fixos acima para poderes ver a tela linda.
    // Quando voltarmos aos bugs das APIs, bastará fazermos um http.get aqui para calcular dinamicamente!
    console.log('Dashboard Admin pronto para monitoramento de assiduidade.');
  }
}
