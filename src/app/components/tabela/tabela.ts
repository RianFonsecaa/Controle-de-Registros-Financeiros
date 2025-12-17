import { Component, ElementRef, inject, ViewChild } from '@angular/core';
import { CobrancaResponse } from '../../model/responses/CobrancaResponse';
import { CobrancaService } from '../../services/cobrancas.service';
import { CurrencyPipe, DatePipe, NgFor } from '@angular/common';
import { DeleteModal } from '../modais/delete-modal/delete-modal';
import { CreateModal } from '../modais/create-modal/create-modal';

@Component({
  selector: 'app-tabela',
  imports: [CurrencyPipe, DatePipe, DeleteModal],
  templateUrl: './tabela.html',
  styleUrl: './tabela.css',
})
export class Tabela {
  private cobrancaService = inject(CobrancaService);
  cobrancas = this.cobrancaService.cobrancas;

  cobrancaSelecionada: CobrancaResponse | null = null;

  ngOnInit() {
    this.cobrancaService.buscaCobrancas();
  }

  deletaCobranca(modal: HTMLDialogElement) {
    if (!this.cobrancaSelecionada) return;

    this.cobrancaService.deletaCobranca(this.cobrancaSelecionada.id).subscribe({
      next: () => {
        this.cobrancaService.buscaCobrancas();
        this.fecharModal(modal);
      },
    });
  }

  abrirModal(cobranca: CobrancaResponse, modal: HTMLDialogElement) {
    this.cobrancaSelecionada = cobranca;
    modal.showModal();
  }

  fecharModal(modal: HTMLDialogElement) {
    this.cobrancaSelecionada = null;
    modal.close();
  }
}
