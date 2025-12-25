import { NgClass } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-add-button',
  imports: [NgClass],
  templateUrl: './add-button.html',
  styleUrl: './add-button.css',
})
export class AddButton {
  @Output() add = new EventEmitter<void>();
  @Input() nome: string = '';
  @Input() textColor: string = '';
  @Input() background: string = '';

  onAdd() {
    this.add.emit();
  }
}
