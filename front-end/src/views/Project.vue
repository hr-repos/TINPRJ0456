<template>
    <div v-if="project" class="flex flex-row">
        <div class="flex-1 flex flex-col gap-4">
            <div class="text-4xl">
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
                <p class="font-bold text-2xl">Sensors</p>
                <div v-if="project.sensors.length > 0" class="grid grid-cols-1">
                    <div v-for="i in project.sensors" v-text="`Pin ${i.pin}: ${i.name}`" />
                </div>
                <div v-else>
                    <p>No sensors found for this project: <RouterLink :to="`/project/${project.id}/sensors`">Add sensors</RouterLink></p>
                </div>
            </div>
        </div>
        <div class="flex flex-col gap-2">
            <RouterLink class="router-button" :to="`/project/${project.id}/history`">View history</RouterLink>
            <RouterLink class="router-button" :to="`/project/${project.id}/sensors`">View sensors</RouterLink>
            <button @click="exportCsv">Export all data to CSV</button>
        </div>
    </div>
</template>

<script>
import LoadSpinner from '../components/LoadSpinner.vue'
import BorderBlock from '@/components/BorderBlock.vue'
import {toast} from "vue3-toastify";

export default {
    components: { BorderBlock, LoadSpinner },
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
        async exportCsv() {
            try {
                const response = await fetch('/api/export-to-csv/' + this.project.id)
                if (response.ok) {
                    const blob = await response.blob()
                    const link = document.createElement('a')

                    link.href = window.URL.createObjectURL(blob)
                    link.download = `export-${this.project.name.toLowerCase().replace(/[^a-z0-9_\-]/g, '')}.csv`
                    document.body.appendChild(link)
                    link.click()
                    document.body.removeChild(link)
                } else {
                    const data = await response.json()
                    toast.error('Download failed: ' + data.error)
                }
            } catch (error) {
                toast.error('Download failed: ' + error)
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
