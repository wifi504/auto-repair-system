<template>
  <div class="logout-container">
    <el-card>
      <!-- 退出状态提示 -->
      <el-result
          class="card-item"
          :icon="icon"
          :title="title"
          :sub-title="subTitle"
      >
        <template #extra>
          <div class="card-extra" v-if="hasLogout !== -1">
            你现在可以
            <el-button @click="doRoute" class="back-button" link type="primary">立即跳转</el-button>
            ！
          </div>
          <div v-else>
            Waiting...
          </div>
        </template>
      </el-result>
    </el-card>
  </div>
  <background-image image="login"/>
</template>

<script setup>
import {onMounted, ref} from "vue";
import {useRouter, useRoute} from "vue-router";
import BackgroundImage from "@/components/common/BackgroundImage.vue";
import request from "@/utils/request.js";

const router = useRouter()
const route = useRoute()
const title = ref('正在请求服务器...')
const icon = ref('info')
const hasLogout = ref(-1)
const subTitle = ref('')

const startCountdown = (count, msg, next) => {
  const timer = setInterval(() => {
    subTitle.value = `${count} ${msg}`
    count--
    if (count < 0) {
      clearInterval(timer)
      next()
    }
  }, 1000)
}

const doRoute = () => {
  if (hasLogout.value === 1) {
    router.push('/')
  } else if (hasLogout.value === 0) {
    router.back()
  }
}

onMounted(async () => {
  try {
    let res
    if (route.query.t && route.query.t === 'all') {
      // 注销用户全部设备登录态
      res = await request.post('logoutAll');
    } else {
      // 默认注销当前环境登录态
      res = await request.post('logout');
    }
    icon.value = 'success'
    title.value = res.msg || '退出成功';
    localStorage.removeItem('token');
    hasLogout.value = 1
    startCountdown(3, '秒后前往首页', () => doRoute())
  } catch (error) {
    icon.value = 'error'
    title.value = error.msg || '退出失败，请稍后重试';
    hasLogout.value = 0
    startCountdown(5, '秒后返回原页面', () => doRoute())
  }
});
</script>

<style scoped>
.logout-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.card-item {
  width: 250px;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.back-button {
  position: relative;
  top: -2px;
  font-size: 18px;
}
</style>
