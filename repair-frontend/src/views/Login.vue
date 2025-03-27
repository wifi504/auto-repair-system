<template>
  <div class="login-container">
    <el-card class="login-card">
      <!-- Header部分，显示logo -->
      <template #header>
        <div class="header-container">
          <img src="@/assets/image/banner-color-small.png" alt="Logo" class="logo"/>
        </div>
      </template>

      <h2>欢迎登录汽车维修管理系统</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <!-- 用户名 -->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"/>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"/>
        </el-form-item>

        <!-- 验证码 -->
        <el-form-item label="验证码" prop="captcha">
          <div class="captcha-container">
            <el-input v-model="loginForm.captcha" placeholder="请输入验证码"/>
            <el-image
                :src="captchaSrc"
                alt="验证码"
                class="captcha-img"
                @click="refreshCaptcha"
                fit="contain"
            >
              <template #placeholder>
                <div class="placeholder">加载中...</div>
              </template>
            </el-image>
          </div>
        </el-form-item>

        <!-- 登录按钮 -->
        <el-form-item>
          <el-button type="primary" @click="onSubmit" :loading="isSubmitting">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import {reactive, ref, onMounted} from 'vue'
import {ElMessage} from 'element-plus'
import request from "@/utils/request";
import {useRouter} from "vue-router";

const router = useRouter();

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  uuid: '',
  captcha: ''
})

// 表单引用
const loginFormRef = ref(null)

// 加载状态
const isSubmitting = ref(false)
const isLoading = ref(true)

// 验证码图片地址
const captchaSrc = ref('')

// 表单校验规则
const rules = {
  username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
  password: [{required: true, message: '请输入密码', trigger: 'blur'}],
  captcha: [{required: true, message: '请输入验证码', trigger: 'blur'}]
}

// 加载验证码
const refreshCaptcha = async () => {
  try {
    isLoading.value = true
    const response = await request.get('/captcha')

    // 判断响应成功
    if (response.code === 200) {
      captchaSrc.value = response.data.captcha
      loginForm.uuid = response.data.uuid
    } else {
      ElMessage.error('验证码加载失败，请重试')
    }
  } catch (error) {
    ElMessage.error('验证码加载失败，请重试')
  } finally {
    isLoading.value = false
  }
}

// 提交登录
const onSubmit = () => {
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      isSubmitting.value = true
      try {
        const response = await request.post('/login', loginForm)
        ElMessage.success('登录成功！')
        localStorage.setItem('token', response.data)
        await router.push('/')
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '登录失败，请重试')
        refreshCaptcha()
      } finally {
        isSubmitting.value = false
      }
    } else {
      ElMessage.warning('请填写完整信息')
    }
  })
}

onMounted(() => {
  refreshCaptcha()
})
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.login-card {
  width: 480px; /* 调整宽度 */
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.header-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80px;
}

.logo {
  max-height: 100%;
  object-fit: contain;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.captcha-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.captcha-img {
  width: 120px;
  height: 40px;
  cursor: pointer;
}

.placeholder {
  width: 120px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #e5e7eb;
  color: #888;
}
</style>
