import { Component, Type } from '@angular/core';
import { PaginaEmManutencao } from '../../components/pagina-em-manutencao/pagina-em-manutencao';
import { TabelaCidades } from '../../components/tabelas/tabela-cidades/tabela-cidades';
import { TituloPagina } from '../../components/titulo-pagina/titulo-pagina';
import { TabConfig, Tabs } from '../../components/tabs/tabs';
import { CancelButton } from '../../components/buttons/cancel-button/cancel-button';
import { SaveButton } from '../../components/buttons/save-button/save-button';
import { NgComponentOutlet } from '@angular/common';
import { TabelaFuncionarios } from '../../components/tabelas/tabela-funcionarios/tabela-funcionarios';
import { TabelaVeiculos } from '../../components/tabelas/tabela-veiculos/tabela-veiculos';

@Component({
  selector: 'app-cadastros',
  imports: [Tabs, NgComponentOutlet, TituloPagina],
  templateUrl: './cadastros.html',
  styleUrl: './cadastros.css',
})
export class Cadastros {
  componenteAtivo: Type<any> | null = null;

  configuracaoAbas: TabConfig[] = [
    { label: 'Cidades', componente: TabelaCidades },
    { label: 'Veículos', componente: TabelaVeiculos },
    { label: 'Funcionários', componente: TabelaFuncionarios },
  ];

  setComponenteAtivo(comp: Type<any> | null) {
    this.componenteAtivo = comp;
  }
}
