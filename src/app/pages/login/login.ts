import {
  HttpClient,
  HttpErrorResponse,
  HttpResponse,
} from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { LoginResponse } from '../../model/responses/LoginResponse';
import { LoginRequest } from '../../model/requests/LoginRequest';
import { Router } from '@angular/router';
import { TokenStorageService } from '../../services/auth/token-storage.service';
import { AuthService } from '../../services/auth/auth.service';
import { ToastService } from '../../services/ui/toast.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  mensagemErroLogin: string = '';

  authService = inject(AuthService);
  tokenStorageService = inject(TokenStorageService);
  router = inject(Router);
  toastService = inject(ToastService);

  loginForm = new FormGroup({
    login: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required),
  });

  getMensagemErro(formControl: FormControl): string {
    if (formControl.hasError('required')) {
      return 'Este campo é obrigatório.';
    }

    if (formControl.hasError('email')) {
      return 'Formato de E-mail inválido.';
    }

    if (formControl.hasError('pattern')) {
      return 'Formato inválido.';
    }

    return '';
  }

  onLogin() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const loginRequest = this.loginForm.value as LoginRequest;

    this.authService.login(loginRequest).subscribe({
      next: () => {
        this.router.navigate(['dashboard']);
      },
      error: (error: HttpErrorResponse) => {
        this.mensagemErroLogin = error.error.message || 'Erro inesperado!';
        this.toastService.open('error', this.mensagemErroLogin);
      },
    });
  }
}
