import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../enviroments/enviroments';
import { CidadeResponse } from '../model/responses/CidadeResponse';
import { FuncionarioResponse } from '../model/responses/FuncionarioResponse';

@Injectable({
  providedIn: 'root',
})
export class FuncionarioService {
  private readonly BASE_URL = `${environment.apiUrl}/funcionarios`;
  private http = inject(HttpClient);

  cobradores = signal<FuncionarioResponse[]>([]);

  buscaFuncionarios() {
    this.http.get<FuncionarioResponse[]>(this.BASE_URL).subscribe({
      next: (data) => this.cobradores.set(data),
      error: () => this.cobradores.set([]),
    });
  }
}
