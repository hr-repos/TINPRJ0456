<template>
    <div class="min-h-screen">
        <header class="bg-blue-950 pb-32 text-yellow-50">
            <nav>
                <div class="max-w-7xl mx-auto sm:px-4">
                    <div class="z-500 relative h-16 flex items-center justify-between border-b border-gray-200 border-opacity-25">
                        <div class="px-2 flex items-center lg:px-0">
                            <div class="ml-2 xl:ml-3">
                                <div class="flex space-x-1">
                                    <template v-for="item in navigation">
                                        <RouterLink v-if="item.to"
                                                    :key="item.name"
                                                    :to="item.to"
                                                    v-text="item.name"
                                                    class="text-white font-bold py-1 px-2 hover:bg-slate-700 hover:bg-opacity-75 rounded-md" />
                                    </template>
                                </div>
                            </div>
                            <div class="ml-4" v-if="state">
                                <LoadSpinner/>
                            </div>
                        </div>
                        <div class="flex-1 px-2 flex justify-end">
                            <span v-if="open" class="text-emerald-500">Connected</span>
                            <span v-else class="text-red-500">Disconnected</span>
                        </div>
                    </div>
                </div>
            </nav>
            <div class="mb-4 mt-5 lg:mb-5 lg:mt-6">
                <div class="max-w-7xl mx-auto px-6 sm:px-8 lg:px-10 min-h-[40px]">
                    <div class="lg:flex-1">
                        <span class="text-3xl" v-text="$route.name" />
                    </div>
                </div>
            </div>
        </header>
        <main class="-mt-32">
            <div class="max-w-7xl mx-auto pb-8 px-4 sm:px-6 lg:px-8">
                <div class="bg-white shadow px-5 py-6 sm:px-6 border-b-4 border-neutral-700 min-h-[10em]">
                    <router-view />
                </div>
            </div>
        </main>
        <footer>
            <div class="max-w-7xl mx-auto pb-8 px-4 sm:px-6 md:flex md:items-center md:justify-between lg:px-8">
                <div class="mt-8 md:mt-0 md:order-1">
                    <div class="text-center md:text-left text-base text-gray-500">
                        <p>Dashboard for the SCR system</p>
                        <p class="text-sm">Made by: Tigo Goes, Thomas van Vliet, Tim Kieboom, and Eli van der Does</p>
                    </div>
                </div>
            </div>
        </footer>
    </div>
</template>

<script lang="ts">
// all the used imports
import { websocket } from '@/stores/websocket'
import { defineComponent } from 'vue'
import { mapState } from 'pinia'
import DropdownMenu from './components/DropdownMenu.vue'
import { pageLoading } from '@/stores/pageLoading'
import LoadSpinner from "@/components/LoadSpinner.vue";

//Navigation items and their routes
const navigation = [
    { name: 'Home', to: '/' },
    { name: 'Dashboard', to: '/dashboard' },
    { name: 'Projects', to: '/projects' },
    { name: 'Sensors', to: '/sensors' },
    // { name: 'History', to: '/history' },
]

export default defineComponent({
    created() {
        websocket().init();
    },
    setup() {
        return {
            navigation
        }
    },
    computed: {
        ...mapState(websocket, ['open']),
        ...mapState(pageLoading, ['state'])
    },
    components: {LoadSpinner, DropdownMenu }
})
</script>
