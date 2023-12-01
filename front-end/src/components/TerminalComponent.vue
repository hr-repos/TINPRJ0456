<template>
    <div>
        <div class="terminal">
            <p v-for="(message, index) in terminalMessages" :key="index">{{ message }}</p>
        </div>
        <div class="input-container">
            <input v-model="inputString" @keyup.enter="sendToTerminal" placeholder="Type your command..."/>
            <button @click="sendToTerminal">Send</button>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            inputString: '',
            terminalMessages: [],
        };
    },
    methods: {
        sendToTerminal() {
            // Add the input string to the terminal messages
            this.terminalMessages.push(`New project: ${this.inputString}`);

            // Handle the command (you can customize this logic based on your use case)
            this.handleCommand(this.inputString);

            // Clear the input after sending
            this.inputString = '';
        },
        handleCommand(command) {
            // Handle the command here
            this.terminalMessages.push(`Terminal: ${command}`);
        },
    },
};
</script>

<style scoped>
.terminal {
    border: 1px solid #ccc;
    padding: 10px;
    margin-bottom: 10px;
    max-height: 200px;
    overflow-y: auto;
}

.input-container {
    display: flex;
}

input {
    flex: 1;
    padding: 5px;
}

button {
    padding: 5px 10px;
    margin-left: 5px;
}
</style>
