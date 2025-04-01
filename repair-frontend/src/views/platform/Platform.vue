<template>
  <!-- 管理系统统一页面 -->
  <el-container>
    <!-- 左侧菜单 -->
    <el-aside width="240px">
      <platform-menu/>
    </el-aside>
    <el-container>
      <!-- 头部 -->
      <el-header height="60px">
        <container-header/>
      </el-header>
      <!-- 面板内容 -->
      <el-main>
        <router-view v-slot="{ Component }">
          <keep-alive>
            <component :is="Component"/>
          </keep-alive>
        </router-view>
      </el-main>
      <!-- 脚注 -->
      <el-footer height="45px">
        <div class="footer">
          <p>Copyright © 2025 云修工坊汽车维修管理系统</p>
          <p>作者：刘昊林</p>
          <p>蜀ICP备2022004197号</p>
        </div>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script setup>
import PlatformMenu from "@/components/platform/PlatformMenu.vue";
import ContainerHeader from "@/components/platform/ContainerHeader.vue";
</script>

<style scoped>
.el-container {
  height: 100vh;
  overflow: hidden;
}

.el-header {
  box-shadow: 0 0 3px 3px rgba(0, 0, 0, .1);
  z-index: 999;
}

.el-main {
  background-color: #fafafa;
  height: 100%;
  overflow: hidden;
}

.el-footer {
  background-color: #eeeeee;
}

.footer {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
}

.footer p {
  display: block;
  color: #7e7e7e;
}
</style>