import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-secondary-add-button',
  imports: [],
  templateUrl: './secondary-add-button.html',
  styleUrl: './secondary-add-button.css',
})
export class SecondaryAddButton {
  @Output() adicionar = new EventEmitter<void>();
  @Input() nome: string = '';

  onAdicionar() {
    this.adicionar.emit();
  }
}
