<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">交易撤销</h2><p class="page-desc">撤销已发生的存款或取款交易</p></div></div>
    <el-row :gutter="20"><el-col :span="12"><el-card><h3 style="margin:0 0 16px;font-size:16px">存款撤销</h3>
      <el-form ref="dcFormRef" :model="dcForm" :rules="dcRules" label-width="120px">
        <el-form-item label="原交易流水号" prop="originalTransactionNo"><el-input v-model="dcForm.originalTransactionNo" placeholder="输入要撤销的存款流水号"/></el-form-item>
        <el-form-item><el-button type="danger" @click="handleCancel('deposit')">确认撤销存款</el-button></el-form-item>
      </el-form>
    </el-card></el-col>
    <el-col :span="12"><el-card><h3 style="margin:0 0 16px;font-size:16px">取款撤销</h3>
      <el-form ref="wcFormRef" :model="wcForm" :rules="wcRules" label-width="120px">
        <el-form-item label="原交易流水号" prop="originalTransactionNo"><el-input v-model="wcForm.originalTransactionNo" placeholder="输入要撤销的取款流水号"/></el-form-item>
        <el-form-item><el-button type="danger" @click="handleCancel('withdraw')">确认撤销取款</el-button></el-form-item>
      </el-form>
    </el-card></el-col></el-row>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'; import { ElMessage } from 'element-plus'
import { cancelDeposit, withdrawCancel } from '../../api/transaction'
var dcFormRef=ref(),dcForm=reactive({originalTransactionNo:''}),dcRules={originalTransactionNo:[{required:true,message:'请输入流水号',trigger:'blur'}]}
var wcFormRef=ref(),wcForm=reactive({originalTransactionNo:''}),wcRules={originalTransactionNo:[{required:true,message:'请输入流水号',trigger:'blur'}]}
var handleCancel=async(type)=>{try{if(type==='deposit'){var r=await cancelDeposit(dcForm);ElMessage.success('撤销成功! 新流水号:'+(r.data?.transactionNo||''));dcForm.originalTransactionNo=''}else{var r=await withdrawCancel(wcForm);ElMessage.success('撤销成功! 新流水号:'+(r.data?.transactionNo||''));wcForm.originalTransactionNo=''}}catch(e){console.error(e)}}
</script>
<style scoped>.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>