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
      <div class="avatar-pop-wrapper">
        <div
            @mouseenter="avatarIconEnter"
            @mouseleave="avatarIconLeave"
            class="avatar-icon"
            :class="isHover ? 'avatar-icon-hover' : ''"
        >
          <async-avatar
              class="async-avatar-div"
              type="circle"
              :size="44"
              :avatar-name="currentLoginUserAvatar"
          />
        </div>
        <div
            @mouseenter="avatarCardEnter"
            @mouseleave="avatarCardLeave"
            class="avatar-hover-card"
            :class="isHover ? '' : 'avatar-hover-card-hidden'"
        >
          <el-text type="primary" size="large">
            {{ currentLoginUser.nickname }}
          </el-text>
          <el-divider style="margin: 15px 0">
            <template #default>
              <el-text line-clamp="1"
                       style="max-width: 150px"
                       truncated
                       type="info"
                       size="small">
                <el-icon>
                  <Message/>
                </el-icon>
                {{ currentLoginUser.email }}
              </el-text>
            </template>
          </el-divider>
          <!-- 卡片操作按钮 -->
          <el-dropdown style="width: 100%" trigger="click">
            <el-button class="card-action-btn" text :icon="Switch" @click="switchPanel">切换面板</el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="currentPageType = 'platform'">{{ titleData.platform.name }}</el-dropdown-item>
                <el-dropdown-item @click="currentPageType = 'merchant'">{{ titleData.merchant.name }}</el-dropdown-item>
                <el-dropdown-item @click="currentPageType = 'user'">{{ titleData.user.name }}</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-button class="card-action-btn" text :icon="User">个人中心</el-button>
          <el-button class="card-action-btn" text :icon="SwitchButton" @click="logout">退出登录</el-button>
        </div>
      </div>
      <div>当前面板：{{ titleData[currentPageType].name }}</div>
      <div class="current-time">
        {{ formattedDateTime }}
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount} from "vue";
import {useRouter} from "vue-router";
import request from "@/utils/request.js";
import {Promotion, Shop, Flag, Message, User, SwitchButton, Switch} from '@element-plus/icons-vue'
import AsyncAvatar from "@/components/common/AsyncAvatar.vue";
import {ElMessage} from "element-plus";

// 图标映射
const iconMap = {
  Promotion, Shop, Flag
}

// 标题数据
const titleData = {
  platform: {
    title: '连接车主与商家，用数据驱动更优服务',
    icon: 'Promotion',
    name: '平台端'
  },
  merchant: {
    title: '用心服务每一位车主，让维修更高效',
    icon: 'Shop',
    name: '商户端'
  },
  user: {
    title: '让爱车焕然一新，安心出行每一天',
    icon: 'Flag',
    name: '客户端'
  }
}

// 当前页面
const currentPageType = ref('user')

// 当前登录用户展示
const currentLoginUser = ref({})
const currentLoginUserAvatar = ref('')
// 获取当前登录用户
const loadCurrentLoginUser = async () => {
  try {
    const res = await request.get('/user/current')
    currentLoginUser.value = res.data
    currentLoginUserAvatar.value = res.data.avatarUrl
  } catch (err) {
    ElMessage.error(`获取当前登录用户：${err.msg}`)
    if (err.code === 401) await router.push('/login')
    ElMessage.warning(`请重新登录！`)
  }
}

// 格式化日期时间
const currentDateTime = ref(new Date());
let timer = null;
const formattedDateTime = ref('');
const updateDateTime = () => {
  currentDateTime.value = new Date();
  const year = currentDateTime.value.getFullYear();
  const month = currentDateTime.value.getMonth() + 1;
  const day = currentDateTime.value.getDate();
  const hours = String(currentDateTime.value.getHours()).padStart(2, '0');
  const minutes = String(currentDateTime.value.getMinutes()).padStart(2, '0');
  const seconds = String(currentDateTime.value.getSeconds()).padStart(2, '0');

  formattedDateTime.value = `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`;
};
// 加载时获取信息
onMounted(async () => {
  // 实时显示时间
  updateDateTime();
  timer = setInterval(updateDateTime, 1000);
  await loadCurrentLoginUser()
})

// 组件卸载清空定时器
onBeforeUnmount(() => {
  if (timer) clearInterval(timer);
});

// 鼠标悬浮在头像或者头像卡片上
const isHover = ref(false)
let iconHover = false;
let cardHover = false;
const avatarIconEnter = () => {
  iconHover = true
  isHover.value = true
}
const avatarIconLeave = () => {
  iconHover = false
  if (!cardHover) {
    isHover.value = false
  }
}
const avatarCardEnter = () => {
  cardHover = true
  isHover.value = true
}
const avatarCardLeave = () => {
  cardHover = false
  if (!iconHover) {
    isHover.value = false
  }
}

// 退出登录
const router = useRouter()
const logout = () => {
  router.push('/logout')
}
// 切换面板
const switchPanel = () => {
  //...
}
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

.async-avatar-div {
  border: 2px solid #dedede;
}

.avatar-icon {
  position: relative;
  transition: transform 0.3s ease;
  transform-origin: center;
  z-index: 1001;
}

.avatar-icon-hover {
  transform: scale(2) translateY(15px) translateX(-10px);
}

.avatar-pop-wrapper {
  position: relative;
}

.avatar-hover-card {
  position: absolute;
  top: 65px;
  left: -123px;
  z-index: 1000;
  width: 250px;
  background: #fff;
  text-align: center;
  line-height: 1.5;
  border: 4px solid #dedede;
  padding: 40px 10px 10px 10px;
  border-radius: 10px;
  box-shadow: 5px 5px 10px rgba(0, 0, 0, .05);
  opacity: 1;
  transition: opacity 0.3s ease, top 0.3s ease;
}

.avatar-hover-card-hidden {
  opacity: 0;
  top: 50px;
  pointer-events: none;
}

.card-action-btn {
  width: 100%;
  border: 1px solid #e7e7e7;
  color: #707070;
  margin: 5px 0 0 0;
}

.current-time {
  color: #8c8c8c;
  font-weight: bold;
}
</style>