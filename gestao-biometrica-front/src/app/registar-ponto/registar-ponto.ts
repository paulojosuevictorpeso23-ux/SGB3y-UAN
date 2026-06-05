import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common'; // Importante para formatação de datas

@Component({
  selector: 'app-registar-ponto',
  imports: [CommonModule],
  template: `
    <div style="text-align: center; margin-top: 30px; font-family: sans-serif;">
      <h2>Bem-vindo ao Sistema de Gestão Biométrica</h2>
      
      <div style="margin-bottom: 20px;">
        <label for="idUtilizador" style="display: block; margin-bottom: 8px; font-weight: bold;">
          Introduza o ID do Utilizador / Nº de Estudante:
        </label>
        <input #numEstudante type="text" id="idUtilizador" placeholder="Ex: 1" 
               style="padding: 10px; width: 250px; font-size: 14px; border: 1px solid #ccc; border-radius: 4px;">
      </div>

      <button (click)="baterPonto(numEstudante.value); numEstudante.value=''" 
              style="padding: 12px 24px; font-size: 16px; background-color: #0056b3; color: white; border: none; border-radius: 4px; cursor: pointer; margin-bottom: 30px;">
        Registar Ponto no Servidor
      </button>
<hr style="border: 1px solid #ddd; width: 80%; margin: 20px auto;">

      <h3>Histórico de Assiduidade (Tempo Real)</h3>
      
      <div style="width: 90%; margin: 0 auto; overflow-x: auto; -webkit-overflow-scrolling: touch;">
        
        <table style="width: 100%; min-width: 600px; border-collapse: collapse; text-align: left; font-size: 14px;">
          <thead>
            <tr style="background-color: #f4f4f4; border-bottom: 2px solid #ddd;">
              <th style="padding: 10px;">ID Registo</th>
              <th style="padding: 10px;">ID Utilizador</th>
              <th style="padding: 10px;">Data / Hora</th>
              <th style="padding: 10px;">Movimento</th>
              <th style="padding: 10px;">Origem</th>
            </tr>
          </thead>
          <tbody>
            @for (ponto of listaDePontos(); track ponto.id) {
              <tr style="border-bottom: 1px solid #eee;">
                <td style="padding: 10px;">{{ ponto.id }}</td>
                <td style="padding: 10px;">{{ ponto.utilizador?.id }}</td>
                <td style="padding: 10px;">{{ ponto.dataHora | date:'dd/MM/yyyy HH:mm:ss' }}</td>
                <td style="padding: 10px;">
                  <span style="background-color: #e6f4ea; color: #137333; padding: 4px 8px; border-radius: 4px; font-weight: bold;">
                    {{ ponto.tipoMovimento }}
                  </span>
                </td>
                <td style="padding: 10px; color: #666;">{{ ponto.dispositivoOrigem }}</td>
              </tr>
            } @empty {
              <tr>
                <td colspan="5" style="padding: 20px; text-align: center; color: #888;">
                  Nenhum registo de ponto encontrado no servidor.
                </td>
              </tr>
            }
          </tbody>
        </table>

      </div>
  `,
  styles: []
})
export class RegistarPontoComponent implements OnInit {
  private http = inject(HttpClient);
  private url = 'http://localhost:8080/api/assiduidade';

  // Usamos um Signal moderno para guardar a lista vinda do Spring Boot
  protected listaDePontos = signal<any[]>([]);

  // O Angular executa isto automaticamente assim que a página abre
  ngOnInit() {
    this.carregarPontos();
  }

  // Função que faz o GET para o teu Spring Boot buscar todos os pontos
  carregarPontos() {
    this.http.get<any[]>(this.url).subscribe({
      next: (dados) => {
        this.listaDePontos.set(dados);
      },
      error: (erro) => {
        console.error('Erro ao listar pontos:', erro);
      }
    });
  }

  baterPonto(id: string) {
    if (!id.trim()) {
      alert('Aviso: Por favor, insira um ID válido!');
      return;
    }

    const dadosDoPonto = {
      utilizador: { id: Number(id) },
      tipoMovimento: 'ENTRADA',
      dispositivoOrigem: 'WEB_ANGULAR'
    };

    this.http.post(this.url, dadosDoPonto).subscribe({
      next: () => {
        alert('Sucesso: Ponto registado na base de dados! 🎉');
        this.carregarPontos(); // <-- TRUQUE DE MESTRE: Atualiza a tabela na hora sem recarregar a página!
      },
      error: (erro) => {
        alert('Erro: ID não existe ou falha no servidor.');
        console.error(erro);
      }
    });
  }
}
