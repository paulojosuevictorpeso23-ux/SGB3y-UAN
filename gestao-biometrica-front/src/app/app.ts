import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  template: `
    <h1 style="color: #0000; font-family: sans-serif;">
      Sistema de Gestão Biométrica - UAN
    </h1>
    <p style="font-family: sans-serif;">O nosso Frontend Angular já está a funcionar!</p>

    <router-outlet />
  `,
  styles: [],
})
export class App {
  protected readonly title = signal('gestao-biometrica-front');
}
