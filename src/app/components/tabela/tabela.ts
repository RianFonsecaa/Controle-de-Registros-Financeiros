import { Component, ElementRef, inject, ViewChild } from '@angular/core';
import { CobrancaResponse } from '../../model/responses/CobrancaResponse';
import { CobrancaService } from '../../services/cobrancas.service';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { DeleteModal } from '../modais/delete-modal/delete-modal';
import { SaveCobrancaModal } from '../modais/save-cobranca-modal/save-cobranca-modal';
import { ModalService } from '../../services/modal.service';
import { GerarRelatorioModal } from '../modais/gerar-relatorio-modal/gerar-relatorio-modal';
import { RelatorioButton } from '../buttons/relatorio-button/relatorio-button';
import { ContainerFiltros } from '../container-filtros/container-filtros';
import { PrimaryAddButton } from '../buttons/primary-add-button/primary-add-button';
import { CobrancaQueryFilters } from '../../model/requests/CobrancaQueryFilters';

@Component({
  selector: 'app-tabela',
  imports: [
    CurrencyPipe,
    DatePipe,
    DeleteModal,
    SaveCobrancaModal,
    GerarRelatorioModal,
    PrimaryAddButton,
    RelatorioButton,
    ContainerFiltros,
    ContainerFiltros,
  ],
  templateUrl: './tabela.html',
  styleUrl: './tabela.css',
})
export class Tabela {
  private modalService = inject(ModalService);
  private cobrancaService = inject(CobrancaService);
  cobrancas = this.cobrancaService.cobrancas;

  cobrancaSelecionada: CobrancaResponse | null = null;

  ngOnInit() {
    this.cobrancaService.buscaCobrancas();
  }

  deletaCobranca(modal: HTMLDialogElement) {
    if (!this.cobrancaSelecionada) return;

    this.cobrancaService.deletaCobranca(this.cobrancaSelecionada.id).subscribe({
      next: () => {
        this.cobrancaService.buscaCobrancas();
        this.fecharModal(modal);
      },
    });
  }

  abrirModal(cobranca: CobrancaResponse | null, modal: HTMLDialogElement) {
    if (cobranca) {
      this.cobrancaSelecionada = cobranca;
    }
    this.modalService.abrirModal(modal);
  }

  fecharModal(modal: HTMLDialogElement) {
    this.cobrancaSelecionada = null;
    this.modalService.fecharModal(modal);
  }

  filtrarRegistros(filtros: CobrancaQueryFilters) {
    this.cobrancaService.buscaCobrancasPorFiltro(filtros);
  }
}
