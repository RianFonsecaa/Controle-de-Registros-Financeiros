import { Component, inject, Input, ViewChild } from '@angular/core';
import {
  ApexAxisChartSeries,
  ApexChart,
  ChartComponent,
  ApexDataLabels,
  ApexPlotOptions,
  ApexYAxis,
  ApexTitleSubtitle,
  ApexXAxis,
  NgApexchartsModule,
} from 'ng-apexcharts';
import {
  DashboardService,
  RendimentoDiario,
} from '../../../services/http/dashboard.service';
import { ApexFill } from 'apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries | any;
  chart: ApexChart | any;
  dataLabels: ApexDataLabels | any;
  plotOptions: ApexPlotOptions | any;
  yaxis: ApexYAxis | any;
  xaxis: ApexXAxis | any;
  fill: ApexFill | any;
};

@Component({
  selector: 'app-column-chart',
  imports: [NgApexchartsModule],
  templateUrl: './column-chart.html',
  styleUrl: './column-chart.css',
})
export class ColumnChart {
  @ViewChild('chart') chart!: ChartComponent;

  public chartOptions: Partial<ChartOptions>;

  dashboardService = inject(DashboardService);

  @Input() dados: RendimentoDiario[] | undefined = [];

  ngOnInit() {
    this.dashboardService.buscaDadosDashboard();
  }

  constructor() {
    this.chartOptions = {
      chart: { type: 'bar', height: 350, width: '100%' },
      plotOptions: {
        bar: {
          columnWidth: '90%',
          borderRadius: 8,
          borderRadiusApplication: 'end',
        },
      },
      dataLabels: {
        enabled: true,
        formatter: (val: number) => {
          return val > 0 ? `R$ ${val.toFixed(2)}` : '';
        },
        offsetY: -20,
        style: {
          fontSize: '10px',
        },
      },
      yaxis: {
        labels: {
          formatter: (val: number) => {
            return val.toLocaleString('pt-BR', {
              style: 'currency',
              currency: 'BRL',
            });
          },
        },
      },
      series: [],
      fill: {
        colors: ['#00E396'],
      },
      xaxis: { categories: [] },
    };
  }

  ngOnChanges() {
    this.chartOptions = {
      ...this.chartOptions,
      series: [
        {
          name: 'Arrecadação',
          data: this.dados?.map((d) => d.valor) || [],
        },
      ],
      xaxis: {
        categories: this.dados?.map((d) => d.dia) || [],
      },
    };
  }
}
