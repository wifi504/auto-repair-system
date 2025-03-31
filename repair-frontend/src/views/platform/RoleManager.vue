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
    <!-- 使用插槽自定义列 -->
    <template #status="{ row }">
      <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
        {{ row.status === 'active' ? '激活' : '禁用' }}
      </el-tag>
    </template>
  </base-table>
</template>

<script setup>
import {ref} from 'vue'
import PageTitle from "@/components/platform/PageTitle.vue";
import BaseTable from "@/components/platform/BaseTable.vue";

const loading = ref(false)

// 刷新表格数据
const refreshTableData = () => {
  loading.value = true
  setTimeout(() => loading.value = false, 2000)
}

// 表格数据
const tableData = ref([
  {id: 1, name: '蔡文姬', status: 'active'},
  {id: 2, name: '张飞', status: 'active'},
  {id: 3, name: '李白', status: 'active'},
  {id: 4, name: '蔡徐坤', status: 'active'},
  {id: 5, name: 'Java', status: 'active'},
  {id: 6, name: 'Spring', status: 'active'},
  {id: 7, name: 'China', status: 'active'},
  {id: 8, name: '北京', status: 'active'},
  {id: 9, name: '上海', status: 'active'},
  {id: 10, name: '成都', status: 'active'},
  {id: 11, name: '张三', status: 'active'},
  {id: 12, name: '李四', status: 'active'},
  {id: 13, name: '王五', status: 'active'},
  {id: 14, name: '赵六', status: 'active'},
  {id: 15, name: '前妻', status: 'inactive'}
])

// 选中的数据
const selectedData = ref([])

// 表头配置
const columns = [
  {prop: 'name', label: '姓名', width: '100'},
  {prop: 'status', label: '状态'},
  {prop: 'id', label: '主键ID'}
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
  pageSize: 10,
  total: 100
})

// 事件处理
const handlePageChange = (page) => {
  console.log('当前页:', page)
}
const handlePageSizeChange = (size) => {
  console.log('每页条数:', size)
}
const handleSelectionChange = (selection) => {
  selectedData.value = selection
  console.log('选中项:', selectedData.value)
}
</script>

<style scoped>
</style>