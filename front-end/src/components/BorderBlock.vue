<template>
    <div class="flex flex-col items-center border-2 border-black rounded-md">
        <span class="font-bold" v-text="name"></span>
        <hr class="my-0.5 border-2 w-full">
        <div class="px-5 pb-2 text-end">
            <div class="min-w-[150px]">
                <div v-if="title" class="text-5xl font-mono">
                    <span v-text="title"></span>
                </div>
                <div v-else>
                    <LoadSpinner class="my-2"/>
                </div>
            </div>

            <div> μg/m³</div>
        </div>
    </div>
</template>

<script setup lang="ts">
import LoadSpinner from "@/components/LoadSpinner.vue";

const props = defineProps<{
    title: any;
    name: String
}>();
</script>
