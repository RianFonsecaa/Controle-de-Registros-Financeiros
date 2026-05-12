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
  ],
  templateUrl: "./tabela-vales.html",
  styleUrl: "./tabela-vales.css",
})
export class TabelaVales implements OnInit {
  @ViewChild(ContainerFiltros) containerFiltros!: ContainerFiltros;

  private modalService = inject(ModalService);
  private valeService = inject(ValeService);
  private toastService = inject(ToastService);

  listaVales = this.valeService.listaVales;
  filtrosAtivos!: ValeQueryFilters;
  valeSelecionado: ValeResponse | null = null;
  enviando: boolean = false;

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

  ngOnInit() {
    this.valeService.buscaVales();
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

    this.valeService.buscaValePorFiltro(this.filtrosAtivos);
    console.log(this.filtrosAtivos);
  }

  deletarVale(modal: HTMLDialogElement) {
    if (!this.valeSelecionado) return;

    this.valeService.deletaVale(this.valeSelecionado.id).subscribe({
      next: () => {
        this.valeService.buscaVales();
        this.toastService.abrir(
          "success",
          "Registro de Vale apagado com sucesso!",
        );
      },
    });
    this.fecharModal(modal);
  }

  atualizaRegistros() {
    this.containerFiltros.removerTodosFiltros();
  }

  abrirModal(vale: ValeResponse | null, modal: HTMLDialogElement) {
    this.valeSelecionado = vale;
    this.modalService.abrirModal(modal);
  }

  fecharModal(modal: HTMLDialogElement) {
    this.modalService.fecharModal(modal);
    this.valeSelecionado = null;
  }

  salvarVale(valeData: any, modal: HTMLDialogElement) {
    this.enviando = true;

    const request: ValeRequest = {
      ...valeData,
      id: this.valeSelecionado?.id ?? null,
    };

    const operacao$ = this.valeSelecionado
      ? this.valeService.atualizarVale(request)
      : this.valeService.salvarVale(request);

    operacao$.subscribe({
      next: () => {
        const msg = this.valeSelecionado ? "atualizado" : "cadastrado";
        this.toastService.abrir(
          "success",
          `Registro de Vale ${msg} com sucesso!`,
        );
        this.valeService.buscaVales();
      },
      error: () => {
        this.enviando = false;
      },
      complete: () => (this.enviando = false),
    });

    this.fecharModal(modal);
  }
}
