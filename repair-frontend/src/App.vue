<template>
  <router-view/>
</template>

<script setup>
import {useRouter} from "vue-router";

const router = useRouter()

// 如果有未加载的重定向，尝试加载
const requireRedirect = localStorage.getItem('require-redirect')
if (requireRedirect) {
  localStorage.removeItem('require-redirect')
  router.push(requireRedirect)
}
</script>

<style scoped>
</style>
