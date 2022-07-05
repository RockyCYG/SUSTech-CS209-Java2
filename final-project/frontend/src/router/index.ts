import { createRouter, createWebHashHistory, RouteRecordRaw, RouterView } from "vue-router";
import AppLayout from "../layout/AppLayout.vue"

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        component: AppLayout,
        children: [
            {
                path: '',
                name: 'home',
                component: () => import("../views/Home.vue")
            },
            {
                path: '/stackoverflow',
                name: 'stackoverflow',
                component: RouterView,
                children: [
                    {
                        path: 'chart',
                        name: 'stackoverflowchart',
                        component: () => import("../views/StackOverflowChart.vue")
                    },
                    {
                        path: 'table',
                        name: 'stackoverflowtable',
                        component: () => import("../views/StackOverflowTable.vue")
                    }
                ]
            },
            {
                path: '/github',
                name: 'github',
                component: RouterView,
                children: [
                    {
                        path: 'chart',
                        name: 'githubchart',
                        component: () => import("../views/GithubChart.vue")
                    },
                    {
                        path: 'table',
                        name: 'githubtable',
                        component: () => import("../views/GithubTable.vue")
                    }
                ]
            }
        ]
    },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes: routes
})

export default router