import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { PrimaryInput } from '../../inputs/primary-input/primary-input';
import {
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { PrimarySelect } from '../../inputs/primary-select/primary-select';
import { CidadesService } from '../../../services/cidades.service';
import { FuncionarioService } from '../../../services/funcionario.service';
import { VeiculoService } from '../../../services/veiculo.service';
import { MoneyInput } from '../../inputs/money-input/money-input';
import { CancelButton } from '../../buttons/cancel-button/cancel-button';
import { DeleteButton } from '../../buttons/delete-button/delete-button';
import { AddButton } from '../../buttons/add-button/add-button';
import { SavePixModal } from '../save-pix-modal/save-pix-modal';
import { ModalService } from '../../../services/modal.service';
import { PixRequest } from '../../../model/requests/PixRequest';
import { DecimalPipe } from '@angular/common';
import { SaveButton } from '../../buttons/save-button/save-button';
import { ValeRequest } from '../../../model/requests/ValeRequest';
import { SaveValeModal } from '../save-vale-modal/save-vale-modal';
import { CobrancaRequest } from '../../../model/requests/CobrancaRequest';
import { FuncionarioResponse } from '../../../model/responses/FuncionarioResponse';
import { CidadeResponse } from '../../../model/responses/CidadeResponse';
import { VeiculoResponse } from '../../../model/responses/VeiculoResponse';
import { SelectOptions } from '../../../model/responses/SelectOptions';
import { CobrancaService } from '../../../services/cobrancas.service';
import { PixService } from '../../../services/pix.service';

@Component({
  selector: 'app-save-cobranca-modal',
  imports: [
    PrimaryInput,
    ReactiveFormsModule,
    PrimarySelect,
    MoneyInput,
    CancelButton,
    AddButton,
    SavePixModal,
    SaveValeModal,
    DecimalPipe,
    SaveButton,
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

  modalService = inject(ModalService);

  cobrancaForm!: FormGroup;

  pixs: PixRequest[] = [];
  vales: ValeRequest[] = [];

  @Output() deletar = new EventEmitter<void>();
  @Output() cancelar = new EventEmitter<void>();

  ngOnInit() {
    this.cobrancaForm = new FormGroup({
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
    this.cidadesService.buscaCidades();
    this.funcionarioService.buscaFuncionarios();
    this.veiculoService.buscaVeiculos();

    this.cobrancaForm.valueChanges.subscribe(() => {
      this.calcularTotal();
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

  calcularTotal() {
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

    const cidade: CidadeResponse = this.getControl('cidade').value;
    const cobrador: FuncionarioResponse = this.getControl('cobrador').value;
    const veiculo: VeiculoResponse = this.getControl('veiculo').value;

    const cobrancaRequest: CobrancaRequest = {
      cidadeId: cidade.id,
      cobradorId: cobrador.id,
      veiculoId: veiculo.id,
      data: this.getControl('data').value,
      observacoes: this.getControl('observacoes').value,

      valorTotalEspecie: Number(this.getControl('valorEspecie').value),
      valorTotalPix: Number(this.getControl('valorPix').value),
      valorTotalVale: Number(this.getControl('valorVales').value),
      valorTotal: Number(this.getControl('valorTotal').value),
      vales: this.vales,
    };

    this.cobrancaService.salvaCobranca(cobrancaRequest).subscribe({
      next: (cobrancaSalva) => {
        console.log('Cobrança salva com sucesso');
        const cobrancaId = cobrancaSalva.id;

        this.pixs.forEach((pix) => {
          const pixRequest: PixRequest = {
            cliente: pix.cliente,
            valor: pix.valor,
            data: pix.data,
            cobrancaId: cobrancaId,
            cidadeId: pix.cidadeId,
            comprovante: pix.comprovante,
          };

          console.log(pixRequest);
          this.pixService.salvarPix(pixRequest, pix.comprovante).subscribe();
        });

        this.cobrancaForm.reset();
        this.pixs = [];
        this.vales = [];
      },
      error: () => console.error('Erro ao salvar cobrança'),
    });
  }

  onCancelar() {
    this.cobrancaForm.reset();
    this.pixs = [];
    this.vales = [];
    this.cancelar.emit();
  }

  onDeletar() {
    this.deletar.emit();
  }
}
