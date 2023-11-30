import { defineStore } from 'pinia'

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
