import { Component, computed, inject, OnInit } from '@angular/core';
import { UserResponse } from '../../../model/responses/UserResponse';
import { ModalService } from '../../../services/ui/modal.service';
import { ToastService } from '../../../services/ui/toast.service';
import { UsuariosService } from '../../../services/http/usuarios.service';
import { PrimaryAddButton } from '../../buttons/primary-add-button/primary-add-button';
import { SaveUserModal } from '../../modais/save-modais/save-user-modal/save-user-modal';
import { ToggleStatusModal } from '../../modais/toggle-status-modal/toggle-status-modal';
import { DeleteModal } from '../../modais/delete-modal/delete-modal';
import { ChangePasswordModal } from '../../modais/update-modais/change-password-modal/change-password-modal';

@Component({
  selector: 'app-tabela-usuarios',
  imports: [PrimaryAddButton, SaveUserModal, DeleteModal, ChangePasswordModal],
  templateUrl: './tabela-usuarios.html',
  styleUrl: './tabela-usuarios.css',
})
export class TabelaUsuarios implements OnInit {
  private usuariosService = inject(UsuariosService);
  private modalService = inject(ModalService);
  private toastService = inject(ToastService);

  usuarioSelecionado: UserResponse | null = null;

  listaExibida = computed(() => this.usuariosService.usuarios());

  ngOnInit() {
    this.usuariosService.buscaUsuarios();
  }

  abrirModal(usuario: UserResponse | null, modal: HTMLDialogElement) {
    this.usuarioSelecionado = usuario;
    this.modalService.abrirModal(modal);
  }

  fecharModal(modal: HTMLDialogElement) {
    this.modalService.fecharModal(modal);
    this.usuarioSelecionado = null;
  }

  confirmarExclusao(modal: HTMLDialogElement) {
    if (!this.usuarioSelecionado) return;

    this.usuariosService
      .excluiUsuario(this.usuarioSelecionado.login)
      .subscribe({
        next: () => {
          this.toastService.abrir('success', 'Usuário removido com sucesso!');
          this.fecharModal(modal);
        },
        error: () =>
          this.toastService.abrir('error', 'Erro ao remover usuário.'),
      });
  }
}
