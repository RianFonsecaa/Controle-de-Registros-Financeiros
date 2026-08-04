import { Component, inject, OnInit, ViewChild } from "@angular/core";
import { UpdateButton } from "../../buttons/update-button/update-button";
import { PixResponse } from "../../../model/responses/PixResponse";
import {
  ContainerFiltros,
  FiltroConfig,
} from "../../container-filtros/container-filtros";
import { CidadeFiltro } from "../../container-filtros/filtros/cidade-filtro/cidade-filtro";
import { PeriodoFiltro } from "../../container-filtros/filtros/periodo-filtro/periodo-filtro";
import { ValorFiltro } from "../../container-filtros/filtros/valor-filtro/valor-filtro";
import { FormControl, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { PixService } from "../../../services/http/pix.service";
import { ModalService } from "../../../services/ui/modal.service";
import { ToastService } from "../../../services/ui/toast.service";
import { SavePixModal } from "../../modais/save-modais/save-pix-modal/save-pix-modal";
import { DeleteModal } from "../../modais/delete-modal/delete-modal";
import { CurrencyPipe, DatePipe } from "@angular/common";
import { PrimaryAddButton } from "../../buttons/primary-add-button/primary-add-button";
import { PixRequest } from "../../../model/requests/PixRequest";
import { ClienteFiltro } from "../../container-filtros/filtros/cliente-filtro/cliente-filtro";
import { PixQueryFilters } from "../../../model/query-filters/PixQueryFilters";
import { Paginacao } from "../../paginacao/paginacao";
import { CobrancaFiltro } from "../../container-filtros/filtros/cobranca-filtro/cobranca-filtro";
import { BaseTabelaRegistroComponent } from "../../../model/base/BaseTabelaRegistroComponent";

@Component({
  selector: "app-tabela-pix",
  imports: [
    ReactiveFormsModule,
    ContainerFiltros,
    SavePixModal,
    DeleteModal,
    UpdateButton,
    CurrencyPipe,
    PrimaryAddButton,
    Paginacao,
  ],
  templateUrl: "./tabela-pix.html",
  styleUrl: "./tabela-pix.css",
})
export class TabelaPix extends BaseTabelaRegistroComponent<
  PixResponse,
  PixQueryFilters
> {
  pixService = inject(PixService);

  readonly nomeEntidade = "Pix";
  readonly serviceBase = this.pixService;
  pixs = this.pixService.listaPixs;

  filtroForm = new FormGroup({
    clienteFiltro: new FormControl(null),
    cidadeFiltro: new FormControl(null),
    cobrancaFiltro: new FormControl(null),
    valorInicioFiltro: new FormControl(null),
    valorFimFiltro: new FormControl(null),
    dataInicioFiltro: new FormControl(null),
    dataFimFiltro: new FormControl(null),
  });

  configuracaoFiltros: FiltroConfig[] = [
    {
      key: "clienteFiltro",
      visivel: false,
      componente: ClienteFiltro,
      label: "Cliente",
    },
    {
      key: "cidadeFiltro",
      visivel: false,
      componente: CidadeFiltro,
      label: "Cidade",
    },
    {
      key: "cobrancaFiltro",
      visivel: false,
      componente: CobrancaFiltro,
      label: "Cobranca",
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
    this.pixService.buscaPixs();
  }
  executarBuscaFiltro(filtros: PixQueryFilters) {
    this.pixService.buscaPixPorFiltro(filtros);
  }

  filtrarRegistros(formValue: any) {
    this.filtrosAtivos = {
      cliente: formValue.clienteFiltro,
      cidadeId: formValue.cidadeFiltro?.id ?? null,
      cobrancaId: formValue.cobrancaFiltro,
      dataInicio: formValue.dataInicioFiltro,
      dataFim: formValue.dataFimFiltro,
      valorInicio: formValue.valorInicioFiltro,
      valorFim: formValue.valorFimFiltro,
    };
    this.pixService.paginaAtual.set(0);
    this.executarBuscaFiltro(this.filtrosAtivos);
  }

  deletarRegistro(modal: HTMLDialogElement) {
    if (!this.itemSelecionado) return;
    this.pixService.deletaPix(this.itemSelecionado.id).subscribe({
      next: () => {
        this.carregarDados();
        this.toastService.abrir(
          "success",
          "Registro de Pix apagado com sucesso!",
        );
        this.fecharModal(modal);
      },
    });
  }

  salvarPix(pixData: any, modal: HTMLDialogElement) {
    const { comprovante, ...dados } = pixData;
    const request = { ...dados, id: this.itemSelecionado?.id || null };
    this.enviando = true;

    const op$ = this.itemSelecionado
      ? this.pixService.atualizarPix(request, comprovante)
      : this.pixService.salvarPix(request, comprovante);

    op$.subscribe({
      next: () => {
        const msg = this.itemSelecionado ? "atualizado" : "cadastrado";
        this.toastService.abrir(
          "success",
          `Registro de Pix ${msg} com sucesso!`,
        );
        this.carregarDados();
        this.fecharModal(modal);
      },
      complete: () => (this.enviando = false),
    });
  }

  visualizarComprovante(nomeArquivo: string) {
    this.pixService.getComprovante(nomeArquivo).subscribe({
      next: (blob) => {
        const file = new Blob([blob], { type: blob.type });
        const url = window.URL.createObjectURL(file);
        const novaGuia = window.open(url, "_blank");

        if (
          !novaGuia ||
          novaGuia.closed ||
          typeof novaGuia.closed === "undefined"
        ) {
          alert(
            "O navegador bloqueou a abertura do comprovante. Por favor, permita pop-ups para este site.",
          );
        }
      },
      error: (err) => {
        console.error("Erro ao buscar comprovante", err);
      },
    });
  }
}
