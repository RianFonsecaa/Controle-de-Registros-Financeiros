export type tipoToast = 'success' | 'info' | 'error';

export interface Toast {
  tipo: tipoToast;
  mensagem: string;
}
