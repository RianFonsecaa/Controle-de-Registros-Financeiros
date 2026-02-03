import {
  Component,
  ElementRef,
  EventEmitter,
  inject,
  Input,
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
import { CidadesService } from '../../../services/http/cidades.service';
import { SaveButton } from '../../buttons/save-button/save-button';
import { MoneyInput } from '../../inputs/money-input/money-input';
import { PixRequest } from '../../../model/requests/PixRequest';
import { CidadeResponse } from '../../../model/responses/CidadeResponse';
import { CidadeSelect } from '../../selects/cidade-select/cidade-select';

@Component({
  selector: 'app-save-pix-modal',
  imports: [
    CancelButton,
    ReactiveFormsModule,
    PrimaryInput,
    SaveButton,
    MoneyInput,
    CidadeSelect,
  ],
  templateUrl: './save-pix-modal.html',
  styleUrl: './save-pix-modal.css',
})
export class SavePixModal {
  cidadesService = inject(CidadesService);
  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<any>();
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  @Input() data!: String;
  @Input() cidade!: CidadeResponse;

  pixForm: FormGroup = new FormGroup({
    cliente: new FormControl(null, [Validators.required]),
    valor: new FormControl(null, [Validators.required]),
    cidade: new FormControl(null, [Validators.required]),
    data: new FormControl(null, [Validators.required]),
    comprovante: new FormControl(),
  });

  ngOnInit() {
    this.cidadesService.buscaCidades();
  }

  ngOnChanges() {
    if (!this.pixForm) return;
    this.aplicarValoresIniciais();
  }

  private aplicarValoresIniciais() {
    this.pixForm.patchValue({
      cidade: this.cidade ?? null,
      data: this.data ?? null,
    });
  }

  getControl(name: string): FormControl {
    return this.pixForm.get(name) as FormControl;
  }

  onCancelar() {
    this.cancelar.emit();
    this.pixForm.reset();
    this.resetFileInput();
    this.aplicarValoresIniciais();
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
    this.aplicarValoresIniciais();
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
