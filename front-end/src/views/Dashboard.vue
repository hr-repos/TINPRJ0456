<template>
    <div v-if="project && project.name">
        <div class="grid grid-cols-4 gap-4">
            <div>
                <p class="text-sm">Active project</p>
                <RouterLink :to="`/project/${project.id}`"
                            class="font-bold" v-text="project.name + ' (id: ' + project.id + ')'"></RouterLink>
                <div class="flex flex-row gap-2 mt-2 text-sm">
                    <RouterLink class="router-button" to="/projects">Change project</RouterLink>
                    <RouterLink class="router-button" :to="`/project/${project.id}/sensors`">Add sensors</RouterLink>
                </div>
            </div>
            <div class="col-span-3">
                <div v-if="project.sensors.length > 0" class="grid grid-cols-4 gap-4">
                    <BorderBlock v-for="i in project.sensors"
                                 :name="i.name"
                                 :pin="i.pin"
                                 :title="sensorData[i.pin]" />
                </div>
                <WarningMessage v-else
                                warning="No sensors found for this project."
                                description="Add sensors here."
                                :link="`/project/${project.id}/sensors`" />
            </div>
        </div>
        <div class="chart block">
            <LineChart title="chart" :inputNH3="inputNH3Array" :outputNH3="outputNH3Array"/>
        </div>
        <div>
            <button @click="test" v-if="testing">Disable testing</button>
            <button @click="test" v-else>Enable testing</button>
        </div>
    </div>
    <WarningMessage v-else warning="No active project found." description="Change active project here." link="/projects" />
</template>

<script>
import emitter from '@/router/emitter'
import BorderBlock from '@/components/BorderBlock.vue'
import LineChart from '@/components/LineChart.vue'
import WarningMessage from '@/components/WarningMessage.vue'

export default {
    components: { WarningMessage, LineChart, BorderBlock },
    methods: {
        test() {
            this.testing = !this.testing
            if (this.testing) {
                this.fakeDataInterval = setInterval(() => {
                    const data = {
                        "data": [
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200)
                        ]
                    }

                    fetch('/api/submit-sensor-data', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify(data),
                    }).then(() => console.log('Sent:', data))
                }, 200)
            }
            else {
                clearInterval(this.fakeDataInterval)
            }
        },
    },
    mounted() {
        emitter.on('socket', data => {
            this.sensorData = data['sensor_data'] // = array [200, 250, 200, 200, 200, 200]
                                                  //    pin = 0,   1,   2,   3,   4,   5

            if (this.sensorData[0]) {
                if (this.inputNH3Array.length > 30) {
                    this.inputNH3Array.shift()
                }
                this.inputNH3Array.push(this.sensorData[0])
                this.min = Math.min(Math.min(this.outputNH3Array), Math.min(this.inputNH3Array))
            }
            if (this.sensorData[1]) {
                if (this.outputNH3Array.length > 30) {
                    this.outputNH3Array.shift()
                }
                this.outputNH3Array.push(this.sensorData[1])
                this.max = Math.max(Math.max(this.outputNH3Array), Math.max(this.inputNH3Array))
            }
        })
    },
    unmounted() {
        emitter.off('socket')
    },
    data() {
        return {
            sensorData: [],
            project: null,
            inputNH3Array: [],
            outputNH3Array: [],
            min: 0,
            max: 0,
            testing: false,
            fakeDataInterval: 0,
        }
    },
    async beforeRouteEnter(to, from, next) {
        const data = await (await fetch('/api/get-active-project')).json()
        next(vm => {
            vm.project = data
        })
    }
}
</script>
