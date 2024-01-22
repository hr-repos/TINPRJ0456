<template>
    <div class="flex flex-row justify-between">
        <div>
            <span class="header">Input NH3</span>
            <BorderBlock :title="inputNH3" />
        </div>
        <div>
            <span class="header">Output NH3</span>
            <BorderBlock :title="outputNH3" />
        </div>
        <div>
            <span class="header">Calculation</span>
            <BorderBlock :title="calculation" />
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
                    fetch('/api/submit-sensor-data', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify({"data": [
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200)
                        ]}),
                    })
                        .then(response => console.log('sent'))
                }, 200)
            }
            else {
                clearInterval(this.fakeDataInterval)
            }
        },
    },
    mounted() {
        emitter.on('socket', (data: any) => {
            if (data['inputNH3']) {
                this.inputNH3 = data['inputNH3']
                if (this.inputNH3Array.length > 30) {
                    this.inputNH3Array.shift()
                }
                this.inputNH3Array.push(data['inputNH3'])
                this.min = Math.min(Math.min(this.outputNH3Array), Math.min(this.inputNH3Array))
            }
            if (data['outputNH3']) {
                this.outputNH3 = data['outputNH3']
                if (this.outputNH3Array.length > 30) {
                    this.outputNH3Array.shift()
                }
                this.outputNH3Array.push(data['outputNH3'])
                this.max = Math.max(Math.max(this.outputNH3Array), Math.max(this.inputNH3Array))
            }
            if (data['inputCO2']) {
                this.inputCO2 = data['inputCO2']
            }
            if (data['calculation']) {
                this.calculation = data['calculation']
            }
        })
    },
    unmounted() {
        emitter.off('socket')
    },
    data: () => {
        return {
            inputNH3: -999,
            outputNH3: -999,
            inputCO2: -999,
            calculation: -999,
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
