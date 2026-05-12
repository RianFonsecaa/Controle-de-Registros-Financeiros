import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { ToastService } from "../services/ui/toast.service";
import { catchError, tap, throwError } from "rxjs";
import { Router } from "@angular/router";
import { AuthService } from "../services/auth/auth.service";

export const globalHttpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastService = inject(ToastService);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        return throwError(() => error);
      }

      if ([500].includes(error.status)) {
        authService.logout();
      }

      let mensagem = "Ocorreu um erro inesperado";

      if (error.error instanceof Blob) {
        if (error.status === 404)
          mensagem = "Arquivo não encontrado no servidor.";
      } else {
        mensagem = error.error?.message || error.statusText || mensagem;
      }

      toastService.abrir("error", mensagem);

      return throwError(() => error);
    }),
  );
};
