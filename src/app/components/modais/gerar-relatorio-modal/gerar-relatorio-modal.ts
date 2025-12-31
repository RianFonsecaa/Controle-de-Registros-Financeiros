import {
  Component,
  ElementRef,
  EventEmitter,
  inject,
  Output,
  ViewChild,
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CancelButton } from '../../buttons/cancel-button/cancel-button';
import { GenerateButton } from '../../buttons/generate-button/generate-button';
import { PrimaryInput } from '../../inputs/primary-input/primary-input';
import { RelatorioService } from '../../../services/relatorio.service';
import { LoadingModal } from '../loading-modal/loading-modal';
import { ModalService } from '../../../services/modal.service';

@Component({
  selector: 'app-gerar-relatorio-modal',
  imports: [
    ReactiveFormsModule,
    CancelButton,
    GenerateButton,
    PrimaryInput,
    LoadingModal,
  ],
  templateUrl: './gerar-relatorio-modal.html',
  styleUrl: './gerar-relatorio-modal.css',
})
export class GerarRelatorioModal {
  @ViewChild('loadingModal') loadingModal!: ElementRef<HTMLDialogElement>;
  @Output() cancelar = new EventEmitter<void>();
  @Output() gerar = new EventEmitter<void>();
  relatorioForm!: FormGroup;

  private relatorioService = inject(RelatorioService);
  private modalService = inject(ModalService);

  ngOnInit() {
    this.relatorioForm = new FormGroup({
      dataInicio: new FormControl(null, [Validators.required]),
      dataFim: new FormControl(null, [Validators.required]),
    });
  }

  onCancelar() {
    this.relatorioForm.reset();
    this.cancelar.emit();
  }

  onGerar() {
    const periodo = this.relatorioForm.value;

    this.modalService.abrirModal(this.loadingModal.nativeElement);

    this.relatorioService
      .geraRelatorioDeCobrancas(periodo.dataInicio, periodo.dataFim)
      .subscribe({
        next: (blob: Blob) => {
          const url = window.URL.createObjectURL(blob);
          window.open(url, '_blank');
          this.gerar.emit();
        },
        error: () => {
          this.modalService.fecharModal(this.loadingModal.nativeElement);
        },
        complete: () => {
          this.modalService.fecharModal(this.loadingModal.nativeElement);
        },
      });
  }

  getControl(name: string): FormControl {
    return this.relatorioForm.get(name) as FormControl;
  }
}
