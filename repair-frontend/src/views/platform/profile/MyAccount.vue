<template>
  <div>
    <page-title>我的账号</page-title>
    <div class="form-wrapper">
      <el-form :model="infoData" label-width="100px"
               :rules="rules" ref="infoFormRef">
        <el-form-item label="账户名">
          <p>{{ infoData.loginAct }}</p>
        </el-form-item>
        <el-form-item label="头像" prop="avatarUrl">
          <el-tooltip
              placement="right"
              content="点击上传新头像"
              effect="customized"
          >
            <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                :before-upload="beforeAvatarUpload"
                :http-request="uploadUserAvatar"
            >
              <img v-if="infoData.avatarUrl" :src="currentAvatarUrl" class="avatar"
                   alt="头像"/>
              <el-icon v-else class="avatar-uploader-icon">
                <Plus/>
              </el-icon>
            </el-upload>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname" required>
          <el-input v-model="infoData.nickname" placeholder="请输入你的用户昵称">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="手机号码" prop="phone" required>
          <el-input v-model="infoData.phone" placeholder="请输入手机号">
            <template #prefix>
              <el-icon>
                <Iphone/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="电子邮件" prop="email" required>
          <el-input v-model="infoData.email" placeholder="请输入邮箱地址">
            <template #prefix>
              <el-icon>
                <Message/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <!-- 表单按钮 -->
          <el-button type="primary" @click="onSubmit" :loading="isSubmitting">保存</el-button>
          <el-button type="warning" @click="onShowDialog">修改密码</el-button>
        </el-form-item>
      </el-form>
    </div>
    <base-dialog-data v-model="showDialog">
      <template #title>
        <el-icon style="margin-top: 2px">
          <Key/>
        </el-icon>
        修改密码
      </template>
      <template #context>
        <el-form :model="changePwdData" label-width="120px"
                 :rules="dialogFormRules" ref="dialogFormRef" @keydown.enter="onChangePwd"
        >
          <el-form-item label="请输入原密码" required prop="oldPwd">
            <el-input show-password type="password" v-model="changePwdData.oldPwd"></el-input>
          </el-form-item>
          <el-form-item label="请输入新密码" required prop="loginPwd">
            <el-input show-password type="password" v-model="changePwdData.loginPwd"></el-input>
          </el-form-item>
          <el-form-item label="请再次输入" required prop="confirmNewPwd">
            <el-input show-password type="password" v-model="changePwdData.confirmNewPwd"></el-input>
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <el-button type="primary" :loading="isChanging" @click="onChangePwd">确认修改</el-button>
      </template>
    </base-dialog-data>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from "vue";
import PageTitle from "@/components/platform/PageTitle.vue";
import {Iphone, Key, Message, Plus, User} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import debounce from "@/utils/debounce.js";
import {useCurrentUserStore} from "@/stores/currentUser.js";
import BaseDialogData from "@/components/platform/BaseDialogData.vue";
import {useRouter} from "vue-router";

const router = useRouter()

const currentUser = useCurrentUserStore()

const infoData = reactive({
  id: '',
  loginAct: '',
  loginPwd: '',
  nickname: '',
  phone: '',
  email: '',
  avatarUrl: '',
})

const changePwdData = reactive({
  id: '',
  oldPwd: '',
  loginPwd: '',
  confirmNewPwd: ''
})

const infoFormRef = ref(null)
const dialogFormRef = ref(null)

