<template>
    <div class="flex flex-row gap-8">
        <div class="flex-1">
            <div v-if="sensors.length > 0" class="relative overflow-x-auto shadow-md sm:rounded-lg">
                <table class="w-full text-sm text-left rtl:text-right text-gray-500">
                    <thead class="text-xs text-gray-700 uppercase bg-gray-100">
                    <tr>
                        <th scope="col" class="px-6 py-3">ID</th>
                        <th scope="col" class="px-6 py-3">Name</th>
                        <th scope="col" class="px-6 py-3">Pin</th>
                        <th scope="col" class="px-6 py-3">Calibration</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="i in sensors" class="bg-white border-b hover:bg-gray-50">
                        <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap" v-text="i.id"></th>
                        <td class="px-6 py-4" v-text="i.name"></td>
                        <td class="px-6 py-4" v-text="i.pin"></td>
                        <td class="px-6 py-4">
                            <div v-if="i.calibration_a || i.calibration_b || i.calibration_c">
                                {{i.calibration_a}}x² + {{i.calibration_b}}x + {{i.calibration_c}}
                            </div>
                            <div v-else class="text-red-600 text-xs">Not calibrated</div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div v-else>
                <p class="font-bold">No sensors found for this project.</p>
                <p>Add a sensor on the right.</p>
            </div>
        </div>
        <div class="flex flex-col gap-2">
            <input @keyup.enter="submit" placeholder="Sensor name" type="text" v-model="inputName">
            <input @keyup.enter="submit" placeholder="Pin number" type="number" v-model="inputPin">
            <button id="submit" @click="submit">Add sensor</button>
        </div>
    </div>
</template>

<script>
import { toast } from 'vue3-toastify'

export default {
    data() {
        return {
            sensors: [],
            inputPin: null,
            inputName: ''
        }
    },
    methods: {
        async submit() {
            document.getElementById("submit").disabled = true
            toast.info('Adding sensor')
            await fetch('/api/submit-sensor', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    name: this.inputName,
                    pin: this.inputPin,
                    project: this.$route.params.project_id
                }),
            })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        toast.error(data.error)
                        return
                    }

                    this.sensors.push({
                        id: data.id,
                        name: this.inputName,
                        pin: this.inputPin
                    })
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
            vm.sensors = data
        })
    }
}
</script>
