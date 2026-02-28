import { Component, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { NgxMaskDirective } from 'ngx-mask';

@Component({
  selector: 'app-telefone-input',
  imports: [ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './telefone-input.html',
  styleUrl: './telefone-input.css',
})
export class TelefoneInput {
  @Input() control!: FormControl;
  @Input() label: string = 'Telefone';

  getMensagemErro(formControl: FormControl): string {
    if (!formControl || !formControl.errors) return '';

    if (formControl.hasError('required')) return 'Campo obrigatório';

    return 'Campo inválido';
  }
}
