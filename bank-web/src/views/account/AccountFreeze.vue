<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">账户冻结</h2><p class="page-desc">对指定负债账户进行冻结操作，冻结后账户将无法交易</p></div></div>
    <el-row :gutter="20">
      <el-col :span="10"><el-card><h3 style="margin:0 0 16px;font-size:16px">冻结操作</h3>
        <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
          <el-form-item label="负债账号" prop="liabilityAccountNo"><el-input v-model="form.liabilityAccountNo" placeholder="输入要冻结的负债账号"/></el-form-item>
          <el-form-item label="冻结原因"><el-input v-model="form.reason" type="textarea" :rows="3" placeholder="输入冻结原因（可选）"/></el-form-item>
          <el-form-item><el-button type="danger" size="large" style="width:100%" @click="handleFreeze" :loading="loading">确认冻结</el-button></el-form-item>
        </el-form>
      </el-card></el-col>
      <el-col :span="14"><el-card><h3 style="margin:0 0 16px;font-size:16px">当前正常账户</h3>
        <el-table :data="normalAccounts" size="small" max-height="400" @row-click="(r)=>{form.liabilityAccountNo=r.liabilityAccountNo}">
          <el-table-column prop="liabilityAccountNo" label="负债账号" width="150"/>
          <el-table-column prop="customerAccountNo" label="客户账号" width="170"/>
          <el-table-column prop="balance" label="余额" width="120" align="right"><template #default="{ row: r }">{{Number(r.balance).toLocaleString('zh-CN',{minFractionDigits:2})}}</template></el-table-column>
          <el-table-column label="操作" width="80"><template #default="{ row: r }"><el-button size="small" type="danger" @click.stop="quickFreeze(r)">冻结</el-button></template></el-table-column>
        </el-table>
      </el-card></el-col>
    </el-row>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage } from 'element-plus'; import { getLiabilityAccountList, freezeAccount } from '../../api/account'
var formRef=ref(),loading=ref(false),form=reactive({liabilityAccountNo:'',reason:''}),normalAccounts=ref([])
var rules={liabilityAccountNo:[{required:true,message:'请输入负债账号',trigger:'blur'}]}
var loadNormal=async()=>{try{var r=await getLiabilityAccountList();normalAccounts.value=(r.data||[]).filter(a=>a.status===0)}catch(e){}}
var handleFreeze=async()=>{await formRef.value.validate(async v=>{if(v){loading.value=true;try{await freezeAccount({liabilityAccountNo:form.liabilityAccountNo,reason:form.reason||'柜员冻结'});ElMessage.success('冻结成功');form.liabilityAccountNo='';form.reason='';loadNormal()}catch(e){}finally{loading.value=false}}})}
var quickFreeze=async(r)=>{try{await freezeAccount({liabilityAccountNo:r.liabilityAccountNo,reason:'柜员冻结'});ElMessage.success('冻结成功');loadNormal()}catch(e){}}
onMounted(()=>{loadNormal()})
</script>
<style scoped>.page-header{margin-bottom:24px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>
