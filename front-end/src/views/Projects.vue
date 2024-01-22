<template>
    <div class="flex flex-row gap-8">
        <div class="flex-1">
            <p>You can make a new project here. Just type in the name of the new project in the bar on the right.</p>
        </div>
        <div class="flex-1 flex flex-col gap-2">
            <input type="text" v-model="inputName" @keyup.enter="submit" placeholder="Project name"/>
            <input type="text" v-model="inputDescription" @keyup.enter="submit" placeholder="Description"/>
            <input type="text" v-model="inputCreator" @keyup.enter="submit" placeholder="Creator name"/>
            <button id="submit" @click="submit">Create project</button>
        </div>
    </div>

    <hr class="my-8 border-2">

    <div class="relative overflow-x-auto shadow-md sm:rounded-lg">
        <table class="w-full text-sm text-left rtl:text-right text-gray-800">
            <thead class="text-xs text-gray-700 uppercase bg-gray-100">
                <tr>
                    <th scope="col" class="px-6 py-3">Active</th>
                    <th scope="col" class="px-6 py-3">ID</th>
                    <th scope="col" class="px-6 py-3">Name</th>
                    <th scope="col" class="px-6 py-3">Description</th>
                    <th scope="col" class="px-6 py-3">Sensors</th>
                    <th scope="col" class="px-6 py-3">Creation date</th>
                    <th scope="col" class="px-6 py-3">Creator</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="i in projects" class="bg-white border-b hover:bg-gray-50">
                    <th scope="row" class="px-6 py-4 font-medium text-gray-950 whitespace-nowrap">
                        <input @click="change(i.id)" :checked="i.active" type="radio" id="active_project" name="active_project">
                    </th>
                    <td class="px-6 py-4 text-lg font-mono">
                        <RouterLink :to="`/project/${i.id}`" v-text="i.id"></RouterLink>
                    </td>
                    <td class="px-6 py-4">
                        <p class="text-lg leading-5 mb-1" v-text="i.name"></p>
                        <div class="text-[8pt]">
                            <RouterLink :to="`/project/${i.id}`">Project Page</RouterLink>
                        </div>
                    </td>
                    <td class="px-6 py-4">
                        <p v-if="i.description" class="leading-4" v-text="i.description"></p>
                        <p v-else class="italic text-gray-500">No description provided.</p>
                    </td>
                    <td class="px-6 py-4">
                        <p class="mb-1" v-text="`${i.sensors.length} total`"></p>
                        <div class="mb-1">
                            <p v-for="j in i.sensors" v-text="`${j.name} (pin ${j.pin})`"></p>
                        </div>
                        <RouterLink :to="`/project/${i.id}/sensors`" class="text-[8pt]">Add Sensor</RouterLink>
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
</template>

<script>
import { toast } from 'vue3-toastify'

export default {
    data() {
        return {
            inputName: '',
            inputDescription: '',
            inputCreator: '',
            projects: [],
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
            toast.info('Changing active project')

            await fetch('/api/submit-project-active-change', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: project_id,
            })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        toast.error(data.error)
                        return
                    }

                    toast.success('Changed active project')
                })
                .catch(error => {
                    console.error('Error:', error);
                })
        },
        async submit() {
            document.getElementById("submit").disabled = true
            toast.info('Adding project')
            await fetch('/api/submit-project', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    name: this.inputName,
                    description: this.inputDescription,
                    creatorName: this.inputCreator,
                    active: false
                }),
            })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        toast.error(data.error)
                        return
                    }

                    this.projects.push({
                        id: data.id,
                        name: this.inputName,
                        creation_date: 'now',
                        description: this.inputDescription,
                        creator_name: this.inputCreator,
                        active: false
                    })
                    toast.success('Project added')
                    this.inputName = ''
                    this.inputDescription = ''
                    this.inputCreator = ''
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
        })
    }
}
</script>
