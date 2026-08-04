import { Component, inject, OnInit, ViewChild } from "@angular/core";
import {
  ContainerFiltros,
  FiltroConfig,
} from "../../container-filtros/container-filtros";
import { ValeService } from "../../../services/http/vale.service";
import { ToastService } from "../../../services/ui/toast.service";
import { ModalService } from "../../../services/ui/modal.service";
import { ValeQueryFilters } from "../../../model/query-filters/ValeQueryFilters";
import { ValeResponse } from "../../../model/responses/ValeResponse";
import { FormControl, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { PeriodoFiltro } from "../../container-filtros/filtros/periodo-filtro/periodo-filtro";
import { ValorFiltro } from "../../container-filtros/filtros/valor-filtro/valor-filtro";
import { ValeRequest } from "../../../model/requests/ValeRequest";
import { CobradorFiltro } from "../../container-filtros/filtros/cobrador-filtro/cobrador-filtro";
import { ObservacoesFiltro } from "../../container-filtros/filtros/observacoes-filtro/observacoes-filtro";
import { SaveValeModal } from "../../modais/save-modais/save-vale-modal/save-vale-modal";
import { DeleteModal } from "../../modais/delete-modal/delete-modal";
import { UpdateButton } from "../../buttons/update-button/update-button";
import { DatePipe, CurrencyPipe } from "@angular/common";
import { PrimaryAddButton } from "../../buttons/primary-add-button/primary-add-button";
import { Paginacao } from "../../paginacao/paginacao";
import { BaseTabelaRegistroComponent } from "../../../model/base/BaseTabelaRegistroComponent";

@Component({
  selector: "app-tabela-vales",
  imports: [
    ReactiveFormsModule,
    SaveValeModal,
    DeleteModal,
    UpdateButton,
    CurrencyPipe,
    PrimaryAddButton,
    ContainerFiltros,
    Paginacao,
  ],
  templateUrl: "./tabela-vales.html",
  styleUrl: "./tabela-vales.css",
})
export class TabelaVales extends BaseTabelaRegistroComponent<
  ValeResponse,
  ValeQueryFilters
> {
  valeService = inject(ValeService);

  readonly nomeEntidade = "Vale";
  readonly serviceBase = this.valeService;
  vales = this.valeService.listaVales;

  filtroForm = new FormGroup({
    cobradorFiltro: new FormControl(null),
    valorInicioFiltro: new FormControl(null),
    valorFimFiltro: new FormControl(null),
    dataInicioFiltro: new FormControl(null),
    dataFimFiltro: new FormControl(null),
    observacoesFiltro: new FormControl(null),
  });

  configuracaoFiltros: FiltroConfig[] = [
    {
      key: "cobradorFiltro",
      visivel: false,
      componente: CobradorFiltro,
      label: "Cobrador",
    },
    {
      key: "observacoesFiltro",
      visivel: false,
      componente: ObservacoesFiltro,
      label: "Observações",
    },
    {
      key: "periodoFiltro",
      visivel: false,
      componente: PeriodoFiltro,
      label: "Período",
      controlsParaResetar: ["dataInicioFiltro", "dataFimFiltro"],
    },
    {
      key: "valorFiltro",
      visivel: false,
      componente: ValorFiltro,
      label: "Valor",
      controlsParaResetar: ["valorInicioFiltro", "valorFimFiltro"],
    },
  ];

  carregarDados() {
    this.valeService.buscaVales();
  }
  executarBuscaFiltro(filtros: ValeQueryFilters) {
    this.valeService.buscaValePorFiltro(filtros);
  }

  filtrarRegistros(formValue: any) {
    this.filtrosAtivos = {
      cobradorId: formValue.cobradorFiltro?.id ?? null,
      dataInicio: formValue.dataInicioFiltro,
      dataFim: formValue.dataFimFiltro,
      valorInicio: formValue.valorInicioFiltro,
      valorFim: formValue.valorFimFiltro,
      observacoes: formValue.observacoesFiltro,
    };
    this.valeService.paginaAtual.set(0);
    this.executarBuscaFiltro(this.filtrosAtivos);
  }

  deletarRegistro(modal: HTMLDialogElement) {
    if (!this.itemSelecionado) return;
    this.valeService.deletaVale(this.itemSelecionado.id).subscribe({
      next: () => {
        this.carregarDados();
        this.toastService.abrir(
          "success",
          "Registro de Vale apagado com sucesso!",
        );
        this.fecharModal(modal);
      },
    });
  }

  salvarVale(valeData: any, modal: HTMLDialogElement) {
    this.enviando = true;
    const request = { ...valeData, id: this.itemSelecionado?.id ?? null };

    const op$ = this.itemSelecionado
      ? this.valeService.atualizarVale(request)
      : this.valeService.salvarVale(request);

    op$.subscribe({
      next: () => {
        const msg = this.itemSelecionado ? "atualizado" : "cadastrado";
        this.toastService.abrir(
          "success",
          `Registro de Vale ${msg} com sucesso!`,
        );
        this.carregarDados();
        this.fecharModal(modal);
      },
      complete: () => (this.enviando = false),
    });
  }
}
