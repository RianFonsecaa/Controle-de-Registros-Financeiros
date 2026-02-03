import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { CidadeResponse } from '../../model/responses/CidadeResponse';
import { environment } from '../../../enviroments/enviroments';

@Injectable({
  providedIn: 'root',
})
export class CidadesService {
  private readonly BASE_URL = `${environment.apiUrl}/cidades`;
  private http = inject(HttpClient);

  cidades = signal<CidadeResponse[]>([]);

  buscaCidades() {
    this.http.get<CidadeResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.cidades.set(data),
      error: () => this.cidades.set([]),
    });
  }
}
