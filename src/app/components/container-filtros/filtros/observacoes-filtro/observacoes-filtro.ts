import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-observacoes-filtro',
  imports: [ReactiveFormsModule],
  templateUrl: './observacoes-filtro.html',
  styleUrl: './observacoes-filtro.css',
})
export class ObservacoesFiltro {
  @Input() form!: FormGroup;
  @Input() onRemover!: () => void;
  @Input() onPreencherValor!: () => void;
}
