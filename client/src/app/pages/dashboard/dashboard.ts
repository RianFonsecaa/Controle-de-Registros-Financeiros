import { Component, effect, inject, OnInit } from "@angular/core";
import { SideBar } from "../../components/side-bar/side-bar";
import { PaginaEmManutencao } from "../../components/pagina-em-manutencao/pagina-em-manutencao";
import { ColumnChart } from "../../components/charts/column-chart/column-chart";
import {
  DashboardData,
  DashboardService,
} from "../../services/http/dashboard.service";
import { CardSomatorios } from "../../components/cards/card-somatorios/card-somatorios";
import { TituloPagina } from "../../components/titulo-pagina/titulo-pagina";
import { BarChart } from "../../components/charts/bar-chart/bar-chart";
import { DonutChart } from "../../components/charts/donut-chart/donut-chart";

@Component({
  selector: "app-dashboard",
  imports: [ColumnChart, CardSomatorios, TituloPagina, BarChart, DonutChart],
  templateUrl: "./dashboard.html",
  styleUrl: "./dashboard.css",
})
export class Dashboard implements OnInit {
  dashboardService = inject(DashboardService);

  dadosDashboard = this.dashboardService.dadosDashboard;

  ngOnInit() {
    this.dashboardService.buscaDadosDashboard();
  }
}
