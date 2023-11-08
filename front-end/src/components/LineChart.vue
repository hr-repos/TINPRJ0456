<template>
  <div id="chart">
    <h2 class="chart-title">Chart of Sensors</h2>
    <apexchart type="line" height="500" width="1000" :options="chartOptions" :series="series"></apexchart>
  </div>
</template>

<script>
import VueApexCharts from 'vue3-apexcharts';

export default {
  components: {
    apexchart: VueApexCharts,
  },
  props: {
    inputNH3: Array,
    outputNH3: Array,
    min: Number,
    max: Number,
  },
  data() {
    return {
      series: [
        {
          name: "Input NH3",
          data: this.inputNH3,
        },
        {
          name: "Output NH3",
          data: this.outputNH3,
        },
      ],
      chartOptions: {
        chart: {
          height: 800,
          width: 1200,
          type: 'line',
          dropShadow: {
            enabled: true,
            color: '#000',
            top: 18,
            left: 7,
            blur: 10,
            opacity: 0.2,
          },
          toolbar: {
            show: false,
          },
        },
        colors: ['#83FF33', '#DE5433'],
        dataLabels: {
          enabled: false,
        },
        stroke: {
          curve: 'smooth',
        },
        title: {
          text: 'Data sensors',
          align: 'left',
        },
        grid: {
          borderColor: '#e7e7e7',
          row: {
            colors: ['#494442', '#655F5C'],
            opacity: 0.5,
          },
        },
        markers: {
          size: 1,
        },
        xAxis: {
          categories: Array.from({ length: this.inputNH3.length }, (_, i) => (i + 1).toString()),
          title: {
            text: 'Time in milliseconds',
          },
        },
        yaxis: {
          title: {
            text: 'NH3 in ppm',
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
};
</script>

<style scoped>
.chart-title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
}

</style>
