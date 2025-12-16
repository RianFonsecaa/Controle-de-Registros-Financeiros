import { Injectable } from '@angular/core';
import { LoginResponse } from '../model/responses/LoginResponse';
import { Observable } from 'rxjs/internal/Observable';
import { of } from 'rxjs';
import { TokenPayloadResponse } from '../model/responses/TokenPayloadResponse';

@Injectable({
  providedIn: 'root',
})
export class TokenStorageService {
  private readonly accessTokenKey: string = 'access-token';
  private readonly refreshTokenKey: string = 'refresh-token';
  private cachedPayload: TokenPayloadResponse | null = null;

  setTokens(tokens: LoginResponse) {
    window.localStorage.setItem(this.accessTokenKey, tokens.accessToken);
    window.localStorage.setItem(this.refreshTokenKey, tokens.refreshToken);
  }

  getAccessToken(): string | null {
    return localStorage.getItem(this.accessTokenKey);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.refreshTokenKey);
  }

  deleteTokens() {
    localStorage.removeItem(this.accessTokenKey);
    localStorage.removeItem(this.refreshTokenKey);
  }

  decodeToken(token: string): any {
    const payload = token.split('.')[1];
    return JSON.parse(atob(payload));
  }

  getPayload(): TokenPayloadResponse | null {
    if (this.cachedPayload) {
      return this.cachedPayload;
    }

    const token = this.getAccessToken();
    if (!token) return null;

    this.cachedPayload = this.decodeToken(token);
    return this.cachedPayload;
  }

  private isTokenExpired(token: string): boolean {
    const payload = this.decodeToken(token);
    const expiration = payload.exp * 1000;
    return Date.now() > expiration;
  }

  isLoggedIn(): Observable<boolean> {
    const token = this.getAccessToken();

    if (!token) return of(false);

    if (this.isTokenExpired(token)) {
      this.deleteTokens();
      return of(false);
    }

    return of(true);
  }
}
