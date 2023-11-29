<template>
  <div>
    <h1>Project Page</h1>
    <p>You can make a new project here.</p>
    <p>Just type in the bar below the name of the new project,</p>
    <TerminalComponent />
  </div>
</template>

<script>
import TerminalComponent from '@/components/TerminalComponent.vue';

export default {
  components: {
    TerminalComponent,
  },
};
</script>

<style scoped>
/* Add your page-specific styles here */
h1 {
  text-align: center;
  margin-bottom: 20px;
}

p {
  text-align: center;
  margin-bottom: 10px;
}
</style>