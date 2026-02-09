import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { ToastService } from '../services/ui/toast.service';
import { tap } from 'rxjs';
import { Router } from '@angular/router';

export const globalHttpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastService = inject(ToastService);
  const router = inject(Router);
  return next(req).pipe(
    tap({
      error: (error: HttpErrorResponse) => {
        if ([500, 404, 401].includes(error.status)) {
          router.navigate(['/login']);
        }
        toastService.abrir('error', error.error.message);
      },
    }),
  );
};
