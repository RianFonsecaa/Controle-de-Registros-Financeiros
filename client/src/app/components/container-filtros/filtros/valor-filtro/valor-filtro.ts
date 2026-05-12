import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CurrencyMaskModule } from 'ng2-currency-mask';

@Component({
  selector: 'app-valor-filtro',
  imports: [CurrencyMaskModule, ReactiveFormsModule],
  templateUrl: './valor-filtro.html',
  styleUrl: './valor-filtro.css',
})
export class ValorFiltro {
  @Input() form!: FormGroup;
  @Input() onRemover!: () => void;
  @Input() onPreencherValor!: () => void;
}
