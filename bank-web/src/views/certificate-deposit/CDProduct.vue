<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">大额存单产品管理</h2><p class="page-desc">发行和管理大额存单产品</p></div></div>
    <el-card style="max-width:720px;margin-bottom:20px">
      <template #header><span style="font-weight:600">产品发行</span></template>
      <el-form ref="issueFormRef" :model="issueForm" :rules="issueRules" label-width="130px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="产品编码" prop="productCode"><el-input v-model="issueForm.productCode" placeholder="输入产品编码"/></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="产品名称" prop="productName"><el-input v-model="issueForm.productName" placeholder="输入产品名称"/></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="发行总额" prop="totalQuota"><el-input-number v-model="issueForm.totalQuota" :min="0.01" :precision="2" style="width:100%"/></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="最低认购金额" prop="minSubscriptionAmount"><el-input-number v-model="issueForm.minSubscriptionAmount" :min="0.01" :precision="2" style="width:100%"/></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="年利率(%)" prop="interestRate"><el-input-number v-model="issueForm.interestRate" :min="0" :precision="4" :step="0.01" style="width:100%"/></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="期限(月)" prop="termMonths"><el-input-number v-model="issueForm.termMonths" :min="1" :step="1" style="width:100%"/></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="发行开始日" prop="issueStartDate"><el-date-picker v-model="issueForm.issueStartDate" type="date" placeholder="选择日期" style="width:100%" value-format="YYYY-MM-DD"/></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="发行结束日" prop="issueEndDate"><el-date-picker v-model="issueForm.issueEndDate" type="date" placeholder="选择日期" style="width:100%" value-format="YYYY-MM-DD"/></el-form-item></el-col>
        </el-row>
        <el-form-item><el-button type="primary" @click="handleIssue">确认发行</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <template #header><span style="font-weight:600">产品列表</span></template>
      <el-table :data="productList" class="data-table">
        <el-table-column prop="productCode" label="产品编码" width="140"/>
        <el-table-column prop="productName" label="产品名称" width="140"/>
        <el-table-column label="发行总额" width="120" align="right"><template #default="{ row: r }">{{Number(r.totalQuota).toLocaleString('zh-CN',{minFractionDigits:2})}}</template></el-table-column>
        <el-table-column label="剩余额度" width="120" align="right"><template #default="{ row: r }">{{Number(r.remainingQuota).toLocaleString('zh-CN',{minFractionDigits:2})}}</template></el-table-column>
        <el-table-column prop="interestRate" label="年利率(%)" width="90" align="right"/>
        <el-table-column prop="termMonths" label="期限(月)" width="80" align="right"/>
        <el-table-column label="状态" width="100"><template #default="{ row: r }"><el-tag size="small" :type="r.status==='ACTIVE'?'success':'info'" effect="light" style="border:0">{{r.status==='ACTIVE'?'在售':'已截止'}}</el-tag></template></el-table-column>
        <el-table-column label="发行日期" width="160"><template #default="{ row: r }">{{r.issueStartDate}} ~ {{r.issueEndDate}}</template></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage } from 'element-plus'
import { issueCertificate, getProductList } from '../../api/certificateDeposit'
var issueFormRef=ref(),productList=ref([])
var issueForm=reactive({productCode:'',productName:'',totalQuota:0,minSubscriptionAmount:0,interestRate:0,termMonths:6,issueStartDate:'',issueEndDate:'',customerType:'PUBLIC',currency:'CNY'})
var issueRules={productCode:[{required:true,message:'请输入产品编码',trigger:'blur'}],productName:[{required:true,message:'请输入产品名称',trigger:'blur'}],totalQuota:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('发行总额必须大于0'))}},trigger:'blur'}],minSubscriptionAmount:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('最低认购金额必须大于0'))}},trigger:'blur'}],interestRate:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('利率必须大于0'))}},trigger:'blur'}],termMonths:[{required:true,validator:(r,v,c)=>{if(v&&v>0){c()}else{c(new Error('期限必须大于0'))}},trigger:'blur'}],issueStartDate:[{required:true,message:'请选择发行开始日',trigger:'change'}],issueEndDate:[{required:true,message:'请选择发行结束日',trigger:'change'}]}
var loadProducts=async()=>{try{var r=await getProductList();productList.value=r.data||[]}catch(e){console.error(e)}}
var handleIssue=async()=>{await issueFormRef.value.validate(async v=>{if(v){try{var r=await issueCertificate(issueForm);ElMessage.success('发行成功! 产品编码:'+(r.data?.productCode||''));issueForm.productCode='';issueForm.productName='';issueForm.totalQuota=0;issueForm.minSubscriptionAmount=0;issueForm.interestRate=0;issueForm.termMonths=6;issueForm.issueStartDate='';issueForm.issueEndDate='';loadProducts()}catch(e){console.error(e)}}})}
onMounted(()=>{loadProducts()})
</script>
<style scoped>.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>
