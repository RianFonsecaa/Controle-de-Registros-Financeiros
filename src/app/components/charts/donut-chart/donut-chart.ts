import { Component, inject, Input, ViewChild } from '@angular/core';
import {
  ApexNonAxisChartSeries,
  ApexChart,
  ApexResponsive,
  ApexLegend,
} from 'apexcharts';
import { ChartComponent } from 'ng-apexcharts';
import {
  DashboardService,
  MetodoPagamento,
} from '../../../services/http/dashboard.service';

export type ChartOptions = {
  series: ApexNonAxisChartSeries | any;
  chart: ApexChart | any;
  responsive: ApexResponsive[] | any;
  legend: ApexLegend | any;
  labels: any;
};

@Component({
  selector: 'app-donut-chart',
  imports: [ChartComponent],
  templateUrl: './donut-chart.html',
  styleUrl: './donut-chart.css',
})
export class DonutChart {
  @ViewChild('chart') chart?: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  dashboardService = inject(DashboardService);

  @Input() dados: MetodoPagamento | undefined;

  ngOnInit() {
    this.dashboardService.buscaDadosDashboard();
  }

  constructor() {
    this.chartOptions = {
      series: [],
      chart: {
        type: 'donut',
        width: '100%',
        height: 300,
      },
      labels: ['Pagamentos Espécie', 'Pagamentos Pix'],

      // ADICIONE ESTE BLOCO AQUI
      legend: {
        position: 'bottom',
        horizontalAlign: 'center',
        fontSize: '14px',
        markers: {
          radius: 12,
        },
        itemMargin: {
          horizontal: 10,
          vertical: 10,
        },
      },
    };
  }

  ngOnChanges() {
    if (this.dados) {
      this.chartOptions = {
        ...this.chartOptions,
        series: [this.dados.totalEspecie, this.dados.totalPix],
        labels: ['Pagamentos Espécie', 'Pagamentos Pix'],
      };
    }
  }
}
