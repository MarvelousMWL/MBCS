<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">子账户管理</h2><p class="page-desc">查询客户子账户信息，开通新的子账户</p></div></div>
    <el-card v-if="!queryDone">
      <el-form ref="qFormRef" :model="qForm" :rules="qRules" label-width="100px" inline>
        <el-form-item label="客户账号" prop="customerAccountNo"><el-input v-model="qForm.customerAccountNo" placeholder="输入客户账号"/></el-form-item>
        <el-form-item><el-button type="primary" @click="doQuery">查询</el-button></el-form-item>
      </el-form>
    </el-card>
    <template v-else>
      <div style="margin-bottom:16px;display:flex;justify-content:space-between;align-items:center">
        <span style="font-weight:600;color:var(--text-primary)">客户账号: {{qForm.customerAccountNo}} - 共 {{subList.length}} 条</span>
        <div><el-button size="small" @click="showNewSub">开通新子账户</el-button><el-button size="small" text @click="resetQuery">返回查询</el-button></div>
      </div>
      <el-table :data="subList" border>
        <el-table-column prop="subAccountSeq" label="子账户序号" width="130"/>
        <el-table-column prop="liabilityAccountNo" label="负债账号" width="160"/>
        <el-table-column label="账户类型" width="90"><template #default="{ row: r }">{{r.accountType===0?'活期':'定期'}}</template></el-table-column>
        <el-table-column label="状态" width="90"><template #default="{ row: r }"><el-tag size="small" :type="r.status===0?'success':'info'" style="border:0">{{r.status===0?'正常':'已销户'}}</el-tag></template></el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180"/>
      </el-table>
    </template>
    <el-dialog v-model="newDlg" title="开通子账户" width="420px">
      <el-form ref="newFormRef" :model="newForm" :rules="newRules" label-width="100px">
        <el-form-item label="账户类型" prop="accountType"><el-select v-model="newForm.accountType" style="width:100%"><el-option label="活期 (DEMAND)" :value="0"/><el-option label="定期 (TIME)" :value="1"/></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="newDlg=false">取消</el-button><el-button type="primary" @click="doNewSub">确定</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'; import { ElMessage } from 'element-plus'
import { getCustomerSubAccountList, openSubAccount } from '../../api/account'
var qFormRef=ref(),qForm=reactive({customerAccountNo:''}),qRules={customerAccountNo:[{required:true,message:'请输入客户账号',trigger:'blur'}]}
var queryDone=ref(false),subList=ref([]),newDlg=ref(false),newFormRef=ref(),newForm=reactive({accountType:0}),newRules={accountType:[{required:true,message:'请选择',trigger:'change'}]}
var doQuery=async()=>{await qFormRef.value.validate(async v=>{if(v){try{var r=await getCustomerSubAccountList(qForm.customerAccountNo);subList.value=r.data||[];queryDone.value=true}catch(e){console.error(e);subList.value=[]}}})}
var resetQuery=()=>{queryDone.value=false;subList.value=[]}
var showNewSub=()=>{newForm.accountType=0;newDlg.value=true}
var doNewSub=async()=>{await newFormRef.value.validate(async v=>{if(v){try{await openSubAccount({customerAccountNo:qForm.customerAccountNo,accountType:newForm.accountType});ElMessage.success('开通成功');newDlg.value=false;var r=await getCustomerSubAccountList(qForm.customerAccountNo);subList.value=r.data||[]}catch(e){console.error(e)}}})}
</script>
<style scoped>
.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
</style>
