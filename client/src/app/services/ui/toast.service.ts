import { Injectable, signal } from '@angular/core';
import { Toast, tipoToast } from '../../model/ui/Toast';

@Injectable({ providedIn: 'root' })
export class ToastService {
  private readonly _toast = signal<Toast | null>(null);
  readonly toast = this._toast.asReadonly();

  abrir(tipo: tipoToast, mensagem: string, duration = 6000) {
    this.fechar();
    this._toast.set({ tipo, mensagem });

    setTimeout(() => {
      this.fechar();
    }, duration);
  }

  fechar() {
    this._toast.set(null);
  }
}
