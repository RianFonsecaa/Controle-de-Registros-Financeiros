import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-save-button',
  imports: [],
  templateUrl: './save-button.html',
  styleUrl: './save-button.css',
})
export class SaveButton {
  @Output() salvar = new EventEmitter<void>();

  onSalvar() {
    this.salvar.emit();
  }
}
