import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../enviroments/enviroments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CobrancaResponse } from '../../model/responses/CobrancaResponse';
import { CobrancaRequest } from '../../model/requests/CobrancaRequest';
import { CobrancaQueryFilters } from '../../model/requests/CobrancaQueryFilters';

@Injectable({
  providedIn: 'root',
})
export class CobrancaService {
  private readonly BASE_URL = `${environment.apiUrl}/cobrancas`;

  cobrancas = signal<CobrancaResponse[]>([]);

  constructor(private http: HttpClient) {}

  salvaCobranca(cobranca: CobrancaRequest) {
    return this.http.post<CobrancaResponse>(this.BASE_URL, cobranca);
  }

  editaCobranca(cobranca: CobrancaRequest) {
    return this.http.put<CobrancaResponse>(this.BASE_URL, cobranca);
  }

  buscaCobrancas() {
    this.http.get<CobrancaResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.cobrancas.set(data),
      error: () => this.cobrancas.set([]),
    });
  }

  buscaCobrancasPorFiltro(filtros: CobrancaQueryFilters) {
    let params = new HttpParams();

    Object.entries(filtros).forEach(([key, value]) => {
      if (value !== null && value !== undefined && value !== '') {
        params = params.set(key, value as any);
      }
    });

    this.http
      .get<CobrancaResponse[]>(`${this.BASE_URL}/buscaPorFiltro`, { params })
      .subscribe({
        next: (data) => this.cobrancas.set(data),
        error: () => this.cobrancas.set([]),
      });
  }

  deletaCobranca(id: number) {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }
}
