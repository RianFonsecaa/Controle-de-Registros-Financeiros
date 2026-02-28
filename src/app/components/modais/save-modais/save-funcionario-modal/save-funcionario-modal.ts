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
import { FuncionarioRequest } from '../../../../model/requests/FuncionarioRequest';
import { FuncionarioResponse } from '../../../../model/responses/FuncionarioResponse';
import { FuncionarioService } from '../../../../services/http/funcionario.service';
import { ToastService } from '../../../../services/ui/toast.service';
import { PrimaryInput } from '../../../inputs/primary-input/primary-input';
import { CancelButton } from '../../../buttons/cancel-button/cancel-button';
import { SaveButton } from '../../../buttons/save-button/save-button';
import { TelefoneInput } from '../../../inputs/telefone-input/telefone-input';

@Component({
  selector: 'app-save-funcionario-modal',
  imports: [
    PrimaryInput,
    ReactiveFormsModule,
    CancelButton,
    SaveButton,
    TelefoneInput,
  ],
  templateUrl: './save-funcionario-modal.html',
  styleUrl: './save-funcionario-modal.css',
})
export class SaveFuncionarioModal implements OnInit, OnChanges {
  @Input() funcionario!: FuncionarioResponse | null;
  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<void>();

  private funcionarioService = inject(FuncionarioService);
  private toastService = inject(ToastService);

  enviando: boolean = false;

  funcionarioForm: FormGroup = new FormGroup({
    nome: new FormControl(null, [Validators.required, Validators.minLength(3)]),

    telefone: new FormControl(null, [
      Validators.required,
      Validators.minLength(14),
    ]),

    email: new FormControl(null, [Validators.required, Validators.email]),

    dataNascimento: new FormControl(null, [Validators.required]),
  });

  ngOnInit() {
    this.funcionarioForm.reset();
  }

  ngOnChanges() {
    if (this.funcionario) {
      this.preencherFormulario();
    } else {
      this.funcionarioForm.reset();
    }
  }

  onSalvar() {
    if (this.funcionarioForm.invalid || this.enviando) {
      this.funcionarioForm.markAllAsTouched();
      return;
    }

    this.enviando = true;

    const request: FuncionarioRequest = {
      ...this.funcionarioForm.value,
      ativo: this.funcionario?.ativo ?? true,
      id: this.funcionario?.id || null,
    };

    const operacao$ = request.id
      ? this.funcionarioService.atualizaFuncionario(request)
      : this.funcionarioService.salvaFuncionario(request);

    operacao$.subscribe({
      next: () => {
        const mensagem = request.id ? 'atualizado' : 'cadastrado';
        this.toastService.abrir(
          'success',
          `Funcionário ${mensagem} com sucesso!`,
        );
        this.finalizarSucesso();
      },
      error: (error) => {
        this.enviando = false;
        const msgErro = error.error?.message || 'Erro ao processar requisição';
        this.toastService.abrir('error', msgErro);
      },
      complete: () => (this.enviando = false),
    });
  }

  private finalizarSucesso() {
    this.salvar.emit();
    this.funcionarioForm.reset();
    this.funcionarioService.buscaFuncionarios();
  }

  private preencherFormulario() {
    if (!this.funcionario) return;

    this.funcionarioForm.patchValue({
      nome: this.funcionario.nome,
      telefone: this.funcionario.telefone,
      email: this.funcionario.email,
      dataNascimento: this.funcionario.dataNascimento,
    });
  }

  getControl(name: string): FormControl {
    return this.funcionarioForm.get(name) as FormControl;
  }
}
