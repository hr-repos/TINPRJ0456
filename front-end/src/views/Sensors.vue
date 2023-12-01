<script lang="ts">
import { defineComponent } from "vue"

export default defineComponent({
    name: 'Sensors',
    data() {
        return {
            sensors: [],
            inputPin: 0,
            inputName: ''
        }
    },
    methods: {
        async submit() {
            await fetch('/api/submit-sensor', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    name: this.inputName,
                    pin: this.inputPin
                }),
            })
                .then(response => {
                    if (response.ok) {
                        this.sensors.push(response.json())
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    },
    async beforeRouteEnter(to, from, next) {
        const data = await (await fetch('/api/get-sensors')).json()
        next(vm => {
            vm.sensors = data
        })
    }
})
</script>

<template>
    <input class="w-full border" placeholder="Sensornaam" type="text" v-model="inputName">
    <input class="w-full border" placeholder="Nummer van pin" type="number" v-model="inputPin">
    <button @click="submit">Voeg sensor toe</button>

    <div class="flex flex-col">
        <div v-for="i in sensors" class="flex flex-row gap-8 border">
            <div>Naam: {{ i.name }}</div>
            <div>Pin: {{ i.pin }}</div>
            <div>
                <div v-if="i.calibration_a || i.calibration_b || i.calibration_c">
                    {{i.calibration_a}}x² + {{i.calibration_b}}x + {{i.calibration_c}}
                </div>
                <div v-else>Niet gekalibreerd</div>
            </div>
        </div>
    </div>
</template>
