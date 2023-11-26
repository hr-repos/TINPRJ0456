<template>
    <DropdownMenu />
    <router-view />

    <p>Connected: {{ open }}</p>
</template>

<script lang="ts">
import { websocket } from '@/stores/websocket'
import { defineComponent } from 'vue'
import { mapState } from 'pinia'
import DropdownMenu from './components/DropdownMenu.vue'

export default defineComponent({
    created() {
        websocket().init();
    },
    computed: {
        ...mapState(websocket, ['open'])
    },
    components: { DropdownMenu }
})
</script>
