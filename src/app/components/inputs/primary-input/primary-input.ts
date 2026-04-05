import { Component, Input } from '@angular/core';
import {
  FormControl,
  FormControlName,
  ReactiveFormsModule,
  ɵInternalFormsSharedModule,
} from '@angular/forms';

@Component({
  selector: 'app-primary-input',
  imports: [ɵInternalFormsSharedModule, ReactiveFormsModule],
  templateUrl: './primary-input.html',
  styleUrl: './primary-input.css',
})
export class PrimaryInput {
  @Input() placeholder: string = '';
  @Input() id: string = '';
  @Input() control!: FormControl;
  @Input() type: string = 'text';
  @Input() label: string = '';

  getMensagemErro(formControl: FormControl): string {
    if (!formControl || !formControl.errors) return '';

    if (formControl.hasError('required')) return 'Campo obrigatório';
    if (formControl.hasError('minlength'))
      return `Mínimo de ${formControl.errors['minlength'].requiredLength} caracteres`;
    if (formControl.hasError('email')) return 'E-mail inválido';
    if (formControl.hasError('dataFutura')) return 'A data não pode ser futura';

    return 'Campo inválido';
  }
}
