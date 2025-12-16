import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { TokenStorageService } from '../../services/token-storage.service';
import { TokenPayloadResponse } from '../../model/responses/TokenPayloadResponse';
import { AuthService } from '../../services/auth.service';

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
    const token = this.tokenStorage.getAccessToken();

    if (token) {
      this.usuario = this.tokenStorage.decodeToken(token);
      console.log(this.usuario);
    }
  }
}