// 获取头像预签名链接
const currentAvatarUrl = ref('')
const loadCurrentAvatarUrl = async (fileName) => {
  try {
    const res = await request.get(`/file/get-avatars-url/${fileName}`)
    currentAvatarUrl.value = res.data
  } catch (err) {
    infoData.avatarUrl = ''
  }
}
// 校验头像
const beforeAvatarUpload = (rawFile) => {
  const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/jpg']
  const isImage = allowedTypes.includes(rawFile.type)
  const isLt2M = rawFile.size < 2 * 1024 * 1024
  if (!isImage) {
    ElMessage.error('头像格式必须是JPG/JPEG/PNG/GIF!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过2MB!')
    return false
  }
  return true
}
// 上传头像文件
const uploadUserAvatar = async (options) => {
  const {file} = options
  const formData = new FormData()
  formData.append('file', file)
  try {
    const res = await request.post('/file/upload-avatars', formData)
    currentAvatarUrl.value = res.data
    const url = new URL(res.data).pathname.split('/')
    infoData.avatarUrl = url[url.length - 1]
  } catch (err) {
    ElMessage.error(`上传头像：${err.msg}`)
  }
}
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
  nickname: [
    {required: true, message: '请输入昵称', trigger: 'blur'},
    {max: 10, message: '昵称长度不能超过10字符', trigger: 'blur'}
  ],
  phone: [
    {validator: validatePhone, trigger: 'blur'}
  ],
  email: [
    {validator: validateEmail, trigger: 'blur'}
  ]
}
const dialogFormRules = {
  oldPwd: [{required: true, message: '请输入密码', trigger: 'blur'}],
  loginPwd: [{required: true, message: '请输入密码', trigger: 'blur'}],
  confirmNewPwd: [
    {validator: validateConfirmPwd(changePwdData), trigger: 'blur'}
  ],
}

// 加载状态
const isSubmitting = ref(false)
// 提交修改
const onSubmit = () => {
  infoFormRef.value.validate((valid) => {
    if (valid) {
      debounceSubmit()
    } else {
      ElMessage.warning('请填写完整信息')
    }
  })
}
const submit = async () => {
  isSubmitting.value = true
  try {
    const response = await request.put('/profile/update', infoData)
    ElMessage.success(response.msg || '更新用户信息成功')
    await currentUser.forceRefresh()
  } catch (error) {
    ElMessage.error(`更新用户信息：${error.msg}`)
  } finally {
    isSubmitting.value = false
  }
}
const debounceSubmit = debounce(submit, 200)

// 加载用户信息
const loadUserInfo = async () => {
  try {
    await currentUser.refresh()
    infoData.id = currentUser.data.id
    infoData.loginAct = currentUser.data.loginAct
    infoData.nickname = currentUser.data.nickname
    infoData.phone = currentUser.data.phone
    infoData.email = currentUser.data.email
    infoData.avatarUrl = currentUser.data.avatarUrl
    await loadCurrentAvatarUrl(infoData.avatarUrl)
  } catch (err) {
    ElMessage.error(`加载用户信息：${err.msg || 'Error'}`)
  }
}

// 修改密码
const showDialog = ref(false)
const isChanging = ref(false)
const onShowDialog = () => {
  changePwdData.oldPwd = ''
  changePwdData.loginPwd = ''
  changePwdData.confirmNewPwd = ''
  showDialog.value = true
}
const onChangePwd = () => {
  dialogFormRef.value.validate(async (valid) => {
    if (valid) {
      isChanging.value = true
      try {
        changePwdData.id = currentUser.data.id
        const res = await request.put('/profile/edit-pwd', changePwdData)
        ElMessage.success(`修改用户密码：${res.msg || 'Success'}`)
        await router.push('/login')
      } catch (err) {
        ElMessage.error(`修改用户密码：${err.msg || 'Error'}`)
      } finally {
        isChanging.value = false
        showDialog.value = false
      }
    } else {
      ElMessage.warning('请填写完整信息')
    }
  })
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 128px;
  height: 128px;
  display: block;
}

.avatar-uploader-icon {
  font-size: 24px;
  color: #8c939d;
  width: 128px;
  height: 128px;
  text-align: center;
}

.form-wrapper {
  width: 500px;
  margin: 50px auto auto;
  background: #fff;
  border: 1px solid #dadada;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 5px 5px 10px rgba(0, 0, 0, .02);
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-popper.is-customized {
  padding: 6px 12px;
  background: #efefef;
}

.el-popper.is-customized .el-popper__arrow::before {
  background: #efefef;
  right: 0;
}
</style>