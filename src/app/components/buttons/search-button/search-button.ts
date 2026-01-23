import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-search-button',
  imports: [],
  templateUrl: './search-button.html',
  styleUrl: './search-button.css',
})
export class SearchButton {
  @Output() atualizar = new EventEmitter<void>();

  onAtualizar() {
    this.atualizar.emit();
  }
}
