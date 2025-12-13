import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TokenStorageService } from '../../services/token-storage.service';
import { TokenPayloadResponse } from '../../model/responses/TokenPayloadResponse';

@Component({
  selector: 'app-side-bar',
  imports: [RouterLink],
  templateUrl: './side-bar.html',
  styleUrl: './side-bar.css',
})
export class SideBar {
  private tokenStorage = inject(TokenStorageService);

  usuario: TokenPayloadResponse | null = null;

  constructor() {
    this.carregarDadosUsuario();
  }

  private carregarDadosUsuario() {
    const token = this.tokenStorage.getAccessToken();

    if (token) {
      this.usuario = this.tokenStorage.decodeToken(token);
      console.log(this.usuario);
    }
  }
}
