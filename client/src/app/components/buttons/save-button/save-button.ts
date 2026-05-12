import { NgClass } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-save-button',
  imports: [NgClass],
  templateUrl: './save-button.html',
  styleUrl: './save-button.css',
})
export class SaveButton {
  @Input() label: string = 'CONFIRMAR';
  @Input() loadingText: string = 'PROCESSANDO...';
  @Input() loading: boolean = false;
  @Input() disabled: boolean = false;
  @Input() tipo: 'button' | 'submit' = 'button';
  @Input() variante: 'amber' | 'blue' | 'red' = 'blue';

  @Output() salvar = new EventEmitter<void>();

  get colorClass() {
    const variants = {
      amber: 'bg-amber-500 text-white hover:bg-amber-600',
      blue: 'bg-blue-500 text-white hover:bg-blue-600',
      red: 'bg-red-500 text-white hover:bg-red-600',
    };
    return variants[this.variante];
  }
}
