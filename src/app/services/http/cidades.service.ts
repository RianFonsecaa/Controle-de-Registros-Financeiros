import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { CidadeResponse } from '../../model/responses/CidadeResponse';
import { environment } from '../../../enviroments/enviroments';
import { CidadeRequest } from '../../model/requests/CidadeRequest';

@Injectable({
  providedIn: 'root',
})
export class CidadesService {
  private readonly BASE_URL = `${environment.apiUrl}/cidades`;
  private http = inject(HttpClient);

  readonly cidades = signal<CidadeResponse[]>([]);

  readonly cidadesAtivas = computed(() =>
    this.cidades().filter((cidade) => cidade.ativo === true),
  );

  buscaCidades() {
    this.http.get<CidadeResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.cidades.set(data),
      error: () => this.cidades.set([]),
    });
  }

  salvaCidade(cidade: CidadeRequest) {
    return this.http.post<CidadeResponse>(this.BASE_URL, cidade);
  }

  atualizaCidade(cidade: CidadeRequest) {
    return this.http.put<CidadeResponse>(this.BASE_URL, cidade);
  }

  toggleStatus(id: number) {
    return this.http.patch<void>(`${this.BASE_URL}/${id}/toggle`, {});
  }
}
