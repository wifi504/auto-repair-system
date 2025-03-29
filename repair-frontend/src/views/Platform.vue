<template>
  <!-- 管理系统统一页面 -->
  <div class="common-layout">
    <el-container>
      <!-- 左侧菜单 -->
      <el-aside width="240px">
        <platform-menu/>
      </el-aside>
      <el-container>
        <!-- 面板 -->
        <el-header>Header</el-header>
        <el-main>Main</el-main>
        <el-footer>Footer</el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import PlatformMenu from "@/components/content/PlatformMenu.vue";

</script>

<style scoped>
</style>