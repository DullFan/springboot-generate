import { createApp } from 'vue'
import App from './App.vue'
import "tailwindcss/tailwind.css"
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
const app = createApp(App)
app.config.globalProperties.$api = "http://localhost:9968/api"
app.use(ElementPlus).mount('#app')




