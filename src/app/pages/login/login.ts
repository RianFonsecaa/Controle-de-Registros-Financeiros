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
import { AuthService } from '../../services/auth.service';
import { TokenStorageService } from '../../services/token-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  mensagemErro: string = '';
  exibirToast = signal(false);
  authService = inject(AuthService);
  tokenStorageService = inject(TokenStorageService);
  router = inject(Router);

  loginForm = new FormGroup({
    login: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required),
  });

  getMensagemErro(formControl: FormControl): string {
    if (!formControl.errors) return '';

    if (formControl.hasError('required')) {
      return 'Este campo é obrigatório.';
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
      next: (response: LoginResponse) => {
        this.router.navigate(['home']);
        this.tokenStorageService.setTokens(response);
      },
      error: (error: HttpErrorResponse) => {
        this.mensagemErro = error.error.message || 'Erro inesperado!';
        console.log(this.mensagemErro);
        this.abreErrorToast();
      },
    });
  }

  abreErrorToast() {
    this.exibirToast.set(true);

    setTimeout(() => {
      this.exibirToast.set(false);
    }, 4000);
  }
}
