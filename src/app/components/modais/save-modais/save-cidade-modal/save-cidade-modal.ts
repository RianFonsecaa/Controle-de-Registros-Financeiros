import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { CidadeResponse } from '../../../../model/responses/CidadeResponse';
import {
  FormGroup,
  FormControl,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { PrimaryInput } from '../../../inputs/primary-input/primary-input';
import { CancelButton } from '../../../buttons/cancel-button/cancel-button';
import { SaveButton } from '../../../buttons/save-button/save-button';
import { CidadeRequest } from '../../../../model/requests/CidadeRequest';
import { CidadesService } from '../../../../services/http/cidades.service';
import { ToastService } from '../../../../services/ui/toast.service';

@Component({
  selector: 'app-save-cidade-modal',
  imports: [PrimaryInput, ReactiveFormsModule, CancelButton, SaveButton],
  templateUrl: './save-cidade-modal.html',
  styleUrl: './save-cidade-modal.css',
})
export class SaveCidadeModal {
  @Input() cidade!: CidadeResponse | null;
  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<void>();

  private cidadeService = inject(CidadesService);
  private toastService = inject(ToastService);

  enviando: boolean = false;

  cidadeForm: FormGroup = new FormGroup({
    nome: new FormControl(null, Validators.required),
  });

  ngOnInit() {
    this.cidadeForm.reset();
  }

  ngOnChanges() {
    if (this.cidade) {
      this.preencherFormulario();
    } else {
      this.cidadeForm.reset();
    }
  }

  onSalvar() {
    if (this.cidadeForm.invalid || this.enviando) {
      this.cidadeForm.markAllAsTouched();
      return;
    }

    this.enviando = true;

    const request: CidadeRequest = {
      ...this.cidadeForm.value,
      ativo: this.cidade?.ativo,
      id: this.cidade?.id || null,
    };

    const operacao$ = request.id
      ? this.cidadeService.atualizaCidade(request)
      : this.cidadeService.salvaCidade(request);

    operacao$.subscribe({
      next: () => {
        const mensagem = request.id ? 'atualizada' : 'cadastrada';
        this.toastService.abrir('success', `Cidade ${mensagem} com sucesso!`);
        this.finalizarSucesso();
      },
      complete: () => (this.enviando = false),
    });
  }

  private finalizarSucesso() {
    this.salvar.emit();
    this.cidadeForm.reset();
    this.cidadeService.buscaCidades();
  }

  private preencherFormulario() {
    if (!this.cidade) return;

    this.cidadeForm.patchValue({
      nome: this.cidade.nome,
    });
  }

  getControl(name: string): FormControl {
    return this.cidadeForm.get(name) as FormControl;
  }
}
