import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-delete-modal',
  imports: [],
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

  fecharModal(modal: HTMLDialogElement) {
    modal.close();
  }
}
