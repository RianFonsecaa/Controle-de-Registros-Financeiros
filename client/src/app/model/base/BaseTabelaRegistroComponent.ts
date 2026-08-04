import {
  Directive,
  OnInit,
  Signal,
  ViewChild,
  WritableSignal,
  inject,
} from "@angular/core";
import { FormGroup } from "@angular/forms";
import {
  ContainerFiltros,
  FiltroConfig,
} from "../../components/container-filtros/container-filtros";
import { ModalService } from "../../services/ui/modal.service";
import { ToastService } from "../../services/ui/toast.service";

export interface ServiceRegistroBase<TEntity, TFilter> {
  paginaAtual: WritableSignal<number>;
  itensPorPagina: WritableSignal<number>;
  totalPaginas: Signal<number> | WritableSignal<number>;
  deletaRegistro?(id: number): any;
  buscaPorFiltro?(filtros: TFilter): void;
}

@Directive()
export abstract class BaseTabelaRegistroComponent<
  TEntity,
  TFilter,
> implements OnInit {
  @ViewChild(ContainerFiltros) containerFiltros!: ContainerFiltros;

  protected modalService = inject(ModalService);
  protected toastService = inject(ToastService);

  filtrosAtivos!: TFilter;
  itemSelecionado: TEntity | null = null;
  enviando: boolean = false;

  abstract readonly nomeEntidade: string;
  abstract filtroForm: FormGroup;
  abstract configuracaoFiltros: FiltroConfig[];
  abstract readonly serviceBase: any;

  ngOnInit(): void {
    this.serviceBase?.limparFiltros();
  }

  abstract carregarDados(): void;
  abstract executarBuscaFiltro(filtros: TFilter): void;

  abrirModal(item: TEntity | null, modal: HTMLDialogElement): void {
    if (item) this.itemSelecionado = item;
    this.modalService.abrirModal(modal);
  }

  fecharModal(modal: HTMLDialogElement): void {
    this.modalService.fecharModal(modal);
    this.itemSelecionado = null;
  }

  atualizaRegistros(): void {
    this.containerFiltros?.removerTodosFiltros();
  }

  aoMudarDePagina(novaPagina: number): void {
    this.serviceBase.paginaAtual.set(novaPagina);
    this.recarregarTabela();
  }

  aoMudarItensPorPagina(novoTamanho: number): void {
    this.serviceBase.itensPorPagina.set(novoTamanho);
    this.serviceBase.paginaAtual.set(0);
    this.recarregarTabela();
  }

  protected recarregarTabela(): void {
    if (this.possuiFiltros()) {
      this.executarBuscaFiltro(this.filtrosAtivos);
    } else {
      this.carregarDados();
    }
  }

  protected possuiFiltros(): boolean {
    if (!this.filtrosAtivos) return false;
    return Object.values(this.filtrosAtivos as Record<string, any>).some(
      (v) => v !== null && v !== undefined && v !== "",
    );
  }
}
