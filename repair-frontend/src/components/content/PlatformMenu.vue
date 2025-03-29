<template>
  <el-scrollbar height="100vh" noresize>
    <el-menu
        class="left-main-menu"
        :background-color="'#06304d'"
        default-active="1"
        :text-color="'#fff'"
        :active-text-color="'#54f9ff'"
    >
      <!-- 首页logo -->
      <el-menu-item index="banner" class="banner" @click="router.push('/')">
        <img src="@/assets/image/banner-white-small.png" alt="banner">
      </el-menu-item>
      <!-- 渲染菜单项 -->
      <template v-for="item in menuData" :key="item.index">
        <!-- 有子菜单时 -->
        <el-sub-menu v-if="item.children" :index="item.index">
          <template #title>
            <el-icon>
              <component :is="getIconComponent(item.icon)"/>
            </el-icon>
            <span>{{ item.title }}</span>
          </template>
          <!-- 递归渲染两级子菜单 -->
          <template v-for="child in item.children" :key="child.index">
            <el-sub-menu v-if="child.children" :index="child.index" :disabled="child.disabled">
              <template #title>{{ child.title }}</template>
              <el-menu-item
                  v-for="subChild in child.children"
                  :key="subChild.index"
                  :index="subChild.index"
                  :disabled="subChild.disabled"
              >
                {{ subChild.title }}
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item v-else :index="child.index" :disabled="child.disabled">
              {{ child.title }}
            </el-menu-item>
          </template>
        </el-sub-menu>
        <!-- 无子菜单时 -->
        <el-menu-item v-else :index="item.index" :disabled="item.disabled">
          <el-icon>
            <component :is="getIconComponent(item.icon)"/>
          </el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </template>
    </el-menu>
  </el-scrollbar>
</template>

<script setup>
import {reactive} from "vue"
import {useRouter} from "vue-router"
import {
  Menu, Monitor, User, OfficeBuilding,
  Avatar, Document, Ticket
} from "@element-plus/icons-vue"

const router = useRouter()

// 图标映射表
const iconMap = {
  Menu,
  Monitor,
  User,
  OfficeBuilding,
  Avatar,
  Document,
  Ticket
}

// 从映射表获取图标组件
const getIconComponent = (iconName) => {
  return iconMap[iconName] || Menu
}

// 菜单数据示例Data
// {index, title, icon, children, disabled}
const menuData = reactive([
  {
    index: '1',
    title: '概览',
    icon: 'Monitor'
  },
  {
    index: '2',
    title: '平台用户管理',
    icon: 'User',
    children: [
      {
        index: '2-1',
        title: '用户列表查询'
      },
      {
        index: '2-2',
        title: '账号申诉列表'
      }
    ]
  },
  {
    index: '3',
    title: '商铺管理',
    icon: 'OfficeBuilding',
    children: [
      {
        index: '3-1',
        title: '商铺列表查询'
      },
      {
        index: '3-2',
        title: '商铺开设/注销审核'
      }
    ]
  },
  {
    index: '4',
    title: '商家管理',
    icon: 'Avatar',
    children: [
      {
        index: '4-1',
        title: '商家列表'
      },
      {
        index: '4-2',
        title: '商家资质审核'
      }
    ]
  },
  {
    index: '5',
    title: '订单管理',
    icon: 'Document'
  },
  {
    index: '6',
    title: '工单管理',
    icon: 'Ticket'
  }
])
</script>

<style scoped>
.left-main-menu {
  overflow-y: auto;
  min-height: 100vh;
}

.left-main-menu .banner img {
  height: 70%;
  margin: auto;
}
</style>