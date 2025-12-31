import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../enviroments/enviroments';
import { TokenStorageService } from './token-storage.service';

@Injectable({
  providedIn: 'root',
})
export class RelatorioService {
  private readonly BASE_URL = `${environment.apiUrl}/relatorios/periodo`;
  private tokenStorageService = inject(TokenStorageService);
  private http = inject(HttpClient);

  geraRelatorioDeCobrancas(dataInicio: string, dataFim: string) {
    const token = this.tokenStorageService.getAccessToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.get(this.BASE_URL, {
      params: { dataInicio, dataFim },
      responseType: 'blob',
      headers,
    });
  }
}
