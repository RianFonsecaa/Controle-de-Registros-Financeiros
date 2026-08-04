import { HttpClient } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { CidadeResponse } from "../../model/responses/CidadeResponse";
import { environment } from "../../../enviroments/enviroments";
import { CidadeRequest } from "../../model/requests/CidadeRequest";
import { PageResponse } from "../../model/responses/PageResponse";

@Injectable({
  providedIn: "root",
})
export class CidadesService {
  private readonly BASE_URL = `${environment.apiUrl}/cidades`;
  private http = inject(HttpClient);

  readonly cidades = signal<CidadeResponse[]>([]);

  readonly paginaAtual = signal<number>(0);
  readonly itensPorPagina = signal<number>(10);
  readonly totalPaginas = signal<number>(0);

  readonly exibirApenasAtivos = signal<boolean>(true);

  readonly cidadesOpcoesSelect = signal<CidadeResponse[]>([]);

  buscaCidades() {
    this.http
      .get<PageResponse<CidadeResponse>>(this.BASE_URL, {
        params: {
          page: this.paginaAtual().toString(),
          size: this.itensPorPagina().toString(),
          sort: "id,desc",
          ativo: this.exibirApenasAtivos().toString(),
        },
      })
      .subscribe({
        next: (response) => {
          this.cidades.set(response.content || []);
          this.totalPaginas.set(response.page.totalPages ?? 0);
        },
        error: () => this.resetarEstado(),
      });
  }

  buscaCidadesAtivasListaPura() {
    this.http.get<CidadeResponse[]>(`${this.BASE_URL}/ativas`).subscribe({
      next: (dados) => this.cidadesOpcoesSelect.set(dados || []),
      error: () => this.cidadesOpcoesSelect.set([]),
    });
  }

  private resetarEstado() {
    this.cidades.set([]);
    this.totalPaginas.set(0);
  }

  salvaCidade(cidade: CidadeRequest) {
    return this.http.post<CidadeResponse>(this.BASE_URL, cidade);
  }

  atualizaCidade(cidade: CidadeRequest) {
    return this.http.put<CidadeResponse>(this.BASE_URL, cidade);
  }

  toggleStatus(id: number) {
    return this.http.patch<void>(`${this.BASE_URL}/${id}/toggle`, {});
  }
}
