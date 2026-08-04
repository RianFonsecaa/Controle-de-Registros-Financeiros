import { inject, Injectable, signal } from "@angular/core";
import { environment } from "../../../enviroments/enviroments";
import { HttpClient, HttpParams } from "@angular/common/http";
import { CobrancaResponse } from "../../model/responses/CobrancaResponse";
import { CobrancaRequest } from "../../model/requests/CobrancaRequest";
import { CobrancaQueryFilters } from "../../model/query-filters/CobrancaQueryFilters";
import { PageResponse } from "../../model/responses/PageResponse";

@Injectable({
  providedIn: "root",
})
export class CobrancaService {
  private readonly BASE_URL = `${environment.apiUrl}/cobrancas`;
  private http = inject(HttpClient);

  readonly cobrancas = signal<CobrancaResponse[]>([]);
  readonly paginaAtual = signal<number>(0);
  readonly itensPorPagina = signal<number>(10);
  readonly totalPaginas = signal<number>(0);

  private filtroAtivo: CobrancaQueryFilters | null = null;

  salvaCobranca(cobranca: CobrancaRequest) {
    return this.http.post<CobrancaResponse>(this.BASE_URL, cobranca);
  }

  atualizaCobranca(cobranca: CobrancaRequest) {
    return this.http.put<CobrancaResponse>(this.BASE_URL, cobranca);
  }

  deletaCobranca(id: number) {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }

  buscaCobrancas() {
    if (this.filtroAtivo) {
      this.executarBuscaComFiltro();
    } else {
      this.executarBuscaGeral();
    }
  }

  buscaCobrancasPorFiltro(filtros: CobrancaQueryFilters) {
    this.filtroAtivo = filtros;
    this.paginaAtual.set(0);
    this.executarBuscaComFiltro();
  }

  limparFiltros() {
    this.filtroAtivo = null;
    this.paginaAtual.set(0);
    this.executarBuscaGeral();
  }

  private executarBuscaGeral() {
    this.http
      .get<PageResponse<CobrancaResponse>>(this.BASE_URL, {
        params: {
          page: this.paginaAtual().toString(),
          size: this.itensPorPagina().toString(),
          sort: "id,desc",
        },
      })
      .subscribe({
        next: (response) => {
          this.cobrancas.set(response.content);
          this.totalPaginas.set(response.page.totalPages ?? 0);
        },
        error: () => this.resetarEstado(),
      });
  }

  private executarBuscaComFiltro() {
    let httpParams = new HttpParams()
      .set("page", this.paginaAtual().toString())
      .set("size", this.itensPorPagina().toString());

    if (this.filtroAtivo) {
      Object.entries(this.filtroAtivo).forEach(([key, value]) => {
        if (value !== null && value !== undefined && value !== "") {
          httpParams = httpParams.set(key, value as any);
        }
      });
    }

    this.http
      .get<PageResponse<CobrancaResponse>>(`${this.BASE_URL}/buscaPorFiltro`, {
        params: httpParams,
      })
      .subscribe({
        next: (response) => {
          this.cobrancas.set(response.content);
          this.totalPaginas.set(response.page.totalPages ?? 0);
        },
        error: () => this.resetarEstado(),
      });
  }

  private resetarEstado() {
    this.cobrancas.set([]);
    this.totalPaginas.set(0);
  }
}
