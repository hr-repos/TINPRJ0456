<template>
    <div>

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
          Calculation:
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
    </div>
  </template>
  
  <script setup lang="ts">
  import BorderBlock from '../components/BorderBlock.vue'
  import LineChart from '../components/LineChart.vue'
  import DropdownMenu from '../components/DropdownMenu.vue'
  </script>
  
  <script lang="ts">
  import { defineComponent } from 'vue'
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
