import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-relatorio-button',
  imports: [],
  templateUrl: './relatorio-button.html',
  styleUrl: './relatorio-button.css',
})
export class RelatorioButton {
  @Output() abrir = new EventEmitter<void>();

  abrirModal() {
    this.abrir.emit();
  }
}
