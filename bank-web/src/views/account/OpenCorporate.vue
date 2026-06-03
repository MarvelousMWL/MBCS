<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">对公开户</h2><p class="page-desc">为企业客户开立客户账户</p></div></div>
    <el-card style="max-width:520px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="客户号" prop="customerNo"><el-input v-model="form.customerNo" placeholder="输入企业客户号"/></el-form-item>
        <el-form-item><el-alert type="warning" :closable="false" show-icon title="企业账户类型将自动设置为 CORPORATE" style="margin:0"/></el-form-item>
        <el-form-item><el-button type="primary" size="large" style="width:100%" @click="handleSubmit" :loading="loading">确认开户</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'; import { ElMessage } from 'element-plus'; import { openCustomerAccount } from '../../api/account'
var formRef=ref(),loading=ref(false),form=reactive({customerNo:'',accountType:'CORPORATE'})
var rules={customerNo:[{required:true,message:'请输入客户号',trigger:'blur'}]}
var handleSubmit=async()=>{await formRef.value.validate(async v=>{if(v){loading.value=true;try{await openCustomerAccount(form);ElMessage.success('对公开户成功');form.customerNo=''}catch(e){}finally{loading.value=false}}})}
</script>
<style scoped>.page-header{margin-bottom:24px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>