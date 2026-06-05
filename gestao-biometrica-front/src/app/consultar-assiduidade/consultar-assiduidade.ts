import { Component, inject, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-consultar-assiduidade',
  imports: [CommonModule],
  template: `
    <div style="background-color: #fff3cd; color: #856404; padding: 15px; border-radius: 6px; margin-bottom: 25px; font-size: 13px; border: 1px solid #ffeeba; font-family: sans-serif;">
      <h4 style="margin: 0 0 8px 0; color: #533f03;">🛠️ Diagnóstico de Comunicação Frontend ➔ Backend</h4>
      <div style="display: flex; flex-wrap: wrap; gap: 20px;">
        <div>👤 Utilizador Ativo: <b>{{ usuarioLogado?.nome || 'Nenhum' }}</b> (Cargo: <b>{{ usuarioLogado?.cargo || 'Nenhum' }}</b>)</div>
        <div>🌐 Recebidos do Spring Boot: <b style="color: #2e7d32; font-size: 14px;">{{ todosOsPontos.length }} registos</b></div>
        <div>📊 Exibidos na Tabela: <b>{{ pontosFiltrados.length }} registos</b></div>
      </div>
      
      @if (erroDeConexao) {
        <div style="margin-top: 10px; padding: 10px; background-color: #fce8e6; color: #c5221f; border-radius: 4px; font-weight: bold; border: 1px solid #f8b7b5;">
          ❌ FALHA NA REQUISIÇÃO: {{ erroDeConexao }}
        </div>
      }
    </div>

    <div style="background: white; border: 1px solid #eee; border-radius: 8px; padding: 25px; box-shadow: 0 2px 10px rgba(0,0,0,0.03); font-family: sans-serif;">
      
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; flex-wrap: wrap; gap: 15px;">
        <div>
          <h3 style="margin: 0; color: #333;">
            {{ usuarioLogado?.cargo === 'ADMIN' ? '📋 Relatório Geral de Assiduidade (UAN)' : '📖 O Meu Histórico de Presenças' }}
          </h3>
          <p style="margin: 5px 0 0 0; color: #777; font-size: 13px;">
            {{ usuarioLogado?.cargo === 'ADMIN' ? 'Consulta e filtragem de acessos biométricos de todos os utilizadores.' : 'Lista de todas as tuas entradas e saídas registadas no sistema.' }}
          </p>
        </div>

        @if (usuarioLogado?.cargo === 'ADMIN') {
          <div style="display: flex; gap: 10px; align-items: center;">
            <input (input)="filtrarPorTexto($any($event.target).value)"
                   type="text" placeholder="🔍 Pesquise por nome..." 
                   style="padding: 10px 14px; border: 1px solid #ccc; border-radius: 6px; width: 300px; font-size: 13px; outline: none;">
            
            <button (click)="carregarTodosOsPontos()" 
                    style="padding: 10px 14px; background-color: #757575; color: white; border: none; border-radius: 6px; font-size: 13px; cursor: pointer; font-weight: bold;">
              🔄 Atualizar
            </button>
          </div>
        } @else {
          <button (click)="carregarTodosOsPontos()" 
                  style="padding: 10px 14px; background-color: #2e7d32; color: white; border: none; border-radius: 6px; font-size: 13px; cursor: pointer; font-weight: bold;">
            🔄 Atualizar as minhas presenças
          </button>
        }
      </div>

      <div style="width: 100%; overflow-x: auto; border: 1px solid #eee; border-radius: 6px;">
        <table style="width: 100%; min-width: 850px; border-collapse: collapse; text-align: left; font-size: 13px;">
          <thead>
            <tr style="background-color: #f8f9fa; border-bottom: 2px solid #eee;">
              <th style="padding: 14px; width: 80px;">Nº Registo</th>
              @if (usuarioLogado?.cargo === 'ADMIN') {
                <th style="padding: 14px; width: 80px;">ID User</th>
                <th style="padding: 14px;">Nome do Utilizador</th>
                <th style="padding: 14px; width: 120px;">Cargo</th>
              }
              <th style="padding: 14px; width: 140px;">Tipo de Movimento</th>
              <th style="padding: 14px; width: 220px;">Data & Hora do Acesso</th>
            </tr>
          </thead>
          <tbody>
            @for (ponto of pontosFiltrados; track ponto.id) {
              <tr style="border-bottom: 1px solid #f5f5f5;">
                <td style="padding: 14px; font-weight: bold; color: #666;">#{{ ponto.id }}</td>
                
                @if (usuarioLogado?.cargo === 'ADMIN') {
                  <td style="padding: 14px; color: #999; font-family: monospace;">{{ ponto.utilizador?.id }}</td>
                  <td style="padding: 14px; font-weight: 500; color: #111;">{{ ponto.utilizador?.nome || 'Utilizador Desconhecido' }}</td>
                  <td style="padding: 14px;">
                    <span style="background-color: #e8f0fe; color: #1a73e8; padding: 3px 6px; border-radius: 4px; font-size: 11px; font-weight: bold;">
                      {{ ponto.utilizador?.cargo || 'N/A' }}
                    </span>
                  </td>
                }

                <td style="padding: 14px;">
                  @if (ponto.tipoMovimento === 'ENTRADA') {
                    <span style="background-color: #e6f4ea; color: #137333; padding: 4px 8px; border-radius: 4px; font-size: 11px; font-weight: bold;">
                      🟢 ENTRADA
                    </span>
                  } @else {
                    <span style="background-color: #fce8e6; color: #c5221f; padding: 4px 8px; border-radius: 4px; font-size: 11px; font-weight: bold;">
                      🔴 SAÍDA
                    </span>
                  }
                </td>
                <td style="padding: 14px; font-weight: bold; color: #444; font-family: monospace;">
                  {{ ponto.dataHora | date: 'dd/MM/yyyy - HH:mm:ss' }}
                </td>
              </tr>
            } @empty {
              <tr>
                <td [attr.colspan]="usuarioLogado?.cargo === 'ADMIN' ? 5 : 3" style="padding: 35px; text-align: center; color: #888; font-style: italic;">
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
  erroDeConexao: string = ''; // Armazena a mensagem de erro caso falte rede/CORS

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
        console.log('Dados recebidos no Angular:', dados);
        this.todosOsPontos = dados || [];
        this.erroDeConexao = ''; // Limpa o erro se funcionou
        this.aplicarFiltro();
      },
      error: (erro) => {
        console.error('Erro ao conectar à API:', erro);
        // Captura o erro detalhado para sabermos o motivo
        this.erroDeConexao = `${erro.message} | Status: ${erro.status} (${erro.statusText})`;
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

    // 1. SE FOR ESTUDANTE: Filtra rigorosamente pelo ID do utilizador logado
    if (user.cargo === 'ESTUDANTE') {
      this.pontosFiltrados = this.todosOsPontos.filter(ponto => {
        return String(ponto.utilizador?.id) === String(user.id);
      });
      return;
    }

    // 2. SE FOR ADMIN: Mostra tudo ou filtra pelo nome pesquisado
    const busca = this.textoDaBusca.toLowerCase().trim();
    if (!busca) {
      this.pontosFiltrados = this.todosOsPontos;
      return;
    }

    this.pontosFiltrados = this.todosOsPontos.filter(ponto => {
      const nome = (ponto.utilizador?.nome || '').toLowerCase();
      return nome.includes(busca);
    });
  }
}
