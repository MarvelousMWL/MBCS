<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">大额存单认购</h2><p class="page-desc">认购大额存单产品</p></div></div>
    <el-card style="max-width:560px;margin-bottom:20px">
      <template #header><span style="font-weight:600">认购</span></template>
      <el-form ref="subFormRef" :model="subForm" :rules="subRules" label-width="110px">
        <el-form-item label="客户账号" prop="customerAccountNo"><el-input v-model="subForm.customerAccountNo" placeholder="输入客户账号"/></el-form-item>
        <el-form-item label="产品编号" prop="productId"><el-input v-model="subForm.productId" placeholder="输入产品编号"/></el-form-item>
        <el-form-item label="认购金额" prop="amount"><el-input-number v-model="subForm.amount" :min="0.01" :precision="2" style="width:200px"/></el-form-item>
        <el-form-item><el-button type="primary" @click="handleSubscribe">确认认购</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <template #header><span style="font-weight:600">认购记录</span></template>
      <el-table :data="subList" class="data-table">
        <el-table-column prop="subscribeNo" label="认购编号" width="200"/>
        <el-table-column prop="customerAccountNo" label="客户账号" width="150"/>
        <el-table-column prop="productId" label="产品编号" width="140"/>
        <el-table-column label="认购金额" width="130" align="right"><template #default="{ row: r }">{{Number(r.amount).toLocaleString('zh-CN',{minFractionDigits:2})}}</template></el-table-column>
        <el-table-column prop="subscribeDate" label="认购日期" width="160"/>
        <el-table-column label="状态" width="100"><template #default="{ row: r }"><el-tag size="small" :type="r.status===0?'success':'info'" effect="light" style="border:0">{{r.status===0?'有效':'已兑付'}}</el-tag></template></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage } from 'element-plus'
import { subscribeCertificate, getSubscribeList } from '../../api/certificateDeposit'
var subFormRef=ref(),subList=ref([])
var subForm=reactive({customerAccountNo:'',productId:'',amount:0})
var subRules={customerAccountNo:[{required:true,message:'请输入客户账号',trigger:'blur'}],productId:[{required:true,message:'请输入产品编号',trigger:'blur'}],amount:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('认购金额必须大于0'))}},trigger:'blur'}]}
var loadSubs=async()=>{try{var r=await getSubscribeList();subList.value=r.data?.records||r.data||[]}catch(e){console.error(e)}}
var handleSubscribe=async()=>{await subFormRef.value.validate(async v=>{if(v){try{var r=await subscribeCertificate(subForm);ElMessage.success('认购成功! 认购编号:'+(r.data?.subscribeNo||''));subForm.customerAccountNo='';subForm.productId='';subForm.amount=0;loadSubs()}catch(e){console.error(e)}}})}
onMounted(()=>{loadSubs()})
</script>
<style scoped>.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>
