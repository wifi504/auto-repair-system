import {createRouter, createWebHistory} from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/views/Index.vue'),
    meta: {
      title: '首页'
    }
  },
  {
    path: '/login',
    component: () => import('@/views/account/Login.vue'),
    meta: {
      title: '用户登录'
    }
  },
  {
    path: '/logout',
    component: () => import('@/views/account/Logout.vue'),
    meta: {
      title: '退出登录'
    }
  },
  {
    path: '/register',
    component: () => import('@/views/account/Register.vue'),
    meta: {
      title: '用户注册'
    }
  },
  {
    path: '/platform',
    component: () => import('@/views/platform/Platform.vue'),
    redirect: '/platform/overview',
    children: [
      {
        path: 'overview',
        component: () => import('@/views/platform/Overview.vue'),
        meta: {
          title: '概览'
        }
      },
      {
        path: 'manager/role',
        component: () => import('@/views/platform/RoleManager.vue'),
        meta: {
          title: '角色管理'
        }
      },
      {
        path: 'manager/user',
        component: () => import('@/views/platform/UserManager.vue'),
        meta: {
          title: '用户管理'
        }
      },
      {
        path: ':pathMatch(.*)*',
        component: () => import('@/views/platform/NotFound.vue')
      }
    ]
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  // 跳转页面自动改页面标题
  const title = to.meta.title
  if (title) {
    document.title = `${title} | 云修工坊`
  } else {
    document.title = '云修工坊'
  }

  // 如果已登录且访问的是登录页面，清除本地token
  const token = localStorage.getItem('token')
  if (token && to.path === '/login') {
    localStorage.removeItem('token')
  }
  next()
})

export default router