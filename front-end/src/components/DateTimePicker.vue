
<template>
  <div class="date-time-pickers-container">
    <div class="date-time-picker">
      <label for="startDateTime">Start:</label>
      <input
        v-model="startDateTime"
        id="startDateTime"
        class="flatpickr"
        :data-alt-input="true"
        :data-alt-format="'F j, Y H:i'"
      />
    </div>
    <div class="date-time-picker">
      <label for="endDateTime">End:</label>
      <input
        v-model="endDateTime"
        id="endDateTime"
        class="flatpickr"
        :data-alt-input="true"
        :data-alt-format="'F j, Y H:i'"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import flatpickr from "flatpickr";
import "flatpickr/dist/flatpickr.css";
import { ref, onMounted } from 'vue';

const startDateTime = ref<string | null>(null);
const endDateTime = ref<string | null>(null);

onMounted(() => {
  flatpickr("#startDateTime", {
    enableTime: true,
    dateFormat: "Y-m-d H:i",
    altInput: true,
    altFormat: "F j, Y H:i",
    time_24hr: true,
    onClose(selectedDates) {
      startDateTime.value = selectedDates[0] instanceof Date ? selectedDates[0].toISOString() : null;
      // Log or send the startDateTime to the console or server
      console.log("Start Date: ", startDateTime.value);
    },
  });

  flatpickr("#endDateTime", {
    enableTime: true,
    dateFormat: "Y-m-d H:i",
    altInput: true,
    altFormat: "F j, Y H:i",
    time_24hr: true,
    onClose(selectedDates) {
      endDateTime.value = selectedDates[0] instanceof Date ? selectedDates[0].toISOString() : null;
      // Log or send the endDateTime to the console or server
      console.log("End Date: ", endDateTime.value);
    },
  });
});
</script>


<style scoped>
.date-time-pickers-container {
  display: flex;
  justify-content: space-between;
}

.date-time-picker {
  flex: 1;
  margin-right: 20px;
}
</style>
