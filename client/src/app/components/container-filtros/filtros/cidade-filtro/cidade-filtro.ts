import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { CidadeSelect } from '../../../selects/cidade-select/cidade-select';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CidadeResponse } from '../../../../model/responses/CidadeResponse';
import { CidadesService } from '../../../../services/http/cidades.service';

@Component({
  selector: 'app-cidade-filtro',
  imports: [ReactiveFormsModule],
  templateUrl: './cidade-filtro.html',
  styleUrl: './cidade-filtro.css',
})
export class CidadeFiltro {
  @Input() form!: FormGroup;
  @Input() onRemover!: () => void;
  @Input() onPreencherValor!: () => void;
  cidadesService = inject(CidadesService);

  ngOnInit() {
    this.cidadesService.buscaCidades();
  }

  compareById(a: CidadeResponse, b: CidadeResponse) {
    return a && b ? a.id === b.id : a === b;
  }
}
