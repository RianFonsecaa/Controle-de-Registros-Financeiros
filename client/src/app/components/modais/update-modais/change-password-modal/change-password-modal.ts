import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { UserResponse } from '../../../../model/responses/UserResponse';
import { UsuariosService } from '../../../../services/http/usuarios.service';
import { ToastService } from '../../../../services/ui/toast.service';
import { CancelButton } from '../../../buttons/cancel-button/cancel-button';
import { PrimaryInput } from '../../../inputs/primary-input/primary-input';
import { SaveButton } from '../../../buttons/save-button/save-button';

@Component({
  selector: 'app-change-password-modal',
  imports: [ReactiveFormsModule, CancelButton, PrimaryInput, SaveButton],
  templateUrl: './change-password-modal.html',
  styleUrl: './change-password-modal.css',
})
export class ChangePasswordModal {
  @Input() usuario!: UserResponse | null;
  @Output() cancelar = new EventEmitter<void>();
  @Output() confirmar = new EventEmitter<void>();

  private usuariosService = inject(UsuariosService);
  private toastService = inject(ToastService);

  enviando = false;

  passwordForm = new FormGroup(
    {
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(6),
      ]),
      confirmPassword: new FormControl('', [Validators.required]),
    },
    { validators: this.mustMatch },
  );

  mustMatch(group: any) {
    const password = group.get('password').value;
    const confirm = group.get('confirmPassword').value;
    return password === confirm ? null : { mismatch: true };
  }

  onConfirmar() {
    if (this.passwordForm.invalid || this.enviando || !this.usuario) {
      this.passwordForm.markAllAsTouched();
      return;
    }

    this.enviando = true;
    const novaSenha = this.passwordForm.get('password')?.value!;

    this.usuariosService.mudarSenha(this.usuario.login, novaSenha).subscribe({
      next: () => {
        this.toastService.abrir('success', 'Senha alterada com sucesso!');
        this.passwordForm.reset();
        this.confirmar.emit();
      },
      error: () => {
        this.enviando = false;
        this.toastService.abrir('error', 'Erro ao alterar a senha.');
      },
      complete: () => (this.enviando = false),
    });
  }

  getControl(name: string): FormControl {
    return this.passwordForm.get(name) as FormControl;
  }
}
