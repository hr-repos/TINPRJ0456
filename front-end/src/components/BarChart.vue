<template>
    <Bar
      id="my-chart-id"
      :options="chartOptions"
      :data="chartData"
    />
  </template>
  
  <script>
  import { Bar } from 'vue-chartjs'
  import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'
  
  ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)
  
  export default {
    name: 'BarChart',
    components: { Bar },
    data() {
      return {
        chartData: {
          labels: [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10' ],
          datasets: [ { data: [50, 42, 11, 32, 15, 40, 60, 32, 12, 38] } ]
        },
        chartOptions: {
          responsive: true,
          maintainAspectRatio: false, // Set this to false to allow custom sizing
          scales: {
            x: {
              display: true,
              title: {
                display: true,
                text: 'Time',
              },
            },
            y: {
              display: true,
              title: {
                display: true,
                text: 'Amount',
              },
            },
          },
          // Adjust the size as needed
          width: 400, // Width in pixels
          height: 300, // Height in pixels
        }
      }
    }
  }
  </script>
  