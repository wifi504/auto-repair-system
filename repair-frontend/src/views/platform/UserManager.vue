<template>
  <div>
    <page-title>
      用户管理
    </page-title>
    <div>
      <el-button icon="Refresh" @click="refreshTableData"/>
      <el-button type="primary" @click="newUser">新增</el-button>
      <el-button type="danger" @click="deleteByList" :disabled="!selectedData.length">批量删除</el-button>
      <el-button type="danger" @click="banList" plain :disabled="!selectedData.length">批量封禁</el-button>
      <el-button type="success" @click="unbanList" plain :disabled="!selectedData.length">批量解封</el-button>
      <el-button type="warning" @click="resetPwd" plain :disabled="!selectedData.length">重设密码</el-button>
    </div>
    <base-table
        :data="tableData"
        :columns="columns"
        :loading="loading"
        :actions="actions"
        :pagination="pagination"
        selectable
        @pageChange="handlePageChange"
        @pageSizeChange="handlePageSizeChange"
        @selectionChange="handleSelectionChange"
    >
      <!-- 使用插槽自定义列 -->
      <template #avatarUrl="{ row }">
        <async-avatar :avatar-name="row.avatarUrl"/>
      </template>
      <template #status="{ row }">
        <el-tag :type="row.status === 1 ? 'success' : 'danger'">
          {{ row.status === 1 ? '正常' : '已封禁' }}
        </el-tag>
      </template>
    </base-table>
    <base-dialog-data v-model="showDialog">
      <template #title>
        <el-icon style="margin-top: 2px">
          <UserFilled/>
        </el-icon>
        {{ dialogTitle }}
      </template>
      <template #context>
        <div v-if="dialogTitle === '确认批量删除'">
          您将要删除{{ selectedData.length }}个用户，此操作不可撤销！
        </div>
        <div v-else-if="dialogTitle === '确认批量封禁'">
          您将要封禁{{ selectedData.length }}个用户
        </div>
        <div v-else-if="dialogTitle === '确认批量解封'">
          您将要解封{{ selectedData.length }}个用户
        </div>
        <div v-else-if="dialogTitle === '确认重设密码'">
          您将要把{{ selectedData.length }}个用户密码全部重置为：“pwd123456”
        </div>
        <el-form
            :model="dialogEditData"
            ref="dialogFormRef"
            :rules="rules"
            :disabled="dialogTitle === '删除用户'"
            label-width="130px"
            v-else
        >
          <el-form-item v-if="dialogTitle !== '创建用户'" label="ID" prop="id">
            <div>{{ dialogEditData.id }}</div>
          </el-form-item>
          <el-form-item v-if="dialogTitle !== '创建用户'" label="登录名" prop="loginAct">
            <div>{{ dialogEditData.loginAct }}</div>
          </el-form-item>
          <el-form-item v-if="dialogTitle !== '创建用户'" label="创建时间" prop="createTime">
            <div>{{ dialogEditData.createTime }}</div>
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
                <img v-if="dialogEditData.avatarUrl" :src="currentAvatarUrl" class="avatar"
                     alt="头像"/>
                <el-icon v-else class="avatar-uploader-icon">
                  <Plus/>
                </el-icon>
              </el-upload>
            </el-tooltip>
          </el-form-item>
          <el-form-item v-if="dialogTitle === '创建用户'" label="登录名" prop="loginAct">
            <el-input v-model="dialogEditData.loginAct"></el-input>
          </el-form-item>
          <el-form-item label="用户昵称" prop="nickname">
            <el-input v-model="dialogEditData.nickname"></el-input>
          </el-form-item>
          <el-form-item label="手机号码" prop="phone">
            <el-input v-model="dialogEditData.phone"></el-input>
          </el-form-item>
          <el-form-item label="电子邮件" prop="email">
            <el-input v-model="dialogEditData.email"></el-input>
          </el-form-item>
          <el-divider/>
          <el-form-item label="用户角色">
            <el-select
                v-model="rolesSelectedData"
                multiple
                placeholder="角色为空，请选择"
            >
              <el-option
                  v-for="role in rolesList"
                  :key="role.id"
                  :label="role.name"
                  :value="role.id"
              />
            </el-select>
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <el-button v-show="!isDialogLoading" @click="cancel" type="primary" plain>取消</el-button>
        <el-button :loading="isDialogLoading" v-if="dialogTitle === '编辑用户'" @click="submit" type="primary">
          提交更改
        </el-button>
        <el-button :loading="isDialogLoading" v-if="dialogTitle === '确认批量封禁'" @click="doBanList" type="danger">
          确认全部封禁
        </el-button>
        <el-button :loading="isDialogLoading" v-if="dialogTitle === '确认批量解封'" @click="doUnbanList" type="success">
          确认全部解封
        </el-button>
        <el-button :loading="isDialogLoading" v-if="dialogTitle === '删除用户'" @click="delOnce" type="danger">
          确认删除
        </el-button>
        <el-button :loading="isDialogLoading" v-if="dialogTitle === '确认批量删除'" @click="doDelList" type="danger">
          确认全部删除
        </el-button>
        <el-button :loading="isDialogLoading" v-if="dialogTitle === '创建用户'" @click="createOnce" type="primary">
          立即创建
        </el-button>
        <el-button :loading="isDialogLoading" v-if="dialogTitle === '确认重设密码'" @click="doResetPwd" type="primary">
          立即重置
        </el-button>
      </template>
    </base-dialog-data>
  </div>

