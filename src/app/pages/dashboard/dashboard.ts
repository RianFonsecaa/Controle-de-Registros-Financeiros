import { Component } from '@angular/core';
import { SideBar } from '../../components/side-bar/side-bar';
import { PaginaEmManutencao } from '../../components/pagina-em-manutencao/pagina-em-manutencao';

@Component({
  selector: 'app-dashboard',
  imports: [SideBar, PaginaEmManutencao],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {}
