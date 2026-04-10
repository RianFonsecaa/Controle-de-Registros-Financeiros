import { Component, Input } from "@angular/core";
import { FormControl, ReactiveFormsModule } from "@angular/forms";

@Component({
  selector: "app-date-input",
  imports: [ReactiveFormsModule],
  templateUrl: "./date-input.html",
  styleUrl: "./date-input.css",
})
export class DateInput {
  @Input() control!: FormControl;
  @Input() label: string = "";
  dataHoje = new Date().toISOString().split("T")[0];

  getMensagemErro(formControl: FormControl): string {
    if (!formControl || !formControl.errors) return "";

    if (formControl.hasError("required")) {
      return "Campo obrigatório";
    }

    if (formControl.hasError("minlength")) {
      return `Mínimo de ${formControl.errors["minlength"].requiredLength} caracteres`;
    }

    if (formControl.hasError("email")) {
      return "E-mail inválido";
    }

    if (formControl.hasError("dataFutura")) {
      return "A data não pode ser futura";
    }

    if (formControl.hasError("foraDoRange")) {
      return "Data fora do intervalo permitido";
    }

    if (formControl.hasError("matDatepickerParse")) {
      return "Data inválida";
    }

    if (formControl.hasError("pattern")) {
      return "Formato inválido";
    }

    return "Campo inválido";
  }
}
