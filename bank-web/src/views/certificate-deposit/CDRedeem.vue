<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">大额存单兑付</h2><p class="page-desc">兑付到期或提前支取的大额存单</p></div></div>
    <el-card style="max-width:560px;margin-bottom:20px">
      <template #header><span style="font-weight:600">兑付</span></template>
      <el-form ref="redeemFormRef" :model="redeemForm" :rules="redeemRules" label-width="120px">
        <el-form-item label="认购编号" prop="subscribeNo"><el-input v-model="redeemForm.subscribeNo" placeholder="输入认购编号"/></el-form-item>
        <el-form-item label="兑付方式" prop="redeemType"><el-select v-model="redeemForm.redeemType" placeholder="选择兑付方式" style="width:100%"><el-option label="到期兑付" value="MATURITY"/><el-option label="提前支取" value="EARLY"/></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="handleRedeem">确认兑付</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <template #header><span style="font-weight:600">兑付记录</span></template>
      <el-table :data="redeemList" class="data-table">
        <el-table-column prop="redeemNo" label="兑付编号" width="200"/>
        <el-table-column prop="subscribeNo" label="认购编号" width="180"/>
        <el-table-column label="兑付方式" width="100"><template #default="{ row: r }">{{r.redeemType==='MATURITY'?'到期兑付':'提前支取'}}</template></el-table-column>
        <el-table-column label="兑付金额" width="130" align="right"><template #default="{ row: r }">{{Number(r.amount).toLocaleString('zh-CN',{minFractionDigits:2})}}</template></el-table-column>
        <el-table-column prop="redeemDate" label="兑付日期" width="160"/>
        <el-table-column label="状态" width="100"><template #default="{ row: r }"><el-tag size="small" :type="r.status==='SUCCESS'?'success':'warning'" effect="light" style="border:0">{{r.status==='SUCCESS'?'成功':'处理中'}}</el-tag></template></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage } from 'element-plus'
import { redeemCertificate, getSubscribeList } from '../../api/certificateDeposit'
var redeemFormRef=ref(),redeemList=ref([])
var redeemForm=reactive({subscribeNo:'',redeemType:'MATURITY'})
var redeemRules={subscribeNo:[{required:true,message:'请输入认购编号',trigger:'blur'}],redeemType:[{required:true,message:'请选择兑付方式',trigger:'change'}]}
var loadRedeems=async()=>{try{var r=await getSubscribeList({redeemed:true});redeemList.value=r.data?.records||r.data||[]}catch(e){console.error(e)}}
var handleRedeem=async()=>{await redeemFormRef.value.validate(async v=>{if(v){try{var r=await redeemCertificate(redeemForm);ElMessage.success('兑付成功! 兑付编号:'+(r.data?.redeemNo||''));redeemForm.subscribeNo='';loadRedeems()}catch(e){console.error(e)}}})}
onMounted(()=>{loadRedeems()})
</script>
<style scoped>.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>
