import { Component, signal, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [CommonModule],
  template: `
    <div style="display: flex; justify-content: center; align-items: center; min-height: 100vh; background-color: #f0f2f5; font-family: sans-serif; padding: 20px; box-sizing: border-box;">
      
      <div style="width: 100%; max-width: 400px; background: white; padding: 35px 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); border-top: 5px solid #0d47a1;">
        
        <div style="text-align: center; margin-bottom: 25px;">
          <h2 style="margin: 0; color: #0d47a1; font-size: 22px;">SGB - Login</h2>
          <p style="margin: 5px 0 0 0; color: #666; font-size: 14px;">Universidade Agostinho Neto</p>
        </div>

        <hr style="border: 0; border-top: 1px solid #eee; margin-bottom: 25px;">

        <div style="display: flex; flex-direction: column; gap: 18px;">
          
          <div>
            <label style="display: block; margin-bottom: 6px; font-weight: bold; font-size: 13px; color: #333;">
              Nome de Utilizador (Username):
            </label>
            <input #userInput type="text" placeholder="Digite o seu username" 
                   style="width: 100%; padding: 11px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 14px;">
          </div>

          <div>
            <label style="display: block; margin-bottom: 6px; font-weight: bold; font-size: 13px; color: #333;">
              Palavra-passe:
            </label>
            <input #passwordInput type="password" placeholder="••••••••" 
                   style="width: 100%; padding: 11px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 14px;">
          </div>

          @if (mensagemErro()) {
            <div style="background-color: #fce8e6; color: #c5221f; padding: 10px; border-radius: 4px; font-size: 13px; font-weight: bold; text-align: center;">
              ⚠️ {{ mensagemErro() }}
            </div>
          }

          <button (click)="autenticar(userInput.value, passwordInput.value)" 
                  style="width: 100%; padding: 12px; background-color: #0d47a1; color: white; border: none; border-radius: 4px; font-size: 15px; font-weight: bold; cursor: pointer; margin-top: 5px; transition: background 0.2s;">
            Entrar no Sistema
          </button>

        </div>

        <p style="text-align: center; color: #888; font-size: 11px; margin-top: 30px; margin-bottom: 0;">
          Área restrita. Acessos não autorizados serão auditados.
        </p>

      </div>
    </div>
  `,
  styles: []
})
export class LoginComponent {
  // Emite o objeto completo do utilizador (id, nome, cargo, estado) para o App Component
  loginSucesso = output<any>();
  
  protected mensagemErro = signal('');

  async autenticar(user: string, pass: string) {
    this.mensagemErro.set(''); 

    if (!user.trim() || !pass.trim()) {
      this.mensagemErro.set('Por favor, preencha todos os campos.');
      return;
    }

    try {
      const resposta = await fetch('http://localhost:8080/api/utilizadores/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: user.trim(), palavraPasse: pass })
      });

      if (resposta.ok) {
        const dadosUtilizador = await resposta.json();
        this.loginSucesso.emit(dadosUtilizador); 
      } else {
        const textoErro = await resposta.text();
        this.mensagemErro.set(textoErro || 'Username ou palavra-passe incorretos.');
      }
    } catch (erro) {
      this.mensagemErro.set('Erro ao ligar ao servidor. Garanta que o Backend está ativo.');
    }
  }
}
