import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { TokenStorageService } from "../services/auth/token-storage.service";
import { catchError, switchMap, throwError } from "rxjs";
import { AuthService } from "../services/auth/auth.service";
import { LoginResponse } from "../model/responses/LoginResponse";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenStorageService);
  const authService = inject(AuthService);

  const token = tokenService.getAccessToken();
  const publicRoutes = ["auth/login", "auth/refresh-token", "auth/register"];
  const isPublicRoute = publicRoutes.some((route) => req.url.includes(route));

  if (isPublicRoute) {
    return next(req);
  }

  let authRequest = req;
  if (token) {
    authRequest = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }

  return next(authRequest).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        const refreshToken = tokenService.getRefreshToken();

        if (refreshToken) {
          return authService.refreshToken(refreshToken).pipe(
            switchMap((response: LoginResponse) => {
              tokenService.setTokens(response);

              const retryRequest = req.clone({
                setHeaders: { Authorization: `Bearer ${response.accessToken}` },
              });

              return next(retryRequest);
            }),
            catchError((refreshError) => {
              tokenService.deleteTokens();
              authService.logout();
              return throwError(() => refreshError);
            }),
          );
        } else {
          tokenService.deleteTokens();
          authService.logout();
        }
      }

      return throwError(() => error);
    }),
  );
};
