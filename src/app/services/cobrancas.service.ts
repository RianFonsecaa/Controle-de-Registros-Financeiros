import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../enviroments/enviroments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CobrancaResponse } from '../model/responses/CobrancaResponse';

@Injectable({
  providedIn: 'root',
})
export class CobrancaService {
  private readonly BASE_URL = `${environment.apiUrl}/cobrancas`;

  cobrancas = signal<CobrancaResponse[]>([]);

  constructor(private http: HttpClient) {}

  buscaCobrancas() {
    this.http.get<CobrancaResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.cobrancas.set(data),
      error: () => this.cobrancas.set([]),
    });
  }

  deletaCobranca(id: number) {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }
}
