<template>
  <div
      class="base-table"
      v-loading="loading"
      :element-loading-background="'rgb(250,250,250,.7)'">
    <!-- 表格区域 -->
    <el-table
        :data="data"
        border
        stripe
        :row-key="rowKey"
        @selection-change="handleSelectionChange"
        :max-height="tableHeight"
    >
      <!-- 多选框 -->
      <el-table-column v-if="selectable" type="selection" width="40"/>

      <!-- 动态列渲染 -->
      <el-table-column
          v-for="column in columns"
          :key="column.prop"
          :prop="column.prop"
          :label="column.label"
          :width="column.width"
          :align="column.align || 'left'"
      >
        <!-- 自定义插槽 -->
        <template v-if="$slots[column.prop]" #default="scope">
          <slot :name="column.prop" v-bind="scope"/>
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column v-if="actions.length" label="操作" width="300" fixed="right">
        <template #default="scope">
          <el-button
              v-for="action in actions"
              :key="action.label"
              :type="action.type || 'primary'"
              size="small"
              @click="action.handler(scope.row)"
          >
            {{ action.label }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
        v-if="pagination"
        class="pagination"
        background
        :pager-count="5"
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="sizes, prev, pager, next, jumper, ->, total"
        @size-change="onPageSizeChange"
        @current-change="onPageChange"
    />
  </div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount} from "vue";

// Props
const props = defineProps({
  data: {
    type: Array,
    default: () => []
  },
  columns: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  },
  actions: {
    type: Array,
    default: () => []
  },
  pagination: {
    type: Object,
    default: null
  },
  selectable: {
    type: Boolean,
    default: false
  },
  rowKey: {
    type: String,
    default: 'id'
  }
})

// 发射事件
const emit = defineEmits(['pageChange', 'pageSizeChange', 'selectionChange'])

// 分页变化
const onPageChange = (page) => {
  emit('pageChange', page)
}
const onPageSizeChange = (size) => {
  emit('pageSizeChange', size)
}

// 多选
const handleSelectionChange = (selection) => {
  emit('selectionChange', selection)
}

// 自动控制表高度
let tableHeight = ref(0)
const updateTableHeight = () => {

  // 窗口高-标题-脚注-main的两个margin-标题高-分割线加margin-操作按钮-分页条高度加margin
  tableHeight.value = window.innerHeight - 60 - 45 - 40 - 24 - 21 - 32 - 62
}
// 窗口变化时调用的方法
const onWindowResize = () => {
  console.log('窗口大小发生变化，执行方法')
  updateTableHeight()
}
// 监听窗口变化
onMounted(() => {
  window.addEventListener('resize', onWindowResize)
  updateTableHeight()
})
// 组件销毁时移除监听，防止内存泄漏
onBeforeUnmount(() => {
  window.removeEventListener('resize', onWindowResize)
})
</script>

<style scoped>
.base-table {
  margin: 10px 0;
}

.pagination {
  margin-top: 10px;
  display: flex;
  justify-content: flex-start;

}
</style>