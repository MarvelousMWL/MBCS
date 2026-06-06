<template>
  <div class="account-page">
    <div class="page-header">
      <div>
        <h2 class="page-title">账户管理</h2>
        <p class="page-desc">负债账户查询、开立、冻结与销户管理</p>
      </div>
      <div class="page-actions">
        <el-button @click="showCustomerAccountDialog">
          <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
          开立客户账户
        </el-button>
        <el-button type="primary" @click="showLiabilityAccountDialog">
          <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
          开立负债账户
        </el-button>
        <el-button @click="showSubAccountDialog">
          <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>
          子账户管理
        </el-button>
      </div>
    </div>

    <!-- 统计概览 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6"><div class="stat-mini"><span class="stat-mini-label">总账户数</span><span class="stat-mini-value">{{ accountList.length }}</span></div></el-col>
      <el-col :span="6"><div class="stat-mini"><span class="stat-mini-label">正常</span><span class="stat-mini-value" style="color:#38a169">{{ normalCount }}</span></div></el-col>
      <el-col :span="6"><div class="stat-mini"><span class="stat-mini-label">活期</span><span class="stat-mini-value" style="color:#3182ce">{{ demandCount }}</span></div></el-col>
      <el-col :span="6"><div class="stat-mini"><span class="stat-mini-label">定期</span><span class="stat-mini-value" style="color:#dd6b20">{{ timeCount }}</span></div></el-col>
    </el-row>

    <!-- 账户列表 -->
    <el-table :data="accountList" class="data-table" @row-click="showDetail">
      <el-table-column prop="liabilityAccountNo" label="负债账号" width="150" />
      <el-table-column prop="customerAccountNo" label="客户账号" width="170" />
      <el-table-column prop="accountType" label="产品类型" width="100">
        <template #default="{row}"><el-tag size="small" :type="row.accountType===0?'primary':'warning'" effect="plain" style="border:0">{{$enumDict.LIABILITY_ACCOUNT_TYPE_STR[row.accountType]||row.accountType}}</el-tag></template>
      </el-table-column>
      <el-table-column prop="balance" label="账户余额" width="130" align="right">
        <template #default="{row}"><span class="balance-text">{{Number(row.balance).toLocaleString('zh-CN',{minimumFractionDigits:2})}}</span></template>
      </el-table-column>
      <el-table-column label="账户状态" width="160">
        <template #default="{row}">
          <div class="status-group">
            <el-tag size="small" :type="row.status===0?'success':row.status===3?'danger':row.status===2?'info':'warning'" effect="light" style="border:0">
              {{statusMap[row.status]||row.status}}
            </el-tag>
            <el-tag v-if="row.frozen" size="small" type="danger" effect="dark" style="border:0;margin-left:4px">冻结</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="openDate" label="开户日期" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{row}">
          <el-button text size="small" @click.stop="showDetail(row)">详情</el-button>
          <el-button v-if="row.status===0 && !row.frozen" text size="small" type="danger" @click.stop="handleFreeze(row)">冻结</el-button>
          <el-button v-if="row.frozen" text size="small" type="warning" @click.stop="handleUnfreeze(row)">解冻</el-button>
          <el-button v-if="row.status===0 && !row.frozen" text size="small" type="danger" @click.stop="closeLiabilityAccount(row)">销户</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 账户详情抽屉 -->
    <el-drawer v-model="detailVisible" :title="'账户详情 - ' + (detailData.liabilityAccountNo||'')" size="500px" destroy-on-close>
      <template v-if="detailData.liabilityAccountNo">
        <div class="detail-section">
          <div class="detail-section-title">基本信息</div>
          <el-descriptions :column="1" border size="small">
            <el-descriptions-item label="负债账号">{{detailData.liabilityAccountNo}}</el-descriptions-item>
            <el-descriptions-item label="客户账号">{{detailData.customerAccountNo}}</el-descriptions-item>
            <el-descriptions-item label="子账户序号">{{detailData.subAccountSeq||'-'}}</el-descriptions-item>
            <el-descriptions-item label="产品类型">{{$enumDict.LIABILITY_ACCOUNT_TYPE_STR[detailData.accountType]||detailData.accountType}}</el-descriptions-item>
            <el-descriptions-item label="账户余额"><span style="font-weight:700;font-size:16px;color:#3182ce">{{Number(detailData.balance).toLocaleString('zh-CN',{minimumFractionDigits:2})}}</span></el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="detail-section">
          <div class="detail-section-title">账户状态</div>
          <el-descriptions :column="1" border size="small">
            <el-descriptions-item label="账户状态">{{statusMap[detailData.status]||detailData.status}}</el-descriptions-item>
            <el-descriptions-item label="冻结标志">{{detailData.frozen?'已冻结':'正常'}}</el-descriptions-item>
            <el-descriptions-item label="开户日期">{{detailData.openDate}}</el-descriptions-item>
            <el-descriptions-item label="销户日期">{{detailData.closeDate||'-'}}</el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="detail-section">
          <div class="detail-section-title">利率信息</div>
          <el-descriptions :column="1" border size="small">
            <el-descriptions-item label="年利率">{{detailData.interestRate!=null?detailData.interestRate+'%':'-'}}</el-descriptions-item>
            <el-descriptions-item label="计息方式">{{detailData.accountType==='DEMAND'?'按季结息':'到期还本付息'}}</el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="detail-section">
          <div class="detail-section-title">操作</div>
          <div style="display:flex;gap:8px">
            <el-button v-if="detailData.status===0 && !detailData.frozen" type="danger" @click="handleFreeze(detailData)">冻结账户</el-button>
            <el-button v-if="detailData.frozen" type="warning" @click="handleUnfreeze(detailData)">解冻账户</el-button>
            <el-button v-if="detailData.status===0" @click="closeLiabilityAccount(detailData)">销户</el-button>
          </div>
        </div>
      </template>
    </el-drawer>

    <!-- 冻结对话框 -->
    <el-dialog v-model="freezeDialogVisible" title="冻结账户" width="450px">
      <el-form ref="freezeFormRef" :model="freezeForm" label-width="80px">
        <el-form-item label="负债账号"><el-input :model-value="freezeForm.liabilityAccountNo" disabled/></el-form-item>
        <el-form-item label="冻结原因" prop="reason"><el-input v-model="freezeForm.reason" type="textarea" :rows="3" placeholder="请输入冻结原因"/></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="freezeDialogVisible=false">取消</el-button>
        <el-button type="danger" @click="confirmFreeze">确认冻结</el-button>
      </template>
    </el-dialog>

    <!-- 解冻对话框 -->
    <el-dialog v-model="unfreezeDialogVisible" title="解冻账户" width="450px">
      <el-form ref="unfreezeFormRef" :model="unfreezeForm" label-width="80px">
        <el-form-item label="负债账号"><el-input :model-value="unfreezeForm.liabilityAccountNo" disabled/></el-form-item>
        <el-form-item label="解冻原因" prop="reason"><el-input v-model="unfreezeForm.reason" type="textarea" :rows="3" placeholder="请输入解冻原因"/></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="unfreezeDialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmUnfreeze">确认解冻</el-button>
      </template>
    </el-dialog>

    <!-- 开立客户账户对话框 -->
    <el-dialog v-model="customerDialogVisible" title="开立客户账户" width="500px">
      <el-form ref="customerFormRef" :model="customerForm" :rules="customerRules" label-width="100px">
        <el-form-item label="客户号" prop="customerNo"><el-input v-model="customerForm.customerNo" placeholder="输入客户号"/></el-form-item>
        <el-form-item label="账户类型" prop="accountType"><el-select v-model="customerForm.accountType"><el-option label="个人" value="PERSONAL"/><el-option label="企业" value="CORPORATE"/></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="customerDialogVisible=false">取消</el-button><el-button type="primary" @click="handleOpenCustomerAccount">确定</el-button></template>
    </el-dialog>

    <!-- 开立负债账户对话框 -->
    <el-dialog v-model="liabilityDialogVisible" title="开立负债账户" width="550px">
      <el-form ref="liabilityFormRef" :model="liabilityForm" :rules="liabilityRules" label-width="100px">
        <el-form-item label="客户账号" prop="customerAccountNo"><el-input v-model="liabilityForm.customerAccountNo" @blur="loadLiabilitySubAccounts" placeholder="输入客户账号"/></el-form-item>
        <el-form-item label="产品类型" prop="accountType">
          <el-select v-model="liabilityForm.accountType" @change="loadLiabilitySubAccounts" style="width:100%">
            <el-option label="活期存款 (DEMAND)" value="DEMAND"/>
            <el-option label="定期存款 (TIME)" value="TIME"/>
          </el-select>
          <div class="field-hint">选择产品类型后，系统将自动匹配对应的存款产品属性</div>
        </el-form-item>
        <el-form-item label="子账户" v-if="availableSubAccounts.length>0">
          <el-select v-model="selectedSubAccount" placeholder="选择已有子账户（留空自动创建）" clearable style="width:100%">
            <el-option v-for="item in availableSubAccounts" :key="item.id" :label="'序号:'+item.subAccountSeq+' | 负债账号:'+item.liabilityAccountNo" :value="item"/>
          </el-select>
        </el-form-item>
        <el-form-item label="新建子账户" v-else-if="liabilityForm.customerAccountNo"><span style="color:#909399">将自动创建新的子账户</span></el-form-item>
        <el-form-item label="子账户" v-else><span style="color:#909399">请输入客户账号</span></el-form-item>
      </el-form>
      <template #footer><el-button @click="liabilityDialogVisible=false">取消</el-button><el-button type="primary" @click="handleOpenLiabilityAccount">确定</el-button></template>
    </el-dialog>

    <!-- 子账户管理对话框 -->
    <el-dialog v-model="subDialogVisible" title="子账户管理" width="680px" @close="resetSubDialog">
      <template v-if="!subQueryDone">
        <el-form ref="subQueryFormRef" :model="subQueryForm" :rules="subQueryRules" label-width="100px">
          <el-form-item label="客户账号" prop="customerAccountNo"><el-input v-model="subQueryForm.customerAccountNo" placeholder="输入客户账号"/></el-form-item>
          <el-form-item><el-button type="primary" @click="querySubAccounts">查询</el-button></el-form-item>
        </el-form>
      </template>
      <template v-else>
        <div style="margin-bottom:16px;display:flex;justify-content:space-between;align-items:center">
          <span style="font-weight:600">子账户列表 ({{subAccountList.length}})</span>
          <el-button size="small" @click="showNewSubAccountDialog">开通新子账户</el-button>
        </div>
        <el-table :data="subAccountList" border size="small" max-height="400">
          <el-table-column prop="subAccountSeq" label="子账户序号" width="120"/>
          <el-table-column prop="liabilityAccountNo" label="负债账号" width="150"/>
          <el-table-column prop="accountType" label="类型" width="80"><template #default="{row}">{{$enumDict.LIABILITY_ACCOUNT_TYPE_STR[row.accountType]||row.accountType}}</template></el-table-column>
          <el-table-column prop="status" label="状态" width="80"><template #default="{row}"><el-tag size="small" :type="row.status===0?'success':'info'" style="border:0">{{$enumDict.LIABILITY_ACCOUNT_STATUS[row.status]||row.status}}</el-tag></template></el-table-column>
          <el-table-column prop="createdAt" label="创建时间" width="170"/>
        </el-table>
        <el-button style="margin-top:12px" @click="subQueryDone=false">返回查询</el-button>
      </template>
    </el-dialog>

    <!-- 开通子账户对话框 -->
    <el-dialog v-model="newSubDialogVisible" title="开通子账户" width="450px">
      <el-form ref="newSubFormRef" :model="newSubForm" :rules="newSubRules" label-width="100px">
        <el-form-item label="账户类型" prop="accountType"><el-select v-model="newSubForm.accountType" style="width:100%"><el-option label="活期 (DEMAND)" value="DEMAND"/><el-option label="定期 (TIME)" value="TIME"/></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="newSubDialogVisible=false">取消</el-button><el-button type="primary" @click="handleOpenSubAccount">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getLiabilityAccountList, getLiabilityAccountByNo, openCustomerAccount, openLiabilityAccount, closeLiabAcct, freezeAccount, unfreezeAccount, getCustomerSubAccountList, getCustomerSubAccountByType, openSubAccount } from '../../api/account'

