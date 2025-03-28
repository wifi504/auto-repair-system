<template>
  <div class="background">
    <img :src="image" alt="页面背景"/>
  </div>
</template>

<script setup>
import loginImage from '@/assets/image/login-bg.svg'
import {ref} from "vue";

const props = defineProps(['image'])
const image = ref()

switch (props.image) {
  case 'login' : image.value = loginImage
}
</script>

<style scoped>
.background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: -999;
  overflow: hidden;
}

.background img {
  width: 100vw;
  height: 100vh;
  object-fit: cover;
}
</style>