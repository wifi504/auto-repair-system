<template>
  <div class="login-container">
    <el-card class="login-card">
      <!-- Header部分，显示logo -->
      <template #header>
        <div class="header-container">
          <img src="../../assets/image/banner-color-small.png" alt="Logo" class="logo"/>
        </div>
      </template>

      <h2>欢迎登录汽车维修管理系统</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef"
               label-width="80px" @keydown.enter="onSubmit">
        <!-- 用户名 -->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" ref="usernameInputRef">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password>
            <template #prefix>
              <el-icon>
                <Key/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 验证码 -->
        <el-form-item label="验证码" prop="captcha">
          <div class="captcha-container">
            <el-input v-model="loginForm.captcha" placeholder="请输入验证码" ref="captchaInputRef">
              <template #prefix>
                <el-icon>
                  <Paperclip/>
                </el-icon>
              </template>
            </el-input>
            <el-image
                :src="captchaSrc"
                alt="验证码"
                class="captcha-img"
                @click="refreshCaptcha"
                fit="contain"
            >
              <template #error>
                <el-button class="error-holder" :loading="isLoading" bg text>
                  <div v-show="!isLoading">
                    <el-icon>
                      <Refresh/>
                    </el-icon>
                    重试
                  </div>
                </el-button>
              </template>
            </el-image>
          </div>
        </el-form-item>

        <el-form-item>
          <!-- 表单按钮 -->
          <el-button class="button" type="primary" @click="onSubmit" :loading="isSubmitting">登 录</el-button>
          <el-button class="button" type="warning" @click="" plain>立即注册</el-button>
        </el-form-item>

        <el-form-item>
          <el-button @click="" link>遇到问题无法登录？</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
  <background-image image="login"/>
</template>

<script setup>
import {reactive, ref, onMounted} from 'vue'
import {useRouter} from "vue-router";
import {ElMessage} from 'element-plus'
import {Key, Paperclip, Refresh, User} from "@element-plus/icons-vue";
import BackgroundImage from "@/components/common/BackgroundImage.vue";
import request from "@/utils/request.js";
import debounce from "@/utils/debounce.js";
import {jwtDecode} from "jwt-decode";

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
// 用户名输入框引用
const usernameInputRef = ref(null)
// 验证码输入框引用
const captchaInputRef = ref(null)

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
const loadCaptcha = async () => {
  try {
    const response = await request.get('captcha')
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
    if (loginForm.username !== '') {
      loginForm.captcha = ''
      captchaInputRef.value.focus()
    }
  }
}

const debounceRefreshCaptcha = debounce(loadCaptcha, 250)

const refreshCaptcha = () => {
  captchaSrc.value = ''
  isLoading.value = true
  debounceRefreshCaptcha()
}

// 提交登录
const onSubmit = () => {
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      isSubmitting.value = true
      try {
        const response = await request.post('login', loginForm)
        ElMessage.success('登录成功！欢迎您，'
            + jwtDecode(response.data).nickname + '！')
        localStorage.setItem('token', response.data)
        let back = router.options.history.state.back
        await router.push(back || '/platform')
      } catch (error) {
        console.log(error)
        ElMessage.error(error.msg || '登录失败，请重试')
        loginForm.captcha = ''
        if (!error.msg.includes('验证码')) {
          loginForm.username = ''
          loginForm.password = ''
          loginForm.uuid = ''
          usernameInputRef.value.focus()
        }
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
  usernameInputRef.value.focus()
})
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.login-card {
  width: 480px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
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
  height: 32px;
  cursor: pointer;
}

.error-holder {
  width: 100%;
  height: 100%;
}

.button {
  width: calc(50% - 6px);
}
</style>
