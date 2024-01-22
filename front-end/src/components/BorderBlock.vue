<template>
    <div class="border">
        <p v-if="title" v-text="title + 'ppm'"></p>
        <div v-else><LoadSpinner/></div>
        <slot></slot>
    </div>
</template>

<script setup lang="ts">
import LoadSpinner from "@/components/LoadSpinner.vue";

const props = defineProps<{
    title: any;
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
