import { inject, Injectable, signal } from '@angular/core';
import { VeiculoResponse } from '../../model/responses/VeiculoResponse';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../enviroments/enviroments';

@Injectable({
  providedIn: 'root',
})
export class VeiculoService {
  private readonly BASE_URL = `${environment.apiUrl}/veiculos`;
  private http = inject(HttpClient);

  veiculos = signal<VeiculoResponse[]>([]);

  buscaVeiculos() {
    this.http.get<VeiculoResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.veiculos.set(data),
      error: () => this.veiculos.set([]),
    });
  }
}
