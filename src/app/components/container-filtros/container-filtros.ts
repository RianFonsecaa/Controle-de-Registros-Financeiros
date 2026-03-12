import { NgComponentOutlet, UpperCasePipe } from '@angular/common';
import {
  Component,
  EventEmitter,
  inject,
  Output,
  Type,
  OnInit,
  Input,
} from '@angular/core';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';

export type FiltroKey =
  | 'cidadeFiltro'
  | 'registranteFiltro'
  | 'periodoFiltro'
  | 'cobradorFiltro'
  | 'observacoesFiltro'
  | 'clienteFiltro'
  | 'valorFiltro';

export interface FiltroConfig {
  key: FiltroKey;
  visivel: boolean;
  componente: Type<any>;
  label: string;
  controlsParaResetar?: string[];
}

@Component({
  selector: 'app-container-filtros',
  standalone: true,
  imports: [NgComponentOutlet, FormsModule, ReactiveFormsModule, UpperCasePipe],
  templateUrl: './container-filtros.html',
  styleUrl: './container-filtros.css',
})
export class ContainerFiltros {
  @Input() titulo: string = 'FILTRAGEM';
  @Input() filtroForm!: FormGroup;
  @Input() configuracaoFiltros: FiltroConfig[] = [];

  @Output() filtrar = new EventEmitter<any>();

  onFiltrar(): void {
    this.filtrar.emit(this.filtroForm.value);
  }

  adicionarFiltro(event: Event) {
    const select = event.target as HTMLSelectElement;
    const key = select.value;

    const filtro = this.configuracaoFiltros.find((f) => f.key === key);
    if (filtro) {
      filtro.visivel = true;
    }
    select.value = '';
  }

  removerFiltro(key: string) {
    const filtro = this.configuracaoFiltros.find((f) => f.key === key);
    if (!filtro) return;

    const controls = filtro.controlsParaResetar || [key];

    const precisaRecarregar = controls.some((name: any) =>
      this.temValor(this.filtroForm.get(name)?.value),
    );

    controls.forEach((name: any) => this.filtroForm.get(name)?.reset());
    filtro.visivel = false;

    if (precisaRecarregar) {
      this.onFiltrar();
    }
  }

  removerTodosFiltros() {
    this.configuracaoFiltros.forEach((filtro) => {
      if (filtro.visivel) {
        const controls = filtro.controlsParaResetar || [filtro.key];
        controls.forEach((name: any) => this.filtroForm.get(name)?.reset());
        filtro.visivel = false;
      }
    });
    this.onFiltrar();
  }

  private temValor(valor: any): boolean {
    return valor !== null && valor !== undefined && valor !== '';
  }

  get possuiFiltrosAtivos(): boolean {
    return this.configuracaoFiltros.some((f) => f.visivel);
  }
}
