import { createRouter, createWebHistory } from 'vue-router'
import { pageLoading } from '@/stores/pageLoading'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        { path: '/', name: 'Home', component: () => import('../views/Home.vue') },
        { path: '/dashboard', name: 'Dashboard', component: () => import('../views/Dashboard.vue') },
        { path: '/history', name: 'History', component: () => import('../views/History.vue') },
        { path: '/projects', name: 'Projects', component: () => import('../views/Projects.vue') },
        { path: '/project/:project_id', name: 'Project', component: () => import('../views/Project.vue') },
        { path: '/project/:project_id/history', name: 'Project History', component: () => import('../views/ProjectHistory.vue') },
        { path: '/project/:project_id/sensors', name: 'Project Sensors', component: () => import('../views/ProjectSensors.vue') },
        { path: '/sensors', name: 'Sensors', component: () => import('../views/Sensors.vue') },
        { path: '/:catchAll(.*)', name: 'Error', component: () => import('/src/views/error/404.vue') },
    ]
});

router.afterEach(to => {
    if (to.name) {
        document.title = `SCR / ${to.name.toString()}`
    }
});

let current = null
let loading = false

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
