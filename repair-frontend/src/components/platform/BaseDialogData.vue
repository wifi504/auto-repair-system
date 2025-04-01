<template>
  <el-dialog
      :model-value="modelValue"
      @update:modelValue="val => emit('update:modelValue', val)"
      :close-on-click-modal="false"
      width="500"
  >
    <template #header>
      <h3 class="title-h3">
        <slot name="title"></slot>
      </h3>
    </template>
    <template #default>
      <slot name="context"></slot>
    </template>
    <template #footer>
      <slot name="footer"></slot>
    </template>
  </el-dialog>
</template>

<script setup>
const props = defineProps({
  modelValue: Boolean
})
const emit = defineEmits(['update:modelValue'])
</script>

<style scoped>
.title-h3 {
  font-weight: 600;
  display: flex;
  gap: 6px;
  color: #1067c9;
}
</style>