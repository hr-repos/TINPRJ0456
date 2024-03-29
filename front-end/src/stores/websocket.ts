import { defineStore } from 'pinia'
import emitter from '@/router/emitter'

// keep a websocket connection
export const websocket = defineStore('websocket', {
    state: () => ({
        socket: null as WebSocket | null,
        open: false as boolean
    }),
    actions: {
        init() {
            this.socket = new WebSocket(`ws://${window.location.hostname}/api/socket`)
            // this.socket = new WebSocket(`ws://145.24.238.97/api/socket`)

            this.socket.onmessage = event => {
                const data = JSON.parse(event.data)
                // console.log(`Socket data received:`, data)

                if (data['connected'] === true) {
                    this.open = true
                    return
                }

                emitter.emit('socket', data)
            }

            // keep the connection alive
            const heartbeat = setInterval(() => this.socket?.send(''), 20000)

            this.socket.onclose = () => {
                this.open = false
                clearInterval(heartbeat)
                console.log('Socket disconnected. Reconnecting in 500ms...')
                setTimeout(() => this.init(), 500)
            }

            this.socket.onerror = () => {
                this.socket?.close()
            }
        }
    }
})