var statusMap = { NORMAL:'正常', FROZEN:'冻结', CLOSED:'已销户', STOPPED:'停用' }
var accountList = ref([])
var normalCount = computed(()=>accountList.value.filter(a=>a.status===0).length)
var demandCount = computed(()=>accountList.value.filter(a=>a.accountType==='DEMAND').length)
var timeCount = computed(()=>accountList.value.filter(a=>a.accountType==='TIME').length)

// Detail drawer
var detailVisible = ref(false)
var detailData = ref({})

// Freeze
var freezeDialogVisible = ref(false)
var freezeForm = reactive({liabilityAccountNo:'',reason:''})
var freezeFormRef = ref()

// Unfreeze
var unfreezeDialogVisible = ref(false)
var unfreezeForm = reactive({liabilityAccountNo:'',reason:''})
var unfreezeFormRef = ref()

// Customer account open
var customerDialogVisible = ref(false)
var customerFormRef = ref()
var customerForm = reactive({customerNo:'',accountType:'PERSONAL'})
var customerRules = {customerNo:[{required:true,message:'请输入客户号',trigger:'blur'}],accountType:[{required:true,message:'请选择账户类型',trigger:'change'}]}

// Liability account open
var liabilityDialogVisible = ref(false)
var liabilityFormRef = ref()
var liabilityForm = reactive({customerAccountNo:'',accountType:'DEMAND'})
var liabilityRules = {customerAccountNo:[{required:true,message:'请输入客户账号',trigger:'blur'}],accountType:[{required:true,message:'请选择产品类型',trigger:'change'}]}
var availableSubAccounts = ref([])
var selectedSubAccount = ref(null)

