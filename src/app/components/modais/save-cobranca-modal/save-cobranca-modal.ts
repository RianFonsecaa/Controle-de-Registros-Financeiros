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
    DecimalPipe,
    SaveButton,
  ],
  templateUrl: './save-cobranca-modal.html',
  styleUrl: './save-cobranca-modal.css',
})
export class SaveCobrancaModal {
  private modalService = inject(ModalService);

  cidadesService = inject(CidadesService);
  funcionarioService = inject(FuncionarioService);
  veiculoService = inject(VeiculoService);

  cobrancaForm!: FormGroup;

  pixs: PixRequest[] = [];

  @Output() deletar = new EventEmitter<void>();
  @Output() cancelar = new EventEmitter<void>();
  @Input() modalId = '';

  ngOnInit() {
    this.cobrancaForm = new FormGroup({
      cidade: new FormControl(null),
      cobrador: new FormControl(null),
      valorEspecie: new FormControl(0),
      valorPix: new FormControl(0),
      valorVales: new FormControl(0),
      valorTotal: new FormControl(0),
      data: new FormControl(null),
      veiculo: new FormControl(null),
      observacoes: new FormControl(null),
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

  calcularTotal() {
    const especie = Number(this.getControl('valorEspecie').value) || 0;
    const pix = Number(this.getControl('valorPix').value) || 0;
    const vales = Number(this.getControl('valorVales').value) || 0;

    this.getControl('valorTotal').setValue(especie + pix + vales, {
      emitEvent: false,
    });
  }

  onSalvar() {}

  onAddPix(modal: HTMLDialogElement) {
    this.modalService.abrirModal(modal);
  }

  onCancelar() {
    this.cobrancaForm.reset();
    this.pixs = [];
    this.cancelar.emit();
  }

  onDeletar() {
    this.deletar.emit();
  }
}
