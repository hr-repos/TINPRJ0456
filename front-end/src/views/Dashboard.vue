<template>
    <div class="flex flex-row justify-between">
        <div>
            <span class="header">Input NH3</span>
            <BorderBlock :title="sensorData[0]" />
        </div>
        <div>
            <span class="header">Output NH3</span>
            <BorderBlock :title="sensorData[1]" />
        </div>
        <div>
            <span class="header">Calculation</span>
            <BorderBlock :title="sensorData[2]" />
        </div>
        <div>
            <button @click="test" v-if="testing">Disable testing</button>
            <button @click="test" v-else>Enable testing</button>
        </div>
    </div>
    <div class="chart block">
        <LineChart title="chart" :inputNH3="inputNH3Array" :outputNH3="outputNH3Array"/>
    </div>
</template>

<script setup lang="ts">
import BorderBlock from '../components/BorderBlock.vue'
import LineChart from '../components/LineChart.vue'
</script>

<script lang="ts">
import { defineComponent } from 'vue'
import emitter from '@/router/emitter'

export default defineComponent({
    methods: {
        test() {
            this.testing = !this.testing
            if (this.testing) {
                this.fakeDataInterval = setInterval(() => {
                    const data = {
                        "data": [
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200)
                        ]
                    }

                    fetch('/api/submit-sensor-data', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify(data),
                    }).then(() => console.log('Sent:', data))
                }, 200)
            }
            else {
                clearInterval(this.fakeDataInterval)
            }
        },
    },
    mounted() {
        emitter.on('socket', (data: any) => {
            this.sensorData = data['sensor_data'] // = array [200, 250, 200, 200, 200, 200]
                                                  //    pin = 0,   1,   2,   3,   4,   5

            if (this.sensorData[0]) {
                if (this.inputNH3Array.length > 30) {
                    this.inputNH3Array.shift()
                }
                this.inputNH3Array.push(this.sensorData[0])
                this.min = Math.min(Math.min(this.outputNH3Array), Math.min(this.inputNH3Array))
            }
            if (this.sensorData[1]) {
                if (this.outputNH3Array.length > 30) {
                    this.outputNH3Array.shift()
                }
                this.outputNH3Array.push(this.sensorData[1])
                this.max = Math.max(Math.max(this.outputNH3Array), Math.max(this.inputNH3Array))
            }
        })
    },
    unmounted() {
        emitter.off('socket')
    },
    data: () => {
        return {
            sensorData: [],
            inputNH3Array: [],
            outputNH3Array: [],
            min: 0,
            max: 0,
            testing: false,
            fakeDataInterval: 0,
        }
    }
})
</script>
