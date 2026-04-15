import { HttpClient } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { Observable, tap } from "rxjs";
import { environment } from "../../../enviroments/enviroments";

export interface ResumoGeral {
  totalArrecadado: number;
  totalPix: number;
  totalEspecie: number;
  cidadesAtendidas: number;
}

export interface RendimentoCidade {
  cidade: string;
  total: number;
}

export interface RendimentoDiario {
  dia: string;
  valor: number;
}

export interface MetodoPagamento {
  totalPix: number;
  totalEspecie: number;
}

export interface DashboardData {
  resumoGeral: ResumoGeral;
  rankingCidades: RendimentoCidade[];
  rendimentoDiario: RendimentoDiario[];
  distribuicaoPagamentos: MetodoPagamento;
  mesReferencia: String;
  ultimaAtualizacao: String;
}

@Injectable({
  providedIn: "root",
})
export class DashboardService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/dashboard`;

  dadosDashboard = signal<DashboardData | null>(null);
  loading = signal<boolean>(false);

  buscaDadosDashboard() {
    this.loading.set(true);

    this.http.get<DashboardData>(this.API_URL).subscribe({
      next: (data) => {
        this.dadosDashboard.set(data);
      },
      error: (err) => {
        console.error("Erro ao buscar dados do dashboard:", err);
        this.loading.set(false);
      },
      complete: () => {
        this.loading.set(false);
      },
    });
  }
}
