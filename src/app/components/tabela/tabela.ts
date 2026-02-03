import { Component, ElementRef, inject, ViewChild } from '@angular/core';
import { CobrancaResponse } from '../../model/responses/CobrancaResponse';
import { CobrancaService } from '../../services/http/cobrancas.service';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { DeleteModal } from '../modais/delete-modal/delete-modal';
import { SaveCobrancaModal } from '../modais/save-cobranca-modal/save-cobranca-modal';
import { ModalService } from '../../services/ui/modal.service';
import { RelatorioButton } from '../buttons/relatorio-button/relatorio-button';
import { ContainerFiltros } from '../container-filtros/container-filtros';
import { PrimaryAddButton } from '../buttons/primary-add-button/primary-add-button';
import { CobrancaQueryFilters } from '../../model/requests/CobrancaQueryFilters';
import { UpdateButton } from '../buttons/update-button/update-button';
import { UpdateModal } from '../modais/update-modal/update-modal';
import { LoadingModal } from '../modais/loading-modal/loading-modal';
import { RelatorioService } from '../../services/http/relatorio.service';
import { ToastService } from '../../services/ui/toast.service';

@Component({
  selector: 'app-tabela',
  imports: [
    CurrencyPipe,
    DatePipe,
    DeleteModal,
    SaveCobrancaModal,
    PrimaryAddButton,
    RelatorioButton,
    ContainerFiltros,
    ContainerFiltros,
    UpdateButton,
    UpdateModal,
    LoadingModal,
  ],
  templateUrl: './tabela.html',
  styleUrl: './tabela.css',
})
export class Tabela {
  @ViewChild('loadingModal') loadingModal!: ElementRef<HTMLDialogElement>;
  @ViewChild(ContainerFiltros) containerFiltros!: ContainerFiltros;
  @ViewChild(SaveCobrancaModal) saveCobrancaModal!: SaveCobrancaModal;
  private modalService = inject(ModalService);
  private cobrancaService = inject(CobrancaService);
  private relatorioService = inject(RelatorioService);

  toastService = inject(ToastService);

  cobrancas = this.cobrancaService.cobrancas;
  filtros!: CobrancaQueryFilters;

  exibirInfoToast: boolean = false;
  mensagemToast: string = '';

  cobrancaSelecionada: CobrancaResponse | null = null;

  ngOnInit() {
    this.cobrancaService.buscaCobrancas();
  }

  atualizaRegistros() {
    this.containerFiltros.removerTodosFiltros();
  }

  deletaCobranca(modal: HTMLDialogElement) {
    if (!this.cobrancaSelecionada) return;

    this.cobrancaService.deletaCobranca(this.cobrancaSelecionada.id).subscribe({
      next: () => {
        this.cobrancaService.buscaCobrancas();
        this.fecharModal(modal);
        this.toastService.open(
          'success',
          'Registro de cobrança foi apagado com sucesso!',
        );
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
    this.modalService.fecharModal(modal);
    this.cobrancaSelecionada = null;
  }

  filtrarRegistros(filtros: CobrancaQueryFilters) {
    this.filtros = filtros;
    this.cobrancaService.buscaCobrancasPorFiltro(filtros);
  }

  geraRelatorio() {
    if (!this.possuiFiltros() || this.cobrancas().length === 0) {
      this.toastService.open(
        'error',
        'Nenhum filtro foi preenchido ou a tabela está vazia',
      );
      return;
    }

    this.modalService.abrirModal(this.loadingModal.nativeElement);

    this.relatorioService.geraRelatorioDeCobrancas(this.filtros).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        window.open(url, '_blank');
      },
      error: (err) => {
        this.modalService.fecharModal(this.loadingModal.nativeElement);
      },
      complete: () => {
        this.modalService.fecharModal(this.loadingModal.nativeElement);
      },
    });
  }

  private possuiFiltros(): boolean {
    return (
      this.filtros &&
      Object.values(this.filtros).some(
        (valor) => valor !== null && valor !== undefined && valor !== '',
      )
    );
  }
}
