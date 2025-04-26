<template>
  <div class="index-container">
    <el-card>
      <h2>欢迎来到汽车维修管理系统</h2>

      <el-divider></el-divider>

      <!-- 用户信息展示 -->
      <el-descriptions title="用户信息" v-if="hasLogin" :column="1" border>
        <el-descriptions-item label="用户名">{{ userDetail.data.nickname }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ userDetail.data.createTime}}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ userDetail.data.email }}</el-descriptions-item>
      </el-descriptions>

      <!-- 未登录提示 -->
      <el-alert v-else title="您当前未登录，请先登录以查看用户信息" type="warning" show-icon />

      <el-divider></el-divider>

      <!-- 操作按钮 -->
      <el-space>
        <el-button type="primary" plain @click="goPlatform">管理面板</el-button>
        <el-button type="primary" @click="login" :disabled="hasLogin">登录</el-button>
        <el-button type="danger" @click="logout" :disabled="!hasLogin">注销</el-button>
        <el-button type="danger" @click="logoutAll" :disabled="!hasLogin">注销所有设备</el-button>
      </el-space>
    </el-card>
  </div>
</template>

<script setup>
import request from "@/utils/request.js"
import {onMounted, ref} from "vue"
import {useRouter} from "vue-router"

const router = useRouter()
const hasLogin = ref(false)
const userDetail = ref({})

onMounted(async () => {
  try {
    userDetail.value = await request.get('user/current')
    hasLogin.value = true
  } catch (error) {
    console.error('获取用户信息失败:', error)
    hasLogin.value = false
  }
})

const goPlatform = () => {
  router.push('/platform')
}

const login = () => {
  router.push('/login')
}

const logout = () => {
  router.push('/logout')
}

const logoutAll = () => {
  router.push('/logout?t=all')
}
</script>

<style scoped>
.index-container {
  padding: 40px;
}

h2 {
  margin-bottom: 16px;
}
</style>
