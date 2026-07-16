import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BiometriaService } from '../../services/biometria.service';

@Component({
  selector: 'app-terminal-ponto',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './terminal-ponto.component.html',
  styleUrls: ['./terminal-ponto.component.css']
})
export class TerminalPontoComponent {
  private biometriaService = inject(BiometriaService);
  private cdr = inject(ChangeDetectorRef); // Injeção para forçar a atualização da tela

  vectorHexa: string = '';
  mensagem: string = 'Toque no sensor ou insira o código';
  resultado: any = null;

  verificarPonto() {
  if (!this.vectorHexa.trim()) return; // Previne cliques com input vazio

  this.mensagem = 'A validar...';
  this.resultado = null;

  this.biometriaService.identificarDigital(this.vectorHexa).subscribe({
    next: (data: any) => {
      this.resultado = { ...data };
      this.mensagem = data.mensagem;
      
      // AQUI É ONDE A MÁGICA ACONTECE:
      this.vectorHexa = ''; 
      
      this.cdr.detectChanges();
    },
    error: (err) => {
      this.resultado = null;
      this.mensagem = 'Digital não reconhecida.';
      
      // Opcional: limpa mesmo no erro para permitir nova tentativa rápida
      this.vectorHexa = ''; 
      
      this.cdr.detectChanges();
    }
  });
}
}

