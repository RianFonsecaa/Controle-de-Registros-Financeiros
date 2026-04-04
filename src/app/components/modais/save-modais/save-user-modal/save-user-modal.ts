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
import { TokenStorageService } from '../../../../services/auth/token-storage.service';

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
  private tokenStorageService = inject(TokenStorageService);

  enviando = false;

  userForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(3)]),
    login: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
    ]),
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

  onSalvar() {
    if (this.userForm.invalid || this.enviando) {
      this.userForm.markAllAsTouched();
      return;
    }

    const formValue = this.userForm.getRawValue();

    if (this.isTrocaDaPropriaRole(formValue.role as string)) return;

    this.enviando = true;

    const request: UserRequest = {
      ...formValue,
      ativo: this.usuario?.ativo ?? true,
      password: formValue.password || undefined,
    } as UserRequest;

    const operacao$ = this.usuario
      ? this.usuariosService.atualizaUsuario(this.usuario.login, request)
      : this.usuariosService.registrarUsuario(request);

    operacao$.subscribe({
      next: () => {
        console.log(request);
        this.toastService.abrir(
          'success',
          `Usuário ${this.usuario ? 'atualizado' : 'cadastrado'} com sucesso!`,
        );
        this.finalizarSucesso();
      },
      complete: () => (this.enviando = false),
    });
  }

  private isTrocaDaPropriaRole(roleSolicitada: string): boolean {
    const emailLogado = this.tokenStorageService.getPayload()?.email;
    const ehProprioPerfil = this.usuario?.login === emailLogado;

    if (ehProprioPerfil && roleSolicitada !== 'ADMIN') {
      this.toastService.abrir(
        'error',
        'Você não pode alterar o seu próprio nível de acesso!',
      );
      return true;
    }
    return false;
  }

  private finalizarSucesso() {
    this.salvar.emit();
    this.userForm.reset();
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

  private resetForm() {
    this.userForm.reset({
      role: 'USER',
      name: '',
      login: '',
      password: '',
    });

    this.userForm.get('role')?.setValue('USER');
  }

  onCancelar() {
    this.cancelar.emit();
    this.resetForm();
  }
}
