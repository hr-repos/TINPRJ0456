<template>
    <div class="my-10 mx-20 text-2xl">
        <p class="font-bold text-center">Select a project to see its registered sensors.</p>
        <div class="flex items-center text-xl">
            <select name="select_project" id="select_project" v-on:change="changeRoute($event)" class="my-2 w-full py-3">
                <option selected disabled>Select a project</option>
                <option v-for="project in projects"
                        :value="project.id"
                        v-text="`${project.id}. ${project.name} (${project.sensors.length} sensors)`"></option>
            </select>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            projects: []
        };
    },
    methods: {
        changeRoute(event) {
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
