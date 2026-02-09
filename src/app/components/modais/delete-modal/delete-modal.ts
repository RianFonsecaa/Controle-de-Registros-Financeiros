import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { DeleteButton } from '../../buttons/delete-button/delete-button';
import { CancelButton } from '../../buttons/cancel-button/cancel-button';
import { CobrancaResponse } from '../../../model/responses/CobrancaResponse';
import { ToastService } from '../../../services/ui/toast.service';
import { CobrancaService } from '../../../services/http/cobrancas.service';

@Component({
  selector: 'app-delete-modal',
  imports: [DeleteButton, CancelButton],
  templateUrl: './delete-modal.html',
  styleUrl: './delete-modal.css',
})
export class DeleteModal {
  @Output() deletar = new EventEmitter<void>();
  @Output() cancelar = new EventEmitter<void>();

  @Input() cobranca!: CobrancaResponse | null;

  toastService = inject(ToastService);
  cobrancaService = inject(CobrancaService);

  onDeletar() {
    if (!this.cobranca) return;

    this.cobrancaService.deletaCobranca(this.cobranca.id).subscribe({
      next: () => {
        this.cobrancaService.buscaCobrancas();
        this.toastService.abrir(
          'success',
          'Registro de cobrança foi apagado com sucesso!',
        );
      },
    });
    this.deletar.emit();
  }

  onCancelar() {
    this.cancelar.emit();
  }
}
