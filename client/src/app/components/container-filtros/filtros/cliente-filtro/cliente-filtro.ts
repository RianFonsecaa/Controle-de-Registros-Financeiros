import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-cliente-filtro',
  imports: [ReactiveFormsModule],
  templateUrl: './cliente-filtro.html',
  styleUrl: './cliente-filtro.css',
})
export class ClienteFiltro {
  @Input() form!: FormGroup;
  @Input() onRemover!: () => void;
  @Input() onPreencherValor!: () => void;
}
