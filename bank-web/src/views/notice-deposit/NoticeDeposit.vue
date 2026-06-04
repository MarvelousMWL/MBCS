<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">通知存款管理</h2><p class="page-desc">通知存款开户、预约及支取操作</p></div></div>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="开户" name="open">
        <el-form ref="openFormRef" :model="openForm" :rules="openRules" label-width="120px" style="max-width:560px">
          <el-form-item label="客户账号" prop="customerAccountNo"><el-input v-model="openForm.customerAccountNo" placeholder="输入客户账号"/></el-form-item>
          <el-form-item label="存款金额" prop="amount"><el-input-number v-model="openForm.amount" :min="0.01" :precision="2" style="width:200px"/></el-form-item>
          <el-form-item label="通知天数" prop="noticeDays"><el-select v-model="openForm.noticeDays" placeholder="选择通知天数" style="width:200px"><el-option label="1天" :value="1"/><el-option label="7天" :value="7"/></el-select></el-form-item>
          <el-form-item><el-button type="primary" @click="handleOpen">确认开户</el-button></el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="预约支取" name="appoint">
        <el-form ref="appointFormRef" :model="appointForm" :rules="appointRules" label-width="120px" style="max-width:560px">
          <el-form-item label="通知存款账号" prop="accountNo"><el-input v-model="appointForm.accountNo" placeholder="输入通知存款账号"/></el-form-item>
          <el-form-item label="预约支取日期" prop="appointDate"><el-date-picker v-model="appointForm.appointDate" type="date" placeholder="选择日期" style="width:200px" value-format="YYYY-MM-DD"/></el-form-item>
          <el-form-item><el-button type="primary" @click="handleAppoint">确认预约</el-button></el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="支取" name="withdraw">
        <el-form ref="withdrawFormRef" :model="withdrawForm" :rules="withdrawRules" label-width="120px" style="max-width:560px">
          <el-form-item label="通知存款账号" prop="accountNo"><el-input v-model="withdrawForm.accountNo" placeholder="输入通知存款账号"/></el-form-item>
          <el-form-item label="支取金额" prop="amount"><el-input-number v-model="withdrawForm.amount" :min="0.01" :precision="2" style="width:200px"/></el-form-item>
          <el-form-item><el-button type="primary" @click="handleWithdraw">确认支取</el-button></el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
    <el-card style="margin-top:20px">
      <template #header><span style="font-weight:600">账户列表</span></template>
      <div class="search-bar">
        <el-input v-model="query.accountNo" placeholder="通知存款账号" clearable style="width:180px"/>
        <el-button type="primary" @click="doSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
      <el-table :data="accountList" class="data-table" style="margin-top:12px">
        <el-table-column prop="accountNo" label="账号" width="180"/>
        <el-table-column prop="customerAccountNo" label="客户账号" width="150"/>
        <el-table-column label="余额" width="130" align="right"><template #default="{ row: r }">{{Number(r.balance).toLocaleString('zh-CN',{minFractionDigits:2})}}</template></el-table-column>
        <el-table-column label="通知天数" width="100"><template #default="{ row: r }">{{r.noticeDays}}天</template></el-table-column>
        <el-table-column prop="appointDate" label="预约支取日" width="140"/>
        <el-table-column label="状态" width="100"><template #default="{ row: r }"><el-tag size="small" :type="r.status===0?'success':r.status==='APPOINTED'?'warning':'info'" effect="light" style="border:0">{{{'NORMAL':'正常','APPOINTED':'已预约','WITHDRAWN':'已支取','CLOSED':'已销户'}[r.status]||r.status}}</el-tag></template></el-table-column>
        <el-table-column prop="openDate" label="开户日期" width="160"/>
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage } from 'element-plus'
import { openNoticeDeposit, appointWithdraw, withdrawNoticeDeposit, getNoticeDepositList } from '../../api/noticeDeposit'
var activeTab=ref('open'),accountList=ref([])
var openFormRef=ref(),appointFormRef=ref(),withdrawFormRef=ref()
var openForm=reactive({customerAccountNo:'',amount:0,noticeDays:7})
var openRules={customerAccountNo:[{required:true,message:'请输入客户账号',trigger:'blur'}],amount:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('金额必须大于0'))}},trigger:'blur'}],noticeDays:[{required:true,message:'请选择通知天数',trigger:'change'}]}
var appointForm=reactive({accountNo:'',appointDate:''})
var appointRules={accountNo:[{required:true,message:'请输入通知存款账号',trigger:'blur'}],appointDate:[{required:true,message:'请选择预约支取日期',trigger:'change'}]}
var withdrawForm=reactive({accountNo:'',amount:0})
var withdrawRules={accountNo:[{required:true,message:'请输入通知存款账号',trigger:'blur'}],amount:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('金额必须大于0'))}},trigger:'blur'}]}
var query=reactive({accountNo:''})
var loadAccounts=async(p)=>{try{var r=await getNoticeDepositList(p||{});accountList.value=r.data?.records||r.data||[]}catch(e){console.error(e)}}
var doSearch=()=>{var p={};if(query.accountNo)p.accountNo=query.accountNo;loadAccounts(p)}
var resetSearch=()=>{query.accountNo='';loadAccounts()}
var handleOpen=async()=>{await openFormRef.value.validate(async v=>{if(v){try{var r=await openNoticeDeposit(openForm);ElMessage.success('开户成功! 账号:'+(r.data?.accountNo||''));openForm.customerAccountNo='';openForm.amount=0;openForm.noticeDays=7;loadAccounts()}catch(e){console.error(e)}}})}
var handleAppoint=async()=>{await appointFormRef.value.validate(async v=>{if(v){try{var r=await appointWithdraw(appointForm);ElMessage.success('预约成功!');appointForm.accountNo='';appointForm.appointDate='';loadAccounts()}catch(e){console.error(e)}}})}
var handleWithdraw=async()=>{await withdrawFormRef.value.validate(async v=>{if(v){try{var r=await withdrawNoticeDeposit(withdrawForm);ElMessage.success('支取成功!');withdrawForm.accountNo='';withdrawForm.amount=0;loadAccounts()}catch(e){console.error(e)}}})}
onMounted(()=>{loadAccounts()})
</script>
<style scoped>
.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
.search-bar{display:flex;gap:10px;align-items:center}
</style>
