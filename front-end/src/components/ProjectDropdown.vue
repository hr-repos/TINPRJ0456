<template>
    <div class="project-dropdown">
      <label for="projectDropdown">Select Project:</label>
      <select v-model="selectedProjectId" @change="selectProject" id="projectDropdown">
        <option value="" disabled>Select a project</option>
        <option v-for="project in projects" :key="project.id" :value="project.id">{{ project.name }}</option>
      </select>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      projects: Array,
    },
    data() {
      return {
        selectedProjectId: '',
      };
    },
    methods: {
      selectProject() {
        this.$emit('projectSelected', this.selectedProjectId);
      },
    },
  };
  </script>
  
  <style scoped>
  .project-dropdown {
    display: flex;
    align-items: flex-start;
  }
  
  label {
    margin-right: 10px;
  }
  
  select {
    width: 150px;
  }
  
  </style>
  