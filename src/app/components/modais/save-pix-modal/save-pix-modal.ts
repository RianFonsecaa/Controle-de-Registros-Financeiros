import {
  Component,
  ElementRef,
  EventEmitter,
  inject,
  Output,
  ViewChild,
} from '@angular/core';
import { CancelButton } from '../../buttons/cancel-button/cancel-button';
import { DeleteButton } from '../../buttons/delete-button/delete-button';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { PrimaryInput } from '../../inputs/primary-input/primary-input';
import { PrimarySelect } from '../../inputs/primary-select/primary-select';
import { CidadesService } from '../../../services/cidades.service';
import { AddButton } from '../../buttons/add-button/add-button';
import { SaveButton } from '../../buttons/save-button/save-button';
import { MoneyInput } from '../../inputs/money-input/money-input';
import { PixRequest } from '../../../model/requests/PixRequest';

@Component({
  selector: 'app-save-pix-modal',
  imports: [
    CancelButton,
    ReactiveFormsModule,
    PrimaryInput,
    PrimarySelect,
    SaveButton,
    MoneyInput,
  ],
  templateUrl: './save-pix-modal.html',
  styleUrl: './save-pix-modal.css',
})
export class SavePixModal {
  cidadesService = inject(CidadesService);
  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<any>();
  pixForm!: FormGroup;
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  ngOnInit() {
    this.pixForm = new FormGroup({
      cliente: new FormControl(null, [Validators.required]),
      valor: new FormControl(0, [Validators.required]),
      cidade: new FormControl(null, [Validators.required]),
      data: new FormControl(null, [Validators.required]),
      comprovante: new FormControl(),
    });

    this.cidadesService.buscaCidades();
  }

  getControl(name: string): FormControl {
    return this.pixForm.get(name) as FormControl;
  }

  onCancelar() {
    this.pixForm.reset();
    this.resetFileInput();
    this.cancelar.emit();
  }

  onSalvar() {
    if (this.pixForm.invalid) {
      this.pixForm.markAllAsTouched();
      return;
    }

    const formValue = this.pixForm.value;

    const pix: PixRequest = {
      cliente: formValue.cliente,
      valor: formValue.valor,
      cidadeId: formValue.cidade.id,
      data: formValue.data,
      comprovante: formValue.comprovante,
      cobrancaId: null,
    };

    this.salvar.emit(pix);

    this.pixForm.reset();
    this.resetFileInput();
  }

  private resetFileInput() {
    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
  }

  onFileChange(input: HTMLInputElement) {
    if (!input.files?.length) return;

    this.getControl('comprovante').setValue(input.files[0]);
  }
}
