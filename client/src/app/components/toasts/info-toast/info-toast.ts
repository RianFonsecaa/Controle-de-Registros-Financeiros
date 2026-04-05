import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-info-toast',
  imports: [],
  templateUrl: './info-toast.html',
  styleUrl: './info-toast.css',
})
export class InfoToast {
  @Input() mensagem?: string;
}
