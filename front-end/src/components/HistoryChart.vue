<template>
    <div>
      <apexchart type="line" height="500" class="w-full" :options="chartOptions" :series="series"></apexchart>
    </div>
  </template>
  
  <script>
  import VueApexCharts from 'vue3-apexcharts';
  
  export default {
    components: {
      apexchart: VueApexCharts,
    },
    props: {
      databaseValues: Array,
      min: Number,
      max: Number,
    },
    data() {
      return {
        series: [
          {
            name: 'Database Values',
            data: this.databaseValues,
          },
        ],
        chartOptions: {
          chart: {
            height: 800,
            type: 'line',
            dropShadow: {
              enabled: false,
              color: '#fff',
              top: 18,
              left: 7,
              blur: 10,
              opacity: 0.2,
            },
            toolbar: {
              show: false,
            },
          },
          colors: ['#3366FF'],
          dataLabels: {
            enabled: false,
          },
          stroke: {
            curve: 'smooth',
          },
          grid: {
            borderColor: '#8f8f8f',
            row: {
              colors: ['#e2e2e2', '#f0f0f0'],
              opacity: 0.5,
            },
          },
          markers: {
            size: 1,
          },
          xAxis: {
            categories: Array.from({ length: this.databaseValues.length }, (_, i) => (i + 1).toString()),
            title: {
              text: 'Time in milliseconds',
            },
          },
          yaxis: {
            title: {
              text: 'NH3 in µg/m³',
            },
            min: this.min,
            max: this.max,
          },
          legend: {
            position: 'top',
            horizontalAlign: 'right',
            floating: true,
            offsetY: -25,
            offsetX: -5,
          },
        },
      };
    },
    watch: {
      databaseValues: {
        handler(newValues) {
          this.series[0].data = newValues;
        },
        immediate: true,
      },
      min: {
        handler(newMin) {
          this.chartOptions.yaxis.min = newMin;
        },
        immediate: true,
      },
      max: {
        handler(newMax) {
          this.chartOptions.yaxis.max = newMax;
        },
        immediate: true,
      },
    },
  };
  </script>
