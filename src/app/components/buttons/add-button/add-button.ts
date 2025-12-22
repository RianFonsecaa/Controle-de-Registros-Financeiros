import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-add-button',
  imports: [],
  templateUrl: './add-button.html',
  styleUrl: './add-button.css',
})
export class AddButton {
  @Output() add = new EventEmitter<void>();
  @Input() nome: string = '';

  onAdd() {
    this.add.emit();
  }
}
