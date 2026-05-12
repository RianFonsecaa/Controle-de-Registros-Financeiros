export interface CobrancaRequest {
  id: number | null;
  cidadeId: number;
  cobradorId: number;
  veiculoId: number;
  data: string | Date;
  observacoes: string;
  valorEspecie: number;
  valorTotalPix: number;
  valorTotalVale: number;
  valorTotal: number;
}
