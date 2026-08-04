import {
  Component,
  computed,
  effect,
  inject,
  OnInit,
  signal,
} from "@angular/core";
import { FuncionarioResponse } from "../../../model/responses/FuncionarioResponse";
import { FuncionarioService } from "../../../services/http/funcionario.service";
import { ModalService } from "../../../services/ui/modal.service";
import { ToastService } from "../../../services/ui/toast.service";
import { NgClass } from "@angular/common";
import { PrimaryAddButton } from "../../buttons/primary-add-button/primary-add-button";
import { ToggleStatusModal } from "../../modais/toggle-status-modal/toggle-status-modal";
import { SaveFuncionarioModal } from "../../modais/save-modais/save-funcionario-modal/save-funcionario-modal";
import { Paginacao } from "../../paginacao/paginacao";
import { BaseTabelaCadastroComponent } from "../../../model/base/BaseTabelaCadastroComponent";

@Component({
  selector: "app-tabela-funcionarios",
  imports: [
    NgClass,
    PrimaryAddButton,
    ToggleStatusModal,
    SaveFuncionarioModal,
    Paginacao,
  ],
  templateUrl: "./tabela-funcionarios.html",
  styleUrl: "./tabela-funcionarios.css",
})
export class TabelaFuncionarios extends BaseTabelaCadastroComponent<FuncionarioResponse> {
  funcionarioService = inject(FuncionarioService);

  readonly nomeEntidade = "Funcionário";
  readonly serviceBase = this.funcionarioService;
  readonly listaExibida = this.funcionarioService.funcionarios;

  carregarDados(): void {
    this.funcionarioService.buscaFuncionarios();
  }

  toggleStatusFuncionario(modal: HTMLDialogElement): void {
    this.toggleStatusStatusGeral(modal);
  }
}
