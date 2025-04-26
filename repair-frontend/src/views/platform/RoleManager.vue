<template>
  <div>
    <page-title>
      角色管理
    </page-title>
    <div>
      <el-button icon="Refresh" @click="refreshTableData"/>
      <el-button type="primary" @click="newRole">新增</el-button>
      <el-button type="danger" @click="deleteByList" :disabled="!selectedData.length">批量删除</el-button>
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
    />
    <base-dialog-data v-model="showDialog">
      <template #title>
        <el-icon style="margin-top: 2px">
          <UserFilled/>
        </el-icon>
        {{ dialogTitle }}
      </template>
      <template #context>
        <div v-if="dialogTitle === '确认批量删除'">
          您将要删除{{ selectedData.length }}个角色，此操作不可撤销！
        </div>
        <el-form
            :model="dialogEditData"
            ref="dialogFormRef"
            :rules="rules"
            :disabled="dialogTitle === '删除角色'"
            label-width="130px"
            v-else
        >
          <el-form-item label="角色名称" prop="name">
            <el-input v-model="dialogEditData.name"></el-input>
          </el-form-item>
          <el-form-item label="角色标识符" prop="code">
            <el-input v-model="dialogEditData.code"></el-input>
          </el-form-item>
          <el-form-item label="角色描述" prop="remark">
            <el-input
                :rows="3"
                type="textarea"
                v-model="dialogEditData.remark"></el-input>
          </el-form-item>
          <el-form-item label="角色排序索引" prop="orderNum">
            <el-input v-model="dialogEditData.orderNum"></el-input>
          </el-form-item>
          <el-divider/>
          <el-form-item label="角色权限">
            <el-tree-select
                multiple
                :render-after-expand="false"
                show-checkbox
            />
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <el-button @click="cancel" type="primary" plain>取消</el-button>
        <el-button v-if="dialogTitle === '编辑角色'" @click="submit" type="primary">提交更改</el-button>
        <el-button v-if="dialogTitle === '删除角色'" @click="delOnce" type="danger">确认删除</el-button>
        <el-button v-if="dialogTitle === '确认批量删除'" @click="doDelList" type="danger">确认全部删除</el-button>
        <el-button v-if="dialogTitle === '创建角色'" @click="createOnce" type="primary">立即创建</el-button>
      </template>
    </base-dialog-data>
  </div>
</template>

<script setup>
import {ref, onActivated} from 'vue'
import {ElMessage} from "element-plus";
import {UserFilled} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import debounce from "@/utils/debounce.js";
import PageTitle from "@/components/platform/PageTitle.vue";
import BaseTable from "@/components/platform/BaseTable.vue";
import BaseDialogData from "@/components/platform/BaseDialogData.vue";

// 显示对话框编辑
const showDialog = ref(false)
// 对话框标题
const dialogTitle = ref("")
// 对话框编辑数据
const dialogEditData = ref({})
// 表单引用
const dialogFormRef = ref()
// 表单校验规则
const rules = {
  name: [{required: true, message: '名称不能为空', trigger: 'blur'}],
  code: [{required: true, message: '标识符不能为空', trigger: 'blur'}],
  orderNum: [{required: true, message: '排序索引不能为空', trigger: 'blur'}]
}
// 校验提交
const submit = async () => {
  if (dialogFormRef.value) {
    await dialogFormRef.value.validate(async (valid) => {
      if (valid) {
        try {
          const response = await request.put('role/update', dialogEditData.value)
          ElMessage.success(`提交成功：${response.msg || 'success'}`)
          refreshTableData()
        } catch (e) {
          ElMessage.error(`提交失败：${e.msg || e.message || 'error'}`)
        } finally {
          showDialog.value = false
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
    const response = await request.delete('role/delete', [dialogEditData.value])
    ElMessage.success(`删除成功：${response.msg || 'success'}`)
    refreshTableData()
  } catch (e) {
    ElMessage.error(`提交失败：${e.msg || e.message || 'error'}`)
  } finally {
    showDialog.value = false
  }
}
// 批量删除
const doDelList = async () => {
  try {
    const response = await request.delete('role/delete', selectedData.value)
    ElMessage.success(`删除成功：${response.msg || 'success'}`)
    refreshTableData()
  } catch (e) {
    ElMessage.error(`提交失败：${e.msg || e.message || 'error'}`)
  } finally {
    showDialog.value = false
  }
}
// 插入一条数据
const createOnce = async () => {
  if (dialogFormRef.value) {
    await dialogFormRef.value.validate(async (valid) => {
      if (valid) {
        try {
          const response = await request.post('role/create', dialogEditData.value)
          ElMessage.success(`创建成功：${response.msg || 'success'}`)
          refreshTableData()
        } catch (e) {
          ElMessage.error(`创建失败：${e.msg || e.message || 'error'}`)
        } finally {
          showDialog.value = false
        }
      }
    })
  }
}
// 创建角色
const newRole = () => {
  dialogEditData.value = {}
  dialogTitle.value = '创建角色'
  showDialog.value = true
}
// 批量删除
const deleteByList = () => {
  dialogEditData.value = {}
  dialogTitle.value = '确认批量删除'
  showDialog.value = true
}

const loading = ref(false)
// 表格数据
const tableData = ref([])
// 选中的数据
const selectedData = ref([])
// 表头配置
const columns = [
  {prop: 'name', label: '角色名称'},
  {prop: 'code', label: '角色标识符'},
  {prop: 'remark', label: '角色描述'},
  {prop: 'orderNum', label: '排序索引', width: '90'}
]
// 操作按钮
const actions = [
  {
    label: '编辑',
    handler: (row) => {
      dialogEditData.value = {}
      Object.assign(dialogEditData.value, row)
      dialogTitle.value = '编辑角色'
      showDialog.value = true
    }
  },
  {
    label: '删除',
    type: 'danger',
    handler: (row) => {
      dialogEditData.value = {}
      Object.assign(dialogEditData.value, row)
      dialogTitle.value = '删除角色'
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
    const response = await request.get('role/view', {
      pageNo: pagination.value.currentPage,
      pageSize: pagination.value.pageSize
    })
    tableData.value = response.data.list
    pagination.value.total = response.data.total

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
</style>