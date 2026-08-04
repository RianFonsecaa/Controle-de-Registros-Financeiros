import { HttpClient } from "@angular/common/http";
import { computed, inject, Injectable, signal } from "@angular/core";
import { environment } from "../../../enviroments/enviroments";
import { FuncionarioResponse } from "../../model/responses/FuncionarioResponse";
import { FuncionarioRequest } from "../../model/requests/FuncionarioRequest";
import { PageResponse } from "../../model/responses/PageResponse";

@Injectable({
  providedIn: "root",
})
export class FuncionarioService {
  private readonly BASE_URL = `${environment.apiUrl}/funcionarios`;
  private http = inject(HttpClient);

  readonly funcionarios = signal<FuncionarioResponse[]>([]);

  readonly paginaAtual = signal<number>(0);
  readonly itensPorPagina = signal<number>(10);
  readonly totalPaginas = signal<number>(0);

  readonly exibirApenasAtivos = signal<boolean>(true);

  readonly funcionariosOpcoesSelect = signal<FuncionarioResponse[]>([]);

  buscaFuncionarios() {
    this.http
      .get<PageResponse<FuncionarioResponse>>(this.BASE_URL, {
        params: {
          page: this.paginaAtual().toString(),
          size: this.itensPorPagina().toString(),
          sort: "id,desc",
          ativo: this.exibirApenasAtivos().toString(),
        },
      })
      .subscribe({
        next: (response) => {
          this.funcionarios.set(response.content || []);
          this.totalPaginas.set(response.page.totalPages ?? 0);
        },
        error: () => this.resetarEstado(),
      });
  }

  buscaFuncionariosAtivosListaPura() {
    this.http.get<FuncionarioResponse[]>(`${this.BASE_URL}/ativos`).subscribe({
      next: (dados) => this.funcionariosOpcoesSelect.set(dados || []),
      error: () => this.funcionariosOpcoesSelect.set([]),
    });
  }

  private resetarEstado() {
    this.funcionarios.set([]);
    this.totalPaginas.set(0);
  }

  salvaFuncionario(funcionario: FuncionarioRequest) {
    return this.http.post<FuncionarioResponse>(this.BASE_URL, funcionario);
  }

  atualizaFuncionario(funcionario: FuncionarioRequest) {
    return this.http.put<FuncionarioResponse>(this.BASE_URL, funcionario);
  }

  toggleStatus(id: number) {
    return this.http.patch<void>(`${this.BASE_URL}/${id}/toggle`, {});
  }
}
