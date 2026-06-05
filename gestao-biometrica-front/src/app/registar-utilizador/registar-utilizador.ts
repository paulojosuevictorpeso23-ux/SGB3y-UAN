import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registar-utilizador',
  imports: [CommonModule],
  template: `
    <div style="max-width: 1200px; margin: 30px auto; font-family: sans-serif; padding: 0 20px;">
      
      <div style="text-align: center; margin-bottom: 30px;">
        <h2>SGB - Gestão Global de Utilizadores (UAN)</h2>
        <p style="color: #666;">Painel de Auditoria e CRUD com Segurança de Credenciais</p>
      </div>

      <div [style.border-top]="idEmEdicao() ? '5px solid #f57c00' : '5px solid #2e7d32'"
           style="background-color: #f9f9f9; padding: 25px; border-radius: 8px; border: 1px solid #eee; margin-bottom: 40px; box-shadow: 0 2px 4px rgba(0,0,0,0.05);">
        
        <h3 style="margin-top: 0; color: #333;">
          {{ idEmEdicao() ? '📝 Editar Utilizador (ID: ' + idEmEdicao() + ')' : '➕ Registar Novo Utilizador' }}
        </h3>
        
        <div style="display: flex; flex-direction: column; gap: 15px;">
          
          <div style="display: flex; gap: 15px; flex-wrap: wrap;">
            <div style="flex: 1; min-width: 250px;">
              <label style="display: block; margin-bottom: 5px; font-weight: bold; font-size: 13px;">Nome Completo:</label>
              <input [value]="nomeForm()" (input)="nomeForm.set($any($event.target).value)"
                     type="text" placeholder="Ex: Paulo Josué" 
                     style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;">
            </div>

            <div style="flex: 1; min-width: 250px;">
              <label style="display: block; margin-bottom: 5px; font-weight: bold; font-size: 13px;">Username / Nome de Utilizador:</label>
              <input [value]="usernameForm()" (input)="usernameForm.set($any($event.target).value)"
                     type="text" placeholder="Ex: paulo.josue" 
                     style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;">
            </div>
          </div>

          <div style="display: flex; gap: 15px; flex-wrap: wrap;">
            <div style="flex: 1; min-width: 200px;">
              <label style="display: block; margin-bottom: 5px; font-weight: bold; font-size: 13px;">Cargo / Função:</label>
              <select [value]="cargoForm()" (change)="cargoForm.set($any($event.target).value)"
                      style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; background: white; height: 38px;">
                <option value="ESTUDANTE">Estudante</option>
                <option value="DOCENTE">Docente (Professor)</option>
                <option value="TECNICO">Técnico / Funcionário</option>
                <option value="ADMIN">Administrador</option>
              </select>
            </div>

            <div style="flex: 1; min-width: 200px;">
              <label style="display: block; margin-bottom: 5px; font-weight: bold; font-size: 13px;">Estado do Acesso:</label>
              <select [value]="estadoForm()" (change)="estadoForm.set($any($event.target).value)"
                      [disabled]="!idEmEdicao()"
                      style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; background: white; height: 38px;">
                <option value="ATIVO">ATIVO (Acesso Permitido)</option>
                <option value="INATIVO">INATIVO (Bloqueado)</option>
              </select>
            </div>

            <div style="flex: 1; min-width: 200px;">
              <label style="display: block; margin-bottom: 5px; font-weight: bold; font-size: 13px;">Palavra-passe:</label>
              <input [value]="palavraPasseForm()" (input)="palavraPasseForm.set($any($event.target).value)"
                     type="password" placeholder="Senha (Deixe em branco para 123456)" 
                     style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; height: 38px;">
            </div>
          </div>

          <div style="display: flex; gap: 10px; margin-top: 10px;">
            @if (idEmEdicao()) {
              <button (click)="atualizar()" 
                      style="flex: 1; padding: 12px; background-color: #f57c00; color: white; border: none; border-radius: 4px; font-size: 15px; font-weight: bold; cursor: pointer;">
                Gravar Alterações
              </button>
              <button (click)="limparFormulario()" 
                      style="padding: 12px 20px; background-color: #757575; color: white; border: none; border-radius: 4px; font-size: 15px; font-weight: bold; cursor: pointer;">
                Cancelar
              </button>
            } @else {
              <button (click)="cadastrar()" 
                      style="width: 100%; padding: 12px; background-color: #2e7d32; color: white; border: none; border-radius: 4px; font-size: 15px; font-weight: bold; cursor: pointer;">
                Salvar Utilizador no Sistema
              </button>
            }
          </div>
          
        </div>
      </div>

      <hr style="border: 1px solid #ddd; margin: 30px 0;">

      <h3>Registos Auditáveis (PostgreSQL)</h3>
      
      <div style="width: 100%; overflow-x: auto; border: 1px solid #eee; border-radius: 4px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
        <table style="width: 100%; min-width: 1100px; border-collapse: collapse; text-align: left; font-size: 13px;">
          <thead>
            <tr style="background-color: #f4f4f4; border-bottom: 2px solid #ddd;">
              <th style="padding: 12px; width: 50px;">ID</th>
              <th style="padding: 12px;">Nome</th>
              <th style="padding: 12px;">Username</th>
              <th style="padding: 12px; width: 160px;">Palavra-passe</th>
              <th style="padding: 12px;">Cargo</th>
              <th style="padding: 12px;">Estado</th>
              <th style="padding: 12px;">Data Criação</th>
              <th style="padding: 12px;">Última Atualização</th>
              <th style="padding: 12px; text-align: center; width: 200px;">Ações</th>
            </tr>
          </thead>
          <tbody>
            @for (user of listaDeUtilizadores(); track user.id) {
              <tr style="border-bottom: 1px solid #eee;" [style.background-color]="idEmEdicao() === user.id ? '#fff3e0' : 'transparent'">
                <td style="padding: 12px; font-weight: bold; color: #555;">{{ user.id }}</td>
                <td style="padding: 12px; font-weight: 500;">{{ user.nome }}</td>
                <td style="padding: 12px; font-family: monospace; color: #0d47a1;">{{ user.username }}</td>
                
                <td style="padding: 12px; font-family: monospace; color: #666; display: flex; align-items: center; gap: 8px; border-bottom: none;">
                  <span>{{ idSenhaVisivel() === user.id ? user.palavraPasse : '••••••••' }}</span>
                  <button (click)="alternarVisibilidadeSenha(user.id)" 
                          style="background: none; border: none; cursor: pointer; font-size: 12px; padding: 2px;"
                          title="Mostrar/Esconder">
                    {{ idSenhaVisivel() === user.id ? '🙈' : '👁️' }}
                  </button>
                </td>

                <td style="padding: 12px;">
                  <span style="background-color: #e8f0fe; color: #1a73e8; padding: 3px 6px; border-radius: 4px; font-size: 11px; font-weight: bold;">
                    {{ user.cargo }}
                  </span>
                </td>
                <td style="padding: 12px;">
                  <span [style.background-color]="user.estado === 'ATIVO' ? '#e6f4ea' : '#fce8e6'"
                        [style.color]="user.estado === 'ATIVO' ? '#137333' : '#c5221f'"
                        style="padding: 3px 6px; border-radius: 4px; font-size: 11px; font-weight: bold;">
                    {{ user.estado }}
                  </span>
                </td>
                <td style="padding: 12px; color: #555;">
                  {{ user.dataCriacao ? (user.dataCriacao | date: 'dd/MM/yyyy HH:mm') : '---' }}
                </td>
                <td style="padding: 12px; color: #555;">
                  {{ user.dataAtualizacao ? (user.dataAtualizacao | date: 'dd/MM/yyyy HH:mm') : '---' }}
                </td>
                <td style="padding: 12px; text-align: center;">
                  <div style="display: flex; gap: 4px; justify-content: center;">
                    <button (click)="prepararEdicao(user)"
                            style="padding: 5px 8px; background-color: #1976d2; color: white; border: none; border-radius: 4px; font-size: 11px; font-weight: bold; cursor: pointer;">
                      ✏️ Editar
                    </button>
                    
                    <button (click)="resetarSenha(user.id)"
                            style="padding: 5px 8px; background-color: #f57c00; color: white; border: none; border-radius: 4px; font-size: 11px; font-weight: bold; cursor: pointer;"
                            title="Resetar direto para 123456">
                      🔄 Reset
                    </button>

                    <button (click)="eliminar(user.id, user.nome)"
                            style="padding: 5px 8px; background-color: #d32f2f; color: white; border: none; border-radius: 4px; font-size: 11px; font-weight: bold; cursor: pointer;">
                      🗑️ Apagar
                    </button>
                  </div>
                </td>
              </tr>
            } @empty {
              <tr>
                <td colspan="9" style="padding: 25px; text-align: center; color: #888;">
                  Nenhum utilizador registado na base de dados.
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
export class RegistarUtilizadorComponent implements OnInit {
  private http = inject(HttpClient);
  private url = 'http://localhost:8080/api/utilizadores';

  protected listaDeUtilizadores = signal<any[]>([]);

  // Signals do Formulário
  protected nomeForm = signal('');
  protected usernameForm = signal('');
  protected cargoForm = signal('ESTUDANTE');
  protected estadoForm = signal('ATIVO');
  protected palavraPasseForm = signal(''); // Nova track da palavra-passe no input
  
  protected idEmEdicao = signal<number | null>(null);
  protected idSenhaVisivel = signal<number | null>(null); // Controla qual linha exibe a senha pura

  ngOnInit() {
    this.carregarUtilizadores();
  }

  carregarUtilizadores() {
    this.http.get<any[]>(this.url).subscribe({
      next: (dados) => this.listaDeUtilizadores.set(dados),
      error: (erro) => console.error('Erro ao listar utilizadores:', erro)
    });
  }

  // C = CREATE
  cadastrar() {
    if (!this.nomeForm().trim() || !this.usernameForm().trim()) {
      alert('Aviso: Por favor, preencha o Nome e o Username!');
      return;
    }

    const novoUtilizador = {
      nome: this.nomeForm().trim(),
      username: this.usernameForm().trim(),
      cargo: this.cargoForm(),
      palavraPasse: this.palavraPasseForm().trim() || '123456', // Se deixar vazio, assume o padrão do sistema
      estado: 'ATIVO'
    };

    this.http.post(this.url, novoUtilizador).subscribe({
      next: () => {
        alert('Sucesso: Novo utilizador guardado! 🎉');
        this.limparFormulario();
        this.carregarUtilizadores();
      },
      error: (erro) => {
        alert('Erro: Verifique se o Username já existe.');
        console.error(erro);
      }
    });
  }

  // Carrega os dados na caixa de texto, incluindo a palavra-passe para edição
  prepararEdicao(user: any) {
    this.idEmEdicao.set(user.id);
    this.nomeForm.set(user.nome);
    this.usernameForm.set(user.username);
    this.cargoForm.set(user.cargo);
    this.estadoForm.set(user.estado);
    this.palavraPasseForm.set(user.palavraPasse); // Coloca a senha atual no input (estará oculta pela tag type="password")
  }

  // U = UPDATE (Agora atualiza também a palavra-passe editada!)
  atualizar() {
    if (!this.idEmEdicao()) return;

    if (!this.nomeForm().trim() || !this.usernameForm().trim() || !this.palavraPasseForm().trim()) {
      alert('Aviso: O Nome, Username e Palavra-passe não podem ficar vazios!');
      return;
    }

    const utilizadorAtualizado = {
      id: this.idEmEdicao(),
      nome: this.nomeForm().trim(),
      username: this.usernameForm().trim(),
      palavraPasse: this.palavraPasseForm().trim(), // Envia a nova senha digitada
      cargo: this.cargoForm(),
      estado: this.estadoForm()
    };

    this.http.post(this.url, utilizadorAtualizado).subscribe({
      next: () => {
        alert('Sucesso: Utilizador e palavra-passe atualizados! 📝');
        this.limparFormulario();
        this.carregarUtilizadores();
      },
      error: (erro) => {
        alert('Erro ao atualizar dados.');
        console.error(erro);
      }
    });
  }

  // D = DELETE
  eliminar(id: number, nome: string) {
    if (confirm(`⚠️ ATENÇÃO: Tens a certeza que queres eliminar permanentemente "${nome}"?`)) {
      this.http.delete(`${this.url}/${id}`).subscribe({
        next: () => {
          alert('Sucesso: Utilizador removido do sistema! 🗑️');
          if (this.idEmEdicao() === id) this.limparFormulario();
          this.carregarUtilizadores();
        },
        error: (erro) => {
          alert('Erro ao eliminar. Este utilizador pode ter registos de assiduidade associados.');
          console.error(erro);
        }
      });
    }
  }

  resetarSenha(id: number) {
    if (confirm('Tens a certeza que desejas resetar a palavra-passe deste utilizador para "123456"?')) {
      this.http.put(`${this.url}/${id}/reset-senha`, {}).subscribe({
        next: () => {
          alert('Sucesso: Palavra-passe redefinida para "123456"! 🔒');
          this.carregarUtilizadores();
        },
        error: (erro) => console.error('Erro ao fazer reset da senha:', erro)
      });
    }
  }

  alternarVisibilidadeSenha(id: number) {
    if (this.idSenhaVisivel() === id) {
      this.idSenhaVisivel.set(null); // Esconde
    } else {
      this.idSenhaVisivel.set(id); // Revela apenas esta linha
    }
  }

  limparFormulario() {
    this.idEmEdicao.set(null);
    this.nomeForm.set('');
    this.usernameForm.set('');
    this.cargoForm.set('ESTUDANTE');
    this.estadoForm.set('ATIVO');
    this.palavraPasseForm.set('');
  }
}
