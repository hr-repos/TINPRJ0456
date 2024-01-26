<template>
    <div v-if="project && project.id" class="flex flex-row">
        <div class="flex-1 flex flex-col gap-4">
            <div class="text-3xl">
                <span class="font-bold" v-text="project.name"></span>
                <span v-text="project.active? ' (active)' : ' (not active)'"></span>
            </div>

            <div class="text-xl">
                <p v-text="'ID: ' + project.id"></p>
                <p v-text="'Created by: ' + project.creator_name"></p>
                <p v-text="'Created at: ' + timeString(project.creation_date)"></p>
                <p v-text="'Frequency: ' + project.frequency + 'ms'"></p>
            </div>

            <div>
                <div v-if="project.sensors.length > 0" class="grid grid-cols-1">
                    <div v-for="i in project.sensors" v-text="`Pin ${i.pin}: ${i.name}`" />
                </div>
                <WarningMessage v-else
                                warning="No sensors found for this project."
                                description="Add sensors here."
                                :link="`/project/${project.id}/sensors`" />
            </div>
        </div>
        <div class="flex flex-col gap-2">
            <RouterLink class="router-button" :to="`/project/${project.id}/history`">View history</RouterLink>
            <RouterLink class="router-button" :to="`/project/${project.id}/sensors`">View sensors</RouterLink>
            <ExportToCsv :project="project" />
            <button @click="deleteProject">Delete project</button>
        </div>
    </div>
    <WarningMessage v-else-if="project" warning="Project not found." description="Register a new project here." link="/projects" />
</template>

<script>
import LoadSpinner from '../components/LoadSpinner.vue'
import BorderBlock from '@/components/BorderBlock.vue'
import {toast} from "vue3-toastify";
import WarningMessage from '@/components/WarningMessage.vue'
import ExportToCsv from '@/components/ExportToCsv.vue'

export default {
    components: { ExportToCsv, WarningMessage, BorderBlock, LoadSpinner },
    data() {
        return {
            project: null,
        };
    },
    methods: {
        timeString(date) {
            return new Date(date * 1000).toLocaleString('en-US', {
                month: 'long',
                day: 'numeric',
                year: 'numeric',
                hour: 'numeric',
                minute: 'numeric',
                second: 'numeric',
                hour12: false,
            })
        },
        //delete project function
        //this function will delete the project and all the data associated with it
        async deleteProject() {
            if (confirm('Are you sure you want to delete this project? This will also delete all measure data and sensors within this project.')) {
                const t = toast.info('Deleting project...')
                await fetch('/api/delete-project', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({
                        id: this.project.id
                    }),
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.error) {
                            toast.remove(t)
                            toast.error(data.error)
                            return
                        }

                        toast.remove(t)
                        this.$router.push('/projects')
                        toast.success('Project deleted successfully.')
                    })
            }
        }
    },
    async beforeRouteEnter(to, from, next) {
        const data = await (await fetch('/api/get-project/' + to.params.project_id)).json()
        next(vm => {
            vm.project = data
        })
    }
}
</script>
