import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-cancel-button',
  imports: [],
  templateUrl: './cancel-button.html',
  styleUrl: './cancel-button.css',
})
export class CancelButton {
  @Output() cancelar = new EventEmitter<void>();

  onCancelar() {
    this.cancelar.emit();
  }
}
