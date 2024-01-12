<template>
  <div>
    <div class="input-container">
      <DateTimeRangePicker />
      <ProjectDropdown :projects="projects" @projectSelected="fetchAndAddToHistory" />
    </div>

    <LineChart :inputNH3="inputNH3" :outputNH3="outputNH3" :min="min" :max="max" />

    <div v-if="activeProject">
      <h2>Active Project Information</h2>
      <p>Project ID: {{ activeProject.id }}</p>
      <p>Project Name: {{ activeProject.name }}</p>
    </div>
    <ul>
      <li v-for="project in projectHistory" :key="project.id">
        {{ project.name }} - {{ project.date }}
      </li>
    </ul>
  </div>
</template>

<script>
import DateTimeRangePicker from '../components/DateTimePicker.vue';
import LineChart from '../components/LineChart.vue';
import ProjectDropdown from '@/components/ProjectDropdown.vue';

export default {
  components: {
    DateTimeRangePicker,
    ProjectDropdown,
    LineChart,
  },
  data() {
    return {
      inputNH3: [],
      outputNH3: [],
      min: 0,
      max: 100,
      activeProject: null,
      projectHistory: [],
      projects: [],
    };
  },
  methods: {
    async fetchActiveProject(projectId) {
      const response = await fetch(`/api/get-active-project/${projectId}`);
      const data = await response.json();
      this.activeProject = data;

      if (this.activeProject) {
        this.projectHistory.unshift({
          id: this.activeProject.id,
          name: this.activeProject.name,
          date: new Date().toLocaleString(),
        });
      }
    },
    async fetchAndAddToHistory(projectId) {
      await this.fetchActiveProject(projectId);
    },
  },
  created() {
    fetch('/api/get-projects')
      .then((response) => response.json())
      .then((data) => {
        this.projects = data;
      });
  },
};
</script>

<style scoped>
.input-container {
  display: flex;
  justify-content: space-between;
}
</style>
