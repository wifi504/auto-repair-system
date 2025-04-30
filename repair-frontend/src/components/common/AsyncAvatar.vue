<template>
  <div class="avatar-box"
       :class="`${imageUrl ? '' : 'avatar-box-background'}
       ${prop.type === 'square' ? 'avatar-box-square' : 'avatar-box-circle'}`"
       :style="`width: ${prop.size}px; height: ${prop.size}px;`"
  >
    <img v-if="imageUrl" :src="imageUrl" alt="用户头像" class="avatar-img">
    <component v-else :is="Avatar" class="avatar-default"></component>
  </div>
</template>

<script setup>
import {Avatar} from "@element-plus/icons-vue";
import {ref, onMounted, onUpdated} from "vue";
import request from "@/utils/request.js";

const prop = defineProps({
  avatarName: {
    type: String,
    default: ''
  },
  type: {
    type: String,
    default: 'square'
  },
  size: {
    type: Number,
    default: 32
  }
})

const imageUrl = ref('')

const loadImage = async () => {
  try {
    const res = await request.get(`/file/get-avatars-url/${prop.avatarName}`)
    imageUrl.value = res.data
  } catch (ignore) {
  }
}

onMounted(loadImage)
onUpdated(loadImage)
</script>

<style scoped>
.avatar-box {
  text-align: center;
  position: relative;
  overflow: hidden;
}

.avatar-box-square {
  border-radius: 10%;
}

.avatar-box-circle {
  border-radius: 50%;
}

.avatar-box-background {
  background: #e3e3e3;
}

.avatar-img {
  width: 100%;
  height: 100%;
}

.avatar-default {
  width: 60%;
  height: 60%;
  position: relative;
  top: 50%;
  transform: translateY(-50%);
  color: #8d8d8d;
}
</style>