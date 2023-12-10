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
        <table class="w-full text-sm text-left rtl:text-right text-gray-500">
            <thead class="text-xs text-gray-700 uppercase bg-gray-100">
                <tr>
                    <th scope="col" class="px-6 py-3">
                        Active
                    </th>
                    <th scope="col" class="px-6 py-3">
                        ID
                    </th>
                    <th scope="col" class="px-6 py-3">
                        Name
                    </th>
                    <th scope="col" class="px-6 py-3">
                        Description
                    </th>
                    <th scope="col" class="px-6 py-3">
                        Creation date
                    </th>
                    <th scope="col" class="px-6 py-3">
                        Creator
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="i in projects" class="bg-white border-b hover:bg-gray-50">
                    <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
                        <input @click="change(i.id)" :checked="i.active" type="radio" id="active_project" name="active_project">
                    </th>
                    <td class="px-6 py-4">
                        {{ i.id }}
                    </td>
                    <td class="px-6 py-4">
                        {{ i.name }}
                    </td>
                    <td class="px-6 py-4">
                        {{ i.description }}
                    </td>
                    <td class="px-6 py-4">
                        {{ i.creation_date }}
                    </td>
                    <td class="px-6 py-4">
                        {{ i.creator_name }}
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
