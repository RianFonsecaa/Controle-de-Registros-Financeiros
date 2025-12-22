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
    if (formControl.hasError('required')) {
      return 'Este campo é obrigatório.';
    }

    if (formControl.hasError('email')) {
      return 'Formato de E-mail inválido.';
    }

    if (formControl.hasError('pattern')) {
      return 'Formato inválido.';
    }

    return '';
  }
}
