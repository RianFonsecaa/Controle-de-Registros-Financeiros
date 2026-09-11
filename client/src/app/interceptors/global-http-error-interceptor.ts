import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { ToastService } from "../services/ui/toast.service";
import { catchError, throwError } from "rxjs";
import { AuthService } from "../services/auth/auth.service";

export const globalHttpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const toastService = inject(ToastService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const isLoginRoute = req.url.includes("auth/login");

      if (error.status === 401 && !isLoginRoute) {
        return throwError(() => error);
      }

      if (error.status === 500) {
        authService.logout();
      }

      let mensagem = "Ocorreu um erro inesperado";

      if (error.error instanceof Blob) {
        if (error.status === 404) {
          mensagem = "Arquivo não encontrado no servidor.";
        }
      } else {
        mensagem =
          error.error?.message ||
          (isLoginRoute && error.status === 401
            ? "Login ou senha incorretos."
            : error.statusText) ||
          mensagem;
      }

      toastService.abrir("error", mensagem);

      return throwError(() => error);
    }),
  );
};
