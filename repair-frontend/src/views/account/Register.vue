<template>
  <div class="register-container">
    <el-card class="register-card">
      <!-- Header部分，显示logo -->
      <template #header>
        <div class="header-container">
          <img src="@/assets/image/banner-color-small.png" alt="Logo" class="logo"/>
        </div>
      </template>

      <h2>1 分钟快速注册，解锁专属养车福利</h2>
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef"
               label-width="100px" @keydown.enter="onSubmit">
        <!-- 登录名 -->
        <el-form-item label="用户名" prop="loginAct" required>
          <el-input v-model="registerForm.loginAct" placeholder="请输入登录账号" ref="loginActInputRef">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item label="密码" prop="loginPwd" required>
          <el-input v-model="registerForm.loginPwd" type="password" placeholder="请输入密码" show-password>
            <template #prefix>
              <el-icon>
                <Key/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 确认密码 -->
        <el-form-item label="确认密码" prop="confirmPwd" required>
          <el-input v-model="registerForm.confirmPwd" type="password" placeholder="请再次输入密码" show-password>
            <template #prefix>
              <el-icon>
                <Key/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 昵称 -->
        <el-form-item label="用户昵称" prop="nickname" required>
          <el-input v-model="registerForm.nickname" placeholder="请输入你的用户昵称">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 手机号 -->
        <el-form-item label="手机号" prop="phone" required>
          <el-input v-model="registerForm.phone" placeholder="请输入手机号">
            <template #prefix>
              <el-icon>
                <Iphone/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 电子邮件 -->
        <el-form-item label="电子邮件" prop="email" required>
          <el-input v-model="registerForm.email" placeholder="请输入邮箱地址">
            <template #prefix>
              <el-icon>
                <Message/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 验证码 -->
        <el-form-item label="验证码" prop="captcha">
          <div class="captcha-container">
            <el-input v-model="registerForm.captcha" placeholder="请输入验证码" ref="captchaInputRef">
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
          <el-button class="button" type="success" @click="onSubmit" :loading="isSubmitting">立即注册</el-button>
          <el-button class="button" type="primary" @click="router.push('/login')" plain>返回登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
  <background-image image="login"/>
</template>

<script setup>
import {ref, reactive, onMounted} from "vue";
import BackgroundImage from "@/components/common/BackgroundImage.vue";
import {Iphone, Key, Message, Paperclip, Plus, Refresh, User} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
import request from "@/utils/request.js";
import debounce from "@/utils/debounce.js";

const router = useRouter()

// 注册表单引用
const registerFormRef = ref(null)
// 用户名输入框引用
const loginActInputRef = ref(null)
// 验证码输入框引用
const captchaInputRef = ref(null)
// 注册表单数据
const registerForm = reactive({
  loginAct: '',
  loginPwd: '',
  confirmPwd: '',
  nickname: '',
  phone: '',
  email: '',
  avatarUrl: '',
  uuid: '',
  captcha: ''
})

// 手机号正则验证
const validatePhone = (rule, value, callback) => {
  if (!value) {
    return callback(new Error('请输入手机号'))
  }
  const reg = /^1[3-9]\d{9}$/
  if (!reg.test(value)) {
    return callback(new Error('手机号格式错误'))
  }
  callback()
}
// 邮箱正则验证
const validateEmail = (rule, value, callback) => {
  if (!value) {
    return callback(new Error('请输入邮箱地址'))
  }
  const reg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
  if (!reg.test(value)) {
    return callback(new Error('电子邮件格式错误'))
  }
  callback()
}
// 确认密码验证
const validateConfirmPwd = (formData) => (rule, value, callback) => {
  if (!value) {
    return callback(new Error('请再次输入密码'))
  }
  if (value !== formData.loginPwd) {
    return callback(new Error('两次输入密码不一致'))
  }
  callback()
}
// 表单校验规则
const rules = {
  loginAct: [{required: true, message: '请输入登录账号', trigger: 'blur'}],
  loginPwd: [{required: true, message: '请输入密码', trigger: 'blur'}],
  confirmPwd: [
    {validator: validateConfirmPwd(registerForm), trigger: 'blur'}
  ],
  nickname: [
    {required: true, message: '请输入昵称', trigger: 'blur'},
    {max: 10, message: '昵称长度不能超过10字符', trigger: 'blur'}
  ],
  phone: [
    {validator: validatePhone, trigger: 'blur'}
  ],
  email: [
    {validator: validateEmail, trigger: 'blur'}
  ],
  captcha: [{required: true, message: '请输入验证码', trigger: 'blur'}]
}
// 加载状态
const isSubmitting = ref(false)
const isLoading = ref(true)
// 验证码图片地址
const captchaSrc = ref('')
// 重新请求验证码
const refreshCaptcha = () => {
  registerForm.captcha = ''
  captchaSrc.value = ''
  isLoading.value = true
  debounceRefreshCaptcha()
}
const loadCaptcha = async () => {
  try {
    const response = await request.get('/captcha')
    captchaSrc.value = response.data.captcha
    registerForm.uuid = response.data.uuid
  } catch (error) {
    ElMessage.error('验证码加载失败，请重试')
  } finally {
    isLoading.value = false
    if (registerForm.loginAct !== '') {
      captchaInputRef.value.focus()
    }
  }
}
const debounceRefreshCaptcha = debounce(loadCaptcha, 250)
// 提交注册
const onSubmit = () => {
  registerFormRef.value.validate(async (valid) => {
    if (valid) {
      isSubmitting.value = true
      try {
        const response = await request.post('/register', registerForm)
        ElMessage.success('注册成功！请登录')
        await router.push('/login')
      } catch (error) {
        ElMessage.error(error.msg || '登录失败，请重试')
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
  loginActInputRef.value.focus()
})
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.register-card {
  width: 500px;
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