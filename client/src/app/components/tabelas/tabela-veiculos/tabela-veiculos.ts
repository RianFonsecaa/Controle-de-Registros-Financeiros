import { Component, computed, inject, OnInit, signal } from "@angular/core";
import { VeiculoResponse } from "../../../model/responses/VeiculoResponse";
import { VeiculoService } from "../../../services/http/veiculo.service";
import { ModalService } from "../../../services/ui/modal.service";
import { ToastService } from "../../../services/ui/toast.service";
import { NgClass } from "@angular/common";
import { ToggleStatusModal } from "../../modais/toggle-status-modal/toggle-status-modal";
import { PrimaryAddButton } from "../../buttons/primary-add-button/primary-add-button";
import { SaveVeiculoModal } from "../../modais/save-modais/save-veiculo-modal/save-veiculo-modal";
import { Paginacao } from "../../paginacao/paginacao";
import { BaseTabelaCadastroComponent } from "../../../model/base/BaseTabelaCadastroComponent";

@Component({
  selector: "app-tabela-veiculos",
  imports: [
    NgClass,
    ToggleStatusModal,
    PrimaryAddButton,
    SaveVeiculoModal,
    Paginacao,
  ],
  templateUrl: "./tabela-veiculos.html",
  styleUrl: "./tabela-veiculos.css",
})
export class TabelaVeiculos extends BaseTabelaCadastroComponent<VeiculoResponse> {
  veiculoService = inject(VeiculoService);

  readonly nomeEntidade = "Veículo";
  readonly serviceBase = this.veiculoService;
  readonly listaExibida = this.veiculoService.veiculos;

  carregarDados(): void {
    this.veiculoService.buscaVeiculos();
  }

  toggleStatusVeiculo(modal: HTMLDialogElement): void {
    this.toggleStatusStatusGeral(modal);
  }
}
