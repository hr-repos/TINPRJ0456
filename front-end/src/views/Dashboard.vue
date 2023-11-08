<template>
    <div>
        <div class="header">
            Input NH3:
        </div>
        <div class="block">
            <BorderBlock :title="inputNH3">
                <!-- You can place your content here -->
            </BorderBlock>
        </div>
        <div class="header">
            Output NH3:
        </div>
        <div class="block">
            <BorderBlock :title="outputNH3">
                <!-- You can place your content here -->
            </BorderBlock>
        </div>
        <div class="header">
            Input CO2:
        </div>
        <div class="block">
            <BorderBlock :title="inputCO2">
                <!-- You can place your content here -->
            </BorderBlock>
        </div>
        <div class="header">
            Berekening:
        </div>
        <div class="block">
            <BorderBlock :title="calculation">
                <!-- You can place your content here -->
            </BorderBlock>
        </div>
    </div>
    <div class="chart">
        <LineChart title="chart" :inputNH3="inputNH3Array" :outputNH3="outputNH3Array" />
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
            max: 0
        }
    }
})
</script>
