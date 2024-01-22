<template>
    <p>Of which project do you want to add or change sensors?</p>
    <div class="flex items-center">
        <select name="select_project" id="select_project" v-on:change="changeRoute($event)" class="my-2">
            <option selected disabled>Select a project</option>
            <option v-for="project in projects" :value="project.id" v-text="`${project.id}. ${project.name} (${project.sensors.length} sensors)`"></option>
        </select>
        <LoadSpinner v-if="loading" class="ml-4"/>
    </div>
</template>

<script>
import LoadSpinner from '../components/LoadSpinner.vue'

export default {
    components: { LoadSpinner },
    data() {
        return {
            projects: [],
            loading: false
        };
    },
    methods: {
        changeRoute(event) {
            this.loading = true
            this.$router.push(`/project/${event.target.value}/sensors`)
        }
    },
    async beforeRouteEnter(to, from, next) {
        const data = await (await fetch('/api/get-projects')).json()
        next(vm => {
            vm.projects = data
        })
    }
}
</script>
