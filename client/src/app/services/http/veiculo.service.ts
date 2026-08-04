import { computed, inject, Injectable, signal } from "@angular/core";
import { VeiculoResponse } from "../../model/responses/VeiculoResponse";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../../enviroments/enviroments";
import { VeiculoRequest } from "../../model/requests/VeiculoRequest";
import { PageResponse } from "../../model/responses/PageResponse";

@Injectable({
  providedIn: "root",
})
export class VeiculoService {
  private readonly BASE_URL = `${environment.apiUrl}/veiculos`;
  private http = inject(HttpClient);

  readonly veiculos = signal<VeiculoResponse[]>([]);

  readonly paginaAtual = signal<number>(0);
  readonly itensPorPagina = signal<number>(10);
  readonly totalPaginas = signal<number>(0);

  readonly exibirApenasAtivos = signal<boolean>(true);

  readonly veiculosOpcoesSelect = signal<VeiculoResponse[]>([]);

  buscaVeiculos() {
    this.http
      .get<PageResponse<VeiculoResponse>>(this.BASE_URL, {
        params: {
          page: this.paginaAtual().toString(),
          size: this.itensPorPagina().toString(),
          sort: "id,desc",
          ativo: this.exibirApenasAtivos().toString(),
        },
      })
      .subscribe({
        next: (response) => {
          this.veiculos.set(response.content || []);
          this.totalPaginas.set(response.page.totalPages ?? 0);
        },
        error: () => this.resetarEstado(),
      });
  }

  buscaVeiculosAtivosListaPura() {
    this.http.get<VeiculoResponse[]>(`${this.BASE_URL}/ativos`).subscribe({
      next: (dados) => this.veiculosOpcoesSelect.set(dados || []),
      error: () => this.veiculosOpcoesSelect.set([]),
    });
  }

  private resetarEstado() {
    this.veiculos.set([]);
    this.totalPaginas.set(0);
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
