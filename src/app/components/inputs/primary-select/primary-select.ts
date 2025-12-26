import { Component, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { SelectOptions } from '../../../model/responses/SelectOptions';

@Component({
  selector: 'app-primary-select',
  imports: [ReactiveFormsModule],
  templateUrl: './primary-select.html',
  styleUrl: './primary-select.css',
})
export class PrimarySelect {
  @Input() id: string = '';
  @Input() control!: FormControl;
  @Input() options: SelectOptions[] = [];
  @Input() label: string = '';

  getMensagemErro(formControl: FormControl): string {
    if (formControl.hasError('required')) {
      return 'Este campo é obrigatório.';
    }

    return '';
  }

  compareById(a: SelectOptions, b: SelectOptions) {
    return a && b ? a.id === b.id : a === b;
  }
}
