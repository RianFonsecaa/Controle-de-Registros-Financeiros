import { Component, EventEmitter, inject, Input, Output } from "@angular/core";
import { PrimaryInput } from "../../../inputs/primary-input/primary-input";
import {
  AbstractControl,
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from "@angular/forms";

import { MoneyInput } from "../../../inputs/money-input/money-input";
import { CancelButton } from "../../../buttons/cancel-button/cancel-button";
import { PrimaryAddButton } from "../../../buttons/primary-add-button/primary-add-button";
import { SavePixModal } from "../save-pix-modal/save-pix-modal";
import { ModalService } from "../../../../services/ui/modal.service";
import { PixRequest } from "../../../../model/requests/PixRequest";
import { CurrencyPipe } from "@angular/common";
import { SaveButton } from "../../../buttons/save-button/save-button";
import { ValeRequest } from "../../../../model/requests/ValeRequest";
import { SaveValeModal } from "../save-vale-modal/save-vale-modal";
import { CobrancaRequest } from "../../../../model/requests/CobrancaRequest";
import { CobrancaService } from "../../../../services/http/cobrancas.service";
import { PixService } from "../../../../services/http/pix.service";
import { ValeService } from "../../../../services/http/vale.service";
import { CidadeSelect } from "../../../selects/cidade-select/cidade-select";
import { CobradorSelect } from "../../../selects/cobrador-select/cobrador-select";
import { VeiculoSelect } from "../../../selects/veiculo-select/veiculo-select";
import { ToastService } from "../../../../services/ui/toast.service";
import { DateInput } from "../../../inputs/date-input/date-input";

@Component({
  selector: "app-save-cobranca-modal",
  imports: [
    ReactiveFormsModule,
    MoneyInput,
    CancelButton,
    PrimaryAddButton,
    SavePixModal,
    SaveValeModal,
    SaveButton,
    CurrencyPipe,
    CidadeSelect,
    CobradorSelect,
    VeiculoSelect,
    DateInput,
  ],
  templateUrl: "./save-cobranca-modal.html",
  styleUrl: "./save-cobranca-modal.css",
})
export class SaveCobrancaModal {
  cobrancaService = inject(CobrancaService);
  pixService = inject(PixService);
  valeService = inject(ValeService);
  modalService = inject(ModalService);
  toastService = inject(ToastService);

  cobrancaForm: FormGroup = new FormGroup({
    cidade: new FormControl(null, [Validators.required]),
    cobrador: new FormControl(null, [Validators.required]),
    valorEspecie: new FormControl(null, [Validators.required]),
    valorTotalPix: new FormControl(0),
    valorTotalVale: new FormControl(0),
    valorTotal: new FormControl(0, [Validators.required]),
    data: new FormControl(null, [
      Validators.required,
      this.dataNaoFutura,
      this.dataRange("2010-01-01", new Date()),
    ]),
    veiculo: new FormControl(null, [Validators.required]),
    observacoes: new FormControl(null, [Validators.required]),
  });

  enviando: boolean = false;

  pixs: PixRequest[] = [];
  vales: ValeRequest[] = [];

  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<void>();

  exibirSuccessToast: boolean = false;
  mensagemToast: string = "";

  ngOnInit() {
    this.cobrancaForm.valueChanges.subscribe(() => {
      this.calcularValoresTotais();
    });
  }

  getControl(name: string): FormControl {
    return this.cobrancaForm.get(name) as FormControl;
  }

  onAdicionarPix(pix: PixRequest, modal: HTMLDialogElement) {
    this.pixs.push(pix);
    const totalPix = this.pixs.reduce((acc, p) => acc + Number(p.valor), 0);
    this.getControl("valorTotalPix").setValue(totalPix);
    modal.close();
  }

  onAdicionarVale(vale: ValeRequest, modal: HTMLDialogElement) {
    this.vales.push(vale);
    const totalVale = this.vales.reduce((acc, v) => acc + Number(v.valor), 0);
    this.getControl("valorTotalVale").setValue(totalVale);
    modal.close();
  }

  calcularValoresTotais() {
    const especie = Number(this.getControl("valorEspecie").value) || 0;
    const pix = Number(this.getControl("valorTotalPix").value) || 0;
    const vales = Number(this.getControl("valorTotalVale").value) || 0;

    this.getControl("valorTotal").setValue(especie + pix + vales, {
      emitEvent: false,
    });
  }

  onSalvar() {
    if (this.cobrancaForm.invalid) {
      this.cobrancaForm.markAllAsTouched();
      return;
    }

    this.enviando = true;

    const formValue = this.cobrancaForm.value;

    const cobrancaRequest: CobrancaRequest = {
      id: null,
      cidadeId: formValue.cidade.id,
      cobradorId: formValue.cobrador.id,
      veiculoId: formValue.veiculo.id,
      data: this.formatarDataParaApi(formValue.data),
      observacoes: formValue.observacoes,
      valorEspecie: Number(formValue.valorEspecie),
      valorTotalPix: Number(formValue.valorTotalPix) || 0,
      valorTotalVale: Number(formValue.valorTotalVale) || 0,
      valorTotal: Number(formValue.valorTotal),
    };

    this.cobrancaService.salvaCobranca(cobrancaRequest).subscribe({
      next: (cobrancaSalva) => {
        const cobrancaId = cobrancaSalva.id;

        this.salvarPixs(this.pixs, cobrancaId);
        this.salvarVales(this.vales, cobrancaId);

        this.salvar.emit();
        this.resetarFormulario();
        this.cobrancaService.buscaCobrancas();
        this.toastService.abrir(
          "success",
          "Registro de cobrança foi salvo com sucesso!",
        );
      },
      error: () => {
        this.enviando = false;
      },
    });
  }

  private salvarPixs(pixs: PixRequest[], cobrancaId: number) {
    pixs.forEach((pix) => {
      this.pixService
        .salvarPix({ ...pix, cobrancaId }, pix.comprovante)
        .subscribe({
          error: () => console.error("Erro ao salvar pix!"),
        });
    });
  }

  private salvarVales(vales: ValeRequest[], cobrancaId: number) {
    vales.forEach((vale) => {
      this.valeService.salvarVale({ ...vale, cobrancaId }).subscribe({
        error: () => console.error("Erro ao salvar vale!"),
      });
    });
  }

  onCancelar() {
    this.cancelar.emit();
    this.resetarFormulario();
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

  private formatarDataParaApi(data: string): string {
    const [ano, mes, dia] = data.split("-");
    return `${dia}-${mes}-${ano}`;
  }

  private resetarFormulario() {
    this.cobrancaForm.reset();
    this.pixs = [];
    this.vales = [];
  }
}
