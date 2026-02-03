import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenStorageService } from '../services/auth/token-storage.service';

export const roleGuard: CanActivateFn = (route) => {
  const tokenStorage = inject(TokenStorageService);
  const router = inject(Router);

  const payload = tokenStorage.getPayload();
  const allowedRoles = route.data?.['roles'] as string[];

  if (!payload) {
    return router.createUrlTree(['/login']);
  }

  if (!allowedRoles || !allowedRoles.includes(payload.role)) {
    return router.createUrlTree(['/dashboard']);
  }

  return true;
};
