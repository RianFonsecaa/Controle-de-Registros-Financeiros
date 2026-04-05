import { computed, inject, Injectable, signal } from '@angular/core';
import { VeiculoResponse } from '../../model/responses/VeiculoResponse';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../enviroments/enviroments';
import { VeiculoRequest } from '../../model/requests/VeiculoRequest';

@Injectable({
  providedIn: 'root',
})
export class VeiculoService {
  private readonly BASE_URL = `${environment.apiUrl}/veiculos`;
  private http = inject(HttpClient);

  readonly veiculos = signal<VeiculoResponse[]>([]);

  readonly veiculosAtivos = computed(() =>
    this.veiculos().filter((veiculo) => veiculo.ativo === true),
  );

  buscaVeiculos() {
    this.http.get<VeiculoResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.veiculos.set(data),
      error: () => this.veiculos.set([]),
    });
  }

  salvaVeiculo(veiculo: VeiculoRequest) {
    return this.http.post<VeiculoResponse>(this.BASE_URL, veiculo);
  }

  atualizaVeiculo(veiculo: VeiculoRequest) {
    return this.http.put<VeiculoResponse>(this.BASE_URL, veiculo);
  }

  toggleStatus(id: number) {
    return this.http.patch<void>(`${this.BASE_URL}/${id}/toggle`, {});
  }
}
