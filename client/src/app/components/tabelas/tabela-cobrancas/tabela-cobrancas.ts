import { Component, ElementRef, inject, ViewChild } from "@angular/core";
import { CobrancaResponse } from "../../../model/responses/CobrancaResponse";
import { CobrancaService } from "../../../services/http/cobrancas.service";
import { CurrencyPipe, DatePipe } from "@angular/common";

import { ModalService } from "../../../services/ui/modal.service";
import { RelatorioButton } from "../../buttons/relatorio-button/relatorio-button";
import {
  ContainerFiltros,
  FiltroConfig,
} from "../../container-filtros/container-filtros";
import { PrimaryAddButton } from "../../buttons/primary-add-button/primary-add-button";
import { UpdateButton } from "../../buttons/update-button/update-button";
import { LoadingModal } from "../../modais/loading-modal/loading-modal";
import { RelatorioService } from "../../../services/http/relatorio.service";
import { ToastService } from "../../../services/ui/toast.service";
import { SaveCobrancaModal } from "../../modais/save-modais/save-cobranca-modal/save-cobranca-modal";
import { DeleteModal } from "../../modais/delete-modal/delete-modal";
import { UpdateCobrancaModal } from "../../modais/update-modais/update-cobranca-modal/update-cobranca-modal";
import { FormGroup, FormControl } from "@angular/forms";
import { CidadeFiltro } from "../../container-filtros/filtros/cidade-filtro/cidade-filtro";
import { CobradorFiltro } from "../../container-filtros/filtros/cobrador-filtro/cobrador-filtro";
import { ObservacoesFiltro } from "../../container-filtros/filtros/observacoes-filtro/observacoes-filtro";
import { PeriodoFiltro } from "../../container-filtros/filtros/periodo-filtro/periodo-filtro";
import { RegistranteFiltro } from "../../container-filtros/filtros/registrante-filtro/registrante-filtro";
import { ValorFiltro } from "../../container-filtros/filtros/valor-filtro/valor-filtro";
import { CobrancaQueryFilters } from "../../../model/query-filters/CobrancaQueryFilters";
import { WhatsappButton } from "../../buttons/whatsapp-button/whatsapp-button";
import { SelectUserModal } from "../../modais/select-user-modal/select-user-modal";
import { EnviaRelatorioWppRequest } from "../../../model/requests/EnviaRelatorioWppRequest";
import { WhatsappService } from "../../../services/http/whatsapp.service";
import { PageResponse } from "../../../model/responses/PageResponse";
import { Paginacao } from "../../paginacao/paginacao";
import { BaseTabelaRegistroComponent } from "../../../model/base/BaseTabelaRegistroComponent";

@Component({
  selector: "app-tabela-cobrancas",
  imports: [
    CurrencyPipe,
    DeleteModal,
    SaveCobrancaModal,
    PrimaryAddButton,
    RelatorioButton,
    WhatsappButton,
    ContainerFiltros,
    ContainerFiltros,
    UpdateButton,
    UpdateCobrancaModal,
    LoadingModal,
    SelectUserModal,
    Paginacao,
  ],
  templateUrl: "./tabela-cobrancas.html",
  styleUrl: "./tabela-cobrancas.css",
})
export class TabelaCobrancas extends BaseTabelaRegistroComponent<
  CobrancaResponse,
  CobrancaQueryFilters
> {
  @ViewChild("loadingModal") loadingModal!: ElementRef<HTMLDialogElement>;
  @ViewChild("selectUserModal") selectUserModal!: ElementRef<HTMLDialogElement>;

  public cobrancaService = inject(CobrancaService);
  private relatorioService = inject(RelatorioService);
  private whatsappService = inject(WhatsappService);

  readonly nomeEntidade = "Cobrança";
  readonly serviceBase = this.cobrancaService;
  cobrancas = this.cobrancaService.cobrancas;

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
      key: "cidadeFiltro",
      visivel: false,
      componente: CidadeFiltro,
      label: "Cidade",
    },
    {
      key: "cobradorFiltro",
      visivel: false,
      componente: CobradorFiltro,
      label: "Cobrador",
    },
    {
      key: "registranteFiltro",
      visivel: false,
      componente: RegistranteFiltro,
      label: "Registrante",
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
      label: "Valor Total",
      controlsParaResetar: ["valorInicioFiltro", "valorFimFiltro"],
    },
  ];

  carregarDados() {
    this.cobrancaService.buscaCobrancas();
  }
  executarBuscaFiltro(filtros: CobrancaQueryFilters) {
    this.cobrancaService.buscaCobrancasPorFiltro(filtros);
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
    this.cobrancaService.paginaAtual.set(0);
    this.executarBuscaFiltro(this.filtrosAtivos);
  }

  deletarRegistro(modal: HTMLDialogElement) {
    if (!this.itemSelecionado) return;
    this.cobrancaService.deletaCobranca(this.itemSelecionado.id).subscribe({
      next: () => {
        this.carregarDados();
        this.toastService.abrir(
          "success",
          "Registro de cobrança apagado com sucesso!",
        );
        this.fecharModal(modal);
      },
    });
  }

  geraRelatorio() {
    if (!this.possuiFiltros() || this.cobrancas().length === 0) {
      this.toastService.abrir(
        "error",
        "Nenhum filtro preenchido ou tabela vazia",
      );
      return;
    }

    this.modalService.abrirModal(this.loadingModal.nativeElement);
    this.relatorioService
      .geraRelatorioDeCobrancas(this.filtrosAtivos)
      .subscribe({
        next: (blob) => window.open(window.URL.createObjectURL(blob), "_blank"),
        error: () =>
          this.modalService.fecharModal(this.loadingModal.nativeElement),
        complete: () =>
          this.modalService.fecharModal(this.loadingModal.nativeElement),
      });
  }

  enviaRelatorioParaWhatsapp(numerosSelecionados: string[]) {
    if (!this.possuiFiltros() || this.cobrancas().length === 0) {
      this.modalService.fecharModal(this.selectUserModal.nativeElement);
      this.toastService.abrir(
        "error",
        "Nenhum filtro preenchido ou tabela vazia",
      );
      return;
    }

    if (!numerosSelecionados || numerosSelecionados.length === 0) {
      this.toastService.abrir(
        "error",
        "Selecione ao menos um usuário para enviar o relatório",
      );
      return;
    }

    this.modalService.abrirModal(this.loadingModal.nativeElement);

    const payload: EnviaRelatorioWppRequest = {
      filtros: this.filtrosAtivos,
      numerosTelefone: numerosSelecionados,
    };

    this.whatsappService.enviaRelatorio(payload).subscribe({
      next: (mensagemSucesso) => {
        this.toastService.abrir("success", mensagemSucesso);
      },
      error: (err) => {
        this.toastService.abrir(
          "error",
          "Falha ao iniciar envio dos relatórios.",
        );
        this.modalService.fecharModal(this.loadingModal.nativeElement);
      },
      complete: () => {
        this.modalService.fecharModal(this.loadingModal.nativeElement);
      },
    });
  }
}
