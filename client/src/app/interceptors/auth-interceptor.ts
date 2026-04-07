import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenStorageService } from '../services/auth/token-storage.service';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth/auth.service';
import { LoginResponse } from '../model/responses/LoginResponse';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenStorageService);
  const authService = inject(AuthService);

  const token = tokenService.getAccessToken();

  const publicRoutes = ['/auth/login', '/auth/refresh-token', '/auth/register'];
  const isPublicRoute = publicRoutes.some((route) => req.url.includes(route));

  // ignora rotas públicas ou ausência de token
  if (isPublicRoute || !token) return next(req);

  const authRequest = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(authRequest).pipe(
    catchError((error) => {
      if (error.status === 401) {
        return authService.refreshToken().pipe(
          switchMap((response: LoginResponse) => {
            const retryRequest = req.clone({
              setHeaders: {
                Authorization: `Bearer ${response.accessToken}`,
              },
            });

            return next(retryRequest);
          }),
          catchError((err) => {
            authService.logout();
            return throwError(() => err);
          }),
        );
      }

      return throwError(() => error);
    }),
  );
};
