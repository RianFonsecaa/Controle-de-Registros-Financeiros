import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastService } from './services/ui/toast.service';
import { ErrorToast } from './components/toasts/error-toast/error-toast';
import { SuccessToast } from './components/toasts/success-toast/success-toast';
import { InfoToast } from './components/toasts/info-toast/info-toast';
import { Toast } from './model/ui/Toast';
import { ToastHost } from './components/toasts/toast-host/toast-host';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ToastHost],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('frontend');

  private toastService = inject(ToastService);
  toast = this.toastService.toast;
}
