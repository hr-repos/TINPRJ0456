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
    // Function to generate random numbers between a given range
    const generateRandomNumber = (min, max) => {
      return Math.floor(Math.random() * (max - min + 1)) + min;
    };

    // Generate an array of 10 random numbers between 1 and 100
    const randomData = Array.from({ length: 10 }, () => generateRandomNumber(1, 100));

    return {
      chartData: {
        labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'],
        datasets: [{ data: randomData }],
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
      },
    };
  },
};

  </script>
  