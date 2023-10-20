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
    <div>
        <BarChart title="Chart"/>
    </div>
</template>

<script setup lang="ts">
import BorderBlock from '../components/BorderBlock.vue'
import BarChart from '../components/BarChart.vue'
</script>

<script lang="ts">
import { defineComponent } from 'vue'
import emitter from '@/router/emitter'

export default defineComponent({
    mounted() {
        emitter.on('socket', (data: any) => {
            if (data['inputNH3']) {
                this.inputNH3 = data['inputNH3']
            }
            if (data['outputNH3']) {
                this.outputNH3 = data['outputNH3']
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
            calculation: -999
        }
    }
})
</script>
