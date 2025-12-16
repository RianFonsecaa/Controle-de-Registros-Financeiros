import { Component, inject } from '@angular/core';
import { CobrancaResponse } from '../../model/responses/CobrancaResponse';
import { CobrancaService } from '../../services/cobrancas.service';
import { CurrencyPipe, DatePipe, NgFor } from '@angular/common';

@Component({
  selector: 'app-tabela',
  imports: [CurrencyPipe, DatePipe],
  templateUrl: './tabela.html',
  styleUrl: './tabela.css',
})
export class Tabela {
  private cobrancaService = inject(CobrancaService);
  cobrancas = this.cobrancaService.cobrancas;

  ngOnInit() {
    this.cobrancaService.carregar();
  }
}
