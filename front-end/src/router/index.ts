import { createRouter, createWebHistory } from 'vue-router'
import { pageLoading } from '@/stores/pageLoading'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        { path: '/', name: 'Home', component: () => import('../views/Home.vue') },
        { path: '/dashboard', name: 'Dashboard', component: () => import('../views/Dashboard.vue') },
        { path: '/projects', name: 'Projects', component: () => import('../views/Projects.vue') },
        { path: '/sensors', name: 'Sensors', component: () => import('../views/Sensors.vue') },
    ]
});

router.afterEach(to => {
    if (to.name) {
        document.title = `SCR / ${to.name.toString()}`
    }
});

let current: String
let loading: Boolean = false

router.beforeEach((to, from, next) => {
    if (current !== to.path && !loading) {
        pageLoading().set(true)
        current = to.path
        loading = true
    }

    next()
})

router.afterEach(to => {
    if (loading) {
        pageLoading().set(false)
        loading = false
    }
})

export default router
