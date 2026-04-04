import { CurrencyPipe, NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-card-somatorios',
  imports: [CurrencyPipe, NgClass],
  templateUrl: './card-somatorios.html',
  styleUrl: './card-somatorios.css',
})
export class CardSomatorios {
  @Input() label: String = '';
  @Input() valor: number = 0;
  @Input() usarMoeda: boolean = true;
  @Input() textColor: string = 'text-blue-900';
}
