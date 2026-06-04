<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">大额存单产品管理</h2><p class="page-desc">发行和管理大额存单产品</p></div></div>
    <el-card style="max-width:640px;margin-bottom:20px">
      <template #header><span style="font-weight:600">产品发行</span></template>
      <el-form ref="issueFormRef" :model="issueForm" :rules="issueRules" label-width="110px">
        <el-form-item label="产品名称" prop="productName"><el-input v-model="issueForm.productName" placeholder="输入产品名称"/></el-form-item>
        <el-form-item label="发行金额" prop="issueAmount"><el-input-number v-model="issueForm.issueAmount" :min="0.01" :precision="2" style="width:200px"/></el-form-item>
        <el-form-item label="年利率(%)" prop="interestRate"><el-input-number v-model="issueForm.interestRate" :min="0" :precision="4" :step="0.01" style="width:200px"/></el-form-item>
        <el-form-item label="期限(天)" prop="termDays"><el-input-number v-model="issueForm.termDays" :min="1" :step="1" style="width:200px"/></el-form-item>
        <el-form-item><el-button type="primary" @click="handleIssue">确认发行</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <template #header><span style="font-weight:600">产品列表</span></template>
      <el-table :data="productList" class="data-table">
        <el-table-column prop="productId" label="产品编号" width="180"/>
        <el-table-column prop="productName" label="产品名称" width="140"/>
        <el-table-column label="发行金额" width="130" align="right"><template #default="{ row: r }">{{Number(r.issueAmount).toLocaleString('zh-CN',{minFractionDigits:2})}}</template></el-table-column>
        <el-table-column prop="interestRate" label="年利率(%)" width="100" align="right"/>
        <el-table-column prop="termDays" label="期限(天)" width="90" align="right"/>
        <el-table-column label="状态" width="100"><template #default="{ row: r }"><el-tag size="small" :type="r.status==='ACTIVE'?'success':'info'" effect="light" style="border:0">{{r.status==='ACTIVE'?'在售':'已截止'}}</el-tag></template></el-table-column>
        <el-table-column prop="issueDate" label="发行日期" width="160"/>
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage } from 'element-plus'
import { issueCertificate, getProductList } from '../../api/certificateDeposit'
var issueFormRef=ref(),productList=ref([])
var issueForm=reactive({productName:'',issueAmount:0,interestRate:0,termDays:0})
var issueRules={productName:[{required:true,message:'请输入产品名称',trigger:'blur'}],issueAmount:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('发行金额必须大于0'))}},trigger:'blur'}],interestRate:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('利率必须大于0'))}},trigger:'blur'}],termDays:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('期限必须大于0'))}},trigger:'blur'}]}
var loadProducts=async()=>{try{var r=await getProductList();productList.value=r.data?.records||r.data||[]}catch(e){console.error(e)}}
var handleIssue=async()=>{await issueFormRef.value.validate(async v=>{if(v){try{var r=await issueCertificate(issueForm);ElMessage.success('发行成功! 产品编号:'+(r.data?.productId||''));issueForm.productName='';issueForm.issueAmount=0;issueForm.interestRate=0;issueForm.termDays=0;loadProducts()}catch(e){console.error(e)}}})}
onMounted(()=>{loadProducts()})
</script>
<style scoped>.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>
