import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../enviroments/enviroments';
import { PixRequest } from '../../model/requests/PixRequest';
import { PixResponse } from '../../model/responses/PixResponse';
import { ValeRequest } from '../../model/requests/ValeRequest';

@Injectable({
  providedIn: 'root',
})
export class ValeService {
  private readonly BASE_URL = `${environment.apiUrl}/vales`;
  private http = inject(HttpClient);

  salvarVale(vale: ValeRequest) {
    return this.http.post<PixResponse>(this.BASE_URL, vale);
  }
}
