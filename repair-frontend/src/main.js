import {createApp} from 'vue'
import '@/assets/css/base.css'
import App from './App.vue'
import router from "./router";

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as Icons from '@element-plus/icons-vue'

const app = createApp(App)

// 全局注册所有图标
Object.keys(Icons).forEach((key) => {
  app.component(key, Icons[key])
})

app
  .use(router)
  .use(ElementPlus, {locale: zhCn})
  .mount('#app')
