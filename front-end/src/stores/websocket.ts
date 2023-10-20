import { defineStore } from 'pinia'
import emitter from '@/router/emitter'

export const websocket = defineStore('websocket', {
    state: () => ({
        socket: null as WebSocket | null,
        open: false as boolean
    }),
    actions: {
        init() {
            this.socket = new WebSocket('ws://localhost/api/socket')

            this.socket.onmessage = event => {
                const data = JSON.parse(event.data)
                console.log(`Socket data received:`, data)

                if (data['connected'] === true) {
                    this.open = true
                    return
                }

                emitter.emit('socket', data)
            }

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
