import { ValeRequest } from './ValeRequest';

export interface CobrancaRequest {
  cidadeId: number;
  cobradorId: number;
  veiculoId: number;
  data: string | Date;
  observacoes: string;

  valorTotalEspecie: number;
  valorTotalPix: number;
  valorTotalVale: number;
  valorTotal: number;
  vales: ValeRequest[];
}
