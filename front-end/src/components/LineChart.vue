<template>
    <div id="chart">
        <apexchart type="line" height="500" class="w-full" :options="chartOptions" :series="series"></apexchart>
    </div>
</template>

<script>
import VueApexCharts from 'vue3-apexcharts'

export default {
    components: {
        apexchart: VueApexCharts
    },
    props: {
      //props to get the data from the dashboard
        inputNH3: Array,
        outputNH3: Array,
        array3: Array,
        array4: Array,
        array5: Array,
        array6: Array,
        array7: Array,
        array8: Array,
        min: Number,
        max: Number,
    },
    data()
    {
        return {
          //name of the sensors
            series: [
                {
                    name: 'Sensor 1',
                    data: this.inputNH3
                },
                {
                    name: 'Sensor 2',
                    data: this.outputNH3
                },
                {
                    name: 'Sensor 3',
                    data: this.array3
                },
                {
                    name: 'Sensor 4',
                    data: this.array4
                },
                {
                    name: 'Sensor 5',
                    data: this.array5
                },
                {
                    name: 'Sensor 6',
                    data: this.array6
                },
                {
                    name: 'Sensor 7',
                    data: this.array7
                },
                {
                    name: 'Sensor 8',
                    data: this.array8
                }
            ],
            //options for the chart
            chartOptions: {
                chart: {
                    id : 'realtime',
                    height: 800,
                    // width: 1200,
                    type: 'line',
                    dropShadow: {
                        enabled: false,
                        color: '#fff',
                        top: 18,
                        left: 7,
                        blur: 10,
                        opacity: 0.2
                    },
                    toolbar: {
                        show: false
                    },
                    animation:{
                        enabled: true,
                        dynamicAnimation: {
                            speed: 1000
                        }
                    },
                    
                    zoom: {
                        enabled: false
                    },
                    marker: {
                        show: false
                    },
                },
                colors: ['#83FF33', '#DE5433', '#4472BD', '#BC46D4', '#D44673', '#33FCFF', '#B37768', '#D3FF33'],
                dataLabels: {
                    enabled: false
                },
                grid: {
                    borderColor: '#8f8f8f',
                    row: {
                        colors: ['#e2e2e2', '#f0f0f0'],
                        opacity: 0.5
                    }
                },
                xAxis: {
                    categories: Array.from({ length: this.inputNH3.length }, (_, i) => (i + 1).toString()),
                    title: {
                        text: 'Time in milliseconds'
                    },
                    range : 1000,
                },
                yaxis: {
                    min: this.min,
                    max: this.max
                },
                stroke: {
                        curve: 'smooth',
                    },
                legend: {
                    position: 'top',
                    horizontalAlign: 'right',
                    floating: true,
                    offsetY: -25,
                    offsetX: -5,
                }
            }
        }
    }
    
}
</script>
