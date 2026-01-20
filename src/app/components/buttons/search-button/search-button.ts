import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-search-button',
  imports: [],
  templateUrl: './search-button.html',
  styleUrl: './search-button.css',
})
export class SearchButton {
  @Output() filtrar = new EventEmitter<void>();

  onFiltrar() {
    this.filtrar.emit();
  }
}
