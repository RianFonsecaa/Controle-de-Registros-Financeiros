import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../enviroments/enviroments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CobrancaResponse } from '../model/responses/CobrancaResponse';

@Injectable({
  providedIn: 'root',
})
export class CobrancaService {
  private readonly apiUrl = 'http://localhost:8080/cobrancas';

  cobrancas = signal<CobrancaResponse[]>([]);

  constructor(private http: HttpClient) {}

  carregar() {
    this.http.get<CobrancaResponse[]>(this.apiUrl).subscribe({
      next: (data) => this.cobrancas.set(data),
      error: () => this.cobrancas.set([]),
    });
  }
}
