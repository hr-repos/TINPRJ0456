<template>
  <div>
    <div class="header">
      Input NH3:
    </div>
    <div class="block">
      <BorderBlock :title="randomTitle1">
        <!-- You can place your content here -->
      </BorderBlock>
    </div>
    <div class="header">
      Output NH3:
    </div>
    <div class="block">
      <BorderBlock :title="randomTitle2">
        <!-- You can place your content here -->
      </BorderBlock>
    </div>
    <div class="header">
      Input CO2:
    </div>
    <div class="block">
      <BorderBlock :title="randomTitle3">
        <!-- You can place your content here -->
      </BorderBlock>
    </div>
    <div class="header">
      Berekening:
    </div>
    <div class="block">
      <BorderBlock :title="randomTitle4">
        <!-- You can place your content here -->
      </BorderBlock>
    </div>
  </div>
  <div>
    <BarChart title="Chart" />
  </div>
</template>

<script setup lang="ts">
import BorderBlock from './components/BorderBlock.vue';
import BarChart from './components/BarChart.vue';

const randomTitle1 = generateRandomTitle();
const randomTitle2 = generateRandomTitle();
const randomTitle3 = generateRandomTitle();
const randomTitle4 = generateRandomTitle();

function generateRandomTitle() {
  // Function to generate a random number between 1 and 100 and convert it to a string
  return Math.floor(Math.random() * 100) + 1 + ' ppm';
}
</script>
