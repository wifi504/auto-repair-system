<template>
  <el-empty description="这里什么都没有呢...">
    <el-button @click="router.push('/platform')" type="primary" plain>去概览页瞧瞧~</el-button>
  </el-empty>
</template>

<script setup>
import {useRouter} from "vue-router";

const router = useRouter()
</script>

<style scoped>

</style>