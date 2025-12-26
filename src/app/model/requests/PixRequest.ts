export interface PixRequest {
  cliente: string;
  valor: number;
  cidadeId: number;
  data: string;
  comprovante: File | null;
  cobrancaId: number | null;
}
