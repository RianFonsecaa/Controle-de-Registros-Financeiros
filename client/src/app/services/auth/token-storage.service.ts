import { Injectable } from "@angular/core";
import { TokenPayloadResponse } from "../../model/responses/TokenPayloadResponse";
import { LoginResponse } from "../../model/responses/LoginResponse";

@Injectable({
  providedIn: "root",
})
export class TokenStorageService {
  private readonly accessTokenKey: string = "access-token";
  private readonly refreshTokenKey: string = "refresh-token";
  private cachedPayload: TokenPayloadResponse | null = null;

  setTokens(tokens: LoginResponse) {
    window.localStorage.setItem(this.accessTokenKey, tokens.accessToken);
    window.localStorage.setItem(this.refreshTokenKey, tokens.refreshToken);

    this.cachedPayload = null;
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

    this.cachedPayload = null;
  }

  decodeToken(token: string): any {
    const payload = token.split(".")[1];

    const base64 = payload.replace(/-/g, "+").replace(/_/g, "/");

    const binary = atob(base64);

    const bytes = Uint8Array.from(binary, (char) => char.charCodeAt(0));

    const decodedPayload = new TextDecoder("utf-8").decode(bytes);

    return JSON.parse(decodedPayload);
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

  isLoggedIn(): boolean {
    const token = this.getAccessToken();

    if (!token) return false;

    return !this.isTokenExpired(token);
  }
}
