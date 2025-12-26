import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../enviroments/enviroments';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CobrancaResponse } from '../model/responses/CobrancaResponse';
import { CobrancaRequest } from '../model/requests/CobrancaRequest';
import { TokenStorageService } from './token-storage.service';

@Injectable({
  providedIn: 'root',
})
export class CobrancaService {
  private readonly BASE_URL = `${environment.apiUrl}/cobrancas`;
  private tokenStorageService = inject(TokenStorageService);

  cobrancas = signal<CobrancaResponse[]>([]);

  constructor(private http: HttpClient) {}

  salvaCobranca(cobranca: CobrancaRequest) {
    const token = this.tokenStorageService.getAccessToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    return this.http.post<CobrancaResponse>(this.BASE_URL, cobranca, {
      headers,
    });
  }

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
