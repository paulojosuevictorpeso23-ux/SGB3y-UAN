// src/app/services/biometria.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class BiometriaService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/biometria/identificar'; 

  identificarDigital(vectorHexa: string): Observable<any> {
    // Atenção: O backend espera um JSON com a chave "templateBiometrico"
    return this.http.post(this.apiUrl, { templateBiometrico: vectorHexa });
  }
}

