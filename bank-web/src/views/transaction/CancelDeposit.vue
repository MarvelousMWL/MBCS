<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">存款撤销</h2><p class="page-desc">撤销一笔已完成的存款交易</p></div></div>
    <el-card style="max-width:520px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="原交易流水号" prop="originalTransactionNo"><el-input v-model="form.originalTransactionNo" placeholder="输入要撤销的存款流水号" size="large"/></el-form-item>
        <el-alert type="warning" :closable="false" show-icon title="撤销操作不可恢复，请确认流水号正确" style="margin-bottom:16px"/>
        <el-form-item><el-button type="danger" size="large" style="width:100%" @click="handleCancel" :loading="loading">确认撤销存款</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'; import { ElMessage } from 'element-plus'; import { cancelDeposit } from '../../api/transaction'
var formRef=ref(),loading=ref(false),form=reactive({originalTransactionNo:''}),rules={originalTransactionNo:[{required:true,message:'请输入流水号',trigger:'blur'}]}
var handleCancel=async()=>{await formRef.value.validate(async v=>{if(v){loading.value=true;try{var r=await cancelDeposit(form);ElMessage.success('撤销成功! 新流水号:'+(r.data?.transactionNo||''));form.originalTransactionNo=''}catch(e){}finally{loading.value=false}}})}
</script>
<style scoped>.page-header{margin-bottom:24px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>