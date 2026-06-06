<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">大额存单转让</h2><p class="page-desc">将大额存单转让给其他客户</p></div></div>
    <el-card style="max-width:600px;margin-bottom:20px">
      <template #header><span style="font-weight:600">转让</span></template>
      <el-form ref="transferFormRef" :model="transferForm" :rules="transferRules" label-width="120px">
        <el-form-item label="存单账号" prop="cdAccountNo"><el-input v-model="transferForm.cdAccountNo" placeholder="输入存单账号"/></el-form-item>
        <el-form-item label="转出人账号" prop="transferorAccountNo"><el-input v-model="transferForm.transferorAccountNo" placeholder="输入转出人账号"/></el-form-item>
        <el-form-item label="转入人账号" prop="transfereeAccountNo"><el-input v-model="transferForm.transfereeAccountNo" placeholder="输入转入人账号"/></el-form-item>
        <el-form-item label="转让价格" prop="transferPrice"><el-input-number v-model="transferForm.transferPrice" :min="0.01" :precision="2" style="width:200px"/></el-form-item>
        <el-form-item label="定价方式" prop="pricingType"><el-select v-model="transferForm.pricingType" style="width:200px"><el-option label="面值转让" value="PAR"/><el-option label="溢价转让" value="PREMIUM"/><el-option label="折价转让" value="DISCOUNT"/></el-select></el-form-item>
        <el-form-item label="手续费"><el-input-number v-model="transferForm.handlingFee" :min="0" :precision="2" style="width:200px"/></el-form-item>
        <el-form-item><el-button type="primary" @click="handleTransfer">确认转让</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <template #header><span style="font-weight:600">转让记录</span></template>
      <el-empty description="暂无数据" :image-size="60"/>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'; import { ElMessage } from 'element-plus'
import { transferCertificate } from '../../api/certificateDeposit'
var transferFormRef=ref()
var transferForm=reactive({cdAccountNo:'',transferorAccountNo:'',transfereeAccountNo:'',transferPrice:0,pricingType:'PAR',handlingFee:0})
var transferRules={cdAccountNo:[{required:true,message:'请输入存单账号',trigger:'blur'}],transferorAccountNo:[{required:true,message:'请输入转出人账号',trigger:'blur'}],transfereeAccountNo:[{required:true,message:'请输入转入人账号',trigger:'blur'}],transferPrice:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('转让价格必须大于0'))}},trigger:'blur'}],pricingType:[{required:true,message:'请选择定价方式',trigger:'change'}]}
var handleTransfer=async()=>{await transferFormRef.value.validate(async v=>{if(v){try{var r=await transferCertificate(transferForm);ElMessage.success('转让成功!');transferForm.cdAccountNo='';transferForm.transferorAccountNo='';transferForm.transfereeAccountNo='';transferForm.transferPrice=0;transferForm.pricingType='PAR';transferForm.handlingFee=0}catch(e){console.error(e)}}})}
</script>
<style scoped>.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>
