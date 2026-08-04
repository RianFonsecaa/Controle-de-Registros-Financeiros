import {
  WritableSignal,
  Directive,
  OnInit,
  inject,
  Signal,
} from "@angular/core";
import { ModalService } from "../../services/ui/modal.service";
import { ToastService } from "../../services/ui/toast.service";

export interface EntidadeCadastro {
  id?: number;
  login?: string;
  ativo?: boolean;
}

export interface ServiceCadastroBase<T> {
  paginaAtual: WritableSignal<number>;
  itensPorPagina: WritableSignal<number>;
  exibirApenasAtivos: WritableSignal<boolean>;
  toggleStatus(idOrLogin: any): any;
}

@Directive()
export abstract class BaseTabelaCadastroComponent<
  T extends EntidadeCadastro,
> implements OnInit {
  protected modalService = inject(ModalService);
  protected toastService = inject(ToastService);

  itemSelecionado: T | null = null;

  abstract readonly nomeEntidade: string;
  abstract readonly serviceBase: ServiceCadastroBase<T>;
  abstract readonly listaExibida: Signal<T[]>;

  get mostrarAtivos(): Signal<boolean> {
    return this.serviceBase.exibirApenasAtivos;
  }

  ngOnInit(): void {
    this.carregarDados();
  }

  abstract carregarDados(): void;

  abrirModal(item: T | null, modal: HTMLDialogElement): void {
    if (item) {
      this.itemSelecionado = item;
    }
    this.modalService.abrirModal(modal);
  }

  fecharModal(modal: HTMLDialogElement): void {
    this.modalService.fecharModal(modal);
    this.itemSelecionado = null;
  }

  toggleStatusStatusGeral(modal: HTMLDialogElement, identificador?: any): void {
    if (!this.itemSelecionado) return;

    const idParaToggle =
      identificador ?? this.itemSelecionado.id ?? this.itemSelecionado.login;

    this.serviceBase.toggleStatus(idParaToggle).subscribe({
      next: () => {
        this.carregarDados();

        const statusAtual = this.itemSelecionado?.ativo ?? false;
        const acao = statusAtual ? "inativado" : "ativado";

        this.toastService.abrir(
          "success",
          `Cadastro de ${this.nomeEntidade} ${acao} com sucesso!`,
        );

        this.fecharModal(modal);
      },
      error: () => {
        this.toastService.abrir("error", "Erro ao atualizar status.");
      },
    });
  }

  toggleStatusLista(): void {
    this.serviceBase.exibirApenasAtivos.update((status) => !status);
    this.serviceBase.paginaAtual.set(0);
    this.carregarDados();
  }

  aoMudarDePagina(novaPagina: number): void {
    this.serviceBase.paginaAtual.set(novaPagina);
    this.carregarDados();
  }

  aoMudarItensPorPagina(novoTamanho: number): void {
    this.serviceBase.itensPorPagina.set(novoTamanho);
    this.serviceBase.paginaAtual.set(0);
    this.carregarDados();
  }

  formatarTelefone(telefone?: string): string {
    if (!telefone) return "";
    const numero = telefone.replace(/\D/g, "");

    if (numero.length === 11) {
      return numero.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
    }

    return telefone;
  }
}
