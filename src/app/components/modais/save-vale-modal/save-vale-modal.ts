import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { FuncionarioService } from '../../../services/funcionario.service';
import { PrimaryInput } from '../../inputs/primary-input/primary-input';
import { PrimarySelect } from '../../inputs/primary-select/primary-select';
import { MoneyInput } from '../../inputs/money-input/money-input';
import { CancelButton } from '../../buttons/cancel-button/cancel-button';
import { SaveButton } from '../../buttons/save-button/save-button';
import { ValeRequest } from '../../../model/requests/ValeRequest';
import { CidadeResponse } from '../../../model/responses/CidadeResponse';

@Component({
  selector: 'app-save-vale-modal',
  imports: [
    PrimaryInput,
    PrimarySelect,
    MoneyInput,
    CancelButton,
    SaveButton,
    ReactiveFormsModule,
  ],
  templateUrl: './save-vale-modal.html',
  styleUrl: './save-vale-modal.css',
})
export class SaveValeModal {
  funcionarioService = inject(FuncionarioService);
  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<any>();
  @Input() data!: String;
  @Input() cidade!: CidadeResponse;
  valeForm!: FormGroup;

  ngOnInit() {
    this.valeForm = new FormGroup({
      funcionario: new FormControl(null, [Validators.required]),
      justificativa: new FormControl(null, [Validators.required]),
      valor: new FormControl(0, [Validators.required]),
      data: new FormControl(null, [Validators.required]),
    });
    this.funcionarioService.buscaFuncionarios();
  }

  ngOnChanges() {
    if (!this.valeForm) return;
    this.aplicarValorInicial();
  }

  getControl(name: string): FormControl {
    return this.valeForm.get(name) as FormControl;
  }

  onCancelar() {
    this.cancelar.emit();
    this.valeForm.reset();
    this.aplicarValorInicial();
  }

  onSalvar() {
    if (this.valeForm.invalid) {
      this.valeForm.markAllAsTouched();
      return;
    }
    const formValue = this.valeForm.value;
    const vale: ValeRequest = {
      funcionarioId: formValue.funcionario.id,
      funcionarioNome: formValue.funcionario.nome,
      cobrancaId: 0,
      justificativa: formValue.justificativa,
      valor: formValue.valor,
      data: formValue.data,
    };
    this.salvar.emit(vale);
    this.valeForm.reset();
    this.aplicarValorInicial();
  }

  private aplicarValorInicial() {
    this.valeForm.patchValue({
      data: this.data ?? null,
    });
  }
}
