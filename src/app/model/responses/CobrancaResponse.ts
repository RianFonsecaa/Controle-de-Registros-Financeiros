import { CidadeResponse } from './CidadeResponse';
import { FuncionarioResponse } from './FuncionarioResponse';

export interface CobrancaResponse {
  id: number;
  cidade: CidadeResponse;
  cobrador: FuncionarioResponse;
  registroPor: string;
  valorEspecie: number;
  valorTotal: number;
  valorTotalPix: number;
  valorTotalVale: number;
  data: string;
  veiculo: string;
  observacoes?: string;
}
