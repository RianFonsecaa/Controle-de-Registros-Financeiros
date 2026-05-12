export interface PixResponse {
  id: number;
  valor: number;
  cliente: string;
  data: string;
  cidadeId: number;
  cidadeNome: string;
  cobrancaId: number;
  nomeComprovante?: string;
}
