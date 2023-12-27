<template>
    <div>
      <DateTimeRangePicker />

      <LineChart :inputNH3="inputNH3" :outputNH3="outputNH3" :min="min" :max="max" />
    </div>
  </template>
  
  <script>
  import DateTimeRangePicker from '../components/DateTimePicker.vue';
  import LineChart from '../components/LineChart.vue'; // Make sure to adjust the path based on your project structure
  
  export default {
    components: {
      DateTimeRangePicker,
      LineChart,
    },
    data() {
      return {
       
        inputNH3: [],
        outputNH3: [],
        min: 0,
        max: 100,
      };
    },
  };
  </script>
  