// Sub account
var subDialogVisible = ref(false)
var subQueryDone = ref(false)
var subQueryFormRef = ref()
var subAccountList = ref([])
var subQueryForm = reactive({customerAccountNo:''})
var subQueryRules = {customerAccountNo:[{required:true,message:'请输入客户账号',trigger:'blur'}]}
var newSubDialogVisible = ref(false)
var newSubFormRef = ref()
var newSubForm = reactive({accountType:'DEMAND'})
var newSubRules = {accountType:[{required:true,message:'请选择账户类型',trigger:'change'}]}

var loadData = async () => {
  try {
    var res = await getLiabilityAccountList()
    accountList.value = (res.data||[]).map(a=>({...a,frozen:a.frozen||false}))
  } catch(e) { console.error(e) }
}

var loadLiabilitySubAccounts = async () => {
  if(!liabilityForm.customerAccountNo||liabilityForm.accountType===null||liabilityForm.accountType===undefined) { availableSubAccounts.value=[]; return }
  var typeStr = liabilityForm.accountType===0?'DEMAND':'TIME'
  try {
    var res = await getCustomerSubAccountByType(liabilityForm.customerAccountNo, typeStr)
    availableSubAccounts.value = (res.data||[]).filter(i=>i.status===0)
  } catch(e) { availableSubAccounts.value=[]; console.error(e) }
}

