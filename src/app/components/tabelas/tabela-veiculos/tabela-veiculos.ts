import { Component, inject, OnInit } from '@angular/core';
import { VeiculoResponse } from '../../../model/responses/VeiculoResponse';
import { VeiculoService } from '../../../services/http/veiculo.service';
import { ModalService } from '../../../services/ui/modal.service';
import { ToastService } from '../../../services/ui/toast.service';
import { NgClass } from '@angular/common';
import { ToggleStatusModal } from '../../modais/toggle-status-modal/toggle-status-modal';
import { PrimaryAddButton } from '../../buttons/primary-add-button/primary-add-button';
import { SaveVeiculoModal } from '../../modais/save-modais/save-veiculo-modal/save-veiculo-modal';

@Component({
  selector: 'app-tabela-veiculos',
  imports: [NgClass, ToggleStatusModal, PrimaryAddButton, SaveVeiculoModal],
  templateUrl: './tabela-veiculos.html',
  styleUrl: './tabela-veiculos.css',
})
export class TabelaVeiculos implements OnInit {
  private modalService = inject(ModalService);
  private veiculoService = inject(VeiculoService);
  private toastService = inject(ToastService);

  veiculos = this.veiculoService.veiculos;
  veiculoSelecionado: VeiculoResponse | null = null;
  mostrarAtivos = true;

  ngOnInit() {
    this.veiculoService.buscaVeiculos();
  }

  abrirModal(veiculo: VeiculoResponse | null, modal: HTMLDialogElement) {
    if (veiculo) {
      this.veiculoSelecionado = veiculo;
    }
    this.modalService.abrirModal(modal);
  }

  fecharModal(modal: HTMLDialogElement) {
    this.modalService.fecharModal(modal);
    this.veiculoSelecionado = null;
  }

  toggleStatusCadastro(modal: HTMLDialogElement) {
    if (!this.veiculoSelecionado) return;

    this.veiculoService.toggleStatus(this.veiculoSelecionado.id).subscribe({
      next: () => {
        this.veiculoService.buscaVeiculos();
        this.toastService.abrir(
          'success',
          'Status do veículo atualizado com sucesso!',
        );
        this.fecharModal(modal);
      },
      error: () =>
        this.toastService.abrir('error', 'Erro ao atualizar status.'),
    });
  }

  toggleStatusLista() {
    this.mostrarAtivos = !this.mostrarAtivos;
  }

  filtraVeiculos(): VeiculoResponse[] {
    return this.veiculos().filter((v) => v.ativo === this.mostrarAtivos);
  }
}
