import { Component, inject, inject as angularInject, OnInit, Input, OnChanges, SimpleChanges, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-consultar-assiduidade',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div style="background-color: #fff3cd; color: #856404; padding: 15px; border-radius: 6px; margin-bottom: 25px; font-size: 13px; border: 1px solid #ffeeba; font-family: sans-serif;">
      <h4 style="margin: 0 0 8px 0; color: #533f03;"> Diagnóstico de Comunicação Frontend -> Backend</h4>
      <div style="display: flex; flex-wrap: wrap; gap: 20px;">
        <div>Utilizador Ativo: <b>{{ usuarioLogado?.nome || 'Nenhum' }}</b> (Cargo: <b>{{ usuarioLogado?.cargo || 'Nenhum' }}</b>)</div>
        <div>Recebidos do Spring Boot: <b style="color: #2e7d32; font-size: 14px;">{{ todosOsPontos.length }} registos</b></div>
        <div>Exibidos na Tabela: <b>{{ pontosFiltrados.length }} registos</b></div>
      </div>
    </div>

    @if (usuarioLogado?.cargo === 'ADMIN') {
      <div style="background-color: #f9f9f9; padding: 20px; border-radius: 8px; border: 1px solid #eee; border-top: 5px solid #0288d1; margin-bottom: 30px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); font-family: sans-serif; box-sizing: border-box;">
        <h3 style="margin-top: 0; color: #333; font-size: 1.1rem; margin-bottom: 5px;">
          ⚙️ Painel do Administrador: Bater Ponto de Contingência (Online)
        </h3>
        <p style="font-size: 0.85rem; color: #666; margin-bottom: 20px; margin-top: 0;">
          Use este formulário caso o dispositivo biométrico falhe. O registo constará explicitamente como efetuado via "SITE".
        </p>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 15px; align-items: flex-end;">
          
          <div style="display: flex; flex-direction: column;">
            <label style="margin-bottom: 5px; font-weight: bold; font-size: 13px;">ID ou Username do Utilizador:</label>
            <input [value]="identificadorForm()" (input)="identificadorForm.set($any($event.target).value)"
                   type="text" placeholder="Ex: paulo.josue ou ID numérico" 
                   style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 14px; height: 40px; outline: none;">
          </div>

          <div style="display: flex; flex-direction: column;">
            <label style="margin-bottom: 5px; font-weight: bold; font-size: 13px;">Tipo de Marcação:</label>
            <select [value]="tipoForm()" (change)="tipoForm.set($any($event.target).value)"
                    style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; background: white; height: 40px; font-size: 14px; box-sizing: border-box; outline: none;">
              <option value="ENTRADA">📥 ENTRADA</option>
              <option value="SAIDA">📤 SAÍDA</option>
            </select>
          </div>

          <div>
            <button (click)="baterPontoOnline()" 
                    style="width: 100%; padding: 12px; background-color: #0288d1; color: white; border: none; border-radius: 4px; font-size: 14px; font-weight: bold; cursor: pointer; height: 40px; display: flex; align-items: center; justify-content: center; gap: 5px;">
              🚀 Confirmar Ponto Online
            </button>
          </div>

        </div>
      </div>
    }

    <div style="background: white; border: 1px solid #eee; border-radius: 8px; padding: 25px; box-shadow: 0 2px 10px rgba(0,0,0,0.03); font-family: sans-serif;">
      
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; flex-wrap: wrap; gap: 15px;">
        <div>
          <h3 style="margin: 0; color: #333;">
            {{ usuarioLogado?.cargo === 'ADMIN' ? 'Relatório Geral de Assiduidade (UAN)' : 'O Meu Histórico de Presenças' }}
          </h3>
          <p style="margin: 5px 0 0 0; color: #777; font-size: 13px;">
            {{ usuarioLogado?.cargo === 'ADMIN' ? 'Consulta e filtragem de acessos biométricos de todos os utilizadores.' : 'Lista de todas as tuas entradas e saídas registadas no sistema.' }}
          </p>
        </div>

        @if (usuarioLogado?.cargo === 'ADMIN') {
          <div style="display: flex; gap: 10px; align-items: center;">
            <input (input)="filtrarPorTexto($any($event.target).value)"
                   type="text" placeholder="Pesquise por nome ou 'entrada'/'saida'..." 
                   style="padding: 10px 14px; border: 1px solid #ccc; border-radius: 6px; width: 300px; font-size: 13px; outline: none;">
            
            <button (click)="carregarTodosOsPontos()" 
                    style="padding: 10px 14px; background-color: #757575; color: white; border: none; border-radius: 6px; font-size: 13px; cursor: pointer; font-weight: bold;">
              Atualizar
            </button>
          </div>
        } @else {
          <button (click)="carregarTodosOsPontos()" 
                  style="padding: 10px 14px; background-color: #2e7d32; color: white; border: none; border-radius: 6px; font-size: 13px; cursor: pointer; font-weight: bold;">
            Atualizar as minhas presenças
          </button>
        }
      </div>

      <div style="width: 100%; overflow-x: auto; border: 1px solid #eee; border-radius: 6px;">
        <table style="width: 100%; min-width: 1000px; border-collapse: collapse; text-align: left; font-size: 13px;">
          <thead>
            <tr style="background-color: #f8f9fa; border-bottom: 2px solid #eee;">
              <th style="padding: 14px; width: 80px;">Nº Registo</th>
              @if (usuarioLogado?.cargo === 'ADMIN') {
                <th style="padding: 14px; width: 80px;">ID User</th>
                <th style="padding: 14px;">Nome do Utilizador</th>
                <th style="padding: 14px; width: 120px;">Cargo</th>
              }
              <th style="padding: 14px; width: 140px;">Tipo de Movimento</th>
              <th style="padding: 14px; width: 160px; text-align: center;">Origem / Via</th>
              <th style="padding: 14px; width: 220px;">Data & Hora do Acesso</th>
            </tr>
          </thead>
          <tbody>
            @for (ponto of pontosFiltrados; track ponto.id) {
              <tr style="border-bottom: 1px solid #f5f5f5;">
                <td style="padding: 14px; font-weight: bold; color: #666;">#{{ ponto.id }}</td>
                
                @if (usuarioLogado?.cargo === 'ADMIN') {
                  <td style="padding: 14px; color: #999; font-family: monospace;">{{ ponto.utilizador?.id || ponto.utilizadorId }}</td>
                  <td style="padding: 14px; font-weight: 500; color: #111;">{{ ponto.utilizador?.nome || 'Utilizador Desconhecido' }}</td>
                  <td style="padding: 14px;">
                    <span style="background-color: #e8f0fe; color: #1a73e8; padding: 3px 6px; border-radius: 4px; font-size: 11px; font-weight: bold;">
                      {{ ponto.utilizador?.cargo?.nome || ponto.utilizador?.cargo || 'N/A' }}
                    </span>
                  </td>
                }

                <td style="padding: 14px;">
                  @if ((ponto.tipo || ponto.type || '') === 'ENTRADA') {
                    <span style="background-color: #e6f4ea; color: #137333; padding: 4px 8px; border-radius: 4px; font-size: 11px; font-weight: bold;">
                      ENTRADA
                    </span>
                  } @else {
                    <span style="background-color: #fce8e6; color: #c5221f; padding: 4px 8px; border-radius: 4px; font-size: 11px; font-weight: bold;">
                      SAÍDA
                    </span>
                  }
                </td>

                <td style="padding: 14px; text-align: center;">
                  @if (ponto.origem === 'SITE' || ponto.viaManual === true || ponto.origem === 'MANUAL_ONLINE') {
                    <span style="background-color: #e1f5fe; color: #0288d1; padding: 4px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; border: 1px dashed #0288d1; white-space: nowrap;">
                      💻 SITE (Online)
                    </span>
                  } @else {
                    <span style="background-color: #ede7f6; color: #5e35b1; padding: 4px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; white-space: nowrap;">
                      🤖 DISPOSITIVO
                    </span>
                  }
                </td>

                <td style="padding: 14px; font-weight: bold; color: #444; font-family: monospace;">
                  {{ (ponto.dataHora || ponto.dataHoraRegisto) | date: 'dd/MM/yyyy - HH:mm:ss' }}
                </td>
              </tr>
            } @empty {
              <tr>
                <td [attr.colspan]="usuarioLogado?.cargo === 'ADMIN' ? 6 : 4" style="padding: 35px; text-align: center; color: #888; font-style: italic;">
                  Nenhum registo de assiduidade encontrado para esta consulta.
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>

    </div>
  `,
  styles: []
})
export class ConsultarAssiduidadeComponent implements OnInit, OnChanges {
  private http = inject(HttpClient);
  private urlPontos = 'http://localhost:8080/api/assiduidade';

  @Input() usuarioLogado: any = null;

  todosOsPontos: any[] = [];
  pontosFiltrados: any[] = [];
  textoDaBusca: string = '';

  // Signals para gerir o formulário de contingência online
  protected identificadorForm = signal('');
  protected tipoForm = signal('ENTRADA');

  ngOnInit() {
    this.carregarTodosOsPontos();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['usuarioLogado']) {
      this.aplicarFiltro();
    }
  }

  carregarTodosOsPontos() {
    this.http.get<any[]>(this.urlPontos).subscribe({
      next: (dados) => {
        console.log('Dados recebidos com sucesso da rota assiduidade:', dados);
        this.todosOsPontos = dados || [];
        this.aplicarFiltro();
      },
      error: (erro) => {
        console.error('Erro crítico ao conectar à rota /api/assiduidade:', erro);
      }
    });
  }

  // Envia a batida de ponto manual/contingência para o Spring Boot
  baterPontoOnline() {
    const identificador = this.identificadorForm().trim();
    if (!identificador) {
      alert('Por favor, introduza o ID ou o Username do utilizador!');
      return;
    }

    const payloadPonto = {
      identificador: identificador,
      tipo: this.tipoForm(),
      origem: 'SITE', // Passa como string explicitando que foi via Site
      dataHora: new Date().toISOString()
    };

    // Ajusta o sub-path para o teu endpoint correto do Spring Boot (ex: /bater-online ou direto na raiz se for tratado lá)
    this.http.post(`${this.urlPontos}/bater-online`, payloadPonto).subscribe({
      next: () => {
        alert('Ponto de Contingência registado com sucesso via Site! 💻');
        this.identificadorForm.set(''); // Limpa o campo de texto
        this.carregarTodosOsPontos();   // Recarrega e re-filtra a tabela automaticamente
      },
      error: (erro) => {
        alert('Erro ao registar ponto online. Verifique o identificador ou se o endpoint mapeado aceita os campos.');
        console.error(erro);
      }
    });
  }

  filtrarPorTexto(valor: string) {
    this.textoDaBusca = valor;
    this.aplicarFiltro();
  }

  aplicarFiltro() {
    const user = this.usuarioLogado;
    if (!user) {
      this.pontosFiltrados = [];
      return;
    }

    if (user.cargo === 'ESTUDANTE') {
      this.pontosFiltrados = this.todosOsPontos.filter((ponto: any) => {
        const idDoUserNoPonto = ponto.utilizador?.id || ponto.utilizadorId || ponto.idUtilizador;
        return String(idDoUserNoPonto) === String(user.id);
      });
      return;
    }

    const busca = this.textoDaBusca.toLowerCase().trim();
    if (!busca) {
      this.pontosFiltrados = this.todosOsPontos;
      return;
    }

    this.pontosFiltrados = this.todosOsPontos.filter((ponto: any) => {
      const nome = (ponto.utilizador?.nome || '').toLowerCase();
      const tipo = (ponto.tipo || ponto.type || '').toLowerCase();
      return nome.includes(busca) || tipo.includes(busca);
    });
  }
}

