import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Importante para formulários

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule], // Adiciona aqui
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  usuario = '';
  senha = '';

  entrar() {
    console.log('Dados do usuário:', this.usuario, this.senha);
    // Aqui vamos chamar o nosso BiometriaService em breve!
  }
}

