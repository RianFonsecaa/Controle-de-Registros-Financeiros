import {
  Component,
  EventEmitter,
  Input,
  Output,
  SimpleChanges,
} from '@angular/core';

@Component({
  selector: 'app-success-toast',
  imports: [],
  templateUrl: './success-toast.html',
  styleUrl: './success-toast.css',
})
export class SuccessToast {
  @Input() mensagem?: string;
}
