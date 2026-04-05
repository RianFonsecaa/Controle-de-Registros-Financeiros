import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnChanges,
  Output,
} from '@angular/core';
import {
  FormGroup,
  FormControl,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { CobrancaRequest } from '../../../../model/requests/CobrancaRequest';
import { CobrancaResponse } from '../../../../model/responses/CobrancaResponse';
import { CidadesService } from '../../../../services/http/cidades.service';
import { CobrancaService } from '../../../../services/http/cobrancas.service';
import { FuncionarioService } from '../../../../services/http/funcionario.service';
import { VeiculoService } from '../../../../services/http/veiculo.service';
import { VeiculoSelect } from '../../../selects/veiculo-select/veiculo-select';
import { MoneyInput } from '../../../inputs/money-input/money-input';
import { CidadeSelect } from '../../../selects/cidade-select/cidade-select';
import { PrimaryInput } from '../../../inputs/primary-input/primary-input';
import { CobradorSelect } from '../../../selects/cobrador-select/cobrador-select';
import { CancelButton } from '../../../buttons/cancel-button/cancel-button';
import { SaveButton } from '../../../buttons/save-button/save-button';
import { ToastService } from '../../../../services/ui/toast.service';

@Component({
  selector: 'app-update-cobranca-modal',
  imports: [
    VeiculoSelect,
    MoneyInput,
    CidadeSelect,
    PrimaryInput,
    CobradorSelect,
    CancelButton,
    SaveButton,
    ReactiveFormsModule,
  ],
  templateUrl: './update-cobranca-modal.html',
  styleUrl: './update-cobranca-modal.css',
})
export class UpdateCobrancaModal implements OnChanges {
  @Input() cobranca!: CobrancaResponse | null;

  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<void>();

  cidadesService = inject(CidadesService);
  funcionarioService = inject(FuncionarioService);
  veiculoService = inject(VeiculoService);
  cobrancaService = inject(CobrancaService);
  toastService = inject(ToastService);

  cobrancaForm: FormGroup = new FormGroup({
    cidade: new FormControl(null, Validators.required),
    cobrador: new FormControl(null, Validators.required),
    veiculo: new FormControl(null, Validators.required),
    data: new FormControl(null, Validators.required),
    observacoes: new FormControl(null, Validators.required),
    valorEspecie: new FormControl(0),
    valorTotalPix: new FormControl(0),
    valorTotalVale: new FormControl(0),
    valorTotal: new FormControl(0),
  });

  ngOnInit() {
    this.cobrancaForm.valueChanges.subscribe(() => {
      this.calcularValoresTotais();
    });
  }

  ngOnChanges() {
    this.preencherFormulario();
  }

  private preencherFormulario() {
    if (!this.cobranca) return;
    console.log(this.cobranca);

    const cidade = this.cidadesService
      .cidades()
      .find((c) => c.id === this.cobranca!.cidadeId);

    const cobrador = this.funcionarioService
      .funcionarios()
      .find((f) => f.id === this.cobranca!.cobradorId);

    const veiculo = this.veiculoService
      .veiculos()
      .find((v) => v.id === this.cobranca!.veiculoId);

    this.cobrancaForm.patchValue({
      cidade: cidade,
      cobrador: cobrador,
      veiculo: veiculo,
      data: this.cobranca.data,
      observacoes: this.cobranca.observacoes,
      valorEspecie: this.cobranca.valorEspecie,
      valorTotalPix: this.cobranca.valorTotalPix,
      valorTotalVale: this.cobranca.valorTotalVale,
      valorTotal: this.cobranca.valorTotal,
    });
  }

  calcularValoresTotais() {
    const especie = Number(this.getControl('valorEspecie').value) || 0;
    const pix = Number(this.getControl('valorTotalPix').value) || 0;
    const vales = Number(this.getControl('valorTotalVale').value) || 0;

    this.getControl('valorTotal').setValue(especie + pix + vales, {
      emitEvent: false,
    });
  }

  getControl(name: string): FormControl {
    return this.cobrancaForm.get(name) as FormControl;
  }

  onSalvar() {
    if (this.cobrancaForm.invalid || !this.cobranca) {
      this.cobrancaForm.markAllAsTouched();
      return;
    }

    const form = this.cobrancaForm.value;

    const request: CobrancaRequest = {
      id: this.cobranca.id,
      cidadeId: form.cidade.id,
      cobradorId: form.cobrador.id,
      veiculoId: form.veiculo.id,
      data: form.data,
      observacoes: form.observacoes,
      valorEspecie: form.valorEspecie,
      valorTotalPix: form.valorTotalPix,
      valorTotalVale: form.valorTotalVale,
      valorTotal: form.valorTotal,
    };

    this.cobrancaService.atualizaCobranca(request).subscribe({
      next: () => this.finalizarSucesso(),
      error: (err) => {
        this.toastService.abrir('error', 'Erro ao atualizar cobrança');
        console.error(err);
      },
    });
  }

  private finalizarSucesso() {
    this.salvar.emit();
    this.cobrancaForm.reset();
    this.cobrancaService.buscaCobrancas();
    this.toastService.abrir(
      'success',
      'Registro de cobrança atualizado com sucesso!',
    );
  }

  onCancelar() {
    this.cobrancaForm.reset();
    this.cancelar.emit();
  }
}
