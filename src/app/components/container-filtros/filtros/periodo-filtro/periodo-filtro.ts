import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-periodo-filtro',
  imports: [ReactiveFormsModule],
  templateUrl: './periodo-filtro.html',
  styleUrl: './periodo-filtro.css',
})
export class PeriodoFiltro {
  @Input() form!: FormGroup;
  @Input() onRemover!: () => void;
  @Input() onPreencherValor!: () => void;
}
