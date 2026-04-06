import { Component, inject, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UsuariosService } from '../../../../services/http/usuarios.service';
import { UserResponse } from '../../../../model/responses/UserResponse';

@Component({
  selector: 'app-registrante-filtro',
  imports: [ReactiveFormsModule],
  templateUrl: './registrante-filtro.html',
  styleUrl: './registrante-filtro.css',
})
export class RegistranteFiltro {
  @Input() form!: FormGroup;
  @Input() onRemover!: () => void;
  @Input() onPreencherValor!: () => void;
  usuarioService = inject(UsuariosService);

  ngOnInit() {
    this.usuarioService.buscaUsuarios();
  }

  compareByLogin(a: UserResponse, b: UserResponse) {
    return a && b ? a.login === b.login : a === b;
  }
}
