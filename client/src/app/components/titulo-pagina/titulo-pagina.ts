import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-titulo-pagina',
  imports: [],
  templateUrl: './titulo-pagina.html',
  styleUrl: './titulo-pagina.css',
})
export class TituloPagina {
  @Input() titulo: String = '';
}
