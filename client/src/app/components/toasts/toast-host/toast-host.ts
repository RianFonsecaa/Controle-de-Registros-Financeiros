import { Component, inject } from '@angular/core';
import { ToastService } from '../../../services/ui/toast.service';
import { InfoToast } from '../info-toast/info-toast';
import { ErrorToast } from '../error-toast/error-toast';
import { SuccessToast } from '../success-toast/success-toast';

@Component({
  selector: 'app-toast-host',
  imports: [InfoToast, ErrorToast, SuccessToast],
  templateUrl: './toast-host.html',
  styleUrl: './toast-host.css',
})
export class ToastHost {
  private toastService = inject(ToastService);
  toast = this.toastService.toast;
}
