import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { environment } from "../../../enviroments/enviroments";
import { ValeRequest } from "../../model/requests/ValeRequest";
import { ValeResponse } from "../../model/responses/ValeResponse";
import { ValeQueryFilters } from "../../model/query-filters/ValeQueryFilters";
import { PageResponse } from "../../model/responses/PageResponse";

@Injectable({
  providedIn: "root",
})
export class ValeService {
  private readonly BASE_URL = `${environment.apiUrl}/vales`;
  private http = inject(HttpClient);

  readonly listaVales = signal<ValeResponse[]>([]);
  readonly paginaAtual = signal<number>(0);
  readonly itensPorPagina = signal<number>(10);
  readonly totalPaginas = signal<number>(0);

  private filtroAtivo: ValeQueryFilters | null = null;

  salvarVale(vale: ValeRequest) {
    return this.http.post<ValeResponse>(this.BASE_URL, vale);
  }

  atualizarVale(vale: ValeRequest) {
    return this.http.put<ValeResponse>(this.BASE_URL, vale);
  }

  deletaVale(id: number) {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }

  buscaPorCobranca(cobrancaId: number) {
    return this.http.get<ValeResponse[]>(
      `${this.BASE_URL}/porCobranca/${cobrancaId}`,
    );
  }

  buscaVales() {
    if (this.filtroAtivo) {
      this.executarBuscaComFiltro();
    } else {
      this.executarBuscaGeral();
    }
  }

  buscaValePorFiltro(filtros: ValeQueryFilters) {
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
      .get<PageResponse<ValeResponse>>(this.BASE_URL, {
        params: {
          page: this.paginaAtual().toString(),
          size: this.itensPorPagina().toString(),
          sort: "id,desc",
        },
      })
      .subscribe({
        next: (response) => {
          this.listaVales.set(response?.content || []);
          this.totalPaginas.set(response?.page?.totalPages ?? 0);
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
      .get<PageResponse<ValeResponse>>(`${this.BASE_URL}/buscaPorFiltro`, {
        params: httpParams,
      })
      .subscribe({
        next: (response) => {
          this.listaVales.set(response?.content || []);
          this.totalPaginas.set(response?.page?.totalPages ?? 0);
        },
        error: () => this.resetarEstado(),
      });
  }

  private resetarEstado() {
    this.listaVales.set([]);
    this.totalPaginas.set(0);
  }
}
