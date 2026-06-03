import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const tellerNo = ref(localStorage.getItem('tellerNo') || '')
  const tellerName = ref(localStorage.getItem('tellerName') || '')
  const institutionNo = ref(localStorage.getItem('institutionNo') || '')
  const tellerType = ref(localStorage.getItem('tellerType') || '')

  function setUser(data) {
    token.value = data.token
    tellerNo.value = data.tellerNo
    tellerName.value = data.tellerName
    institutionNo.value = data.institutionNo
    tellerType.value = data.tellerType || ''
    localStorage.setItem('token', data.token)
    localStorage.setItem('tellerNo', data.tellerNo)
    localStorage.setItem('tellerName', data.tellerName)
    localStorage.setItem('institutionNo', data.institutionNo)
    localStorage.setItem('tellerType', data.tellerType || '')
  }

  function logout() {
    token.value = ''
    tellerNo.value = ''
    tellerName.value = ''
    institutionNo.value = ''
    tellerType.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('tellerNo')
    localStorage.removeItem('tellerName')
    localStorage.removeItem('institutionNo')
    localStorage.removeItem('tellerType')
  }

  return {
    token,
    tellerNo,
    tellerName,
    institutionNo,
    tellerType,
    setUser,
    logout
  }
})