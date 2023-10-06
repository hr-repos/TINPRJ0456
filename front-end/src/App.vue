<template>
  <div>
    <div class="header">
      Input NH3:
    </div>
    <div class="block">
      <BorderBlock title="Border 1">
        <!-- You can place your content here -->
      </BorderBlock>
    </div>
    <div class="header">
      Output NH3:
    </div>
    <div class="block">
      <BorderBlock title="Border 2">
        <!-- You can place your content here -->
      </BorderBlock>
    </div>
    <div class="header">
      Input CO2:
    </div>
    <div class="block">
      <BorderBlock title="Border 3">
        <!-- You can place your content here -->
      </BorderBlock>
    </div>
    <div class="header">
      Berekening:
    </div>
    <div class="block">
      <BorderBlock title="Border 4">
        <!-- You can place your content here -->
      </BorderBlock>
    </div>
  </div>
  <div>
    <LineChart title="Chart"/>
    
  </div>
</template>

<script setup lang="ts">
import BorderBlock from './components/BorderBlock.vue';
import LineChart from './components/LineChart.vue';
</script>

<style scoped>
/* Styling for the header elements */
.header {
  font-weight: bold;
  font-size: 30px;
  margin-top: 20px;
  margin-bottom: 10px;
}

/* Styling for the .block elements */
.block {
  display: inline-block;
  vertical-align: top;
  width: calc(25% - 20px); /* Adjust the width as needed, accounting for margin */
  margin-right: 20px; /* Reduce the margin between blocks to an appropriate value */
  margin-bottom: 20px; /* Add margin to separate blocks vertically */
}
</style>
