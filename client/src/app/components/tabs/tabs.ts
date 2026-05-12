import { NgClass, NgComponentOutlet } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  Type,
} from '@angular/core';

export interface TabConfig {
  label: string;
  componente: Type<any>;
}

@Component({
  selector: 'app-tabs',
  imports: [NgClass],
  templateUrl: './tabs.html',
  styleUrl: './tabs.css',
})
export class Tabs implements OnInit {
  @Input() listaAbas: TabConfig[] = [];

  @Output() componenteAtivo = new EventEmitter<Type<any> | null>();

  abaAtiva: string = '';

  ngOnInit() {
    if (this.listaAbas.length > 0) {
      this.trocaAbas(this.listaAbas[0].label);
    }
  }

  trocaAbas(novaAba: string) {
    this.abaAtiva = novaAba;
    const comp =
      this.listaAbas.find((aba) => aba.label === this.abaAtiva)?.componente ??
      null;
    this.componenteAtivo.emit(comp);
  }
}
