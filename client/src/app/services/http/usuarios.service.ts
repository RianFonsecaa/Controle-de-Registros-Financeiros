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
  private readonly BASE_URL = `${environment.apiUrl}/usuarios`;
  private http = inject(HttpClient);

  readonly usuarios = signal<UserResponse[]>([]);

  buscaUsuarios() {
    this.http.get<UserResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.usuarios.set(data),
      error: () => this.usuarios.set([]),
    });
  }

  registrarUsuario(dados: UserRequest) {
    return this.http.post<UserResponse>(`${this.BASE_URL}/registro`, dados);
  }

  atualizaUsuario(loginOriginal: string, dados: UserRequest) {
    return this.http
      .put<UserResponse>(`${this.BASE_URL}/${loginOriginal}`, dados)
      .pipe(tap(() => this.buscaUsuarios()));
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
