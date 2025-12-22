import { inject, Injectable, signal } from '@angular/core';
import { VeiculoResponse } from '../model/responses/VeiculoResponse';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../enviroments/enviroments';
import { map } from 'rxjs';
import { VeiculoDTO } from '../model/responses/VeiculoDTO';

@Injectable({
  providedIn: 'root',
})
export class VeiculoService {
  private readonly BASE_URL = `${environment.apiUrl}/veiculos`;
  private http = inject(HttpClient);

  veiculos = signal<VeiculoResponse[]>([]);

  buscaVeiculos() {
    this.http
      .get<VeiculoDTO[]>(this.BASE_URL)
      .pipe(
        map((data) =>
          data.map((veiculo) => ({
            id: veiculo.id,
            nome: veiculo.modelo,
            placa: veiculo.placa,
          }))
        )
      )
      .subscribe({
        next: (data) => this.veiculos.set(data),
        error: () => this.veiculos.set([]),
      });
  }
}
