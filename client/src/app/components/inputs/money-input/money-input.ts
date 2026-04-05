import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { CurrencyMaskModule } from 'ng2-currency-mask';

@Component({
  selector: 'app-money-input',
  imports: [CurrencyMaskModule, ReactiveFormsModule, NgClass],
  templateUrl: './money-input.html',
  styleUrl: './money-input.css',
})
export class MoneyInput {
  @Input() control!: FormControl;
  @Input() id = '';
  @Input() readonly = false;
  @Input() placeholder = '';
  @Input() label: string = '';

  getMensagemErro(formControl: FormControl): string {
    if (!formControl || !formControl.errors) return '';

    if (formControl.hasError('required')) return 'Campo obrigatório';

    return 'Campo inválido';
  }
}
