<template>
  <div class="login-page">
    <div class="login-bg">
      <div class="login-bg-circle c1"></div>
      <div class="login-bg-circle c2"></div>
      <div class="login-bg-circle c3"></div>
    </div>
    <div class="login-card">
      <div class="login-card-inner">
        <div class="login-brand">
          <div class="login-brand-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/></svg></div>
          <h1 class="login-brand-title">银行核心系统</h1>
          <p class="login-brand-sub">Bank Core System v2.0</p>
        </div>
        <el-form ref="formRef" :model="form" :rules="rules" class="login-form">
          <el-form-item prop="institutionNo">
            <el-select v-model="form.institutionNo" placeholder="选择机构" @change="onInstitutionChange" size="large" class="login-field">
              <el-option v-for="inst in institutions" :key="inst.institutionNo" :label="inst.institutionName" :value="inst.institutionNo" />
            </el-select>
          </el-form-item>
          <el-form-item prop="tellerNo">
            <el-select v-model="form.tellerNo" placeholder="选择柜员" size="large" class="login-field">
              <el-option v-for="t in tellers" :key="t.tellerNo" :label="t.tellerName" :value="t.tellerNo" />
            </el-select>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" show-password placeholder="输入密码" size="large" class="login-field" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="large" class="login-btn" @click="handleLogin" :loading="loading">登 录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getInstitutionList } from '../../api/institution'
import { getTellerByInstitution } from '../../api/teller'
import { login, logout } from '../../api/login'
import { useUserStore } from '../../stores/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)
const institutions = ref([])
const tellers = ref([])

const form = reactive({ institutionNo: '', tellerNo: '', password: '' })

const rules = {
  institutionNo: [{ required: true, message: '请选择机构', trigger: 'change' }],
  tellerNo: [{ required: true, message: '请选择柜员', trigger: 'change' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

onMounted(async () => {
  // 进入登录页时清理旧会话
  if (userStore.token) {
    try { await logout() } catch (_) {}
    userStore.logout()
  }
  try { const res = await getInstitutionList(); institutions.value = res.data } catch (e) { console.error(e) }
})

const onInstitutionChange = async (instNo) => {
  form.tellerNo = ''
  try { const res = await getTellerByInstitution(instNo); tellers.value = res.data } catch (e) { console.error(e) }
}

const doLogin = async (force) => {
  loading.value = true
  try {
    const res = await login({
      institutionNo: form.institutionNo,
      tellerNo: form.tellerNo,
      password: form.password,
      force: force
    })
    userStore.setUser(res.data)
    ElMessage.success(force ? '强制登录成功' : '登录成功')
    router.push('/')
  } catch (e) {
    if (e && e.code === 409) {
      try {
        await ElMessageBox.confirm(
          e.message || '柜员已登录，是否强制登录（挤掉之前的会话）？',
          '登录确认',
          { confirmButtonText: '强制登录', cancelButtonText: '取消', type: 'warning' }
        )
        await doLogin(true)
      } catch (_) { /* 用户取消 */ }
    } else {
      console.error(e)
    }
  } finally {
    loading.value = false
  }
}

const handleLogin = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) { await doLogin(false) }
  })
}
</script>
<style scoped>
.login-page{height:100vh;display:flex;align-items:center;justify-content:center;position:relative;overflow:hidden;background:#f7fafc}
.login-bg{position:absolute;inset:0;pointer-events:none}
.login-bg-circle{position:absolute;border-radius:50%;opacity:.5}
.login-bg-circle.c1{width:500px;height:500px;background:radial-gradient(circle,rgba(49,130,206,.12) 0%,transparent 70%);top:-200px;right:-100px}
.login-bg-circle.c2{width:400px;height:400px;background:radial-gradient(circle,rgba(49,130,206,.08) 0%,transparent 70%);bottom:-150px;left:-100px}
.login-bg-circle.c3{width:300px;height:300px;background:radial-gradient(circle,rgba(99,179,237,.1) 0%,transparent 70%);top:50%;left:50%;transform:translate(-50%,-50%)}
.login-card{position:relative;width:420px;background:#fff;border-radius:20px;box-shadow:0 20px 60px rgba(0,0,0,.08),0 4px 20px rgba(0,0,0,.04);overflow:hidden}
.login-card-inner{padding:48px 40px 40px}
.login-brand{text-align:center;margin-bottom:32px}
.login-brand-icon{width:60px;height:60px;margin:0 auto 16px;border-radius:16px;background:linear-gradient(135deg,#3182ce,#2b6cb0);display:flex;align-items:center;justify-content:center;color:#fff;box-shadow:0 8px 24px rgba(49,130,206,.25)}
.login-brand-icon svg{width:28px;height:28px}
.login-brand-title{font-size:26px;font-weight:700;color:var(--text-primary);margin:0 0 6px}
.login-brand-sub{font-size:13px;color:var(--text-secondary);letter-spacing:1px;margin:0}
.login-form .el-form-item{margin-bottom:20px}
.login-field{width:100%}
.login-field :deep(.el-input__wrapper){border-radius:10px;border:1.5px solid #e2e8f0;box-shadow:none!important;padding:2px 14px;transition:border-color .2s,box-shadow .2s}
.login-field :deep(.el-input__wrapper):hover{border-color:#a0c4e8}
.login-field :deep(.el-input__wrapper.is-focus){border-color:#3182ce;box-shadow:0 0 0 3px rgba(49,130,206,.1)!important}
.login-field :deep(.el-input__inner){height:46px;color:var(--text-primary)}
.login-btn{width:100%;height:48px;font-size:16px;border-radius:10px;margin-top:6px;letter-spacing:4px;font-weight:600}
</style>


