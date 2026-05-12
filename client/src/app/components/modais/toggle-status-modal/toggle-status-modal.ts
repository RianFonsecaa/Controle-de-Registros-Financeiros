import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CobrancaResponse } from '../../../model/responses/CobrancaResponse';
import { CancelButton } from '../../buttons/cancel-button/cancel-button';
import { DeleteButton } from '../../buttons/delete-button/delete-button';
import { ConfirmButton } from '../../buttons/confirm-button/confirm-button';

@Component({
  selector: 'app-toggle-status-modal',
  imports: [CancelButton, ConfirmButton],
  templateUrl: './toggle-status-modal.html',
  styleUrl: './toggle-status-modal.css',
})
export class ToggleStatusModal {
  @Output() confirmar = new EventEmitter<void>();
  @Output() cancelar = new EventEmitter<void>();

  @Input() toggleStatus!: boolean;
  @Input() tipoCadastro!: string;
}