var showDetail = async (row) => {
  detailData.value = row
  detailVisible.value = true
}

var handleFreeze = (row) => {
  freezeForm.liabilityAccountNo = row.liabilityAccountNo
  freezeForm.reason = ''
  freezeDialogVisible.value = true
}

var confirmFreeze = async () => {
  try {
    await freezeAccount({liabilityAccountNo:freezeForm.liabilityAccountNo, reason:freezeForm.reason||'柜员冻结'})
    ElMessage.success('账户已冻结')
    freezeDialogVisible.value = false
    detailVisible.value = false
    loadData()
  } catch(e) { console.error(e) }
}

var handleUnfreeze = (row) => {
  unfreezeForm.liabilityAccountNo = row.liabilityAccountNo
  unfreezeForm.reason = ''
  unfreezeDialogVisible.value = true
}

var confirmUnfreeze = async () => {
  try {
    await unfreezeAccount({liabilityAccountNo:unfreezeForm.liabilityAccountNo, reason:unfreezeForm.reason||'柜员解冻'})
    ElMessage.success('账户已解冻')
    unfreezeDialogVisible.value = false
    detailVisible.value = false
    loadData()
  } catch(e) { console.error(e) }
}

var showCustomerAccountDialog = () => { customerForm.customerNo=''; customerForm.accountType=0; customerDialogVisible.value=true }
var handleOpenCustomerAccount = async () => { await customerFormRef.value.validate(async v=>{if(v){try{await openCustomerAccount(customerForm);ElMessage.success('开户成功');customerDialogVisible.value=false;loadData()}catch(e){console.error(e)}}}) }
var showLiabilityAccountDialog = () => { liabilityForm.customerAccountNo=''; liabilityForm.accountType=0; availableSubAccounts.value=[]; selectedSubAccount.value=null; liabilityDialogVisible.value=true }
var handleOpenLiabilityAccount = async () => { await liabilityFormRef.value.validate(async v=>{if(v){try{var data={...liabilityForm};if(selectedSubAccount.value)data.subAccountSeq=selectedSubAccount.value.subAccountSeq;await openLiabilityAccount(data);ElMessage.success('开户成功');liabilityDialogVisible.value=false;loadData()}catch(e){console.error(e)}}}) }
var closeLiabilityAccount = async (row) => { try{await ElMessageBox.confirm('确定要销户吗?','提示',{type:'warning'});await closeLiabAcct({liabilityAccountNo:row.liabilityAccountNo});ElMessage.success('销户成功');detailVisible.value=false;loadData()}catch(e){if(e!=='cancel')console.error(e)} }
var showSubAccountDialog = () => { subQueryForm.customerAccountNo=''; subQueryDone.value=false; subAccountList.value=[]; subDialogVisible.value=true }
var querySubAccounts = async () => { await subQueryFormRef.value.validate(async v=>{if(v){try{var res=await getCustomerSubAccountList(subQueryForm.customerAccountNo);subAccountList.value=res.data||[];subQueryDone.value=true}catch(e){console.error(e);subAccountList.value=[]}}}) }
var resetSubDialog = () => { subQueryDone.value=false; subQueryForm.customerAccountNo=''; subAccountList.value=[] }
var showNewSubAccountDialog = () => { newSubForm.accountType=0; newSubDialogVisible.value=true }
var handleOpenSubAccount = async () => { await newSubFormRef.value.validate(async v=>{if(v){try{await openSubAccount({customerAccountNo:subQueryForm.customerAccountNo,accountType:newSubForm.accountType});ElMessage.success('开通子账户成功');newSubDialogVisible.value=false;var res=await getCustomerSubAccountList(subQueryForm.customerAccountNo);subAccountList.value=res.data||[]}catch(e){console.error(e)}}}) }

onMounted(()=>{loadData()})
</script>

<style scoped>
.account-page{}
.page-header{display:flex;align-items:flex-start;justify-content:space-between;margin-bottom:20px}
.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}
.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
.page-actions{display:flex;gap:8px}
.page-actions .el-button{display:inline-flex;align-items:center;gap:6px}
.stats-row{margin-bottom:20px!important}
.stats-row .el-col{padding:0 8px!important}
.stat-mini{background:#fff;border-radius:10px;padding:16px;display:flex;flex-direction:column;gap:4px;border:1px solid #edf2f7}
.stat-mini-label{font-size:13px;color:var(--text-secondary)}
.stat-mini-value{font-size:22px;font-weight:700;color:var(--text-primary);line-height:1.2}
.data-table{margin-top:0}
.data-table :deep(.el-table__row){cursor:pointer}
.balance-text{font-family:'Courier New',monospace;font-weight:600;font-size:14px}
.status-group{display:flex;align-items:center}
.field-hint{color:#a0aec0;font-size:12px;margin-top:4px}
.detail-section{margin-bottom:20px}
.detail-section-title{font-size:14px;font-weight:600;color:var(--text-primary);margin-bottom:10px;padding-bottom:8px;border-bottom:1px solid #edf2f7}
</style>
