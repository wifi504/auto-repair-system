<template>
  <div class="panel-switch-mask">
    <div class="wrapper">
      <div v-show="showMerchant"
           class="card" @click="handleMerchantClick">
        <img src="@/assets/image/merchant.svg" alt="商户Logo">
        <h2>商户面板</h2>
      </div>
      <div v-show="showUser"
           class="card" @click="handleUserClick">
        <img src="@/assets/image/user.svg" alt="用户Logo">
        <h2>用户面板</h2>
      </div>
      <div v-show="showPlatform"
           class="card" @click="handlePlatformClick">
        <img src="@/assets/image/platform.svg" alt="平台Logo">
        <h2>平台面板</h2>
      </div>
    </div>
    <div class="mask-footer" v-show="!showMerchant || !showPlatform">
      <span>您还可以选择：</span>
      <el-button v-show="!showMerchant">商家入驻/注册</el-button>
      <span v-show="!showMerchant && !showPlatform">或者</span>
      <el-button v-show="!showPlatform">加入我们</el-button>
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

const handleMerchantClick = () => emit('switch', 'merchant')
const handleUserClick = () => emit('switch', 'user')
const handlePlatformClick = () => emit('switch', 'platform')

const showMerchant = ref(false)
const showUser = ref(false)
const showPlatform = ref(false)

// 获取用户具有的面板
const loadUserPanels = async () => {
  try {
    const res = await request.get('/profile/panel')
    const userPanels = res.data
    showMerchant.value = userPanels.includes('MERCHANT')
    showUser.value = userPanels.includes('CUSTOMER')
    showPlatform.value = userPanels.includes('PLATFORM')
  } catch (err) {
    ElMessage.error(`获取面板列表：${err.msg}`)
  }
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

.mask-footer {
  position: absolute;
  top: 75%;
  left: 50%;
  transform: translate(-50%, -50%);
  gap: 10px;
  background: #fafafa;
  border-radius: 1000px;
  box-shadow: 5px 5px 10px rgba(0, 0, 0, .1);
  padding: 10px 100px;
}
</style>