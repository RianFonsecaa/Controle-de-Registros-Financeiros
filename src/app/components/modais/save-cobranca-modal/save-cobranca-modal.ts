import { Component, inject } from '@angular/core';
import { PrimaryInput } from '../../primary-input/primary-input';
import {
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { PrimarySelect } from '../../primary-select/primary-select';
import { CidadesService } from '../../../services/cidades.service';
import { FuncionarioService } from '../../../services/funcionario.service';
import { VeiculoService } from '../../../services/veiculo.service';
import { MoneyInput } from '../../money-input/money-input';

@Component({
  selector: 'app-save-cobranca-modal',
  imports: [PrimaryInput, ReactiveFormsModule, PrimarySelect, MoneyInput],
  templateUrl: './save-cobranca-modal.html',
  styleUrl: './save-cobranca-modal.css',
})
export class SaveCobrancaModal {
  private cidadesService = inject(CidadesService);
  cidades = this.cidadesService.cidades;
  private funcionarioService = inject(FuncionarioService);
  cobradores = this.funcionarioService.cobradores;
  private veiculoService = inject(VeiculoService);
  veiculos = this.veiculoService.veiculos;
  cobrancaForm!: FormGroup;

  ngOnInit() {
    this.cidadesService.buscaCidades();
    this.funcionarioService.buscaFuncionarios();
    this.veiculoService.buscaVeiculos();
  }

  constructor() {
    this.cobrancaForm = new FormGroup({
      cidade: new FormControl<string | null>('Cidade'),

      cobrador: new FormControl<string | null>(null),

      valorEspecie: new FormControl<number | null>(0),

      valorPix: new FormControl<number | null>(0),

      valorVales: new FormControl<number | null>(0),

      valorTotal: new FormControl<number | null>(0),

      data: new FormControl<string | null>(null),

      veiculo: new FormControl<string | null>(null),

      observacoes: new FormControl<string | null>(null),
    });
  }

  getControl(name: string): FormControl {
    return this.cobrancaForm.get(name) as FormControl;
  }
}
