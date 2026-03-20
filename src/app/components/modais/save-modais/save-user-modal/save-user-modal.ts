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
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { UsuariosService } from '../../../../services/http/usuarios.service';
import { UserResponse } from '../../../../model/responses/UserResponse';
import { ToastService } from '../../../../services/ui/toast.service';
import { CancelButton } from '../../../buttons/cancel-button/cancel-button';
import { SaveButton } from '../../../buttons/save-button/save-button';
import { PrimaryInput } from '../../../inputs/primary-input/primary-input';
import { NgClass } from '@angular/common';
import { UserRequest, UserRole } from '../../../../model/requests/UserRequest';

@Component({
  selector: 'app-save-user-modal',
  imports: [ReactiveFormsModule, CancelButton, SaveButton, PrimaryInput],
  templateUrl: './save-user-modal.html',
  styleUrl: './save-user-modal.css',
})
export class SaveUserModal implements OnInit, OnChanges {
  @Input() usuario!: UserResponse | null;
  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<void>();

  private usuariosService = inject(UsuariosService);
  private toastService = inject(ToastService);

  enviando = false;

  userForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(3)]),
    login: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl(''),
    role: new FormControl<UserRole>('USER', [Validators.required]),
  });

  ngOnInit() {
    this.resetForm();
  }

  ngOnChanges() {
    if (this.usuario) {
      this.preencherFormulario();
    } else {
      this.resetForm();
    }
  }

  private resetForm() {
    this.userForm.reset({ role: 'USER' });
    this.userForm.get('login')?.enable();

    const passwordControl = this.userForm.get('password');
    passwordControl?.setValidators([
      Validators.required,
      Validators.minLength(6),
    ]);
    passwordControl?.updateValueAndValidity();
  }

  onSalvar() {
    if (this.userForm.invalid || this.enviando) {
      this.userForm.markAllAsTouched();
      return;
    }

    this.enviando = true;
    const formValue = this.userForm.getRawValue() as UserRequest;

    const operacao$ = this.usuario
      ? this.usuariosService.atualizaUsuario(this.usuario.login, formValue)
      : this.usuariosService.registrarUsuario(formValue);

    operacao$.subscribe({
      next: () => {
        const msg = this.usuario ? 'atualizado' : 'cadastrado';
        this.toastService.abrir('success', `Usuário ${msg} com sucesso!`);
        this.finalizarSucesso();
      },
      error: (err) => {
        this.enviando = false;
        this.toastService.abrir(
          'error',
          err.error?.message || 'Erro na operação',
        );
      },
      complete: () => (this.enviando = false),
    });
  }

  private finalizarSucesso() {
    this.salvar.emit();
    this.resetForm();
    this.usuariosService.buscaUsuarios();
  }

  private preencherFormulario() {
    if (!this.usuario) return;

    this.userForm.patchValue({
      name: this.usuario.name,
      login: this.usuario.login,
      role: this.usuario.role,
    });

    this.userForm.get('login')?.disable();

    const passwordControl = this.userForm.get('password');
    passwordControl?.clearValidators();
    passwordControl?.updateValueAndValidity();
  }

  getControl(name: string): FormControl {
    return this.userForm.get(name) as FormControl;
  }
}
