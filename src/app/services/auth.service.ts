import { inject, Injectable } from '@angular/core';
import { LoginResponse } from '../model/responses/LoginResponse';
import { LoginRequest } from '../model/requests/LoginRequest';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../enviroments/enviroments';
import { TokenStorageService } from './token-storage.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly BASE_URL = environment.apiUrl;
  tokenStorageService = inject(TokenStorageService);
  private router = inject(Router);

  constructor(private http: HttpClient) {}

  login(credenciais: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.BASE_URL}/auth/login`,
      credenciais
    );
  }

  isLoggedIn(): Observable<boolean> {
    return this.tokenStorageService.isLoggedIn();
  }

  logout() {
    this.tokenStorageService.deleteTokens();
    this.router.navigate(['/login']);
  }
}
