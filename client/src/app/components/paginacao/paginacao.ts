import { Component, EventEmitter, Input, Output } from "@angular/core";

@Component({
  selector: "app-paginacao",
  imports: [],
  templateUrl: "./paginacao.html",
  styleUrl: "./paginacao.css",
})
export class Paginacao {
  @Input() paginaAtual: number = 0;
  @Input() totalPaginas: number = 0;
  @Input() itensPorPagina: number = 10;

  @Output() aoMudarPagina = new EventEmitter<number>();
  @Output() aoMudarTamanho = new EventEmitter<number>();

  get paginaExibida(): number {
    return this.paginaAtual + 1;
  }

  get estaNaPrimeiraPagina(): boolean {
    return this.paginaAtual === 0;
  }

  get estaNaUltimaPagina(): boolean {
    return this.totalPaginas === 0 || this.paginaAtual >= this.totalPaginas - 1;
  }

  irParaPrimeira() {
    this.atualizarPagina(0);
  }

  irParaAnterior() {
    if (!this.estaNaPrimeiraPagina) {
      this.atualizarPagina(this.paginaAtual - 1);
    }
  }

  irParaProxima() {
    if (!this.estaNaUltimaPagina) {
      this.atualizarPagina(this.paginaAtual + 1);
    }
  }

  irParaUltima() {
    const ultima = this.totalPaginas - 1;
    this.atualizarPagina(ultima > 0 ? ultima : 0);
  }

  alterarTamanho(event: Event) {
    const select = event.target as HTMLSelectElement;
    this.aoMudarTamanho.emit(Number(select.value));
  }

  private atualizarPagina(novaPagina: number) {
    this.aoMudarPagina.emit(novaPagina);
  }
}
