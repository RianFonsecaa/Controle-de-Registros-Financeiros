import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../enviroments/enviroments';
import { CobrancaQueryFilters } from '../../model/requests/CobrancaQueryFilters';

@Injectable({
  providedIn: 'root',
})
export class RelatorioService {
  private readonly BASE_URL = `${environment.apiUrl}/relatorios`;
  private http = inject(HttpClient);

  geraRelatorioDeCobrancas(filtros: CobrancaQueryFilters) {
    let params = new HttpParams();

    Object.entries(filtros).forEach(([key, value]) => {
      if (value !== null && value !== undefined && value !== '') {
        params = params.set(key, value as any);
      }
    });

    return this.http.get(this.BASE_URL, {
      params,
      responseType: 'blob',
    });
  }
}
