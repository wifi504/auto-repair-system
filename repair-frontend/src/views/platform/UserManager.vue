<template>
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

import BaseTable from "@/components/platform/BaseTable.vue";
</script>

<style scoped>

</style>