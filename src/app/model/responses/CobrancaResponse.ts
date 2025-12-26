export interface CobrancaResponse {
  id: number;

  cidadeId: number;
  cidadeNome: string;

  cobradorId: number;
  cobradorNome: string;

  veiculoId: number;
  veiculoModelo: string;

  registroPor: string;

  valorTotalEspecie: number;
  valorTotal: number;
  valorTotalPix: number;
  valorTotalVale: number;

  data: string;

  observacoes?: string;
}
