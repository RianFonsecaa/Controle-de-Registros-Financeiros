import {
  Component,
  ElementRef,
  EventEmitter,
  inject,
  Input,
  Output,
  ViewChild,
} from "@angular/core";
import { CancelButton } from "../../../buttons/cancel-button/cancel-button";
import { DeleteButton } from "../../../buttons/delete-button/delete-button";
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from "@angular/forms";
import { PrimaryInput } from "../../../inputs/primary-input/primary-input";
import { CidadesService } from "../../../../services/http/cidades.service";
import { SaveButton } from "../../../buttons/save-button/save-button";
import { MoneyInput } from "../../../inputs/money-input/money-input";
import { PixRequest } from "../../../../model/requests/PixRequest";
import { CidadeResponse } from "../../../../model/responses/CidadeResponse";
import { CidadeSelect } from "../../../selects/cidade-select/cidade-select";
import { PixResponse } from "../../../../model/responses/PixResponse";
import { DateInput } from "../../../inputs/date-input/date-input";

@Component({
  selector: "app-save-pix-modal",
  imports: [
    CancelButton,
    ReactiveFormsModule,
    PrimaryInput,
    SaveButton,
    MoneyInput,
    CidadeSelect,
    DateInput,
  ],
  templateUrl: "./save-pix-modal.html",
  styleUrl: "./save-pix-modal.css",
})
export class SavePixModal {
  cidadesService = inject(CidadesService);
  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<any>();

  @ViewChild("fileInput") fileInput!: ElementRef<HTMLInputElement>;

  @Input() data!: String;
  @Input() cidade!: CidadeResponse;
  @Input() pix: PixResponse | null = null;

  pixForm: FormGroup = new FormGroup({
    cliente: new FormControl(null, [Validators.required]),
    valor: new FormControl(null, [Validators.required]),
    cidade: new FormControl(null, [Validators.required]),
    data: new FormControl(null, [
      Validators.required,
      this.dataNaoFutura,
      this.dataRange("2010-01-01", new Date()),
    ]),
    comprovante: new FormControl(),
  });

  ngOnInit() {
    this.cidadesService.buscaCidades();
  }

  ngOnChanges() {
    if (!this.pixForm) return;

    if (this.pix) {
      this.preencherFormulario();
    } else {
      this.resetarParaCriacao();
    }
  }

  getControl(name: string): FormControl {
    return this.pixForm.get(name) as FormControl;
  }

  onCancelar() {
    this.cancelar.emit();
    this.pixForm.reset();
    this.resetFileInput();
  }

  private preencherFormulario() {
    const cidade = this.cidadesService
      .cidades()
      .find((c) => c.id === this.pix!.cidadeId);

    this.pixForm.patchValue({
      id: this.pix?.id,
      cliente: this.pix?.cliente,
      valor: this.pix?.valor,
      cidade: cidade,
      data: this.formatarDataParaInput(this.pix?.data),
      comprovante: null,
    });
  }

  onSalvar() {
    if (this.pixForm.invalid) {
      this.pixForm.markAllAsTouched();
      return;
    }

    const formValue = this.pixForm.value;

    const pix: PixRequest = {
      id: formValue.id || null,
      cliente: formValue.cliente,
      valor: formValue.valor,
      cidadeId: formValue.cidade.id,
      data: this.formatarData(formValue.data),
      comprovante: formValue.comprovante,
      cobrancaId: this.pix?.cobrancaId || null,
    };

    this.salvar.emit(pix);
    this.resetarParaCriacao();
  }

  private resetarParaCriacao() {
    this.pixForm.reset();
    this.resetFileInput();
    this.pixForm.patchValue({
      cidade: this.cidade ?? null,
      data: this.data ?? null,
    });
  }

  private resetFileInput() {
    if (this.fileInput) {
      this.fileInput.nativeElement.value = "";
    }
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

  private formatarDataParaInput(data: string | undefined): string {
    if (!data) return "";

    const [dia, mes, ano] = data.split("-");
    return `${ano}-${mes}-${dia}`;
  }

  onFileChange(input: HTMLInputElement) {
    if (!input.files?.length) return;

    this.getControl("comprovante").setValue(input.files[0]);
  }
}
