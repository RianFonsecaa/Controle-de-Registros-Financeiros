import { NgClass } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-primary-add-button',
  imports: [],
  templateUrl: './primary-add-button.html',
  styleUrl: './primary-add-button.css',
})
export class PrimaryAddButton {
  @Output() abrir = new EventEmitter<void>();
  @Input() nome: string = '';

  onAbrir() {
    this.abrir.emit();
  }
}
