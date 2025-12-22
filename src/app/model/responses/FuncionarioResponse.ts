import { SelectOptions } from './SelectOptions';

export interface FuncionarioResponse extends SelectOptions {
  telefone: string;
  email: string;
  dataNascimento: string;
}
