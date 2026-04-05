import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
} from '@angular/core';

@Component({
  selector: 'app-error-toast',
  imports: [],
  templateUrl: './error-toast.html',
  styleUrl: './error-toast.css',
})
export class ErrorToast {
  @Input() mensagem?: string;
}
