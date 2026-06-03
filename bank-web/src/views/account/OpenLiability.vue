<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">开立负债账户</h2><p class="page-desc">为已有客户账号开立具体的负债账户（活期/定期）</p></div></div>
    <el-card style="max-width:560px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="客户账号" prop="customerAccountNo"><el-input v-model="form.customerAccountNo" @blur="loadSubs" placeholder="输入客户账号"/></el-form-item>
        <el-form-item label="产品类型" prop="accountType"><el-select v-model="form.accountType" @change="loadSubs" style="width:100%"><el-option label="活期存款 (DEMAND)" value="DEMAND"/><el-option label="定期存款 (TIME)" value="TIME"/></el-select></el-form-item>
        <el-form-item label="子账户" v-if="subs.length>0"><el-select v-model="selSub" placeholder="选择已有子账户（留空自动创建）" clearable style="width:100%"><el-option v-for="s in subs" :key="s.id" :label="'序号'+s.subAccountSeq+' | '+s.liabilityAccountNo" :value="s"/></el-select></el-form-item>
        <el-form-item label="新建子账户" v-else-if="form.customerAccountNo"><span style="color:#909399">将自动创建新子账户</span></el-form-item>
        <el-form-item><el-button type="primary" size="large" style="width:100%" @click="handleSubmit" :loading="loading">开立账户</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'; import { ElMessage } from 'element-plus'; import { openLiabilityAccount, getCustomerSubAccountByType } from '../../api/account'
var formRef=ref(),loading=ref(false),form=reactive({customerAccountNo:'',accountType:'DEMAND'}),subs=ref([]),selSub=ref(null)
var rules={customerAccountNo:[{required:true,message:'请输入客户账号',trigger:'blur'}],accountType:[{required:true,message:'请选择产品类型',trigger:'change'}]}
var loadSubs=async()=>{if(!form.customerAccountNo||!form.accountType){subs.value=[];return}try{var r=await getCustomerSubAccountByType(form.customerAccountNo,form.accountType);subs.value=(r.data||[]).filter(i=>i.status==='NORMAL')}catch(e){subs.value=[]}}
var handleSubmit=async()=>{await formRef.value.validate(async v=>{if(v){loading.value=true;try{var d={...form};if(selSub.value)d.subAccountSeq=selSub.value.subAccountSeq;await openLiabilityAccount(d);ElMessage.success('负债账户开立成功');form.customerAccountNo='';form.accountType='DEMAND';selSub.value=null;subs.value=[]}catch(e){}finally{loading.value=false}}})}
</script>
<style scoped>.page-header{margin-bottom:24px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>