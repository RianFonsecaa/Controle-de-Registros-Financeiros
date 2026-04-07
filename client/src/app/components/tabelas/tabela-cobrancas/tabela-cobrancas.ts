import { Component, ElementRef, inject, ViewChild } from '@angular/core';
import { CobrancaResponse } from '../../../model/responses/CobrancaResponse';
import { CobrancaService } from '../../../services/http/cobrancas.service';
import { CurrencyPipe, DatePipe } from '@angular/common';

import { ModalService } from '../../../services/ui/modal.service';
import { RelatorioButton } from '../../buttons/relatorio-button/relatorio-button';
import {
  ContainerFiltros,
  FiltroConfig,
} from '../../container-filtros/container-filtros';
import { PrimaryAddButton } from '../../buttons/primary-add-button/primary-add-button';
import { UpdateButton } from '../../buttons/update-button/update-button';
import { LoadingModal } from '../../modais/loading-modal/loading-modal';
import { RelatorioService } from '../../../services/http/relatorio.service';
import { ToastService } from '../../../services/ui/toast.service';
import { SaveCobrancaModal } from '../../modais/save-modais/save-cobranca-modal/save-cobranca-modal';
import { DeleteModal } from '../../modais/delete-modal/delete-modal';
import { UpdateCobrancaModal } from '../../modais/update-modais/update-cobranca-modal/update-cobranca-modal';
import { FormGroup, FormControl } from '@angular/forms';
import { CidadeFiltro } from '../../container-filtros/filtros/cidade-filtro/cidade-filtro';
import { CobradorFiltro } from '../../container-filtros/filtros/cobrador-filtro/cobrador-filtro';
import { ObservacoesFiltro } from '../../container-filtros/filtros/observacoes-filtro/observacoes-filtro';
import { PeriodoFiltro } from '../../container-filtros/filtros/periodo-filtro/periodo-filtro';
import { RegistranteFiltro } from '../../container-filtros/filtros/registrante-filtro/registrante-filtro';
import { ValorFiltro } from '../../container-filtros/filtros/valor-filtro/valor-filtro';
import { CobrancaQueryFilters } from '../../../model/query-filters/CobrancaQueryFilters';

@Component({
  selector: 'app-tabela-cobrancas',
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
    UpdateCobrancaModal,
    LoadingModal,
  ],
  templateUrl: './tabela-cobrancas.html',
  styleUrl: './tabela-cobrancas.css',
})
export class TabelaCobrancas {
  @ViewChild('loadingModal') loadingModal!: ElementRef<HTMLDialogElement>;
  @ViewChild(ContainerFiltros) containerFiltros!: ContainerFiltros;
  private modalService = inject(ModalService);
  private cobrancaService = inject(CobrancaService);
  private relatorioService = inject(RelatorioService);
  private toastService = inject(ToastService);

  cobrancas = this.cobrancaService.cobrancas;
  filtrosAtivos!: CobrancaQueryFilters;
  cobrancaSelecionada: CobrancaResponse | null = null;

  filtroForm = new FormGroup({
    cidadeFiltro: new FormControl(null),
    cobradorFiltro: new FormControl(null),
    observacoesFiltro: new FormControl(null),
    registranteFiltro: new FormControl(null),
    valorInicioFiltro: new FormControl(null),
    valorFimFiltro: new FormControl(null),
    dataInicioFiltro: new FormControl(null),
    dataFimFiltro: new FormControl(null),
  });

  configuracaoFiltros: FiltroConfig[] = [
    {
      key: 'cidadeFiltro',
      visivel: false,
      componente: CidadeFiltro,
      label: 'Cidade',
    },
    {
      key: 'cobradorFiltro',
      visivel: false,
      componente: CobradorFiltro,
      label: 'Cobrador',
    },
    {
      key: 'registranteFiltro',
      visivel: false,
      componente: RegistranteFiltro,
      label: 'Registrante',
    },
    {
      key: 'observacoesFiltro',
      visivel: false,
      componente: ObservacoesFiltro,
      label: 'Observações',
    },
    {
      key: 'periodoFiltro',
      visivel: false,
      componente: PeriodoFiltro,
      label: 'Período',
      controlsParaResetar: ['dataInicioFiltro', 'dataFimFiltro'],
    },
    {
      key: 'valorFiltro',
      visivel: false,
      componente: ValorFiltro,
      label: 'Valor Total',
      controlsParaResetar: ['valorInicioFiltro', 'valorFimFiltro'],
    },
  ];

  ngOnInit() {
    this.cobrancaService.buscaCobrancas();
  }

  deletarCobranca(modal: HTMLDialogElement) {
    if (!this.cobrancaSelecionada) return;
    this.cobrancaService.deletaCobranca(this.cobrancaSelecionada.id).subscribe({
      next: () => {
        this.cobrancaService.buscaCobrancas();
        this.toastService.abrir(
          'success',
          'Registro de cobrança foi apagado com sucesso!',
        );
        this.modalService.fecharModal(modal);
      },
    });
  }

  filtrarRegistros(formValue: any) {
    this.filtrosAtivos = {
      cidadeId: formValue.cidadeFiltro?.id ?? null,
      cobradorId: formValue.cobradorFiltro?.id ?? null,
      observacoes: formValue.observacoesFiltro,
      registranteLogin: formValue.registranteFiltro?.login ?? null,
      dataInicio: formValue.dataInicioFiltro,
      dataFim: formValue.dataFimFiltro,
      valorInicio: formValue.valorInicioFiltro,
      valorFim: formValue.valorFimFiltro,
    };

    console.log(this.filtrosAtivos);

    this.cobrancaService.buscaCobrancasPorFiltro(this.filtrosAtivos);
  }

  atualizaRegistros() {
    this.containerFiltros.removerTodosFiltros();
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

  geraRelatorio() {
    if (!this.possuiFiltros() || this.cobrancas().length === 0) {
      this.toastService.abrir(
        'error',
        'Nenhum filtro preenchido ou tabela vazia',
      );
      return;
    }

    this.modalService.abrirModal(this.loadingModal.nativeElement);
    this.relatorioService
      .geraRelatorioDeCobrancas(this.filtrosAtivos)
      .subscribe({
        next: (blob) => window.open(window.URL.createObjectURL(blob), '_blank'),
        error: () =>
          this.modalService.fecharModal(this.loadingModal.nativeElement),
        complete: () =>
          this.modalService.fecharModal(this.loadingModal.nativeElement),
      });
  }

  private possuiFiltros(): boolean {
    return (
      this.filtrosAtivos &&
      Object.values(this.filtrosAtivos).some((v) => v !== null && v !== '')
    );
  }
}
