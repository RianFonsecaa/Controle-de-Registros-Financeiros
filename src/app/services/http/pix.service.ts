import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../enviroments/enviroments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PixRequest } from '../../model/requests/PixRequest';
import { PixResponse } from '../../model/responses/PixResponse';
import { PixQueryFilters } from '../../model/query-filters/PixQueryFilters';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PixService {
  private readonly BASE_URL = `${environment.apiUrl}/pix`;
  private http = inject(HttpClient);

  readonly pixList = signal<PixResponse[]>([]);

  salvarPix(pix: PixRequest, arquivo: File | null) {
    const formData = new FormData();

    formData.append(
      'pix',
      new Blob([JSON.stringify(pix)], { type: 'application/json' }),
    );

    if (arquivo) {
      formData.append('comprovante', arquivo);
    }

    return this.http.post<PixResponse>(this.BASE_URL, formData);
  }

  buscaTodosPix() {
    this.http.get<PixResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.pixList.set(data),
      error: () => this.pixList.set([]),
    });
  }

  buscaPixPorFiltro(filtros: PixQueryFilters) {
    let params = new HttpParams();

    Object.entries(filtros).forEach(([key, value]) => {
      if (value !== null && value !== undefined && value !== '') {
        params = params.set(key, value as any);
      }
    });

    this.http
      .get<PixResponse[]>(`${this.BASE_URL}/buscaPorFiltro`, { params })
      .subscribe({
        next: (data) => this.pixList.set(data),
        error: () => this.pixList.set([]),
      });
  }

  atualizarPix(pix: PixRequest, arquivo: File | null) {
    const formData = new FormData();
    formData.append(
      'pix',
      new Blob([JSON.stringify(pix)], { type: 'application/json' }),
    );

    if (arquivo) {
      formData.append('comprovante', arquivo);
    }
    console.log(pix);
    return this.http.put<PixResponse>(this.BASE_URL, formData);
  }

  buscaPorCobranca(cobrancaId: number) {
    return this.http.get<PixResponse[]>(
      `${this.BASE_URL}/porCobranca/${cobrancaId}`,
    );
  }

  deletaPix(id: number) {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }

  getComprovante(nome: string): Observable<Blob> {
    return this.http.get(`${this.BASE_URL}/comprovante/${nome}`, {
      responseType: 'blob',
    });
  }
}
