import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-registrante-filtro',
  imports: [ReactiveFormsModule],
  templateUrl: './registrante-filtro.html',
  styleUrl: './registrante-filtro.css',
})
export class RegistranteFiltro {
  @Input() form!: FormGroup;
  @Input() onRemover!: () => void;
  @Input() onPreencherValor!: () => void;
}
