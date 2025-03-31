<template>
  <!-- 头部信息：当前操作端名称（平台、商家、客户）| 消息 | 切换操作端 | 头像 -->
  <div class="header-wrapper">
    <div class="header-left">
      <el-icon size="30px" :color="'#1067c9'" style="margin-right: 5px">
        <component :is="iconMap[titleData[currentPageType].icon]"/>
      </el-icon>
      <h2>{{ titleData[currentPageType].title }}</h2>
    </div>
    <div class="header-right">
      <div>1</div>
      <div>2</div>
      <div>3</div>
    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {Promotion, Shop, Flag} from '@element-plus/icons-vue'

// 图标映射
const iconMap = {
  Promotion, Shop, Flag
}

// 标题数据
const titleData = {
  platform: {
    title: '连接车主与商家，用数据驱动更优服务',
    icon: 'Promotion'
  },
  merchant: {
    title: '用心服务每一位车主，让维修更高效',
    icon: 'Shop'
  },
  user: {
    title: '让爱车焕然一新，安心出行每一天',
    icon: 'Flag'
  }
}

// 当前页面
const currentPageType = ref('platform')
</script>

<style scoped>
.header-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between; /* 左右对齐，自动分配空间 */
  align-items: center; /* 垂直居中 */
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}
</style>