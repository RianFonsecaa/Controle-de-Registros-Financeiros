import { Component, EventEmitter, Output } from '@angular/core';
import { DeleteButton } from '../../buttons/delete-button/delete-button';
import { CancelButton } from '../../buttons/cancel-button/cancel-button';

@Component({
  selector: 'app-delete-modal',
  imports: [DeleteButton, CancelButton],
  templateUrl: './delete-modal.html',
  styleUrl: './delete-modal.css',
})
export class DeleteModal {
  @Output() deletar = new EventEmitter<void>();
  @Output() cancelar = new EventEmitter<void>();

  onDeletar() {
    this.deletar.emit();
  }

  onCancelar() {
    this.cancelar.emit();
  }
}
