export interface PixRequest {
  cliente: string;
  valor: number;
  cidade: string;
  data: string; // yyyy-MM-dd
  comprovante?: File | null;
}
