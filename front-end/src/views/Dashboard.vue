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
import {defineComponent} from 'vue'
import emitter from '@/router/emitter'

export default defineComponent({
    mounted() {
        emitter.on('socket', (data: any) => {
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
