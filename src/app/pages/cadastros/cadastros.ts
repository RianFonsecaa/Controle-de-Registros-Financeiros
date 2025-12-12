import { Component } from '@angular/core';
import { PaginaEmManutencao } from '../../components/pagina-em-manutencao/pagina-em-manutencao';

@Component({
  selector: 'app-cadastros',
  imports: [PaginaEmManutencao],
  templateUrl: './cadastros.html',
  styleUrl: './cadastros.css',
})
export class Cadastros {}
