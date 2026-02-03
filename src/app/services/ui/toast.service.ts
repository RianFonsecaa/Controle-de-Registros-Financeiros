import { Injectable, signal } from '@angular/core';
import { Toast, tipoToast } from '../../model/ui/Toast';

@Injectable({ providedIn: 'root' })
export class ToastService {
  private _toast = signal<Toast | null>(null);
  toast = this._toast.asReadonly();

  open(tipo: tipoToast, mensagem: string, duration = 5000) {
    this._toast.set({ tipo, mensagem });

    setTimeout(() => {
      this.close();
    }, duration);
  }

  close() {
    this._toast.set(null);
  }
}
