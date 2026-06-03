<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">账户解冻</h2><p class="page-desc">对已冻结的负债账户进行解冻操作，恢复交易能力</p></div></div>
    <el-card><h3 style="margin:0 0 16px;font-size:16px">当前冻结账户</h3>
      <el-table :data="frozenAccounts" @row-click="(r)=>{form.liabilityAccountNo=r.liabilityAccountNo}">
        <el-table-column prop="liabilityAccountNo" label="负债账号" width="150"/>
        <el-table-column prop="customerAccountNo" label="客户账号" width="170"/>
        <el-table-column prop="balance" label="余额" width="120" align="right"><template #default="{ row: r }">{{Number(r.balance).toLocaleString('zh-CN',{minFractionDigits:2})}}</template></el-table-column>
        <el-table-column label="操作" width="100"><template #default="{ row: r }"><el-button size="small" @click="quickUnfreeze(r)">解冻</el-button></template></el-table-column>
      </el-table>
      <el-empty v-if="frozenAccounts.length===0" description="暂无冻结账户" :image-size="80" style="padding:40px 0"/>
    </el-card>
    <el-card style="margin-top:16px;max-width:520px"><h3 style="margin:0 0 16px;font-size:16px">手动输入账号解冻</h3>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="负债账号" prop="liabilityAccountNo"><el-input v-model="form.liabilityAccountNo" placeholder="输入要解冻的负债账号"/></el-form-item>
        <el-form-item label="解冻原因"><el-input v-model="form.reason" type="textarea" :rows="2" placeholder="可选"/></el-form-item>
        <el-form-item><el-button type="primary" @click="handleUnfreeze" :loading="loading">确认解冻</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage } from 'element-plus'; import { getLiabilityAccountList, unfreezeAccount } from '../../api/account'
var formRef=ref(),loading=ref(false),form=reactive({liabilityAccountNo:'',reason:''}),frozenAccounts=ref([])
var rules={liabilityAccountNo:[{required:true,message:'请输入负债账号',trigger:'blur'}]}
var loadFrozen=async()=>{try{var r=await getLiabilityAccountList();frozenAccounts.value=(r.data||[]).filter(a=>a.status==='FROZEN')}catch(e){}}
var handleUnfreeze=async()=>{await formRef.value.validate(async v=>{if(v){loading.value=true;try{await unfreezeAccount({liabilityAccountNo:form.liabilityAccountNo,reason:form.reason||'柜员解冻'});ElMessage.success('解冻成功');form.liabilityAccountNo='';form.reason='';loadFrozen()}catch(e){}finally{loading.value=false}}})}
var quickUnfreeze=async(r)=>{try{await unfreezeAccount({liabilityAccountNo:r.liabilityAccountNo,reason:'柜员解冻'});ElMessage.success('解冻成功');loadFrozen()}catch(e){}}
onMounted(()=>{loadFrozen()})
</script>
<style scoped>.page-header{margin-bottom:24px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>
