import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ModalService {
  abrirModal(modal: HTMLDialogElement) {
    modal.showModal();
  }

  fecharModal(modal: HTMLDialogElement) {
    modal.close();
  }
}
