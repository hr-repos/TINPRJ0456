<template>
  <div class="border">
    <p>{{ title }}</p>
    <slot></slot>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';

const props = defineProps<{
  title: string;
}>();
</script>

<style scoped>
/* Style for each border */
.border {
  width: 300px;
  padding: 10px;
  border: 2px solid #000;
}

/* Optional styling for the text inside the borders */
.border p {
  text-align: center;
  font-size: 30px;
}

</style>
