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
          <transition mode="out-in" name="slide-fade" appear>
            <keep-alive>
              <component :is="Component"/>
            </keep-alive>
          </transition>
        </router-view>
      </el-main>
      <!-- 脚注 -->
      <el-footer height="45px">
        <div class="footer">
          <p>Copyright © 2025 云修工坊汽车维修管理系统</p>
          <p>作者：</p>
          <p>蜀ICP备</p>
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

.slide-fade-enter-active {
  transition: all 0.3s cubic-bezier(0, 0.5, 0.5, 1);
}

.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(0.5, 0, 1, 0.5);
}

.slide-fade-enter-from {
  opacity: 0;
  transform: translateX(-150px) scale(0.9);
}

.slide-fade-leave-to {
  opacity: 0;
  transform: translateX(150px) scale(0.9);
}
</style>