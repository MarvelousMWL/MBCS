<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">账户销户</h2><p class="page-desc">对余额为零且状态正常的负债账户进行销户处理</p></div></div>
    <el-row :gutter="20">
      <el-col :span="12"><el-card><h3 style="margin:0 0 16px;font-size:16px">销户操作</h3>
        <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="负债账号" prop="liabilityAccountNo"><el-input v-model="form.liabilityAccountNo" placeholder="输入要销户的负债账号"/></el-form-item>
          <el-form-item><el-button type="danger" size="large" style="width:100%" @click="handleClose" :loading="loading">确认销户</el-button></el-form-item>
        </el-form>
        <el-alert type="warning" :closable="false" show-icon title="销户条件：账户余额为零 且 账户状态为正常" style="margin-top:12px"/>
      </el-card></el-col>
      <el-col :span="12"><el-card><h3 style="margin:0 0 16px;font-size:16px">最近销户记录</h3>
        <el-table :data="closedAccounts" size="small" max-height="300">
          <el-table-column prop="liabilityAccountNo" label="负债账号" width="140"/>
          <el-table-column prop="closeDate" label="销户日期" width="170"/>
        </el-table>
        <el-empty v-if="closedAccounts.length===0" description="暂无记录" :image-size="60"/>
      </el-card></el-col>
    </el-row>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'; import { getLiabilityAccountList, closeLiabAcct } from '../../api/account'
var formRef=ref(),loading=ref(false),form=reactive({liabilityAccountNo:''}),closedAccounts=ref([])
var rules={liabilityAccountNo:[{required:true,message:'请输入负债账号',trigger:'blur'}]}
var handleClose=async()=>{await formRef.value.validate(async v=>{if(v){loading.value=true;try{await ElMessageBox.confirm('确定要销户吗?','提示',{type:'warning'});await closeLiabAcct({liabilityAccountNo:form.liabilityAccountNo});ElMessage.success('销户成功');form.liabilityAccountNo='';loadClosed()}catch(e){if(e!=='cancel')console.error(e)}finally{loading.value=false}}})}
var loadClosed=async()=>{try{var r=await getLiabilityAccountList();closedAccounts.value=(r.data||[]).filter(a=>a.status===2).slice(0,10)}catch(e){}}
onMounted(()=>{loadClosed()})
</script>
<style scoped>.page-header{margin-bottom:24px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}</style>