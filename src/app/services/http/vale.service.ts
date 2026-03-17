import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../enviroments/enviroments';
import { ValeRequest } from '../../model/requests/ValeRequest';
import { ValeResponse } from '../../model/responses/ValeResponse';
import { ValeQueryFilters } from '../../model/query-filters/ValeQueryFilters';

@Injectable({
  providedIn: 'root',
})
export class ValeService {
  private readonly BASE_URL = `${environment.apiUrl}/vales`;
  private http = inject(HttpClient);

  readonly listaVales = signal<ValeResponse[]>([]);

  salvarVale(vale: ValeRequest) {
    return this.http.post<ValeResponse>(this.BASE_URL, vale);
  }

  buscaVales() {
    this.http.get<ValeResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.listaVales.set(data),
      error: () => this.listaVales.set([]),
    });
  }

  buscaValePorFiltro(filtros: ValeQueryFilters) {
    let params = new HttpParams();

    Object.entries(filtros).forEach(([key, value]) => {
      if (value !== null && value !== undefined && value !== '') {
        params = params.set(key, value as any);
      }
    });

    this.http
      .get<ValeResponse[]>(`${this.BASE_URL}/buscaPorFiltro`, { params })
      .subscribe({
        next: (data) => this.listaVales.set(data),
        error: () => this.listaVales.set([]),
      });
  }

  atualizarVale(vale: ValeRequest) {
    return this.http.put<ValeResponse>(this.BASE_URL, vale);
  }

  buscaPorCobranca(cobrancaId: number) {
    return this.http.get<ValeResponse[]>(
      `${this.BASE_URL}/porCobranca/${cobrancaId}`,
    );
  }

  deletaVale(id: number) {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }
}
