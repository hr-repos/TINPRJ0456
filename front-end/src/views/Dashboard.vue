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
                                 :unit="i.unit"
                                 :title="sensorData[i.pin]"
                                 :calibrated="i.calibrated"/>
                </div>
                <WarningMessage v-else
                                warning="No sensors found for this project."
                                description="Add sensors here."
                                :link="`/project/${project.id}/sensors`" />
            </div>
        </div>
        <div class="chart block">
            <LineChart title="chart" :inputNH3="inputNH3Array" :outputNH3="outputNH3Array" :array3= "array3Array" :array4= "array4Array" :array5= "array5Array" :array6= "array6Array" :array7= "array7Array" :array8= "array8Array"/>
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
        //test function to send random data
        test() {
            this.testing = !this.testing
            if (this.testing) {
                this.fakeDataInterval = setInterval(() => {
                    const data = {
                        "data": [
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200),
                            Math.floor(Math.random() * 100 + 200),
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
        //get sensor data from the socket
        emitter.on('socket', data => {
            const array = data['sensor_data']
            
            //calibrate the sensor data
            for (let i = 0; i < array.length; i++) {
                const pin = this.project.pins['pin' + i]
                if (pin) {
                    array[i] = Math.floor(pin['b'] * array[i] + pin['c'])
                }
            }

            this.sensorData = array;
            //add sensor data to array
            if (this.sensorData[0] || this.sensorData[1] || this.sensorData[2] || this.sensorData[3] || this.sensorData[4] || this.sensorData[5] || this.sensorData[6] || this.sensorData[7]) {
                if (this.inputNH3Array.length >= 30) {
                    this.inputNH3Array.shift();
                }
                //add sensor data to array
                this.inputNH3Array.push(this.sensorData[0]);
                this.outputNH3Array.push(this.sensorData[1]);
                this.array3Array.push(this.sensorData[2]);
                this.array4Array.push(this.sensorData[3]);
                this.array5Array.push(this.sensorData[4]);
                this.array6Array.push(this.sensorData[5]);
                this.array7Array.push(this.sensorData[6]);
                this.array8Array.push(this.sensorData[7]);
                this.min = Math.min(...this.inputNH3Array);
                this.max = Math.max(...this.inputNH3Array);
            }
        });
    },
    unmounted() {
        emitter.off('socket')
    },
    data() {
        return {
            //return sensor data
            sensorData: [],
            project: null,
            inputNH3Array: [],
            outputNH3Array: [],
            array3Array: [],
            array4Array: [],
            array5Array: [],
            array6Array: [],
            array7Array: [],
            array8Array: [],
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
