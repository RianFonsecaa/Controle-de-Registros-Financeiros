import { Component, EventEmitter, Output } from "@angular/core";

@Component({
  selector: "app-whatsapp-button",
  imports: [],
  templateUrl: "./whatsapp-button.html",
  styleUrl: "./whatsapp-button.css",
})
export class WhatsappButton {
  @Output() abrir = new EventEmitter<void>();

  abrirModal() {
    this.abrir.emit();
  }
}
