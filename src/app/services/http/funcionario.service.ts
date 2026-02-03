import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../enviroments/enviroments';
import { CidadeResponse } from '../../model/responses/CidadeResponse';
import { FuncionarioResponse } from '../../model/responses/FuncionarioResponse';

@Injectable({
  providedIn: 'root',
})
export class FuncionarioService {
  private readonly BASE_URL = `${environment.apiUrl}/funcionarios`;
  private http = inject(HttpClient);

  funcionarios = signal<FuncionarioResponse[]>([]);

  buscaFuncionarios() {
    this.http.get<FuncionarioResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.funcionarios.set(data),
      error: () => this.funcionarios.set([]),
    });
  }
}
