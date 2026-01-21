import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CurrencyMaskModule } from 'ng2-currency-mask';

@Component({
  selector: 'app-valor-total-filtro',
  imports: [CurrencyMaskModule, ReactiveFormsModule],
  templateUrl: './valor-total-filtro.html',
  styleUrl: './valor-total-filtro.css',
})
export class ValorTotalFiltro {
  @Input() form!: FormGroup;
  @Input() onRemover!: () => void;
  @Input() onPreencherValor!: () => void;

  ngOnInit() {
    this.form.get('valorInicioFiltro')?.valueChanges.subscribe((valor) => {
      if (this.temValor(valor)) {
        this.onPreencherValor();
      }
    });

    this.form.get('valorFimFiltro')?.valueChanges.subscribe((valor) => {
      if (this.temValor(valor)) {
        this.onPreencherValor();
      }
    });
  }

  private temValor(valor: any): boolean {
    return valor !== null && valor !== undefined && valor !== '';
  }
}
