import {
  HttpClient,
  HttpErrorResponse,
  HttpResponse,
} from '@angular/common/http';
import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { LoginResponse } from './LoginResponse';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  mensagemErro: String = '';

  private apiUrl = 'http://localhost:8080/auth/login';

  http = inject(HttpClient);

  loginForm = new FormGroup({
    login: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$'),
    ]),
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

    const formValue = this.loginForm.value as {
      login: string;
      password: string;
    };
    console.log('Dados enviados:', formValue);

    this.http.post<LoginResponse>(this.apiUrl, formValue).subscribe({
      next: (response) => {
        alert('Login Success!');
        console.log('Token recebido:', response.accessToken);
      },
      error: (error: HttpErrorResponse) => {
        alert(error.error.message);
      },
    });
  }
}
