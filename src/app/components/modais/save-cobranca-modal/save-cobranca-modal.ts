import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { PrimaryInput } from '../../inputs/primary-input/primary-input';
import {
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CidadesService } from '../../../services/cidades.service';
import { FuncionarioService } from '../../../services/funcionario.service';
import { VeiculoService } from '../../../services/veiculo.service';
import { MoneyInput } from '../../inputs/money-input/money-input';
import { CancelButton } from '../../buttons/cancel-button/cancel-button';
import { PrimaryAddButton } from '../../buttons/primary-add-button/primary-add-button';
import { SavePixModal } from '../save-pix-modal/save-pix-modal';
import { ModalService } from '../../../services/modal.service';
import { PixRequest } from '../../../model/requests/PixRequest';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { SaveButton } from '../../buttons/save-button/save-button';
import { ValeRequest } from '../../../model/requests/ValeRequest';
import { SaveValeModal } from '../save-vale-modal/save-vale-modal';
import { CobrancaRequest } from '../../../model/requests/CobrancaRequest';
import { FuncionarioResponse } from '../../../model/responses/FuncionarioResponse';
import { CidadeResponse } from '../../../model/responses/CidadeResponse';
import { VeiculoResponse } from '../../../model/responses/VeiculoResponse';
import { CobrancaService } from '../../../services/cobrancas.service';
import { PixService } from '../../../services/pix.service';
import { ValeService } from '../../../services/vale.service';
import { CidadeSelect } from '../../selects/cidade-select/cidade-select';
import { CobradorSelect } from '../../selects/cobrador-select/cobrador-select';
import { VeiculoSelect } from '../../selects/veiculo-select/veiculo-select';

@Component({
  selector: 'app-save-cobranca-modal',
  imports: [
    PrimaryInput,
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
  ],
  templateUrl: './save-cobranca-modal.html',
  styleUrl: './save-cobranca-modal.css',
})
export class SaveCobrancaModal {
  cidadesService = inject(CidadesService);
  funcionarioService = inject(FuncionarioService);
  veiculoService = inject(VeiculoService);
  cobrancaService = inject(CobrancaService);
  pixService = inject(PixService);
  valeService = inject(ValeService);
  modalService = inject(ModalService);

  cobrancaForm: FormGroup = new FormGroup({
    cidade: new FormControl(null, [Validators.required]),
    cobrador: new FormControl(null, [Validators.required]),
    valorEspecie: new FormControl(0),
    valorPix: new FormControl(0),
    valorVales: new FormControl(0),
    valorTotal: new FormControl(0, [Validators.required]),
    data: new FormControl(null, [Validators.required]),
    veiculo: new FormControl(null, [Validators.required]),
    observacoes: new FormControl(null, [Validators.required]),
  });

  pixs: PixRequest[] = [];
  vales: ValeRequest[] = [];

  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<void>();

  ngOnInit() {
    this.cidadesService.buscaCidades();
    this.funcionarioService.buscaFuncionarios();
    this.veiculoService.buscaVeiculos();

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
    this.getControl('valorPix').setValue(totalPix);
    modal.close();
  }

  onAdicionarVale(vale: ValeRequest, modal: HTMLDialogElement) {
    this.vales.push(vale);
    const totalVale = this.vales.reduce((acc, p) => acc + Number(p.valor), 0);
    this.getControl('valorVales').setValue(totalVale);
    modal.close();
  }

  calcularValoresTotais() {
    const especie = Number(this.getControl('valorEspecie').value) || 0;
    const pix = Number(this.getControl('valorPix').value) || 0;
    const vales = Number(this.getControl('valorVales').value) || 0;

    this.getControl('valorTotal').setValue(especie + pix + vales, {
      emitEvent: false,
    });
  }

  onSalvar() {
    if (this.cobrancaForm.invalid) {
      this.cobrancaForm.markAllAsTouched();
      return;
    }

    const formValue = this.cobrancaForm.value;

    const cobrancaRequest: CobrancaRequest = {
      cidadeId: formValue.cidade.id,
      cobradorId: formValue.cobrador.id,
      veiculoId: formValue.veiculo.id,
      data: formValue.data,
      observacoes: formValue.observacoes,
      valorTotalEspecie: formValue.valorEspecie,
      valorTotalPix: formValue.valorPix,
      valorTotalVale: formValue.valorVales,
      valorTotal: formValue.valorTotal,
    };

    this.cobrancaService.salvaCobranca(cobrancaRequest).subscribe({
      next: (cobrancaSalva) => {
        const cobrancaId = cobrancaSalva.id;

        this.salvarPixs(this.pixs, cobrancaId);
        this.salvarVales(this.vales, cobrancaId);

        this.salvar.emit();
        this.cobrancaForm.reset();
        this.pixs = [];
        this.vales = [];
        this.cobrancaService.buscaCobrancas();
      },
      error: () => console.error('Erro ao salvar cobrança'),
    });
  }

  private salvarPixs(pixs: PixRequest[], cobrancaId: number) {
    pixs.forEach((pix) => {
      const pixRequest: PixRequest = {
        cliente: pix.cliente,
        valor: pix.valor,
        data: pix.data,
        cobrancaId: cobrancaId,
        cidadeId: pix.cidadeId,
        comprovante: pix.comprovante,
      };

      this.pixService.salvarPix(pixRequest, pix.comprovante).subscribe({
        error: () => console.error('Erro ao salvar pix!'),
      });
    });
  }

  private salvarVales(vales: ValeRequest[], cobrancaId: number) {
    vales.forEach((vale) => {
      const valeRequest: ValeRequest = {
        valor: vale.valor,
        data: vale.data,
        cobrancaId: cobrancaId,
        funcionarioId: vale.funcionarioId,
        funcionarioNome: vale.funcionarioNome,
        justificativa: vale.justificativa,
      };

      this.valeService.salvarVale(valeRequest).subscribe({
        error: () => console.error('Erro ao salvar vale!'),
      });
    });
  }

  onCancelar() {
    this.cobrancaForm.reset();
    this.pixs = [];
    this.vales = [];
    this.cancelar.emit();
  }
}
