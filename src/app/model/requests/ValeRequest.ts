export interface ValeRequest {
  id: number | null;
  funcionarioId: number;
  funcionarioNome: string;
  justificativa: string;
  valor: number;
  data: string;
  cobrancaId: number | null;
}
