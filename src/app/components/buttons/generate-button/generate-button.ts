import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-generate-button',
  imports: [],
  templateUrl: './generate-button.html',
  styleUrl: './generate-button.css',
})
export class GenerateButton {
  @Output() gerar = new EventEmitter<void>();

  onGerar() {
    this.gerar.emit();
  }
}
