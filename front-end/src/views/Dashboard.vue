<template>
    <div v-if="project && project.name">
        <div class="flex justify-between mb-3">
            <p>
                <span>Current project: </span>
                <span class="font-bold" v-text="`${project.id}. ${project.name}`"></span>
            </p>
            <p>
                <RouterLink to="/projects">Change active project</RouterLink>
                &bull;
                <RouterLink :to="`/project/${project.id}/sensors`">Add sensors</RouterLink>
            </p>
        </div>
        <div v-if="project.sensors.length > 0" class="grid grid-cols-4 gap-4">
            <BorderBlock v-for="i in project.sensors" :name="`${i.name} (pin ${i.pin})`" :title="sensorData[i.pin]" />
        </div>
        <div v-else>
            <p>No sensors found for this project: <RouterLink :to="`/project/${project.id}/sensors`">Add sensors</RouterLink></p>
        </div>
        <div class="chart block">
            <LineChart title="chart" :inputNH3="inputNH3Array" :outputNH3="outputNH3Array"/>
        </div>
        <div>
            <button @click="test" v-if="testing">Disable testing</button>
            <button @click="test" v-else>Enable testing</button>
        </div>
    </div>
    <div v-else>
        <p>No active project found: <RouterLink to="/projects">Change active project</RouterLink></p>
    </div>
</template>

<script>
import emitter from '@/router/emitter'
import BorderBlock from '@/components/BorderBlock.vue'
import LineChart from '@/components/LineChart.vue'

export default {
    components: { LineChart, BorderBlock },
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
