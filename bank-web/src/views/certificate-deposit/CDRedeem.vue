<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">大额存单兑付</h2><p class="page-desc">兑付到期或提前支取的大额存单</p></div></div>
    <el-card style="max-width:560px;margin-bottom:20px">
      <template #header><span style="font-weight:600">兑付</span></template>
      <el-form ref="redeemFormRef" :model="redeemForm" :rules="redeemRules" label-width="120px">
        <el-form-item label="存单账号" prop="cdAccountNo"><el-input v-model="redeemForm.cdAccountNo" placeholder="输入存单账号"/></el-form-item>
        <el-form-item label="兑付方式" prop="redeemType"><el-select v-model="redeemForm.redeemType" placeholder="选择兑付方式" style="width:100%"><el-option label="到期兑付" value="MATURITY"/><el-option label="提前支取" value="EARLY"/></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="handleRedeem">确认兑付</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <template #header><span style="font-weight:600">存单列表</span></template>
      <el-table :data="accountList" class="data-table">
        <el-table-column prop="cdAccountNo" label="存单账号" width="200"/>
        <el-table-column prop="customerAccountNo" label="客户账号" width="150"/>
        <el-table-column label="本金" width="130" align="right"><template #default="{ row: r }">{{Number(r.principal).toLocaleString('zh-CN',{minFractionDigits:2})}}</template></el-table-column>
        <el-table-column prop="maturityDate" label="到期日期" width="160"/>
        <el-table-column label="状态" width="100"><template #default="{ row: r }"><el-tag size="small" :type="r.status==='REDEEMED'?'success':'warning'" effect="light" style="border:0">{{r.status==='ACTIVE'?'正常':r.status==='REDEEMED'?'已兑付':r.status||'未知'}}</el-tag></template></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage } from 'element-plus'
import { redeemCertificate, getSubscribeList } from '../../api/certificateDeposit'
var redeemFormRef=ref(),accountList=ref([])
var redeemForm=reactive({cdAccountNo:'',redeemType:'MATURITY'})
var redeemRules={cdAccountNo:[{required:true,message:'请输入存单账号',trigger:'blur'}],redeemType:[{required:true,message:'请选择兑付方式',trigger:'change'}]}
var loadAccounts=async()=>{try{var r=await getSubscribeList();accountList.value=r.data||[]}catch(e){console.error(e)}}
var handleRedeem=async()=>{await redeemFormRef.value.validate(async v=>{if(v){try{var r=await redeemCertificate(redeemForm);ElMessage.success('兑付成功!');redeemForm.cdAccountNo='';loadAccounts()}catch(e){console.error(e)}}})}
onMounted(()=>{loadAccounts()})
</script>
<style scoped>.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>
