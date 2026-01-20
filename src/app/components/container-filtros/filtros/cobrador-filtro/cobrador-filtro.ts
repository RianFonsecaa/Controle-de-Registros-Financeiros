import { Component, inject, Input } from '@angular/core';
import { FuncionarioResponse } from '../../../../model/responses/FuncionarioResponse';
import { FuncionarioService } from '../../../../services/funcionario.service';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-cobrador-filtro',
  imports: [ReactiveFormsModule],
  templateUrl: './cobrador-filtro.html',
  styleUrl: './cobrador-filtro.css',
})
export class CobradorFiltro {
  @Input() form!: FormGroup;
  @Input() onRemover!: () => void;
  @Input() onPreencherValor!: () => void;
  funcionarioService = inject(FuncionarioService);

  ngOnInit() {
    this.funcionarioService.buscaFuncionarios();
  }

  compareById(a: FuncionarioResponse, b: FuncionarioResponse) {
    return a && b ? a.id === b.id : a === b;
  }
}
