import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenStorageService } from '../services/auth/token-storage.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(TokenStorageService);
  const token = auth.getAccessToken();

  const publicRoutes = ['/auth/login', '/auth/refresh', '/auth/register'];

  const isPublicRoute = publicRoutes.some((route) => req.url.includes(route));

  if (isPublicRoute) return next(req);

  const authRequest = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(authRequest);
};
