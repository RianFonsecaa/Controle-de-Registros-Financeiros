import { NgComponentOutlet } from '@angular/common';
import { Component, EventEmitter, inject, Output, Type } from '@angular/core';
import { CidadeFiltro } from './filtros/cidade-filtro/cidade-filtro';
import { RegistranteFiltro } from './filtros/registrante-filtro/registrante-filtro';
import { PeriodoFiltro } from './filtros/periodo-filtro/periodo-filtro';
import {
  FormControl,
  FormGroup,
  FormsModule,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { CobradorFiltro } from './filtros/cobrador-filtro/cobrador-filtro';
import { ObservacoesFiltro } from './filtros/observacoes-filtro/observacoes-filtro';
import { ValorTotalFiltro } from './filtros/valor-total-filtro/valor-total-filtro';
import { CobrancaQueryFilters } from '../../model/requests/CobrancaQueryFilters';
import { CidadeResponse } from '../../model/responses/CidadeResponse';
import { FuncionarioResponse } from '../../model/responses/FuncionarioResponse';
import { CobrancaService } from '../../services/http/cobrancas.service';

type FiltroKey = keyof ContainerFiltros['filtrosVisiveis'];

@Component({
  selector: 'app-container-filtros',
  imports: [NgComponentOutlet, FormsModule, ReactiveFormsModule],
  templateUrl: './container-filtros.html',
  styleUrl: './container-filtros.css',
})
export class ContainerFiltros {
  @Output() filtrar = new EventEmitter<CobrancaQueryFilters>();
  cobrancaService = inject(CobrancaService);

  filtroForm: FormGroup = new FormGroup({
    cidadeFiltro: new FormControl(null),
    cobradorFiltro: new FormControl(null),
    observacoesFiltro: new FormControl(null),
    registranteFiltro: new FormControl(null),
    valorInicioFiltro: new FormControl(null),
    valorFimFiltro: new FormControl(null),
    dataInicioFiltro: new FormControl(null),
    dataFimFiltro: new FormControl(null),
  });

  filtrosVisiveis = {
    cidadeFiltro: false,
    registranteFiltro: false,
    periodoFiltro: false,
    cobradorFiltro: false,
    observacoesFiltro: false,
    valorTotalFiltro: false,
  };

  componentes: { key: FiltroKey; component: Type<any> }[] = [
    { key: 'cidadeFiltro', component: CidadeFiltro },
    { key: 'registranteFiltro', component: RegistranteFiltro },
    { key: 'periodoFiltro', component: PeriodoFiltro },
    { key: 'cobradorFiltro', component: CobradorFiltro },
    { key: 'observacoesFiltro', component: ObservacoesFiltro },
    { key: 'valorTotalFiltro', component: ValorTotalFiltro },
  ];

  onFiltrar(): () => void {
    return () => {
      const formValue = this.filtroForm.value;

      const filtros: CobrancaQueryFilters = {
        cidadeId: formValue.cidadeFiltro?.id ?? null,
        cobradorId: formValue.cobradorFiltro?.id ?? null,
        observacoes: formValue.observacoesFiltro,
        usuarioRegistrante: formValue.registranteFiltro,
        dataInicio: formValue.dataInicioFiltro,
        dataFim: formValue.dataFimFiltro,
        valorInicio: formValue.valorInicioFiltro,
        valorFim: formValue.valorFimFiltro,
      };
      this.filtrar.emit(filtros);
    };
  }

  getControl(name: string): FormControl {
    return this.filtroForm.get(name) as FormControl;
  }

  adicionarFiltro(event: Event) {
    const select = event.target as HTMLSelectElement;
    this.filtrosVisiveis[select.value as FiltroKey] = true;
    select.value = '';
  }

  removerFiltro(key: FiltroKey): () => void {
    return () => {
      const controlsParaResetar =
        key === 'valorTotalFiltro'
          ? ['valorInicioFiltro', 'valorFimFiltro']
          : key === 'periodoFiltro'
            ? ['dataInicioFiltro', 'dataFimFiltro']
            : [key];

      const precisaRecarregar = controlsParaResetar.some((name) =>
        this.temValor(this.filtroForm.get(name)?.value),
      );

      controlsParaResetar.forEach((name) => this.filtroForm.get(name)?.reset());

      this.filtrosVisiveis[key] = false;

      if (precisaRecarregar) {
        this.onFiltrar()();
      }
    };
  }

  private temValor(valor: any): boolean {
    return valor !== null && valor !== undefined && valor !== '';
  }

  removerTodosFiltros() {
    for (const key in this.filtrosVisiveis) {
      this.filtrosVisiveis[key as FiltroKey] = false;
      this.filtroForm.get(key)?.reset();
    }

    this.onFiltrar()();
  }

  get possuiFiltrosAtivos(): boolean {
    return Object.values(this.filtrosVisiveis).some((v) => v);
  }
}
