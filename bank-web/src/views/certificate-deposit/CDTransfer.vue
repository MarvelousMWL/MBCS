<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">大额存单转让</h2><p class="page-desc">将大额存单转让给其他客户</p></div></div>
    <el-card style="max-width:560px;margin-bottom:20px">
      <template #header><span style="font-weight:600">转让</span></template>
      <el-form ref="transferFormRef" :model="transferForm" :rules="transferRules" label-width="120px">
        <el-form-item label="认购编号" prop="subscribeNo"><el-input v-model="transferForm.subscribeNo" placeholder="输入认购编号"/></el-form-item>
        <el-form-item label="转入客户账号" prop="targetCustomerNo"><el-input v-model="transferForm.targetCustomerNo" placeholder="输入转入客户账号"/></el-form-item>
        <el-form-item><el-button type="primary" @click="handleTransfer">确认转让</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <template #header><span style="font-weight:600">转让记录</span></template>
      <el-table :data="transferList" class="data-table">
        <el-table-column prop="transferNo" label="转让编号" width="200"/>
        <el-table-column prop="subscribeNo" label="认购编号" width="180"/>
        <el-table-column prop="sourceCustomerNo" label="转出客户" width="150"/>
        <el-table-column prop="targetCustomerNo" label="转入客户" width="150"/>
        <el-table-column prop="transferDate" label="转让日期" width="160"/>
        <el-table-column label="状态" width="100"><template #default="{ row: r }"><el-tag size="small" :type="r.status==='SUCCESS'?'success':'warning'" effect="light" style="border:0">{{r.status==='SUCCESS'?'成功':'处理中'}}</el-tag></template></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage } from 'element-plus'
import { transferCertificate, getSubscribeList } from '../../api/certificateDeposit'
var transferFormRef=ref(),transferList=ref([])
var transferForm=reactive({subscribeNo:'',targetCustomerNo:''})
var transferRules={subscribeNo:[{required:true,message:'请输入认购编号',trigger:'blur'}],targetCustomerNo:[{required:true,message:'请输入转入客户账号',trigger:'blur'}]}
var loadTransfers=async()=>{try{var r=await getSubscribeList({transferred:true});transferList.value=r.data?.records||r.data||[]}catch(e){console.error(e)}}
var handleTransfer=async()=>{await transferFormRef.value.validate(async v=>{if(v){try{var r=await transferCertificate(transferForm);ElMessage.success('转让成功! 转让编号:'+(r.data?.transferNo||''));transferForm.subscribeNo='';transferForm.targetCustomerNo='';loadTransfers()}catch(e){console.error(e)}}})}
onMounted(()=>{loadTransfers()})
</script>
<style scoped>.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>
