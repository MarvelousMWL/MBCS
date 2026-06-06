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
          <el-form-item label="原转账流水号" prop="originalTransferNo"><el-input v-model="reverseForm.originalTransferNo" placeholder="输入原交易流水号"/></el-form-item>
          <el-form-item><el-button type="danger" @click="handleReverse">确认冲正</el-button></el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
    <el-card style="margin-top:20px">
      <template #header><span style="font-weight:600">转账记录</span></template>
      <el-empty v-if="recordList.length===0" description="暂无记录" :image-size="60"/>
      <el-table v-else :data="recordList" class="data-table" style="margin-top:12px">
        <el-table-column prop="transferNo" label="流水号" width="200"/>
        <el-table-column prop="fromAccountNo" label="转出账号" width="150"/>
        <el-table-column prop="toAccountNo" label="转入账号" width="150"/>
        <el-table-column label="金额" width="130" align="right"><template #default="{ row: r }">{{Number(r.amount).toLocaleString('zh-CN',{minFractionDigits:2})}}</template></el-table-column>
        <el-table-column prop="createdAt" label="交易时间" width="170"/>
        <el-table-column label="状态" width="100"><template #default="{ row: r }"><el-tag size="small" :type="r.status==='NORMAL'?'success':r.status==='REVERSED'?'danger':'warning'" effect="light" style="border:0">{{{'NORMAL':'成功','REVERSED':'已冲正','FAILED':'失败'}[r.status]||r.status}}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="100" fixed="right"><template #default="{ row: r }"><el-button v-if="r.status==='NORMAL'" text size="small" type="danger" @click="reverseSingle(r.transferNo)">冲正</el-button></template></el-table-column>
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
var reverseForm=reactive({originalTransferNo:''})
var reverseRules={originalTransferNo:[{required:true,message:'请输入原转账流水号',trigger:'blur'}]}
var loadRecords=async()=>{try{var r=await getTransferList();recordList.value=r.data?.records||r.data||[]}catch(e){recordList.value=[]}}
var handleTransfer=async()=>{await transferFormRef.value.validate(async v=>{if(v){try{var r=await transfer(transferForm);ElMessage.success('转账成功! 流水号:'+(r.data?.transferNo||''));transferForm.fromAccountNo='';transferForm.toAccountNo='';transferForm.amount=0;loadRecords()}catch(e){console.error(e)}}})}
var handleReverse=async()=>{await reverseFormRef.value.validate(async v=>{if(v){try{var r=await reverseTransfer(reverseForm);ElMessage.success('冲正成功!');reverseForm.originalTransferNo='';loadRecords()}catch(e){console.error(e)}}})}
var reverseSingle=async(transferNo)=>{try{var r=await reverseTransfer({originalTransferNo:transferNo});ElMessage.success('冲正成功!');loadRecords()}catch(e){console.error(e)}}
onMounted(()=>{loadRecords()})
</script>
<style scoped>
.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
</style>
