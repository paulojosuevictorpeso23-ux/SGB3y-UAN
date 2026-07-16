import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/utilizadores';
  private bioUrl = 'http://localhost:8080/api/biometria'; // Novo endpoint

  defaultUser = { id: null, nome: '', username: '', contacto: '', email: '', palavraPasse: '1234', cargoId: 2, horarioId: 1, estado: 'ATIVO' };
  utilizador: any = { ...this.defaultUser };
  listaUtilizadores: any[] = [];

  ngOnInit() { this.carregarUtilizadores(); }

  carregarUtilizadores() {
  this.http.get<any[]>(this.apiUrl).subscribe(data => {
    // Forçamos a atribuição de uma nova referência para disparar a atualização da tabela
    this.listaUtilizadores = [...data]; 
    console.log('Utilizadores carregados:', this.listaUtilizadores);
  });
}

  salvar() {
    if (this.utilizador.id) {
      this.http.put(`${this.apiUrl}/${this.utilizador.id}`, this.utilizador).subscribe(() => this.limparEAtualizar());
    } else {
      this.http.post(this.apiUrl, this.utilizador).subscribe(() => this.limparEAtualizar());
    }
  }

  // Altere o método apagar:
apagar(id: number) {
  if(confirm('Tem certeza que deseja APAGAR este utilizador?')) {
    this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' }) // Define responseType como 'text'
      .subscribe({
        next: () => this.carregarUtilizadores(),
        error: (err) => {
            // Se o status for 204, o Angular as vezes entra no erro mesmo sendo sucesso
            if (err.status === 204 || err.status === 200) {
                this.carregarUtilizadores();
            } else {
                console.error(err);
            }
        }
      });
  }
}

  // MÉTODO DE ASSOCIAÇÃO ATUALIZADO
  iniciarAssociacao(u: any) {
    const dedo = prompt("Qual o dedo?");
    const template = prompt("Cole o template:");

    if (dedo && template) {
      const payload = {
        utilizadorId: Number(u.id), // Força a ser número
        templateBiometrico: template,
        dedoRegistado: dedo
      };

      // Adicione o { headers } como terceiro argumento
      const headers = { 'Content-Type': 'application/json' };
      
      this.http.post(`${this.bioUrl}/registo`, JSON.stringify(payload), { headers })
        .subscribe({
          next: () => {
            alert('Sucesso!');
            this.carregarUtilizadores();
          },
          error: (err) => {
            console.error('Erro detalhado:', err);
            alert('Erro: ' + (err.error?.message || 'Verifique o terminal do servidor'));
          }
        });
    }
  }

  editar(u: any) {
    this.utilizador = { ...u, cargoId: u.cargo?.id, horarioId: u.horario?.id };
  }

  limparEAtualizar() {
    this.utilizador = { ...this.defaultUser };
    this.carregarUtilizadores();
  }
}

