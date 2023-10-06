<template>
  <div class="chart-container">
    <h2>{{ title }}</h2>
    <canvas ref="chartCanvas"></canvas>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, defineProps } from 'vue';
import { Chart, LinearScale, Title, Tooltip, Legend } from 'chart.js';

const props = defineProps<{
  title: string;
}>();

const chartCanvas = ref<HTMLCanvasElement | null>(null);
let chart: Chart | null = null;

const chartData = {
  labels: ['0', '10', '20', '30', '40', '50'],
  datasets: [
    {
      label: 'Ammonia Concentration (ppm)',
      data: [5, 10, 8, 15, 12, 20],
      fill: false,
      borderColor: 'rgb(75, 192, 192)',
      tension: 0.4,
      yAxisID: 'y-axis-1',
    },
  ],
};

onMounted(() => {
  const ctx = chartCanvas.value?.getContext('2d');
  if (ctx) {
    Chart.register(LinearScale, Title, Tooltip, Legend);

    chart = new Chart(ctx, {
      type: 'line',
      data: chartData,
      options: {
        responsive: true,
        scales: {
          x: {
            title: {
              display: true,
              text: 'Time (seconds)',
            },
          },
          y: {
            position: 'left',
            title: {
              display: true,
              text: 'Ammonia Concentration (ppm)',
            },
          },
        },
        plugins: {
          title: {
            display: true,
            text: 'Ammonia Concentration Over Time',
          },
        },
      },
    });
  }
});

onBeforeUnmount(() => {
  if (chart) {
    chart.destroy();
  }
});
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 400px;
  text-align: center;
  margin-top: 20px;
}

.chart-container h2 {
  font-size: 30px;
  margin-bottom: 10px;
  font-weight: bold;
}
</style>
