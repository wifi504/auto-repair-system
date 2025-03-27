<template>
  <div class="logout-container">
    <el-card>
      <!-- 退出状态提示 -->
      <el-result
          v-if="hasLogout"
          icon="success"
          title="您已成功退出"
          sub-title="2秒后将自动跳转到登录页面"
      >
        <template #extra>
          <p>信息：{{ msg }}</p>
        </template>
      </el-result>

      <!-- 加载提示 使用 el-icon 模拟loading状态 -->
      <el-result v-else>
        <template #icon>
          <el-icon class="is-loading"><i class="el-icon-loading" /></el-icon>
        </template>
        <template #title>正在退出...</template>
      </el-result>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import request from "@/utils/request.js";
import { onMounted, ref } from "vue";

const router = useRouter();
const hasLogout = ref(false);
const msg = ref("");

onMounted(async () => {
  try {
    const res = await request.post('/logout');
    hasLogout.value = true;
    msg.value = res.msg || '退出成功';
    localStorage.removeItem('token');

    // 延迟跳转
    setTimeout(() => {
      router.push('/login');
    }, 2000);
  } catch (error) {
    msg.value = error?.response?.data?.message || '退出失败，请稍后重试';
    hasLogout.value = true;
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
</style>
