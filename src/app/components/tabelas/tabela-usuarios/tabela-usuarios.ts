import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { UserResponse } from '../../../model/responses/UserResponse';
import { ModalService } from '../../../services/ui/modal.service';
import { ToastService } from '../../../services/ui/toast.service';
import { UsuariosService } from '../../../services/http/usuarios.service';
import { PrimaryAddButton } from '../../buttons/primary-add-button/primary-add-button';
import { SaveUserModal } from '../../modais/save-modais/save-user-modal/save-user-modal';
import { DeleteModal } from '../../modais/delete-modal/delete-modal';
import { ChangePasswordModal } from '../../modais/update-modais/change-password-modal/change-password-modal';
import { TokenStorageService } from '../../../services/auth/token-storage.service';
import { ToggleStatusModal } from '../../modais/toggle-status-modal/toggle-status-modal';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-tabela-usuarios',
  imports: [
    PrimaryAddButton,
    SaveUserModal,
    ChangePasswordModal,
    ToggleStatusModal,
    NgClass,
  ],
  templateUrl: './tabela-usuarios.html',
  styleUrl: './tabela-usuarios.css',
})
export class TabelaUsuarios implements OnInit {
  private usuariosService = inject(UsuariosService);
  private tokenStorageService = inject(TokenStorageService);
  private modalService = inject(ModalService);
  private toastService = inject(ToastService);

  mostrarAtivos = signal(true);
  usuarioSelecionado: UserResponse | null = null;

  listaExibida = computed(() => {
    return this.usuariosService
      .usuarios()
      .filter((u) => u.ativo === this.mostrarAtivos());
  });

  ngOnInit() {
    this.usuariosService.buscaUsuarios();
  }

  fecharModal(modal: HTMLDialogElement) {
    this.modalService.fecharModal(modal);
    this.usuarioSelecionado = null;
  }

  abrirModal(usuario: UserResponse | null, modal: HTMLDialogElement) {
    this.usuarioSelecionado = usuario;
    this.modalService.abrirModal(modal);
  }

  toggleStatusUsuario(modal: HTMLDialogElement) {
    if (!this.usuarioSelecionado) return;

    if (this.isUsuarioLogado(this.usuarioSelecionado.login)) {
      this.toastService.abrir(
        'error',
        'Não é possível inativar o seu próprio usuário!',
      );
      return;
    }

    this.usuariosService.toggleStatus(this.usuarioSelecionado.login).subscribe({
      next: () => {
        this.usuariosService.buscaUsuarios();
        this.toastService.abrir(
          'success',
          'Status do usuário alterado com sucesso!',
        );
      },
    });
    this.fecharModal(modal);
  }

  toggleStatusLista() {
    this.mostrarAtivos.update((status) => !status);
  }

  private isUsuarioLogado(login: string): boolean {
    const emailLogado = this.tokenStorageService.getPayload()?.email;
    return login === emailLogado;
  }
}
