import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs';

import { TokenStorageService } from './token-storage.service';
import { Router } from '@angular/router';
import { environment } from '../../../enviroments/enviroments';
import { LoginResponse } from '../../model/responses/LoginResponse';
import { LoginRequest } from '../../model/requests/LoginRequest';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly BASE_URL = environment.apiUrl;
  private router = inject(Router);
  private http = inject(HttpClient);
  private tokenStorageService = inject(TokenStorageService);

  login(credenciais: LoginRequest): Observable<void> {
    return this.http
      .post<LoginResponse>(`${this.BASE_URL}/auth/login`, credenciais)
      .pipe(
        tap((response) => this.tokenStorageService.setTokens(response)),
        map(() => void 0),
      );
  }

  refreshToken(): Observable<LoginResponse> {
    const refreshToken = this.tokenStorageService.getRefreshToken();

    return this.http
      .post<LoginResponse>(`${this.BASE_URL}/auth/refresh-token`, {
        refreshToken,
      })
      .pipe(tap((response) => this.tokenStorageService.setTokens(response)));
  }

  isLoggedIn(): boolean {
    return this.tokenStorageService.isLoggedIn();
  }

  logout() {
    this.tokenStorageService.deleteTokens();
    this.router.navigate(['/login']);
  }
}
