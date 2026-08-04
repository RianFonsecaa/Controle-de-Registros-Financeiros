import { inject, Injectable, signal } from "@angular/core";
import { environment } from "../../../enviroments/enviroments";
import { HttpClient, HttpParams } from "@angular/common/http";
import { PixRequest } from "../../model/requests/PixRequest";
import { PixResponse } from "../../model/responses/PixResponse";
import { PixQueryFilters } from "../../model/query-filters/PixQueryFilters";
import { PageResponse } from "../../model/responses/PageResponse";

@Injectable({
  providedIn: "root",
})
export class PixService {
  private readonly BASE_URL = `${environment.apiUrl}/pix`;
  private http = inject(HttpClient);

  readonly listaPixs = signal<PixResponse[]>([]);
  readonly paginaAtual = signal<number>(0);
  readonly itensPorPagina = signal<number>(10);
  readonly totalPaginas = signal<number>(0);

  private filtroAtivo: PixQueryFilters | null = null;

  salvarPix(pix: PixRequest, arquivo: File | null) {
    const formData = new FormData();
    formData.append(
      "pix",
      new Blob([JSON.stringify(pix)], { type: "application/json" }),
    );

    if (arquivo) {
      formData.append("comprovante", arquivo);
    }

    return this.http.post<PixResponse>(this.BASE_URL, formData);
  }

  atualizarPix(pix: PixRequest, arquivo: File | null) {
    const formData = new FormData();
    formData.append(
      "pix",
      new Blob([JSON.stringify(pix)], { type: "application/json" }),
    );

    if (arquivo) {
      formData.append("comprovante", arquivo);
    }
    return this.http.put<PixResponse>(this.BASE_URL, formData);
  }

  deletaPix(id: number) {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }

  getComprovante(nome: string) {
    return this.http.get(`${this.BASE_URL}/comprovantes/${nome}`, {
      responseType: "blob",
    });
  }

  buscaPorCobranca(cobrancaId: number) {
    return this.http.get<PixResponse[]>(
      `${this.BASE_URL}/porCobranca/${cobrancaId}`,
    );
  }

  buscaPixs() {
    if (this.filtroAtivo) {
      this.executarBuscaComFiltro();
    } else {
      this.executarBuscaGeral();
    }
  }

  buscaPixPorFiltro(filtros: PixQueryFilters) {
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
      .get<PageResponse<PixResponse>>(this.BASE_URL, {
        params: {
          page: this.paginaAtual().toString(),
          size: this.itensPorPagina().toString(),
          sort: "id,desc",
        },
      })
      .subscribe({
        next: (response) => {
          this.listaPixs.set(response.content);
          this.totalPaginas.set(
            response.page?.totalPages ?? response.page.totalPages ?? 0,
          );
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
      .get<PageResponse<PixResponse>>(`${this.BASE_URL}/buscaPorFiltro`, {
        params: httpParams,
      })
      .subscribe({
        next: (response) => {
          this.listaPixs.set(response.content);
          this.totalPaginas.set(
            response.page?.totalPages ?? response.page.totalPages ?? 0,
          );
        },
        error: () => this.resetarEstado(),
      });
  }

  private resetarEstado() {
    this.listaPixs.set([]);
    this.totalPaginas.set(0);
  }
}
