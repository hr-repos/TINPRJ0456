<template>
    <div v-if="project && project.id" class="flex flex-row gap-8">
        <div class="flex-1">
            <div v-if="sensors.length > 0" class="relative overflow-x-auto shadow-md sm:rounded-lg">
                <table class="w-full text-sm text-left rtl:text-right text-gray-500">
                    <thead class="text-xs text-gray-700 uppercase bg-gray-100">
                    <tr>
                        <th scope="col" class="px-6 py-3">ID</th>
                        <th scope="col" class="px-6 py-3">Name</th>
                        <th scope="col" class="px-6 py-3">Pin</th>
                        <th scope="col" class="px-6 py-3">Unit</th>
                        <th scope="col" class="px-6 py-3">Calibration</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="i in sensors" class="bg-white border-b hover:bg-gray-50">
                        <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap" v-text="i.id"></th>
                        <td class="px-6 py-4" v-text="i.name"></td>
                        <td class="px-6 py-4" v-text="i.pin"></td>
                        <td class="px-6 py-4" v-text="i.unit"></td>
                        <td class="px-6 py-4">
                            <div v-if="i.calibration.calibrated">
                                {{i.calibration.a}}x² + {{i.calibration.b}}x + {{i.calibration.c}}
                            </div>
                            <div v-else class="text-red-600 text-xs">Not calibrated</div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <WarningMessage v-else warning="No sensors found for this project." description="Add a sensor on the right." />
        </div>
        <div class="flex flex-col gap-2">
            <div class="mb-2">
                <p class="text-sm">Selected project</p>
                <RouterLink :to="`/project/${project.id}`"
                            class="font-bold" v-text="project.name + ' (id: ' + project.id + ')'"></RouterLink>
            </div>
            <input @keyup.enter="submit" placeholder="Sensor name" type="text" v-model="inputName">
            <input @keyup.enter="submit" placeholder="Pin number" type="number" v-model="inputPin">
            <input @keyup.enter="submit" placeholder="Measure unit" type="text" v-model="inputUnit">
            <button id="submit" @click="submit">Add sensor</button>
        </div>
    </div>
    <WarningMessage v-else-if="project" warning="Project not found." description="Register a new project here." link="/projects" />
</template>

<script>
import { toast } from 'vue3-toastify'
import WarningMessage from '@/components/WarningMessage.vue'

export default {
    components: { WarningMessage },
    data() {
        return {
            sensors: [],
            inputPin: null,
            inputName: '',
            inputUnit: 'μg/m³',
            project: null
        }
    },
    methods: {
        async submit() {
            document.getElementById("submit").disabled = true
            const t = toast.info('Adding sensor')
            await fetch('/api/submit-sensor', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    name: this.inputName,
                    pin: this.inputPin,
                    unit: this.inputUnit,
                    project: this.$route.params.project_id
                }),
            })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        toast.remove(t)
                        toast.error(data.error)
                        return
                    }

                    this.sensors.push({
                        id: data.id,
                        name: this.inputName,
                        pin: this.inputPin,
                        unit: this.inputUnit,
                        calibration: {
                            calibrated: false, a: 0, b: 1, c: 0
                        }
                    })
                    toast.remove(t)
                    toast.success('Sensor added')
                    this.inputPin = null
                    this.inputName = ''
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
        const data = await (await fetch('/api/get-sensors/' + to.params.project_id)).json()
        next(vm => {
            vm.sensors = data.sensors
            vm.project = data.project
        })
    }
}
</script>
