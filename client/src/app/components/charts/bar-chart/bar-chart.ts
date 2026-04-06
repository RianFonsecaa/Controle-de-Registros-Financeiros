import { Component, inject, Input, ViewChild } from '@angular/core';
import {
  ApexAxisChartSeries,
  ApexChart,
  ApexDataLabels,
  ApexFill,
  ApexPlotOptions,
  ApexXAxis,
  ApexYAxis,
} from 'apexcharts';
import { ChartComponent } from 'ng-apexcharts';
import {
  DashboardService,
  RendimentoCidade,
  RendimentoDiario,
} from '../../../services/http/dashboard.service';

export type ChartOptions = {
  series: ApexAxisChartSeries | any;
  chart: ApexChart | any;
  dataLabels: ApexDataLabels | any;
  plotOptions: ApexPlotOptions | any;
  xaxis: ApexXAxis | any;
  fill: ApexFill | any;
  yaxis: ApexYAxis | any;
};

@Component({
  selector: 'app-bar-chart',
  imports: [ChartComponent],
  templateUrl: './bar-chart.html',
  styleUrl: './bar-chart.css',
})
export class BarChart {
  @ViewChild('chart') chart?: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  dashboardService = inject(DashboardService);

  @Input() dados: RendimentoCidade[] | undefined = [];

  ngOnInit() {
    this.dashboardService.buscaDadosDashboard();
  }

  constructor() {
    this.chartOptions = {
      series: [],
      chart: {
        type: 'bar',
        height: 450,
      },
      plotOptions: {
        bar: {
          barHeight: '40%',
          borderRadius: 8,
          borderRadiusApplication: 'end',
          horizontal: true,
        },
      },
      dataLabels: {
        enabled: true,
        formatter: (val: number) => {
          return val > 0 ? `R$ ${val.toFixed(2)}` : '';
        },
      },
      xaxis: { categories: [] },
      fill: {
        colors: ['#4F46E5'],
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
    };
  }

  ngOnChanges() {
    this.chartOptions = {
      ...this.chartOptions,
      series: [
        {
          name: 'Arrecadação',
          data: this.dados?.map((d) => d.total) || [],
        },
      ],
      xaxis: {
        categories: this.dados?.map((d) => d.cidade) || [],
      },
    };
  }
}
