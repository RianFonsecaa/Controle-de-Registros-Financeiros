import { Component, inject, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { CidadeResponse } from '../../../model/responses/CidadeResponse';
import { CidadesService } from '../../../services/http/cidades.service';

@Component({
  selector: 'app-cidade-select',
  imports: [ReactiveFormsModule],
  templateUrl: './cidade-select.html',
  styleUrl: './cidade-select.css',
})
export class CidadeSelect {
  cidadesService = inject(CidadesService);
  @Input() control!: FormControl;

  ngOnInit() {
    this.cidadesService.buscaCidades();
  }

  getMensagemErro(formControl: FormControl): string {
    if (formControl.hasError('required')) {
      return 'Este campo é obrigatório.';
    }
    return '';
  }

  compareById(a: CidadeResponse, b: CidadeResponse) {
    return a && b ? a.id === b.id : a === b;
  }
}
