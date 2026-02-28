import { Component, Type } from '@angular/core';
import { TabConfig, Tabs } from '../../components/tabs/tabs';
import { TabelaCobrancas } from '../../components/tabelas/tabela-cobrancas/tabela-cobrancas';
import { TituloPagina } from '../../components/titulo-pagina/titulo-pagina';
import { PaginaEmManutencao } from '../../components/pagina-em-manutencao/pagina-em-manutencao';
import { NgComponentOutlet } from '@angular/common';

@Component({
  selector: 'app-registros',
  imports: [TituloPagina, Tabs, NgComponentOutlet],
  templateUrl: './registros.html',
  styleUrl: './registros.css',
})
export class Registros {
  componenteAtivo: Type<any> | null = null;

  configuracaoAbas: TabConfig[] = [
    { label: 'Cobranças', componente: TabelaCobrancas },
    { label: 'Pix', componente: PaginaEmManutencao },
    { label: 'Vales', componente: PaginaEmManutencao },
  ];

  setComponenteAtivo(comp: Type<any> | null) {
    this.componenteAtivo = comp;
  }
}
