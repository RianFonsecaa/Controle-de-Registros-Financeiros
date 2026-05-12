import { NgClass } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-confirm-button',
  imports: [NgClass],
  templateUrl: './confirm-button.html',
  styleUrl: './confirm-button.css',
})
export class ConfirmButton {
  @Output() confirmar = new EventEmitter<void>();

  @Input() colorTheme: 'blue' | 'green' | 'red' = 'blue';
}
