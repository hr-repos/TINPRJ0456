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
        inputNH3: Array,
        outputNH3: Array,
        min: Number,
        max: Number,
    },
    data()
    {
        return {
            series: [
                {
                    name: 'Input NH3',
                    data: this.inputNH3
                },
                {
                    name: 'Output NH3',
                    data: this.outputNH3
                }
            ],
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
                colors: ['#83FF33', '#DE5433'],
                dataLabels: {
                    enabled: false
                },
                // title: {
                //     text: 'Data sensors',
                //     align: 'left'
                // },
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
                    title: {
                        text: 'NH3 in µg/m³'
                    },
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
