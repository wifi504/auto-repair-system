<template>
  <el-page-header icon="ArrowLeft" @back="router.back()">
    <template #content>
      <h4>
        <slot name="default"></slot>
      </h4>
    </template>
  </el-page-header>
  <el-divider/>
</template>

<script setup>
import {useRouter} from "vue-router";

const router = useRouter()
</script>

<style scoped>
.el-divider {
  margin: 10px 0 10px 0;
}
</style>