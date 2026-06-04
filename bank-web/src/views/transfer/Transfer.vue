<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">账户转账</h2><p class="page-desc">账户间转账及冲正操作</p></div></div>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="转账" name="transfer">
        <el-form ref="transferFormRef" :model="transferForm" :rules="transferRules" label-width="120px" style="max-width:560px">
          <el-form-item label="转出账号" prop="fromAccountNo"><el-input v-model="transferForm.fromAccountNo" placeholder="输入转出账号"/></el-form-item>
          <el-form-item label="转入账号" prop="toAccountNo"><el-input v-model="transferForm.toAccountNo" placeholder="输入转入账号"/></el-form-item>
          <el-form-item label="转账金额" prop="amount"><el-input-number v-model="transferForm.amount" :min="0.01" :precision="2" style="width:200px"/></el-form-item>
          <el-form-item><el-button type="primary" @click="handleTransfer">确认转账</el-button></el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="冲正" name="reverse">
        <el-form ref="reverseFormRef" :model="reverseForm" :rules="reverseRules" label-width="130px" style="max-width:560px">
          <el-form-item label="原转账流水号" prop="originalTransNo"><el-input v-model="reverseForm.originalTransNo" placeholder="输入原交易流水号"/></el-form-item>
          <el-form-item><el-button type="danger" @click="handleReverse">确认冲正</el-button></el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
    <el-card style="margin-top:20px">
      <template #header><span style="font-weight:600">转账记录</span></template>
      <div class="search-bar">
        <el-input v-model="query.fromAccountNo" placeholder="转出账号" clearable style="width:160px"/>
        <el-input v-model="query.toAccountNo" placeholder="转入账号" clearable style="width:160px"/>
        <el-button type="primary" @click="doSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
      <el-table :data="recordList" class="data-table" style="margin-top:12px">
        <el-table-column prop="transNo" label="流水号" width="200"/>
        <el-table-column prop="fromAccountNo" label="转出账号" width="150"/>
        <el-table-column prop="toAccountNo" label="转入账号" width="150"/>
        <el-table-column label="金额" width="130" align="right"><template #default="{ row: r }">{{Number(r.amount).toLocaleString('zh-CN',{minFractionDigits:2})}}</template></el-table-column>
        <el-table-column prop="transTime" label="交易时间" width="170"/>
        <el-table-column label="状态" width="100"><template #default="{ row: r }"><el-tag size="small" :type="r.status==='SUCCESS'?'success':r.status==='REVERSED'?'danger':'warning'" effect="light" style="border:0">{{{'SUCCESS':'成功','REVERSED':'已冲正','FAILED':'失败'}[r.status]||r.status}}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="100" fixed="right"><template #default="{ row: r }"><el-button v-if="r.status==='SUCCESS'" text size="small" type="danger" @click="reverseSingle(r.transNo)">冲正</el-button></template></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage } from 'element-plus'
import { transfer, reverseTransfer, getTransferList } from '../../api/transfer'
var activeTab=ref('transfer'),recordList=ref([])
var transferFormRef=ref(),reverseFormRef=ref()
var transferForm=reactive({fromAccountNo:'',toAccountNo:'',amount:0})
var transferRules={fromAccountNo:[{required:true,message:'请输入转出账号',trigger:'blur'}],toAccountNo:[{required:true,message:'请输入转入账号',trigger:'blur'}],amount:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('金额必须大于0'))}},trigger:'blur'}]}
var reverseForm=reactive({originalTransNo:''})
var reverseRules={originalTransNo:[{required:true,message:'请输入原转账流水号',trigger:'blur'}]}
var query=reactive({fromAccountNo:'',toAccountNo:''})
var loadRecords=async(p)=>{try{var r=await getTransferList(p||{});recordList.value=r.data?.records||r.data||[]}catch(e){console.error(e)}}
var doSearch=()=>{var p={};if(query.fromAccountNo)p.fromAccountNo=query.fromAccountNo;if(query.toAccountNo)p.toAccountNo=query.toAccountNo;loadRecords(p)}
var resetSearch=()=>{query.fromAccountNo='';query.toAccountNo='';loadRecords()}
var handleTransfer=async()=>{await transferFormRef.value.validate(async v=>{if(v){try{var r=await transfer(transferForm);ElMessage.success('转账成功! 流水号:'+(r.data?.transNo||''));transferForm.fromAccountNo='';transferForm.toAccountNo='';transferForm.amount=0;loadRecords()}catch(e){console.error(e)}}})}
var handleReverse=async()=>{await reverseFormRef.value.validate(async v=>{if(v){try{var r=await reverseTransfer(reverseForm);ElMessage.success('冲正成功!');reverseForm.originalTransNo='';loadRecords()}catch(e){console.error(e)}}})}
var reverseSingle=async(transNo)=>{try{var r=await reverseTransfer({originalTransNo:transNo});ElMessage.success('冲正成功!');loadRecords()}catch(e){console.error(e)}}
onMounted(()=>{loadRecords()})
</script>
<style scoped>
.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
.search-bar{display:flex;gap:10px;align-items:center}
</style>
