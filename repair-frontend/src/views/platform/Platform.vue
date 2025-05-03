<template>
  <!-- 切换面板 -->
  <panel-switcher v-if="showSwitchPanel" @switch="handleSwitchPanel"/>
  <!-- 管理系统统一页面 -->
  <el-container v-else>
    <!-- 左侧菜单 -->
    <el-aside width="240px">
      <platform-menu :menu-data="menuData"/>
    </el-aside>
    <el-container>
      <!-- 头部 -->
      <el-header height="60px">
        <container-header :current-page-type="pageType" @switch="handleSwitchPanel"/>
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
import {ref, onMounted} from "vue";
import {useRouter} from "vue-router";
import PlatformMenu from "@/components/platform/PlatformMenu.vue";
import ContainerHeader from "@/components/platform/ContainerHeader.vue";
import PanelSwitcher from "@/components/platform/PanelSwitcher.vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const router = useRouter()

// 显示切换面板
const showSwitchPanel = ref(false)
// platform merchant customer
const pageType = ref('loading')
// 菜单数据
const menuData = ref([])
// 处理切换
const handleSwitchPanel = (data) => {
  router.push('/platform/overview')
  if (data) {
    pageType.value = data.name
    menuData.value = data.menus
  }
  showSwitchPanel.value = !showSwitchPanel.value
}
// 加载当前用户选择的菜单，如果没有，就让用户选择
const currentPanel = localStorage.getItem('default-panel')
const loadMenu = async () => {
  if (currentPanel) {
    try {
      const res = await request.get('/profile/panel')
      const panel = res.data.find(datum => datum.name === currentPanel)
      pageType.value = panel.name.toLowerCase()
      menuData.value = panel.menus
    } catch (err) {
      ElMessage.error(`获取菜单列表：${err.msg || 'Error'}`)
    }
  } else {
    showSwitchPanel.value = true
  }
}

onMounted(() => {
  loadMenu()
})
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