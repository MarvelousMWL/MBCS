<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">账户限制</h2><p class="page-desc">账户冻结、解冻及限制状态管理</p></div></div>
    <el-row :gutter="20" class="stats-row">
      <el-col :span="8"><div class="stat-mini"><span class="stat-mini-label">正常账户</span><span class="stat-mini-value" style="color:#38a169">{{normalCount}}</span></div></el-col>
      <el-col :span="8"><div class="stat-mini"><span class="stat-mini-label">冻结账户</span><span class="stat-mini-value" style="color:#e53e3e">{{frozenCount}}</span></div></el-col>
      <el-col :span="8"><div class="stat-mini"><span class="stat-mini-label">已销户</span><span class="stat-mini-value" style="color:#909399">{{closedCount}}</span></div></el-col>
    </el-row>
    <el-tabs v-model="activeTab" class="tabs">
      <el-tab-pane label="全部账户" name="all">
        <el-table :data="allAccounts" class="data-table">
          <el-table-column prop="liabilityAccountNo" label="负债账号" width="150"/>
          <el-table-column prop="customerAccountNo" label="客户账号" width="170"/>
          <el-table-column label="状态" width="110"><template #default="{ row: r }"><el-tag size="small" :type="r.status==='NORMAL'?'success':r.status==='FROZEN'?'danger':'info'" effect="light" style="border:0">{{{NORMAL:'正常',FROZEN:'冻结',CLOSED:'已销户',STOPPED:'停用'}[r.status]||r.status}}</el-tag></template></el-table-column>
          <el-table-column prop="balance" label="余额" width="130" align="right"><template #default="{ row: r }"><span style="font-family:'Courier New',monospace;font-weight:600">{{Number(r.balance).toLocaleString('zh-CN',{minFractionDigits:2})}}</span></template></el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row: r }">
              <el-button v-if="r.status==='NORMAL'" size="small" type="danger" @click="handleFreeze(r)">冻结</el-button>
              <el-button v-if="r.status==='FROZEN'" size="small" @click="handleUnfreeze(r)">解冻</el-button>
              <el-button v-if="r.status==='NORMAL'" size="small" type="danger" plain @click="handleClose(r)">销户</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="仅冻结账户" name="frozen">
        <el-table :data="frozenAccounts" class="data-table">
          <el-table-column prop="liabilityAccountNo" label="负债账号" width="150"/>
          <el-table-column prop="customerAccountNo" label="客户账号" width="170"/>
          <el-table-column prop="balance" label="余额" width="130" align="right"><template #default="{ row: r }"><span style="font-family:'Courier New',monospace;font-weight:600">{{Number(r.balance).toLocaleString('zh-CN',{minFractionDigits:2})}}</span></template></el-table-column>
          <el-table-column label="操作" width="120"><template #default="{ row: r }"><el-button size="small" @click="handleUnfreeze(r)">解冻</el-button></template></el-table-column>
        </el-table>
        <el-empty v-if="frozenAccounts.length===0" description="暂无冻结账户" :image-size="80"/>
      </el-tab-pane>
    </el-tabs>
    <el-dialog v-model="freezeDlg" title="冻结账户" width="420px">
      <el-form :model="freezeForm" label-width="80px">
        <el-form-item label="负债账号"><el-input :model-value="freezeForm.liabilityAccountNo" disabled/></el-form-item>
        <el-form-item label="冻结原因"><el-input v-model="freezeForm.reason" type="textarea" :rows="3" placeholder="输入冻结原因"/></el-form-item>
      </el-form>
      <template #footer><el-button @click="freezeDlg=false">取消</el-button><el-button type="danger" @click="confirmFreeze">确认冻结</el-button></template>
    </el-dialog>
    <el-dialog v-model="unfreezeDlg" title="解冻账户" width="420px">
      <el-form :model="unfreezeForm" label-width="80px">
        <el-form-item label="负债账号"><el-input :model-value="unfreezeForm.liabilityAccountNo" disabled/></el-form-item>
        <el-form-item label="解冻原因"><el-input v-model="unfreezeForm.reason" type="textarea" :rows="3" placeholder="输入解冻原因"/></el-form-item>
      </el-form>
      <template #footer><el-button @click="unfreezeDlg=false">取消</el-button><el-button type="primary" @click="confirmUnfreeze">确认解冻</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, computed, onMounted } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'
import { getLiabilityAccountList, freezeAccount, unfreezeAccount, closeLiabAcct } from '../../api/account'
var allAccounts=ref([]),activeTab=ref('all')
var normalCount=computed(()=>allAccounts.value.filter(a=>a.status==='NORMAL').length)
var frozenCount=computed(()=>allAccounts.value.filter(a=>a.status==='FROZEN').length)
var closedCount=computed(()=>allAccounts.value.filter(a=>a.status==='CLOSED').length)
var frozenAccounts=computed(()=>allAccounts.value.filter(a=>a.status==='FROZEN'))
var freezeDlg=ref(false),freezeForm=reactive({liabilityAccountNo:'',reason:''})
var unfreezeDlg=ref(false),unfreezeForm=reactive({liabilityAccountNo:'',reason:''})
var loadData=async()=>{try{var r=await getLiabilityAccountList();allAccounts.value=r.data||[]}catch(e){console.error(e)}}
var handleFreeze=(r)=>{freezeForm.liabilityAccountNo=r.liabilityAccountNo;freezeForm.reason='';freezeDlg.value=true}
var confirmFreeze=async()=>{try{await freezeAccount({liabilityAccountNo:freezeForm.liabilityAccountNo,reason:freezeForm.reason||'柜员冻结'});ElMessage.success('已冻结');freezeDlg.value=false;loadData()}catch(e){console.error(e)}}
var handleUnfreeze=(r)=>{unfreezeForm.liabilityAccountNo=r.liabilityAccountNo;unfreezeForm.reason='';unfreezeDlg.value=true}
var confirmUnfreeze=async()=>{try{await unfreezeAccount({liabilityAccountNo:unfreezeForm.liabilityAccountNo,reason:unfreezeForm.reason||'柜员解冻'});ElMessage.success('已解冻');unfreezeDlg.value=false;loadData()}catch(e){console.error(e)}}
var handleClose=async(r)=>{try{await ElMessageBox.confirm('确定销户?','提示',{type:'warning'});await closeLiabAcct({liabilityAccountNo:r.liabilityAccountNo});ElMessage.success('销户成功');loadData()}catch(e){if(e!=='cancel')console.error(e)}}
onMounted(()=>{loadData()})
</script>
<style scoped>
.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
.stats-row{margin-bottom:20px}.stat-mini{background:#fff;border-radius:10px;padding:16px;display:flex;flex-direction:column;gap:4px;border:1px solid #edf2f7}
.stat-mini-label{font-size:13px;color:var(--text-secondary)}.stat-mini-value{font-size:22px;font-weight:700;color:var(--text-primary);line-height:1.2}
.tabs{margin-top:8px}
</style>
