import {createRouter, createWebHistory} from 'vue-router'

const Index = () => import('@/views/Index.vue')

const Login = () => import('@/views/Login.vue')
const Logout = () => import('@/views/Logout.vue')

const routes = [
  {path: '/', component: Index},
  {path: '/login', component: Login},
  {path: '/logout', component: Logout},
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  // 如果已登录且访问的是登录页面，重定向到首页
  if (token && to.path === '/login') {
    next('/')
  } else {
    next()
  }
})

export default router