<template>
    <div>
        <p class="text-center">You can make a new project here.</p>
        <p class="text-center">Just type in the bar below the name of the new project.</p>
        <TerminalComponent/>
    </div>
</template>

<script>
import TerminalComponent from '@/components/TerminalComponent.vue'

export default {
    components: {
        TerminalComponent
    }
}
</script>
