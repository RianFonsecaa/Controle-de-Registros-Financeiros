import { Component, EventEmitter, inject, Output } from "@angular/core";
import { UserRequest } from "../../../model/requests/UserRequest";
import { UsuariosService } from "../../../services/http/usuarios.service";
import { UserResponse } from "../../../model/responses/UserResponse";
import { FormControl, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { CancelButton } from "../../buttons/cancel-button/cancel-button";
import { SaveButton } from "../../buttons/save-button/save-button";

@Component({
  selector: "app-select-user-modal",
  imports: [ReactiveFormsModule, CancelButton, SaveButton],
  templateUrl: "./select-user-modal.html",
  styleUrl: "./select-user-modal.css",
})
export class SelectUserModal {
  usuariosAdicionados: UserResponse[] = [];

  @Output() cancelar = new EventEmitter<void>();
  @Output() enviar = new EventEmitter<string[]>();

  usuarioService = inject(UsuariosService);

  whatsappListForm = new FormGroup({
    usuarioSelecionado: new FormControl<UserResponse | null>(null),
  });

  ngOnInit() {
    this.usuarioService.buscaUsuarios();

    this.whatsappListForm
      .get("usuarioSelecionado")
      ?.valueChanges.subscribe((user) => {
        this.adicionarUser(user ?? undefined);
      });
  }

  adicionarUser(user: UserResponse | undefined) {
    if (!user) return;

    const usuarioJaAdicionado = this.usuariosAdicionados.some(
      (usuario) => usuario.login === user.login,
    );

    if (usuarioJaAdicionado) return;

    this.usuariosAdicionados.push(user);
    console.log(this.usuariosAdicionados);
  }

  formatarTelefone(telefone: string): string {
    const numero = telefone.replace(/\D/g, "");

    if (numero.length === 11) {
      return numero.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
    }

    return telefone;
  }

  onCancelar() {
    this.cancelar.emit();
    this.whatsappListForm.reset();
    this.usuariosAdicionados = [];
  }

  onEnviar() {
    const listaNumeros = this.usuariosAdicionados.map((user) => user.telefone);

    this.enviar.emit(listaNumeros);
    this.usuariosAdicionados = [];
    this.whatsappListForm.reset();
  }
}
