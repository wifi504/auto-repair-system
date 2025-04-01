<template>
  <page-title>
    角色管理
  </page-title>
  <div>
    <el-button icon="Refresh" @click="refreshTableData"/>
    <el-button type="primary">新增</el-button>
    <el-button type="danger" :disabled="!selectedData.length">批量删除</el-button>
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
  </base-table>
</template>

<script setup>
import {ref, onActivated} from 'vue'
import request from "@/utils/request.js";
import PageTitle from "@/components/platform/PageTitle.vue";
import BaseTable from "@/components/platform/BaseTable.vue";
import debounce from "@/utils/debounce.js";
import {ElMessage} from "element-plus";

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
  {prop: 'orderNum', label: '排序值', width: '80'}
]
// 操作按钮
const actions = [
  {
    label: '编辑',
    handler: (row) => console.log('编辑', row)
  },
  {
    label: '删除',
    type: 'danger',
    handler: (row) => console.log('删除', row)
  },
  {
    label: '哈哈一笑',
    type: 'success',
    handler: (row) => console.log('哈哈一笑', row)
  }
]
// 分页
const pagination = ref({
  currentPage: 1,
  pageSize: 5,
  total: 100
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
  console.log('选中项:', selectedData.value)
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