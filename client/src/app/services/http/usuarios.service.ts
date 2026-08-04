import { HttpClient } from "@angular/common/http";
import { computed, inject, Injectable, signal } from "@angular/core";
import { environment } from "../../../enviroments/enviroments";
import { Observable, tap } from "rxjs";
import { UserResponse } from "../../model/responses/UserResponse";
import { UserRequest } from "../../model/requests/UserRequest";
import { PageResponse } from "../../model/responses/PageResponse";

@Injectable({
  providedIn: "root",
})
export class UsuariosService {
  private readonly BASE_URL = `${environment.apiUrl}/usuarios`;
  private http = inject(HttpClient);

  readonly usuarios = signal<UserResponse[]>([]);

  readonly paginaAtual = signal<number>(0);
  readonly itensPorPagina = signal<number>(10);
  readonly totalPaginas = signal<number>(0);

  readonly exibirApenasAtivos = signal<boolean>(true);

  readonly usuariosOpcoesSelect = signal<UserResponse[]>([]);

  buscaUsuarios() {
    this.http
      .get<PageResponse<UserResponse>>(this.BASE_URL, {
        params: {
          page: this.paginaAtual().toString(),
          size: this.itensPorPagina().toString(),
          sort: "id,desc",
          ativo: this.exibirApenasAtivos().toString(),
        },
      })
      .subscribe({
        next: (response) => {
          this.usuarios.set(response.content || []);
          this.totalPaginas.set(response.page.totalPages ?? 0);
        },
        error: () => this.resetarEstado(),
      });
  }

  buscaUsuariosAtivosListaPura() {
    this.http.get<UserResponse[]>(`${this.BASE_URL}/ativos`).subscribe({
      next: (dados) => this.usuariosOpcoesSelect.set(dados || []),
      error: () => this.usuariosOpcoesSelect.set([]),
    });
  }

  private resetarEstado() {
    this.usuarios.set([]);
    this.totalPaginas.set(0);
  }

  registrarUsuario(dados: UserRequest) {
    return this.http.post<UserResponse>(`${this.BASE_URL}/registro`, dados);
  }

  atualizaUsuario(loginOriginal: string, dados: UserRequest) {
    return this.http.put<UserResponse>(
      `${this.BASE_URL}/${loginOriginal}`,
      dados,
    );
  }

  toggleStatus(login: string) {
    return this.http.patch<void>(`${this.BASE_URL}/${login}/toggle`, {});
  }

  mudarSenha(login: string, novaSenha: string) {
    return this.http.patch<void>(`${this.BASE_URL}/${login}/password`, {
      password: novaSenha,
    });
  }
}
