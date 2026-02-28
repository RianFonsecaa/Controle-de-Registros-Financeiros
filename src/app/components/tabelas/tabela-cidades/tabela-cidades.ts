import { Component, inject } from '@angular/core';
import { PrimaryAddButton } from '../../buttons/primary-add-button/primary-add-button';
import { CidadesService } from '../../../services/http/cidades.service';
import { ModalService } from '../../../services/ui/modal.service';
import { ToastService } from '../../../services/ui/toast.service';
import { CidadeResponse } from '../../../model/responses/CidadeResponse';
import { DeleteModal } from '../../modais/delete-modal/delete-modal';
import { SaveCidadeModal } from '../../modais/save-modais/save-cidade-modal/save-cidade-modal';
import { NgClass } from '@angular/common';
import { ToggleStatusModal } from '../../modais/toggle-status-modal/toggle-status-modal';

@Component({
  selector: 'app-tabela-cidades',
  imports: [PrimaryAddButton, SaveCidadeModal, NgClass, ToggleStatusModal],
  templateUrl: './tabela-cidades.html',
  styleUrl: './tabela-cidades.css',
})
export class TabelaCidades {
  private modalService = inject(ModalService);
  private cidadesService = inject(CidadesService);
  private toastService = inject(ToastService);

  cidades = this.cidadesService.cidades;
  cidadeSelecionada: CidadeResponse | null = null;
  mostrarAtivos = true;

  ngOnInit() {
    this.cidadesService.buscaCidades();
  }

  abrirModal(cidade: CidadeResponse | null, modal: HTMLDialogElement) {
    if (cidade) {
      this.cidadeSelecionada = cidade;
    }

    this.modalService.abrirModal(modal);
  }

  fecharModal(modal: HTMLDialogElement) {
    this.modalService.fecharModal(modal);
    this.cidadeSelecionada = null;
  }

  toggleStatusCadastro(modal: HTMLDialogElement) {
    if (!this.cidadeSelecionada) return;
    this.cidadesService.toggleStatus(this.cidadeSelecionada.id).subscribe({
      next: () => {
        this.cidadesService.buscaCidades();
        this.toastService.abrir(
          'success',
          'Registro de cobrança foi apagado com sucesso!',
        );
        this.modalService.fecharModal(modal);
      },
    });
  }

  toggleStatusLista() {
    this.mostrarAtivos = !this.mostrarAtivos;
  }

  filtraCidades(): CidadeResponse[] {
    return this.cidades().filter(
      (cidade) => cidade.ativo === this.mostrarAtivos,
    );
  }
}
