import { inject, Injectable } from '@angular/core';
import { environment } from '../../../enviroments/enviroments';
import { HttpClient } from '@angular/common/http';
import { PixRequest } from '../../model/requests/PixRequest';
import { PixResponse } from '../../model/responses/PixResponse';

@Injectable({
  providedIn: 'root',
})
export class PixService {
  private readonly BASE_URL = `${environment.apiUrl}/pix`;
  private http = inject(HttpClient);

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

  buscaPorCobranca(cobrancaId: Number) {
    return this.http.get<PixResponse[]>(
      `${this.BASE_URL}/porCobranca/${cobrancaId}`,
    );
  }
}
