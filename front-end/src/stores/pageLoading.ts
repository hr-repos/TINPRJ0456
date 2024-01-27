import { defineStore } from 'pinia'

// keep track of the loading state of the page
export const pageLoading = defineStore('pageLoading', {
    state: () => ({
        state: false as boolean
    }),
    actions: {
        set(state: boolean) {
            this.state = state
        }
    }
})
