import { Component, Input } from "@angular/core";
import { FormGroup, ReactiveFormsModule } from "@angular/forms";

@Component({
  selector: "app-cobranca-filtro",
  imports: [ReactiveFormsModule],
  templateUrl: "./cobranca-filtro.html",
  styleUrl: "./cobranca-filtro.css",
})
export class CobrancaFiltro {
  @Input() form!: FormGroup;
  @Input() onRemover!: () => void;
  @Input() onPreencherValor!: () => void;
}
