import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TokenPayloadResponse } from '../../model/responses/TokenPayloadResponse';
import { TokenStorageService } from '../../services/auth/token-storage.service';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-side-bar',
  imports: [RouterLink],
  templateUrl: './side-bar.html',
  styleUrl: './side-bar.css',
})
export class SideBar {
  private tokenStorage = inject(TokenStorageService);
  authService = inject(AuthService);

  usuario: TokenPayloadResponse | null = null;

  constructor() {
    this.carregarDadosUsuario();
  }

  carregarDadosUsuario() {
    this.usuario = this.tokenStorage.getPayload();
  }

  get isAdmin(): boolean {
    return this.usuario?.role === 'ADMIN';
  }
}
