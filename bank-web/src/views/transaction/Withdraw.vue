<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">取款交易</h2><p class="page-desc">从指定负债账户支取资金</p></div></div>
    <el-card style="max-width:560px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="客户账号" prop="customerAccountNo"><el-input v-model="form.customerAccountNo" @blur="loadSubs" placeholder="输入客户账号查询子账户"/></el-form-item>
        <el-form-item label="子账户" v-if="subList.length>0"><el-select v-model="form.selectedSub" placeholder="选择子账户" @change="onSubChange" style="width:100%"><el-option v-for="s in subList" :key="s.liabilityAccountNo" :label="'序号'+s.subAccountSeq+' | '+s.liabilityAccountNo" :value="s"/></el-select></el-form-item>
        <el-form-item label="负债账号" v-if="form.liabilityAccountNo"><el-input :model-value="form.liabilityAccountNo" disabled/></el-form-item>
        <el-form-item label="取款金额" prop="amount"><el-input-number v-model="form.amount" :min="0.01" :precision="2" style="width:200px"/></el-form-item>
        <el-form-item><el-button type="primary" @click="handleSubmit">确认取款</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'; import { ElMessage } from 'element-plus'
import { withdraw } from '../../api/transaction'; import { getCustomerSubAccountList } from '../../api/account'
var formRef=ref(),subList=ref([])
var form=reactive({customerAccountNo:'',selectedSub:null,liabilityAccountNo:'',amount:0})
var rules={customerAccountNo:[{required:true,message:'请输入客户账号',trigger:'blur'}],amount:[{required:true,validator:(r,v,c)=>{if(v>0){c()}else{c(new Error('金额必须大于0'))}},trigger:'blur'}]}
var loadSubs=async()=>{if(!form.customerAccountNo){subList.value=[];return}try{var r=await getCustomerSubAccountList(form.customerAccountNo);subList.value=(r.data||[]).filter(s=>s.status===0)}catch(e){subList.value=[]}}
var onSubChange=(v)=>{form.liabilityAccountNo=v?v.liabilityAccountNo:''}
var handleSubmit=async()=>{await formRef.value.validate(async v=>{if(v){try{var r=await withdraw({liabilityAccountNo:form.liabilityAccountNo,amount:form.amount});ElMessage.success('取款成功! 流水号:'+(r.data?.transactionNo||''));form.customerAccountNo='';form.selectedSub=null;form.liabilityAccountNo='';form.amount=0;subList.value=[]}catch(e){console.error(e)}}})}
</script>
<style scoped>.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>