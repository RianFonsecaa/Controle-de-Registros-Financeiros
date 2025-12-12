import { Injectable } from '@angular/core';
import { LoginResponse } from '../model/responses/LoginResponse';
import { Observable } from 'rxjs/internal/Observable';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TokenStorageService {
  private readonly accessTokenKey: string = 'acess-token';
  private readonly refreshTokenKey: string = 'refresh-token';

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

  logout() {
    localStorage.removeItem(this.accessTokenKey);
    localStorage.removeItem(this.refreshTokenKey);
  }

  isLoggedIn(): Observable<boolean> {
    return of(!!localStorage.getItem('token'));
  }
}
