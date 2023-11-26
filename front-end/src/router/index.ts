import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        { path: '/', name: 'Home', component: () => import('../views/Home.vue') },
        { path: '/Dashboard', name: 'Dashboard', component: () => import('../views/Dashboard.vue') },
        { path: '/Projects', name: 'Projects', component: () => import('../views/Projects.vue') },
        { path: '/about', name: 'About', component: () => import('../views/About.vue') },
        { path: '/hello-world', name: 'Hello World', component: () => import('../views/HelloWorld.vue') },
    ]
});

router.afterEach(to => {
    if (to.name) {
        document.title = `SCR / ${to.name.toString()}`;
    }
});

export default router;
