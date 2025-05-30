<template>
  <div class="panel-switch-mask">
    <div class="wrapper">
      <div v-show="showMerchant"
           class="card" :class="currentPanel === 'MERCHANT' ? 'panel-card-selected' : ''"
           @click="handleMerchantClick">
        <img src="@/assets/image/merchant.svg" alt="商户Logo">
        <h2>商户面板</h2>
        <h3 v-show="currentPanel === 'MERCHANT'">已选择</h3>
      </div>
      <div v-show="showUser"
           class="card" :class="currentPanel === 'CUSTOMER' ? 'panel-card-selected' : ''"
           @click="handleUserClick">
        <img src="@/assets/image/customer.svg" alt="用户Logo">
        <h2>用户面板</h2>
        <h3 v-show="currentPanel === 'CUSTOMER'">已选择</h3>
      </div>
      <div v-show="showPlatform"
           class="card" :class="currentPanel === 'PLATFORM' ? 'panel-card-selected' : ''"
           @click="handlePlatformClick">
        <img src="@/assets/image/platform.svg" alt="平台Logo">
        <h2>平台面板</h2>
        <h3 v-show="currentPanel === 'PLATFORM'">已选择</h3>
      </div>
    </div>
    <div class="mask-footer" v-show="!showMerchant || !showPlatform">
      <span>您还可以选择：</span>
      <el-button size="small" v-show="!showMerchant">商家入驻/注册</el-button>
      <span v-show="!showMerchant && !showPlatform"> 或者 </span>
      <el-button size="small" v-show="!showPlatform">加入我们</el-button>
    </div>
    <background-image image="login"/>
  </div>
</template>

<script setup>
import {ref, onMounted} from "vue";
import BackgroundImage from "@/components/common/BackgroundImage.vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const emit = defineEmits(['switch'])

const handleMerchantClick = () => emit('switch', getMenuData('MERCHANT'))
const handleUserClick = () => emit('switch', getMenuData('CUSTOMER'))
const handlePlatformClick = () => emit('switch', getMenuData('PLATFORM'))

const showMerchant = ref(false)
const showUser = ref(false)
const showPlatform = ref(false)
const userPanels = ref([])
const currentPanel = ref('')

// 获取用户具有的面板
const loadUserPanels = async () => {
  try {
    currentPanel.value = localStorage.getItem('default-panel')
    const res = await request.get('/profile/panel')
    userPanels.value = res.data
    const userPanelNames = res.data.map(panel => panel.name)
    showMerchant.value = userPanelNames.includes('MERCHANT')
    showUser.value = userPanelNames.includes('CUSTOMER')
    showPlatform.value = userPanelNames.includes('PLATFORM')
  } catch (err) {
    ElMessage.error(`获取面板列表：${err.msg}`)
  }
}

// 从面板数据中提取对应菜单
const getMenuData = (type) => {
  localStorage.setItem('default-panel', type)
  const panel = userPanels.value.find(panel => panel.name === type)
  if (panel) {
    return {
      name: type.toLowerCase(),
      menus: panel.menus
    }
  }
  return null
}

onMounted(() => {
  loadUserPanels()
})
</script>

<style scoped>
.panel-switch-mask {
  position: relative;
  width: 100vw;
  height: 100vh;
}

.panel-switch-mask .wrapper {
  position: absolute;
  top: 45%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  gap: 50px;
}

.panel-switch-mask img {
  margin-top: 60px;
  margin-bottom: 20px;
  width: 100px;
  height: 100px;
}

.panel-switch-mask .wrapper .card {
  width: 220px;
  height: 280px;
  text-align: center;
  background: #fafafa;
  border-radius: 15px;
  box-shadow: 5px 5px 10px rgba(0, 0, 0, .1);
  transition: transform 0.5s ease;
}

.panel-switch-mask .wrapper .card:hover {
  transform: scale(1.1);
}

.panel-switch-mask .wrapper .card:active {
  transform: scale(0.9);
}

.panel-card-selected {
  background: #92cbf4 !important;
  color: #2888cf;
  transform: scale(0.95);
}

.panel-switch-mask .wrapper .card h3 {
  margin-top: 10px;
}

.mask-footer {
  position: absolute;
  top: 75%;
  left: 50%;
  transform: translate(-50%, -50%);
  gap: 10px;
  background: #fafafa;
  color: #606266;
  font-size: 14px;
  border-radius: 1000px;
  box-shadow: 5px 5px 10px rgba(0, 0, 0, .1);
  padding: 10px 50px;
}
</style>