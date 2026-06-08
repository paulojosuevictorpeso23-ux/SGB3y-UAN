import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registar-utilizador',
  imports: [CommonModule],
  template: `
    <div style="width: 100%; max-width: 1200px; margin: 20px auto; font-family: sans-serif; padding: 0 15px; box-sizing: border-box;">
      
      <div style="text-align: center; margin-bottom: 25px;">
        <h2 style="font-size: 1.5rem; margin-bottom: 5px;">SGB - Gestão Global de Utilizadores (UAN)</h2>
        <p style="color: #666; font-size: 0.9rem; margin-top: 0;">Painel de Auditoria e CRUD com Segurança de Credenciais</p>
      </div>

      <div [style.border-top]="idEmEdicao() ? '5px solid #f57c00' : '5px solid #2e7d32'"
           style="background-color: #f9f9f9; padding: 20px; border-radius: 8px; border: 1px solid #eee; margin-bottom: 30px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); box-sizing: border-box;">
        
        <h3 style="margin-top: 0; color: #333; font-size: 1.2rem; margin-bottom: 20px;">
          {{ idEmEdicao() ? '📝 Editar Utilizador (ID: ' + idEmEdicao() + ')' : '➕ Registar Novo Utilizador' }}
        </h3>
        
        <div style="display: flex; flex-direction: column; gap: 15px;">
          
          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 15px;">
            <div style="display: flex; flex-direction: column;">
              <label style="margin-bottom: 5px; font-weight: bold; font-size: 13px;">Nome Completo:</label>
              <input [value]="nomeForm()" (input)="nomeForm.set($any($event.target).value)"
                     type="text" placeholder="Ex: Paulo Josué" 
                     style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 14px;">
            </div>

            <div style="display: flex; flex-direction: column;">
              <label style="margin-bottom: 5px; font-weight: bold; font-size: 13px;">Username / Nome de Utilizador:</label>
              <input [value]="usernameForm()" (input)="usernameForm.set($any($event.target).value)"
                     type="text" placeholder="Ex: paulo.josue" 
                     style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 14px;">
            </div>
          </div>

          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 15px;">
            <div style="display: flex; flex-direction: column;">
              <label style="margin-bottom: 5px; font-weight: bold; font-size: 13px;">Email Institucional / Pessoal:</label>
              <input [value]="emailForm()" (input)="emailForm.set($any($event.target).value)"
                     type="email" placeholder="Ex: estudante@uan.ao" 
                     style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 14px;">
            </div>

            <div style="display: flex; flex-direction: column;">
              <label style="margin-bottom: 5px; font-weight: bold; font-size: 13px;">Contacto Telefónico (Opcional):</label>
              <input [value]="contactoForm()" (input)="contactoForm.set($any($event.target).value)"
                     type="text" placeholder="Ex: +2449XXXXXXXX" 
                     style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 14px;">
            </div>
          </div>

          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px;">
            <div style="display: flex; flex-direction: column;">
              <label style="margin-bottom: 5px; font-weight: bold; font-size: 13px;">Cargo / Função:</label>
              <select [value]="cargoForm()" (change)="cargoForm.set($any($event.target).value)"
                      style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; background: white; height: 40px; font-size: 14px; box-sizing: border-box;">
                <option value="ESTUDANTE">Estudante</option>
                <option value="DOCENTE">Docente (Professor)</option>
                <option value="TECNICO">Técnico / Funcionário</option>
                <option value="ADMIN">Administrador</option>
              </select>
            </div>

            <div style="display: flex; flex-direction: column;">
              <label style="margin-bottom: 5px; font-weight: bold; font-size: 13px;">Estado do Acesso:</label>
              <select [value]="estadoForm()" (change)="estadoForm.set($any($event.target).value)"
                      [disabled]="!idEmEdicao()"
                      style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; background: white; height: 40px; font-size: 14px; box-sizing: border-box;">
                <option value="ATIVO">ATIVO (Acesso Permitido)</option>
                <option value="INATIVO">INATIVO (Bloqueado)</option>
              </select>
            </div>

            <div style="display: flex; flex-direction: column;">
              <label style="margin-bottom: 5px; font-weight: bold; font-size: 13px;">Palavra-passe:</label>
              <input [value]="palavraPasseForm()" (input)="palavraPasseForm.set($any($event.target).value)"
                     type="password" placeholder="{{ idEmEdicao() ? 'Manter atual' : 'Padrão: 123456' }}" 
                     style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; height: 40px; font-size: 14px;">
            </div>
          </div>

          <div style="display: flex; gap: 10px; flex-wrap: wrap; margin-top: 10px;">
            @if (idEmEdicao()) {
              <button (click)="atualizar()" 
                      style="flex: 1; min-width: 150px; padding: 12px; background-color: #f57c00; color: white; border: none; border-radius: 4px; font-size: 14px; font-weight: bold; cursor: pointer;">
                Gravar Alterações
              </button>
              <button (click)="limparFormulario()" 
                      style="padding: 12px 20px; background-color: #757575; color: white; border: none; border-radius: 4px; font-size: 14px; font-weight: bold; cursor: pointer; flex-grow: 0;">
                Cancelar
              </button>
            } @else {
              <button (click)="cadastrar()" 
                      style="width: 100%; padding: 12px; background-color: #2e7d32; color: white; border: none; border-radius: 4px; font-size: 14px; font-weight: bold; cursor: pointer;">
                Salvar Utilizador no Sistema
              </button>
            }
          </div>
          
        </div>
      </div>

      <hr style="border: 1px solid #ddd; margin: 25px 0;">

      <h3 style="font-size: 1.2rem; margin-bottom: 15px;">Registos Auditáveis (PostgreSQL)</h3>
      
      <div style="width: 100%; overflow-x: auto; border: 1px solid #eee; border-radius: 4px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); background: white;">
        <table style="width: 100%; min-width: 1050px; border-collapse: collapse; text-align: left; font-size: 13px;">
          <thead>
            <tr style="background-color: #f4f4f4; border-bottom: 2px solid #ddd;">
              <th style="padding: 12px; width: 50px;">ID</th>
              <th style="padding: 12px;">Nome</th>
              <th style="padding: 12px;">Username</th>
              <th style="padding: 12px;">Email</th>
              <th style="padding: 12px;">Cargo</th>
              <th style="padding: 12px;">Estado</th>
              <th style="padding: 12px;">Data Criação</th>
              <th style="padding: 12px;">Data Atualização</th>
              <th style="padding: 12px; text-align: center; width: 180px;">Ações</th>
            </tr>
          </thead>
          <tbody>
            @for (user of listaDeUtilizadores(); track user.id) {
              <tr style="border-bottom: 1px solid #eee;" [style.background-color]="idEmEdicao() === user.id ? '#fff3e0' : 'transparent'">
                <td style="padding: 12px; font-weight: bold; color: #555;">{{ user.id }}</td>
                <td style="padding: 12px; font-weight: 500;">{{ user.nome }}</td>
                <td style="padding: 12px; font-family: monospace; color: #0d47a1;">{{ user.username }}</td>
                <td style="padding: 12px; color: #444;">{{ user.email }}</td>
                
                <td style="padding: 12px;">
                  <span style="background-color: #e8f0fe; color: #1a73e8; padding: 3px 6px; border-radius: 4px; font-size: 11px; font-weight: bold; white-space: nowrap;">
                    {{ user.cargo?.nome || user.cargo }}
                  </span>
                </td>
                <td style="padding: 12px;">
                  <span [style.background-color]="user.estado === 'ATIVO' ? '#e6f4ea' : '#fce8e6'"
                        [style.color]="user.estado === 'ATIVO' ? '#137333' : '#c5221f'"
                        style="padding: 3px 6px; border-radius: 4px; font-size: 11px; font-weight: bold; white-space: nowrap;">
                    {{ user.estado }}
                  </span>
                </td>
                <td style="padding: 12px; color: #555; white-space: nowrap;">
                  {{ user.dataCriacao ? (user.dataCriacao | date: 'dd/MM/yyyy HH:mm') : '---' }}
                </td>
                <td style="padding: 12px; color: #555; white-space: nowrap;">
                  {{ user.dataAtualizacao ? (user.dataAtualizacao | date: 'dd/MM/yyyy HH:mm') : '---' }}
                </td>
                <td style="padding: 12px; text-align: center;">
                  <div style="display: flex; gap: 4px; justify-content: center;">
                    <button (click)="prepararEdicao(user)"
                            style="padding: 5px 8px; background-color: #1976d2; color: white; border: none; border-radius: 4px; font-size: 11px; font-weight: bold; cursor: pointer; white-space: nowrap;">
                      ✏️ Editar
                    </button>
                    
                    <button (click)="resetarSenha(user.id)"
                            style="padding: 5px 8px; background-color: #f57c00; color: white; border: none; border-radius: 4px; font-size: 11px; font-weight: bold; cursor: pointer; white-space: nowrap;"
                            title="Resetar direto para 123456">
                      🔄 Reset
                    </button>

                    <button (click)="eliminar(user.id, user.nome)"
                            style="padding: 5px 8px; background-color: #d32f2f; color: white; border: none; border-radius: 4px; font-size: 11px; font-weight: bold; cursor: pointer; white-space: nowrap;">
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
  protected emailForm = signal('');
  protected contactoForm = signal('');
  protected cargoForm = signal('ESTUDANTE');
  protected estadoForm = signal('ATIVO');
  protected palavraPasseForm = signal('');
  
  protected idEmEdicao = signal<number | null>(null);

  ngOnInit() {
    this.carregarUtilizadores();
  }

  carregarUtilizadores() {
    this.http.get<any[]>(this.url).subscribe({
      next: (dados) => this.listaDeUtilizadores.set(dados),
      error: (erro: any) => console.error('Erro ao listar utilizadores:', erro)
    });
  }

  private obterObjetoCargo(nomeCargo: string) {
    switch (nomeCargo) {
      case 'ADMIN': return { id: 1, nome: 'ADMIN' };
      case 'ESTUDANTE': return { id: 2, nome: 'ESTUDANTE' };
      case 'DOCENTE': return { id: 3, nome: 'DOCENTE' };
      case 'TECNICO': return { id: 4, nome: 'TECNICO' };
      default: return { id: 2, nome: 'ESTUDANTE' };
    }
  }

  cadastrar() {
    if (!this.nomeForm().trim() || !this.usernameForm().trim() || !this.emailForm().trim()) {
      alert('Aviso: Por favor, preencha o Nome, Username e Email!');
      return;
    }

    const novoUtilizador = {
      nome: this.nomeForm().trim(),
      username: this.usernameForm().trim(),
      email: this.emailForm().trim(),
      contacto: this.contactoForm().trim() || null,
      cargo: this.obterObjetoCargo(this.cargoForm()),
      horario: { id: 1 }, 
      palavraPasse: this.palavraPasseForm().trim() || '123456',
      estado: 'ATIVO'
    };

    this.http.post(this.url, novoUtilizador).subscribe({
      next: () => {
        alert('Sucesso: Novo utilizador guardado! 🎉');
        this.limparFormulario();
        this.carregarUtilizadores();
      },
      error: (erro: any) => {
        alert('Erro: Verifique se o Username/Email já existe ou se faltam dados no backend.');
        console.error(erro);
      }
    });
  }

  prepararEdicao(user: any) {
    this.idEmEdicao.set(user.id);
    this.nomeForm.set(user.nome);
    this.usernameForm.set(user.username);
    this.emailForm.set(user.email || '');
    this.contactoForm.set(user.contacto || '');
    
    const nomeCargo = user.cargo?.nome ? user.cargo.nome : user.cargo;
    this.cargoForm.set(nomeCargo);
    
    this.estadoForm.set(user.estado);
    this.palavraPasseForm.set('');
  }

  atualizar() {
    if (!this.idEmEdicao()) return;

    if (!this.nomeForm().trim() || !this.usernameForm().trim() || !this.emailForm().trim()) {
      alert('Aviso: O Nome, Username e Email não podem ficar vazios!');
      return;
    }

    const utilizadorAtualizado: any = {
      id: this.idEmEdicao(),
      nome: this.nomeForm().trim(),
      username: this.usernameForm().trim(),
      email: this.emailForm().trim(),
      contacto: this.contactoForm().trim() || null,
      cargo: this.obterObjetoCargo(this.cargoForm()),
      horario: { id: 1 },
      estado: this.estadoForm()
    };

    if (this.palavraPasseForm().trim()) {
      utilizadorAtualizado.palavraPasse = this.palavraPasseForm().trim();
    }

    this.http.post(this.url, utilizadorAtualizado).subscribe({
      next: () => {
        alert('Sucesso: Utilizador atualizado! 📝');
        this.limparFormulario();
        this.carregarUtilizadores();
      },
      error: (erro: any) => {
        alert('Erro ao atualizar dados.');
        console.error(erro);
      }
    });
  }

  eliminar(id: number, nome: string) {
    if (confirm(`⚠️ ATENÇÃO: Tens a certeza que queres eliminar permanentemente "${nome}"?`)) {
      this.http.delete(`${this.url}/${id}`).subscribe({
        next: () => {
          alert('Sucesso: Utilizador removido do sistema! 🗑️');
          if (this.idEmEdicao() === id) this.limparFormulario();
          this.carregarUtilizadores();
        },
        error: (erro: any) => {
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
        error: (erro: any) => console.error('Erro ao fazer reset da senha:', erro)
      });
    }
  }

  limparFormulario() {
    this.idEmEdicao.set(null);
    this.nomeForm.set('');
    this.usernameForm.set('');
    this.emailForm.set('');
    this.contactoForm.set('');
    this.cargoForm.set('ESTUDANTE');
    this.estadoForm.set('ATIVO');
    this.palavraPasseForm.set('');
  }
}

