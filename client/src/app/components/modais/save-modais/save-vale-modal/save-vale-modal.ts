import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnChanges,
  OnInit,
  Output,
} from "@angular/core";
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from "@angular/forms";
import { FuncionarioService } from "../../../../services/http/funcionario.service";
import { PrimaryInput } from "../../../inputs/primary-input/primary-input";
import { MoneyInput } from "../../../inputs/money-input/money-input";
import { CancelButton } from "../../../buttons/cancel-button/cancel-button";
import { SaveButton } from "../../../buttons/save-button/save-button";
import { ValeRequest } from "../../../../model/requests/ValeRequest";
import { CidadeResponse } from "../../../../model/responses/CidadeResponse";
import { CobradorSelect } from "../../../selects/cobrador-select/cobrador-select";
import { ValeResponse } from "../../../../model/responses/ValeResponse";
import { DateInput } from "../../../inputs/date-input/date-input";

@Component({
  selector: "app-save-vale-modal",
  imports: [
    MoneyInput,
    CancelButton,
    SaveButton,
    ReactiveFormsModule,
    CobradorSelect,
    DateInput,
  ],
  templateUrl: "./save-vale-modal.html",
  styleUrl: "./save-vale-modal.css",
})
export class SaveValeModal implements OnInit, OnChanges {
  funcionarioService = inject(FuncionarioService);

  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<ValeRequest>();

  @Input() data!: string;
  @Input() vale: ValeResponse | null = null;

  valeForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    funcionario: new FormControl(null, [Validators.required]),
    justificativa: new FormControl(null, [Validators.required]),
    valor: new FormControl(null, [Validators.required]),
    data: new FormControl(null, [
      Validators.required,
      this.dataNaoFutura,
      this.dataRange("2010-01-01", new Date()),
    ]),
  });

  ngOnInit() {
    this.funcionarioService.buscaFuncionarios();
  }

  ngOnChanges() {
    if (!this.valeForm) return;

    if (this.vale) {
      this.preencherFormulario();
    } else {
      this.resetarParaCriacao();
    }
  }

  getControl(name: string): FormControl {
    return this.valeForm.get(name) as FormControl;
  }

  onCancelar() {
    this.cancelar.emit();
    this.resetarParaCriacao();
  }

  private preencherFormulario() {
    const funcionario = this.funcionarioService
      .funcionarios()
      .find((f) => f.id === this.vale!.funcionarioId);

    this.valeForm.patchValue({
      id: this.vale?.id,
      funcionario: funcionario,
      justificativa: this.vale?.justificativa,
      valor: this.vale?.valor,
      data: this.vale?.data,
    });
  }

  onSalvar() {
    if (this.valeForm.invalid) {
      this.valeForm.markAllAsTouched();
      return;
    }

    const formValue = this.valeForm.value;

    const request: ValeRequest = {
      id: this.vale?.id || null,
      funcionarioId: formValue.funcionario.id,
      funcionarioNome: formValue.funcionario.nome,
      justificativa: formValue.justificativa,
      valor: formValue.valor,
      data: this.formatarData(formValue.data),
      cobrancaId: this.vale?.cobrancaId || null,
    };

    this.salvar.emit(request);
    this.resetarParaCriacao();
  }

  private dataNaoFutura(control: AbstractControl) {
    if (!control.value) return null;

    const dataSelecionada = new Date(control.value + "T00:00:00");

    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);

    return dataSelecionada > hoje ? { dataFutura: true } : null;
  }

  private dataRange(min: string, max: Date) {
    return (control: AbstractControl) => {
      if (!control.value) return null;

      const data = new Date(control.value + "T00:00:00");
      const minDate = new Date(min + "T00:00:00");

      const maxDate = new Date(max);
      maxDate.setHours(0, 0, 0, 0);

      if (data < minDate || data > maxDate) {
        return { foraDoRange: true };
      }
      return null;
    };
  }

  private formatarData(data: string): string {
    const [ano, mes, dia] = data.split("-");
    return `${dia}-${mes}-${ano}`;
  }

  private resetarParaCriacao() {
    this.valeForm.reset();
    this.valeForm.patchValue({
      data: this.data ?? null,
    });
  }
}
