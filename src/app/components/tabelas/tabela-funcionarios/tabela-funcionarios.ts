import {
  Component,
  computed,
  effect,
  inject,
  OnInit,
  signal,
} from '@angular/core';
import { FuncionarioResponse } from '../../../model/responses/FuncionarioResponse';
import { FuncionarioService } from '../../../services/http/funcionario.service';
import { ModalService } from '../../../services/ui/modal.service';
import { ToastService } from '../../../services/ui/toast.service';
import { NgClass } from '@angular/common';
import { PrimaryAddButton } from '../../buttons/primary-add-button/primary-add-button';
import { ToggleStatusModal } from '../../modais/toggle-status-modal/toggle-status-modal';
import { SaveFuncionarioModal } from '../../modais/save-modais/save-funcionario-modal/save-funcionario-modal';

@Component({
  selector: 'app-tabela-funcionarios',
  imports: [NgClass, PrimaryAddButton, ToggleStatusModal, SaveFuncionarioModal],
  templateUrl: './tabela-funcionarios.html',
  styleUrl: './tabela-funcionarios.css',
})
export class TabelaFuncionarios implements OnInit {
  private modalService = inject(ModalService);
  private funcionarioService = inject(FuncionarioService);
  private toastService = inject(ToastService);

  funcionarioSelecionado: FuncionarioResponse | null = null;
  mostrarAtivos = signal(true);

  listaExibida = computed(() => {
    return this.funcionarioService
      .funcionarios()
      .filter((f) => f.ativo === this.mostrarAtivos());
  });

  ngOnInit() {
    this.funcionarioService.buscaFuncionarios();
  }

  abrirModal(
    funcionario: FuncionarioResponse | null,
    modal: HTMLDialogElement,
  ) {
    if (funcionario) {
      this.funcionarioSelecionado = funcionario;
    }
    this.modalService.abrirModal(modal);
  }

  fecharModal(modal: HTMLDialogElement) {
    this.modalService.fecharModal(modal);
    this.funcionarioSelecionado = null;
  }

  toggleStatusCadastro(modal: HTMLDialogElement) {
    if (!this.funcionarioSelecionado) return;

    this.funcionarioService
      .toggleStatus(this.funcionarioSelecionado.id)
      .subscribe({
        next: () => {
          this.funcionarioService.buscaFuncionarios();
          this.toastService.abrir(
            'success',
            'Status do funcionário atualizado com sucesso!',
          );
        },
        error: () => {
          this.toastService.abrir('error', 'Erro ao atualizar status.');
        },
      });
    this.fecharModal(modal);
  }

  toggleStatusLista() {
    this.mostrarAtivos.update((status) => !status);
  }
}
