export interface PixRequest {
  cliente: string;
  valor: number;
  cidadeId: number;
  data: string; // yyyy-MM-dd
  comprovante?: File | null;
}
