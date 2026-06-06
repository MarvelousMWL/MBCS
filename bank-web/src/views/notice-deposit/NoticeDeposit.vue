<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">通知存款管理</h2><p class="page-desc">通知存款开户、预约及支取操作</p></div></div>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="开户" name="open">
        <el-form ref="openFormRef" :model="openForm" :rules="openRules" label-width="130px" style="max-width:560px">
          <el-form-item label="客户账号" prop="customerAccountNo"><el-input v-model="openForm.customerAccountNo" placeholder="输入客户账号"/></el-form-item>
          <el-form-item label="活期结算账号" prop="currentAccountNo"><el-input v-model="openForm.currentAccountNo" placeholder="输入活期结算账号"/></el-form-item>
          <el-form-item label="通知类型" prop="noticeType"><el-select v-model="openForm.noticeType" style="width:200px"><el-option label="1天通知" value="DAY1"/><el-option label="7天通知" value="DAY7"/></el-select></el-form-item>
          <el-form-item label="存款金额" prop="amount"><el-input-number v-model="openForm.amount" :min="0.01" :precision="2" style="width:200px"/></el-form-item>
          <el-form-item label="年利率(%)" prop="interestRate"><el-input-number v-model="openForm.interestRate" :min="0" :precision="4" :step="0.01" style="width:200px"/></el-form-item>
          <el-form-item><el-button type="primary" @click="handleOpen">确认开户</el-button></el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="预约支取" name="appoint">
        <el-form ref="appointFormRef" :model="appointForm" :rules="appointRules" label-width="130px" style="max-width:560px">
          <el-form-item label="通知存款账号" prop="noticeDepositAccountNo"><el-input v-model="appointForm.noticeDepositAccountNo" placeholder="输入通知存款账号"/></el-form-item>
          <el-form-item label="预约金额" prop="bookingAmount"><el-input-number v-model="appointForm.bookingAmount" :min="0.01" :precision="2" style="width:200px"/></el-form-item>
          <el-form-item label="预约支取日期" prop="bookingDate"><el-date-picker v-model="appointForm.bookingDate" type="date" placeholder="选择日期" style="width:200px" value-format="YYYY-MM-DD"/></el-form-item>
          <el-form-item><el-button type="primary" @click="handleAppoint">确认预约</el-button></el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="支取" name="withdraw">
        <el-form ref="withdrawFormRef" :model="withdrawForm" :rules="withdrawRules" label-width="130px" style="max-width:560px">
          <el-form-item label="通知存款账号" prop="noticeDepositAccountNo"><el-input v-model="withdrawForm.noticeDepositAccountNo" placeholder="输入通知存款账号"/></el-form-item>
          <el-form-item><el-button type="primary" @click="handleWithdraw">确认支取</el-button></el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'; import { ElMessage } from 'element-plus'
import { openNoticeDeposit, appointWithdraw, withdrawNoticeDeposit } from '../../api/noticeDeposit'
var activeTab=ref('open')
var openFormRef=ref(),appointFormRef=ref(),withdrawFormRef=ref()
var openForm=reactive({customerAccountNo:'',currentAccountNo:'',noticeType:'DAY7',amount:0,interestRate:2.5})
var openRules={customerAccountNo:[{required:true,message:'请输入客户账号',trigger:'blur'}],currentAccountNo:[{required:true,message:'请输入活期结算账号',trigger:'blur'}],noticeType:[{required:true,message:'请选择通知类型',trigger:'change'}],amount:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('金额必须大于0'))}},trigger:'blur'}],interestRate:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('利率必须大于0'))}},trigger:'blur'}]}
var appointForm=reactive({noticeDepositAccountNo:'',bookingAmount:0,bookingDate:''})
var appointRules={noticeDepositAccountNo:[{required:true,message:'请输入通知存款账号',trigger:'blur'}],bookingAmount:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('预约金额必须大于0'))}},trigger:'blur'}],bookingDate:[{required:true,message:'请选择预约日期',trigger:'change'}]}
var withdrawForm=reactive({noticeDepositAccountNo:''})
var withdrawRules={noticeDepositAccountNo:[{required:true,message:'请输入通知存款账号',trigger:'blur'}]}
var handleOpen=async()=>{await openFormRef.value.validate(async v=>{if(v){try{var r=await openNoticeDeposit(openForm);ElMessage.success('开户成功! 账号:'+(r.data?.noticeDepositAccountNo||''));openForm.customerAccountNo='';openForm.currentAccountNo='';openForm.amount=0}catch(e){console.error(e)}}})}
var handleAppoint=async()=>{await appointFormRef.value.validate(async v=>{if(v){try{var r=await appointWithdraw(appointForm);ElMessage.success('预约成功!');appointForm.noticeDepositAccountNo='';appointForm.bookingAmount=0;appointForm.bookingDate=''}catch(e){console.error(e)}}})}
var handleWithdraw=async()=>{await withdrawFormRef.value.validate(async v=>{if(v){try{var r=await withdrawNoticeDeposit(withdrawForm);ElMessage.success('支取成功!');withdrawForm.noticeDepositAccountNo=''}catch(e){console.error(e)}}})}
</script>
<style scoped>
.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
</style>
