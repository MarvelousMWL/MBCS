<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">开销户</h2><p class="page-desc">开立客户账户、开立负债账户、账户销户</p></div></div>
    <el-row :gutter="20"><el-col :span="8"><el-card><h3 style="margin:0 0 16px;font-size:16px">开立客户账户</h3>
      <el-form ref="cFormRef" :model="cForm" :rules="cRules" label-width="80px">
        <el-form-item label="客户号" prop="customerNo"><el-input v-model="cForm.customerNo"/></el-form-item>
        <el-form-item label="账户类型" prop="accountType"><el-select v-model="cForm.accountType" style="width:100%"><el-option label="个人" :value="0"/><el-option label="企业" :value="1"/></el-select></el-form-item>
        <el-form-item><el-button type="primary" style="width:100%" @click="openCustomer">开立</el-button></el-form-item>
      </el-form>
    </el-card></el-col>
    <el-col :span="8"><el-card><h3 style="margin:0 0 16px;font-size:16px">开立负债账户</h3>
      <el-form ref="lFormRef" :model="lForm" :rules="lRules" label-width="100px">
        <el-form-item label="客户账号" prop="customerAccountNo"><el-input v-model="lForm.customerAccountNo" @blur="loadSubs" placeholder="输入客户账号"/></el-form-item>
        <el-form-item label="产品类型" prop="accountType"><el-select v-model="lForm.accountType" @change="loadSubs" style="width:100%"><el-option label="活期存款" :value="0"/><el-option label="定期存款" :value="1"/></el-select></el-form-item>
        <el-form-item label="子账户" v-if="subs.length>0"><el-select v-model="selSub" placeholder="选择已有或留空新建" clearable style="width:100%"><el-option v-for="s in subs" :key="s.id" :label="'序号'+s.subAccountSeq+' | '+s.liabilityAccountNo" :value="s"/></el-select></el-form-item>
        <el-form-item label="新建子账户" v-else-if="lForm.customerAccountNo"><span style="color:#909399">将自动创建新子账户</span></el-form-item>
        <el-form-item><el-button type="primary" style="width:100%" @click="openLiability">开立</el-button></el-form-item>
      </el-form>
    </el-card></el-col>
    <el-col :span="8"><el-card><h3 style="margin:0 0 16px;font-size:16px">账户销户</h3>
      <el-form ref="closeFormRef" :model="closeForm" label-width="80px">
        <el-form-item label="负债账号" prop="liabilityAccountNo"><el-input v-model="closeForm.liabilityAccountNo" placeholder="输入要销户的负债账号"/></el-form-item>
        <el-form-item><el-button type="danger" style="width:100%" @click="closeAccount">销户</el-button></el-form-item>
      </el-form>
      <el-alert type="info" :closable="false" show-icon style="margin-top:12px"><template #title>仅余额为零且状态正常的账户可销户</template></el-alert>
    </el-card></el-col></el-row>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'
import { openCustomerAccount, openLiabilityAccount, closeLiabAcct, getCustomerSubAccountByType } from '../../api/account'
var cFormRef=ref(),cForm=reactive({customerNo:'',accountType:0}),cRules={customerNo:[{required:true,message:'请输入客户号',trigger:'blur'}],accountType:[{required:true,message:'请选择类型',trigger:'change'}]}
var lFormRef=ref(),lForm=reactive({customerAccountNo:'',accountType:0}),lRules={customerAccountNo:[{required:true,message:'请输入客户账号',trigger:'blur'}],accountType:[{required:true,message:'请选择产品类型',trigger:'change'}]}
var subs=ref([]),selSub=ref(null),closeForm=reactive({liabilityAccountNo:''}),closeFormRef=ref()
var typeToSubType={0:'DEMAND',1:'TIME'}
var loadSubs=async()=>{if(!lForm.customerAccountNo||lForm.accountType===null){subs.value=[];return}try{var r=await getCustomerSubAccountByType(lForm.customerAccountNo,typeToSubType[lForm.accountType]);subs.value=(r.data||[]).filter(i=>i.status===0)}catch(e){subs.value=[]}}
var openCustomer=async()=>{await cFormRef.value.validate(async v=>{if(v){try{await openCustomerAccount(cForm);ElMessage.success('开户成功');cForm.customerNo=''}catch(e){console.error(e)}}})}
var openLiability=async()=>{await lFormRef.value.validate(async v=>{if(v){try{var d={...lForm};if(selSub.value)d.subAccountSeq=selSub.value.subAccountSeq;await openLiabilityAccount(d);ElMessage.success('开户成功');lForm.customerAccountNo='';lForm.accountType=0;selSub.value=null;subs.value=[]}catch(e){console.error(e)}}})}
var closeAccount=async()=>{try{await ElMessageBox.confirm('确定要销户吗?','提示',{type:'warning'});await closeLiabAcct({liabilityAccountNo:closeForm.liabilityAccountNo});ElMessage.success('销户成功');closeForm.liabilityAccountNo=''}catch(e){if(e!=='cancel')console.error(e)}}
</script>
<style scoped>
.page-header{margin-bottom:24px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
.el-card{min-height:280px}
</style>
