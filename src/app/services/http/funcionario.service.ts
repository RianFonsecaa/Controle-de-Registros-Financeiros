import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../enviroments/enviroments';
import { FuncionarioResponse } from '../../model/responses/FuncionarioResponse';
import { FuncionarioRequest } from '../../model/requests/FuncionarioRequest';

@Injectable({
  providedIn: 'root',
})
export class FuncionarioService {
  private readonly BASE_URL = `${environment.apiUrl}/funcionarios`;
  private http = inject(HttpClient);

  readonly funcionarios = signal<FuncionarioResponse[]>([]);

  readonly funcionariosAtivos = computed(() =>
    this.funcionarios().filter((funcionario) => funcionario.ativo === true),
  );

  buscaFuncionarios() {
    this.http.get<FuncionarioResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.funcionarios.set(data),
      error: () => this.funcionarios.set([]),
    });
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
