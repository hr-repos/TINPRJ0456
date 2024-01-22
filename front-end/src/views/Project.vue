<template>
    {{ project }}
</template>

<script>
import LoadSpinner from '../components/LoadSpinner.vue'

export default {
    components: { LoadSpinner },
    data() {
        return {
            project: JSON,
        };
    },
    async beforeRouteEnter(to, from, next) {
        const data = await (await fetch('/api/get-project/' + to.params.project_id)).json()
        next(vm => {
            vm.project = data
        })
    }
}
</script>
