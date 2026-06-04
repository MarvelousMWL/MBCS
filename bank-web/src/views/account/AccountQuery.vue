<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">账户查询</h2><p class="page-desc">查询和管理系统中的负债账户</p></div></div>
    <el-table :data="accountList" class="data-table" @row-click="showDetail">
      <el-table-column prop="liabilityAccountNo" label="负债账号" width="150"/>
      <el-table-column prop="customerAccountNo" label="客户账号" width="170"/>
      <el-table-column prop="accountType" label="产品类型" width="100"><template #default="{ row: r }"><el-tag size="small" :type="r.accountType==='DEMAND'?'primary':'warning'" effect="plain" style="border:0">{{r.accountType==='DEMAND'?'活期':'定期'}}</el-tag></template></el-table-column>
      <el-table-column prop="balance" label="余额" width="130" align="right"><template #default="{ row: r }"><span class="balance-text">{{Number(r.balance).toLocaleString('zh-CN',{minFractionDigits:2})}}</span></template></el-table-column>
      <el-table-column label="状态" width="130"><template #default="{ row: r }"><el-tag size="small" :type="[0,3,2,1].indexOf(r.status)===0?'success':[0,3,2,1].indexOf(r.status)===1?'danger':'info'" effect="light" style="border:0">{{$enumDict.LIABILITY_ACCOUNT_STATUS[r.status]||r.status}}</el-tag></template></el-table-column>
      <el-table-column prop="openDate" label="开户日期" width="180"/>
      <el-table-column label="操作" width="100" fixed="right"><template #default="{ row: r }"><el-button text size="small" @click.stop="showDetail(r)">详情</el-button></template></el-table-column>
    </el-table>
    <el-drawer v-model="detailVisible" :title="'账户详情 - '+(detailData.liabilityAccountNo||'')" size="450px" destroy-on-close>
      <template v-if="detailData.liabilityAccountNo">
        <div class="detail-section"><div class="detail-section-title">基本信息</div>
          <el-descriptions :column="1" border size="small">
            <el-descriptions-item label="负债账号">{{detailData.liabilityAccountNo}}</el-descriptions-item>
            <el-descriptions-item label="客户账号">{{detailData.customerAccountNo}}</el-descriptions-item>
            <el-descriptions-item label="子账户序号">{{detailData.subAccountSeq||'-'}}</el-descriptions-item>
            <el-descriptions-item label="产品类型">{{$enumDict.LIABILITY_ACCOUNT_TYPE_STR[detailData.accountType]||detailData.accountType}}</el-descriptions-item>
            <el-descriptions-item label="账户余额"><span style="font-weight:700;font-size:16px;color:#3182ce">{{Number(detailData.balance).toLocaleString('zh-CN',{minFractionDigits:2})}}</span></el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="detail-section"><div class="detail-section-title">账户状态</div>
          <el-descriptions :column="1" border size="small">
            <el-descriptions-item label="账户状态">{{$enumDict.LIABILITY_ACCOUNT_STATUS[detailData.status]||detailData.status}}</el-descriptions-item>
            <el-descriptions-item label="开户日期">{{detailData.openDate}}</el-descriptions-item>
            <el-descriptions-item label="销户日期">{{detailData.closeDate||'-'}}</el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="detail-section"><div class="detail-section-title">利率信息</div>
          <el-descriptions :column="1" border size="small">
            <el-descriptions-item label="年利率">{{detailData.interestRate!=null?detailData.interestRate+'%':'-'}}</el-descriptions-item>
            <el-descriptions-item label="计息方式">{{detailData.accountType==='DEMAND'?'按季结息':'到期还本付息'}}</el-descriptions-item>
          </el-descriptions>
        </div>
      </template>
    </el-drawer>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'; import { getLiabilityAccountList } from '../../api/account'
var accountList=ref([]); var detailVisible=ref(false); var detailData=ref({})
onMounted(async()=>{try{var r=await getLiabilityAccountList();accountList.value=r.data||[]}catch(e){console.error(e)}})
var showDetail=(row)=>{detailData.value=row;detailVisible.value=true}
</script>
<style scoped>
.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
.balance-text{font-family:'Courier New',monospace;font-weight:600;font-size:14px}
.detail-section{margin-bottom:20px}.detail-section-title{font-size:14px;font-weight:600;color:var(--text-primary);margin-bottom:10px;padding-bottom:8px;border-bottom:1px solid #edf2f7}
</style>
