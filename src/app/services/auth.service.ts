import { inject, Injectable } from '@angular/core';
import { LoginResponse } from '../model/responses/LoginResponse';
import { LoginRequest } from '../model/requests/LoginRequest';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../enviroments/enviroments';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly BASE_URL = environment.apiUrl;

  constructor(private http: HttpClient) {}

  login(credenciais: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.BASE_URL}/auth/login`,
      credenciais
    );
  }
}
