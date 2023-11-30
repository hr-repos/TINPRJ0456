import { createRouter, createWebHistory } from 'vue-router'

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

export default router
