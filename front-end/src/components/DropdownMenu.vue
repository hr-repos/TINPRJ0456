<template>
    <div class="dropdown">
      <button class="dropbtn">Select a Page</button>
      <div class="dropdown-content">
        <router-link to="/">Home</router-link>
        <router-link to="/Dashboard">Dashboard</router-link>
        <router-link to="/Projects">Projects</router-link>
      </div>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent } from 'vue';
  
  export default defineComponent({
    
  });
  </script>
  
  <style scoped>
  
  .dropdown {
    position: relative;
    display: inline-block;
  }
  
  .dropdown-content {
    display: none;
    position: absolute;
    background-color: #f9f9f9;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    z-index: 1;
  }
  
  .dropdown:hover .dropdown-content {
    display: block;
  }
  
  .dropdown-content a {
    color: black;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
  }
  
  .dropdown-content a:hover {
    background-color: #f1f1f1;
  }
  </style>
  