import {defineStore} from 'pinia'
import {ref} from 'vue'
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

export const useCurrentUserStore = defineStore('current-user', () => {
  const data = ref({})

  let hasLoaded = false
  let promise

  async function forceRefresh() {
    if (promise) return promise
    promise = new Promise(async (resolve) => {
      try {
        const res = await request.get('/profile/me')
        data.value = res.data
        hasLoaded = true
        resolve(res)
      } catch (err) {
        ElMessage.error(`加载当前用户：${err.msg || 'Error'}`)
        resolve(err)
      }
    })
    promise.finally(() => promise = null)
    return promise
  }

  async function refresh() {
    if (hasLoaded) return true
    await forceRefresh()
  }

  return {data, refresh, forceRefresh}
})