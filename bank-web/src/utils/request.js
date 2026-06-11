import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = token
    }
    const tellerNo = localStorage.getItem('tellerNo')
    if (tellerNo) {
      config.headers['operator-no'] = tellerNo
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    const res = response.data
    // 401 → 未登录/会话过期，清除数据跳转登录页
    if (res.code === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('tellerNo')
      localStorage.removeItem('tellerName')
      localStorage.removeItem('institutionNo')
      localStorage.removeItem('tellerType')
      router.push('/login')
      return Promise.reject(new Error(res.message || '未登录或会话已过期'))
    }
    // 409 登录冲突——由调用方自行处理（显示确认弹窗）
    if (res.code === 409) {
      return Promise.reject({ code: 409, message: res.message, response })
    }
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('tellerNo')
      localStorage.removeItem('tellerName')
      localStorage.removeItem('institutionNo')
      localStorage.removeItem('tellerType')
      router.push('/login')
    }
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request
