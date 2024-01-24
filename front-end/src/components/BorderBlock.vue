<template>
    <div class="bg-stone-100 shadow-md py-1 px-3 flex flex-col justify-between hover:ring-4 hover:ring-gray-600">
        <div class="mb-1 flex flex-row justify-between">
            <span class="font-bold text-xl leading-5 mt-0.5" v-text="name"></span>
            <span class="text-[16px] min-w-fit text-gray-500" v-text="'pin ' + pin"></span>
        </div>
        <div>
            <div class="justify-end flex items-end gap-1">
                <div class="inline-block">
                    <span v-if="title" v-text="title" class="text-5xl font-mono"></span>
                    <LoadSpinner v-else class="my-2 mx-3"/>
                </div>
                <span v-if="calibrated" class="mb-0.5" v-text="unit"></span>
            </div>
            <div v-if="!calibrated" class="text-red-600 text-xs text-right">Not calibrated</div>
        </div>
    </div>
</template>

<script>
import LoadSpinner from '@/components/LoadSpinner.vue'

export default {
    components: { LoadSpinner },
    props: {
        title: Number,
        name: String,
        pin: Number,
        unit: String,
        calibrated: Boolean
    }
}
</script>
