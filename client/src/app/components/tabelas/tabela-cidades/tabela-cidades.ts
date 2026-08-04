import { Component, computed, inject, OnInit, signal } from "@angular/core";
import { PrimaryAddButton } from "../../buttons/primary-add-button/primary-add-button";
import { CidadesService } from "../../../services/http/cidades.service";
import { ModalService } from "../../../services/ui/modal.service";
import { ToastService } from "../../../services/ui/toast.service";
import { CidadeResponse } from "../../../model/responses/CidadeResponse";
import { DeleteModal } from "../../modais/delete-modal/delete-modal";
import { SaveCidadeModal } from "../../modais/save-modais/save-cidade-modal/save-cidade-modal";
import { NgClass } from "@angular/common";
import { ToggleStatusModal } from "../../modais/toggle-status-modal/toggle-status-modal";
import { Paginacao } from "../../paginacao/paginacao";
import { BaseTabelaCadastroComponent } from "../../../model/base/BaseTabelaCadastroComponent";

@Component({
  selector: "app-tabela-cidades",
  imports: [
    PrimaryAddButton,
    SaveCidadeModal,
    NgClass,
    ToggleStatusModal,
    Paginacao,
  ],
  templateUrl: "./tabela-cidades.html",
  styleUrl: "./tabela-cidades.css",
})
export class TabelaCidades extends BaseTabelaCadastroComponent<CidadeResponse> {
  cidadesService = inject(CidadesService);

  readonly nomeEntidade = "Cidade";
  readonly serviceBase = this.cidadesService;
  readonly listaExibida = this.cidadesService.cidades;

  carregarDados(): void {
    this.cidadesService.buscaCidades();
  }

  toggleStatusCidade(modal: HTMLDialogElement): void {
    this.toggleStatusStatusGeral(modal);
  }
}
