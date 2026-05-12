import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-update-button',
  imports: [],
  templateUrl: './update-button.html',
  styleUrl: './update-button.css',
})
export class UpdateButton {
  @Output() atualizar = new EventEmitter<void>();

  onAtualizar() {
    this.atualizar.emit();
  }
}