</template>

<script setup>
import {onActivated, ref, watch} from 'vue'
import {ElMessage} from "element-plus";
import {Plus, UserFilled} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import debounce from "@/utils/debounce.js";
import PageTitle from "@/components/platform/PageTitle.vue";
import BaseTable from "@/components/platform/BaseTable.vue";
import BaseDialogData from "@/components/platform/BaseDialogData.vue";
import AsyncAvatar from "@/components/common/AsyncAvatar.vue";

// 显示对话框编辑
const showDialog = ref(false)
// 对话框标题
const dialogTitle = ref("")
// 对话框编辑数据
const dialogEditData = ref({})
// 对话框事件按钮加载
const isDialogLoading = ref(false)
// 表单引用
const dialogFormRef = ref()
// 表单校验规则
const rules = {
  nickname: [{required: true, message: '昵称不能为空', trigger: 'blur'}],
  loginAct: [{required: true, message: '登录名不能为空', trigger: 'blur'}]
}
// 校验提交
const submit = async () => {
  if (dialogFormRef.value) {
    await dialogFormRef.value.validate(async (valid) => {
      if (valid) {
        try {
          isDialogLoading.value = true
          const res1 = await request.put('user/edit', dialogEditData.value)
          const res2 = await request.put('/user/edit-roles', {
            userId: dialogEditData.value.id,
            idList: rolesSelectedData.value
          })
          const msg = res1.msg + "；" + res2.msg
          ElMessage.success(`提交成功：${msg || 'success'}`)
          refreshTableData()
        } catch (e) {
          ElMessage.error(`提交失败：${e.msg || e.message || 'error'}`)
        } finally {
          showDialog.value = false
          isDialogLoading.value = false
        }
      }
    })
  }
}
// 取消编辑
const cancel = () => {
  dialogEditData.value = {}
  showDialog.value = false
}
// 删除一条数据
const delOnce = async () => {
  try {
    isDialogLoading.value = true
    const response = await request.delete('user/remove', dialogEditData.value)
    ElMessage.success(`删除成功：${response.msg || 'success'}`)
    refreshTableData()
  } catch (e) {
    ElMessage.error(`提交失败：${e.msg || e.message || 'error'}`)
  } finally {
    showDialog.value = false
    isDialogLoading.value = false
  }
}
// 批量删除
const doDelList = async () => {
  try {
    isDialogLoading.value = true
    const response = await request.delete('user/remove-all', selectedData.value)
    ElMessage.success(`删除成功：${response.msg || 'success'}`)
    refreshTableData()
  } catch (e) {
    ElMessage.error(`提交失败：${e.msg || e.message || 'error'}`)
  } finally {
    showDialog.value = false
    isDialogLoading.value = false
  }
}
// 插入一条数据
const createOnce = async () => {
  if (dialogFormRef.value) {
    await dialogFormRef.value.validate(async (valid) => {
      if (valid) {
        try {
          isDialogLoading.value = true
          const res1 = await request.post('user/create', dialogEditData.value)
          const res2 = await request.put('/user/edit-roles', {
            userId: dialogEditData.value.id,
            idList: rolesSelectedData.value
          })
          const msg = res1.msg + "；" + res2.msg
          ElMessage.success(`创建成功：${msg || 'success'}`)
          refreshTableData()
        } catch (e) {
          ElMessage.error(`创建失败：${e.msg || e.message || 'error'}`)
        } finally {
          showDialog.value = false
          isDialogLoading.value = false
        }
      }
    })
  }
}
// 执行批量重设密码
const doResetPwd = async () => {
  try {
    isDialogLoading.value = true
    const response = await request.post('user/reset-pwd-list', selectedData.value)
    ElMessage.success(`重置成功：${response.msg || 'success'}`)
    refreshTableData()
  } catch (e) {
    ElMessage.error(`重置失败：${e.msg || e.message || 'error'}`)
  } finally {
    showDialog.value = false
    isDialogLoading.value = false
  }
}
// 切换封禁状态
const changeBan = async (row) => {
  try {
    let response;
    if (row.status === 1) {
      response = await request.get(`user/ban/${row.id}`);
    } else {
      response = await request.get(`user/unban/${row.id}`);
    }
    ElMessage.success(`操作成功：${response.msg || 'success'}`)
    refreshTableData()
  } catch (e) {
    ElMessage.error(`操作失败：${e.msg || e.message || 'error'}`)
  }
}
// 批量封禁用户
const doBanList = async () => {
  try {
    isDialogLoading.value = true
    const response = await request.post('user/ban-list', selectedData.value)
    ElMessage.success(`封禁成功：${response.msg || 'success'}`)
    refreshTableData()
  } catch (e) {
    ElMessage.error(`封禁失败：${e.msg || e.message || 'error'}`)
  } finally {
    showDialog.value = false
    isDialogLoading.value = false
  }
}
// 批量解封用户
const doUnbanList = async () => {
  try {
    isDialogLoading.value = true
    const response = await request.post('user/unban-list', selectedData.value)
    ElMessage.success(`解封成功：${response.msg || 'success'}`)
    refreshTableData()
  } catch (e) {
    ElMessage.error(`解封失败：${e.msg || e.message || 'error'}`)
  } finally {
    showDialog.value = false
    isDialogLoading.value = false
  }
}
// 创建用户
const newUser = () => {
  dialogEditData.value = {}
  rolesSelectedData.value = []
  dialogTitle.value = '创建用户'
  showDialog.value = true
}
// 批量删除
const deleteByList = () => {
  dialogEditData.value = {}
  dialogTitle.value = '确认批量删除'
  showDialog.value = true
}
// 批量封禁
const banList = () => {
  dialogEditData.value = {}
  dialogTitle.value = '确认批量封禁'
  showDialog.value = true
}
// 批量解封
const unbanList = () => {
  dialogEditData.value = {}
  dialogTitle.value = '确认批量解封'
  showDialog.value = true
}
// 重设密码
const resetPwd = () => {
  dialogEditData.value = {}
  dialogTitle.value = '确认重设密码'
  showDialog.value = true
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
    dialogEditData.value.avatarUrl = url[url.length - 1]
  } catch (err) {
    ElMessage.error(err.msg || '上传头像请求失败')
  }
}
// 获取头像预签名链接
const currentAvatarUrl = ref('')
const loadCurrentAvatarUrl = async (fileName) => {
  try {
    const res = await request.get(`/file/get-avatars-url/${fileName}`)
    currentAvatarUrl.value = res.data
  } catch (err) {
    dialogEditData.value.avatarUrl = ''
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
// 选择的用户角色
const rolesSelectedData = ref([])
// 角色表
const rolesList = ref([])
// 查询用户对应角色
const loadUserRoles = async (id) => {
  rolesSelectedData.value = []
  try {
    const res = await request.get(`/user/list-roles/${id}`)
    rolesSelectedData.value = res.data.map(d => d.id)
  } catch (err) {
    ElMessage.error(err.msg || '用户角色信息查询失败')
  }
}

// 每当对话框打开执行回调
watch(showDialog, newValue => {
  // 如果不是关联行的对话框，啥也不干
  const skip = ['创建用户', '确认批量删除', '确认批量封禁', '确认批量解封', '确认重设密码']
  if (skip.includes(dialogTitle.value)) return
  if (newValue) {
    // 加载头像
    loadCurrentAvatarUrl(dialogEditData.value.avatarUrl)
    // 查询对应用户的角色
    loadUserRoles(dialogEditData.value.id)
  } else {
    currentAvatarUrl.value = ''
  }
})

const loading = ref(false)
// 表格数据
const tableData = ref([])
// 选中的数据
const selectedData = ref([])
// 表头配置
const columns = [
  {prop: 'id', label: 'ID', width: '50px'},
  {prop: 'avatarUrl', label: '头像', width: '85px'},
  {prop: 'nickname', label: '用户昵称'},
  {prop: 'loginAct', label: '登录名'},
  {prop: 'phone', label: '手机号码'},
  {prop: 'email', label: '电子邮件'},
  {prop: 'status', label: '账号状态', width: '90px'},
  {prop: 'createTime', label: '创建时间'}
]
// 操作按钮
const actions = [
  {
    label: '编辑',
    handler: (row) => {
      dialogEditData.value = {}
      Object.assign(dialogEditData.value, row)
      dialogTitle.value = '编辑用户'
      showDialog.value = true
    }
  },
  {
    label: '封禁/解封',
    type: 'warning',
    handler: (row) => changeBan(row)
  },
  {
    label: '删除',
    type: 'danger',
    handler: (row) => {
      dialogEditData.value = {}
      Object.assign(dialogEditData.value, row)
      dialogTitle.value = '删除用户'
      showDialog.value = true
    }
  },
]
// 分页
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 事件处理
const handlePageChange = (page) => {
  refreshTableData()
}
const handlePageSizeChange = (size) => {
  refreshTableData()
}
const handleSelectionChange = (selection) => {
  selectedData.value = selection
}

// 刷新表格数据
const loadTableData = async () => {
  try {
    // 请求用户表数据
    const response = await request.get('user/list', {
      pageNo: pagination.value.currentPage,
      pageSize: pagination.value.pageSize
    })
    tableData.value = response.data.list
    pagination.value.total = response.data.total
    // 请求角色表数据
    const roleTableData = await request.get('role/view')
    rolesList.value = roleTableData.data.list
  } catch (e) {
    tableData.value = []
    if (e.msg) {
      ElMessage.error(`请求失败：${e.msg}`)
    }
  } finally {
    loading.value = false
  }
}
const debounceLoadTableData = debounce(loadTableData, 250)
const refreshTableData = () => {
  loading.value = true
  debounceLoadTableData()
}
onActivated(() => refreshTableData())
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 64px;
  height: 64px;
  display: block;
}

.avatar-uploader-icon {
  font-size: 24px;
  color: #8c939d;
  width: 64px;
  height: 64px;
  text-align: center;
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