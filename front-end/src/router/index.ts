import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        { path: '/', name: 'Dashboard', component: () => import('../views/Dashboard.vue') },
        { path: '/about', name: 'About', component: () => import('../views/About.vue') },
        { path: '/hello-world', name: 'Hello World', component: () => import('../views/HelloWorld.vue') },
    ]
})

router.afterEach(to => {
    if (to.name) {
        document.title = `SCR / ${to.name.toString()}`
    }
})

export default router
