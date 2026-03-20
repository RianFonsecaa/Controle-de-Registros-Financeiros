import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnChanges,
  OnInit,
  Output,
} from '@angular/core';
import {
  FormGroup,
  FormControl,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { VeiculoRequest } from '../../../../model/requests/VeiculoRequest';
import { VeiculoResponse } from '../../../../model/responses/VeiculoResponse';
import { VeiculoService } from '../../../../services/http/veiculo.service';
import { ToastService } from '../../../../services/ui/toast.service';
import { SaveButton } from '../../../buttons/save-button/save-button';
import { CancelButton } from '../../../buttons/cancel-button/cancel-button';
import { PrimaryInput } from '../../../inputs/primary-input/primary-input';

@Component({
  selector: 'app-save-veiculo-modal',
  imports: [SaveButton, CancelButton, ReactiveFormsModule, PrimaryInput],
  templateUrl: './save-veiculo-modal.html',
  styleUrl: './save-veiculo-modal.css',
})
export class SaveVeiculoModal implements OnInit, OnChanges {
  @Input() veiculo!: VeiculoResponse | null;
  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<void>();

  private veiculoService = inject(VeiculoService);
  private toastService = inject(ToastService);

  enviando: boolean = false;

  veiculoForm: FormGroup = new FormGroup({
    modelo: new FormControl(null, [
      Validators.required,
      Validators.minLength(2),
    ]),
    placa: new FormControl(null, [
      Validators.required,
      Validators.pattern(/^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$/i),
    ]),
  });

  ngOnInit() {
    this.veiculoForm.reset();
  }

  ngOnChanges() {
    if (this.veiculo) {
      this.preencherFormulario();
    } else {
      this.veiculoForm.reset();
    }
  }

  onSalvar() {
    if (this.veiculoForm.invalid || this.enviando) {
      this.veiculoForm.markAllAsTouched();
      return;
    }

    this.enviando = true;

    const request: VeiculoRequest = {
      ...this.veiculoForm.value,
      placa: this.veiculoForm.value.placa.toUpperCase(),
      ativo: this.veiculo ? this.veiculo.ativo : true,
      id: this.veiculo?.id || null,
    };

    const operacao$ = request.id
      ? this.veiculoService.atualizaVeiculo(request)
      : this.veiculoService.salvaVeiculo(request);

    operacao$.subscribe({
      next: () => {
        const mensagem = request.id ? 'atualizado' : 'cadastrado';
        this.toastService.abrir('success', `Veículo ${mensagem} com sucesso!`);
        this.finalizarSucesso();
      },
      error: (error) => {
        this.enviando = false;
        this.toastService.abrir(
          'error',
          error.error?.message || 'Erro ao salvar veículo',
        );
      },
      complete: () => (this.enviando = false),
    });
  }

  private finalizarSucesso() {
    this.salvar.emit();
    this.veiculoForm.reset();
    this.veiculoService.buscaVeiculos();
  }

  private preencherFormulario() {
    if (!this.veiculo) return;
    this.veiculoForm.patchValue({
      modelo: this.veiculo.modelo,
      placa: this.veiculo.placa,
    });
  }

  getControl(name: string): FormControl {
    return this.veiculoForm.get(name) as FormControl;
  }
}
