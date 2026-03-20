import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../enviroments/enviroments';
import { Observable, tap } from 'rxjs';
import { UserResponse } from '../../model/responses/UserResponse';
import { UserRequest } from '../../model/requests/UserRequest';

@Injectable({
  providedIn: 'root',
})
export class UsuariosService {
  private readonly BASE_URL = `${environment.apiUrl}/auth/users`;
  private http = inject(HttpClient);

  readonly usuarios = signal<UserResponse[]>([]);

  buscaUsuarios() {
    this.http.get<UserResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.usuarios.set(data),
      error: () => this.usuarios.set([]),
    });
  }

  registrarUsuario(dados: UserRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(
      `${environment.apiUrl}/auth/register`,
      dados,
    );
  }

  atualizaUsuario(
    loginOriginal: string,
    dados: UserRequest,
  ): Observable<UserResponse> {
    return this.http
      .put<UserResponse>(`${this.BASE_URL}/${loginOriginal}`, dados)
      .pipe(tap(() => this.buscaUsuarios()));
  }

  excluiUsuario(login: string): Observable<void> {
    return this.http
      .delete<void>(`${this.BASE_URL}/${login}`)
      .pipe(tap(() => this.buscaUsuarios()));
  }

  mudarSenha(login: string, novaSenha: string): Observable<void> {
    return this.http.patch<void>(`${this.BASE_URL}/${login}/password`, {
      password: novaSenha,
    });
  }
}
