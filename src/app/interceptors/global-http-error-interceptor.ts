import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { ToastService } from '../services/ui/toast.service';
import { tap } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

export const globalHttpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastService = inject(ToastService);
  const authService = inject(AuthService);
  return next(req).pipe(
    tap({
      error: (error: HttpErrorResponse) => {
        if ([401].includes(error.status)) {
          authService.logout();
        }
        toastService.abrir('error', error.error.message);
      },
    }),
  );
};
