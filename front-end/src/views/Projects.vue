<template>
    <div class="flex flex-row gap-8">
        <div class="flex-1 flex flex-col gap-2">
            <input type="text" v-model="inputName" @keyup.enter="submit" placeholder="Project name"/>
            <input type="text" v-model="inputDescription" @keyup.enter="submit" placeholder="Description"/>
        </div>
        <div class="flex-1 flex flex-col gap-2">
            <input type="text" v-model="inputCreator" @keyup.enter="submit" placeholder="Creator name"/>
            <input type="number" v-model="inputFrequency" @keyup.enter="submit" placeholder="Measure frequency (ms)"/>
        </div>
        <div class="items-center flex flex-col gap-1.5">
            <button class="w-full" id="submit" @click="submit">Create project</button>
            <button class="w-full text-sm" :disabled="selected === ''"  @click="change(0)">Stop measurements</button>
        </div>
    </div>

    <hr class="my-8 border-2">

    <div v-if="projects.length > 0" class="relative overflow-x-auto shadow-md sm:rounded-lg">
        <table class="w-full text-sm text-left rtl:text-right text-gray-800">
            <thead class="text-xs text-gray-700 uppercase bg-gray-100">
                <tr>
                    <th scope="col" class="px-6 py-3">Active</th>
                    <th scope="col" class="px-6 py-3">ID</th>
                    <th scope="col" class="px-6 py-3">Name</th>
                    <th scope="col" class="px-6 py-3">Description</th>
                    <th scope="col" class="px-6 py-3">Sensors</th>
                    <th scope="col" class="px-6 py-3">Frequency</th>
                    <th scope="col" class="px-6 py-3">Creation date</th>
                    <th scope="col" class="px-6 py-3">Creator</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="i in projects" class="bg-white border-b hover:bg-gray-50">
                    <th scope="row" class="px-6 py-4 font-medium text-gray-950 whitespace-nowrap">
                        <input @click="change(i.id)"
                               v-model="selected"
                               type="radio"
                               :id="`active_project` + i.id"
                               :value="`active_project` + i.id"
                               name="active_project">
                    </th>
                    <td class="px-6 py-4 text-lg font-mono">
                        <RouterLink :to="`/project/${i.id}`" v-text="i.id"></RouterLink>
                    </td>
                    <td class="px-6 py-4">
                        <p class="text-lg leading-5 mb-1 font-bold" v-text="i.name"></p>
                        <div class="text-[8pt]">
                            <RouterLink :to="`/project/${i.id}`">Project page</RouterLink>
                        </div>
                    </td>
                    <td class="px-6 py-4">
                        <p v-if="i.description" class="leading-4" v-text="i.description"></p>
                        <p v-else class="italic text-gray-500">No description provided.</p>
                    </td>
                    <td class="px-6 py-4">
                        <p class="mb-1 font-bold" v-text="`${i.sensors.length} total`"></p>
                        <div class="mb-1">
                            <p v-for="j in i.sensors" v-text="`(${j.pin}) ${j.name}`"></p>
                        </div>
                        <RouterLink :to="`/project/${i.id}/sensors`" class="text-[8pt]">Add sensor</RouterLink>
                    </td>
                    <td class="px-6 py-4">
                        <p v-text="i.frequency + 'ms'"></p>
                    </td>
                    <td class="px-6 py-4">
                        <p v-text="formatDate(i.creation_date)"></p>
                        <p v-text="formatTime(i.creation_date)"></p>
                    </td>
                    <td class="px-6 py-4">
                        <p v-text="i.creator_name"></p>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <WarningMessage v-else warning="No projects found." description="Create a new project above here." />
</template>

<script>
import { toast } from 'vue3-toastify'
import WarningMessage from '@/components/WarningMessage.vue'

export default {
    components: { WarningMessage },
    data() {
        return {
            // Input fields for creating a new project
            inputName: '',
            inputDescription: '',
            inputCreator: '',
            inputFrequency: null,
            projects: [],
            selected: ''
        };
    },
    methods: {
        formatDate(timestamp) {
            return new Date(timestamp * 1000).toLocaleString('en-US', {
                month: 'long',
                day: 'numeric',
                year: 'numeric',
            })
        },
        formatTime(timestamp) {
            return new Date(timestamp * 1000).toLocaleString('en-US', {
                hour: 'numeric',
                minute: 'numeric',
                second: 'numeric',
                hour12: false,
            })
        },
        async change(project_id) {
            const t = toast.info('Changing active project')

            await fetch('/api/submit-project-active-change', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: project_id,
            })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        toast.remove(t)
                        toast.error(data.error)
                        return
                    }

                    if (data.updated === 0) {
                        this.selected = ''
                    }

                    toast.remove(t)
                    toast.success('Changed active project')
                })
                .catch(error => {
                    console.error('Error:', error);
                })
        },
        async submit() {
            document.getElementById("submit").disabled = true
            const t = toast.info('Adding project')
            await fetch('/api/submit-project', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    name: this.inputName,
                    description: this.inputDescription,
                    creatorName: this.inputCreator,
                    frequency: this.inputFrequency,
                    active: false
                }),
            })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        toast.remove(t)
                        toast.error(data.error)
                        return
                    }

                    this.projects.push({
                        id: data.id,
                        name: this.inputName,
                        creation_date: Date.now() / 1000,
                        description: this.inputDescription,
                        creator_name: this.inputCreator,
                        frequency: this.inputFrequency,
                        active: false,
                        sensors: []
                    })
                    toast.remove(t)
                    toast.success('Project added')
                    this.inputName = ''
                    this.inputDescription = ''
                    this.inputCreator = ''
                    this.inputFrequency = null
                })
                .catch(error => {
                    console.error('Error:', error);
                })
                .finally(() => {
                    document.getElementById("submit").disabled = false
                });
        }
    },
    async beforeRouteEnter(to, from, next) {
        const data = await (await fetch('/api/get-projects')).json()
        next(vm => {
            vm.projects = data
            for (let i = 0; i < data.length; i++) {
                if (data[i].active) {
                    vm.selected = `active_project${data[i].id}`
                    break
                }
            }
        })
    }
}
</script>
