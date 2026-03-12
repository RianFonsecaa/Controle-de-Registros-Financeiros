import { Component, inject, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { FuncionarioService } from '../../../services/http/funcionario.service';
import { FuncionarioResponse } from '../../../model/responses/FuncionarioResponse';

@Component({
  selector: 'app-cobrador-select',
  imports: [ReactiveFormsModule],
  templateUrl: './cobrador-select.html',
  styleUrl: './cobrador-select.css',
})
export class CobradorSelect {
  funcionarioService = inject(FuncionarioService);
  funcionarios = this.funcionarioService.funcionariosAtivos;
  @Input() control!: FormControl;

  getMensagemErro(formControl: FormControl): string {
    if (formControl.hasError('required')) {
      return 'Este campo é obrigatório.';
    }
    return '';
  }

  compareById(a: FuncionarioResponse, b: FuncionarioResponse) {
    return a && b ? a.id === b.id : a === b;
  }
}
