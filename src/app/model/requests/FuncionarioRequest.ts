export interface FuncionarioRequest {
  id: number | null;
  ativo: Boolean;
  nome: string;
  telefone: string;
  email: string;
  dataNascimento: Date;
}
