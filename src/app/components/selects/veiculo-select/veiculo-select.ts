import { Component, inject, Input } from '@angular/core';
import { VeiculoService } from '../../../services/http/veiculo.service';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { CidadeResponse } from '../../../model/responses/CidadeResponse';
import { VeiculoResponse } from '../../../model/responses/VeiculoResponse';

@Component({
  selector: 'app-veiculo-select',
  imports: [ReactiveFormsModule],
  templateUrl: './veiculo-select.html',
  styleUrl: './veiculo-select.css',
})
export class VeiculoSelect {
  veiculoService = inject(VeiculoService);
  veiculos = this.veiculoService.veiculosAtivos;
  @Input() control!: FormControl;

  ngOnInit() {
    this.veiculoService.buscaVeiculos();
  }

  getMensagemErro(formControl: FormControl): string {
    if (formControl.hasError('required')) {
      return 'Este campo é obrigatório.';
    }
    return '';
  }

  compareById(a: VeiculoResponse, b: VeiculoResponse) {
    return a && b ? a.id === b.id : a === b;
  }
}
