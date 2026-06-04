<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">批量执行管理</h2><p class="page-desc">批量结息、兑付、逾期处理及形态转移</p></div></div>
    <el-card style="margin-bottom:20px">
      <template #header><span style="font-weight:600">批量操作</span></template>
      <el-form :inline="true" ref="batchFormRef" :model="batchForm" label-width="120px">
        <el-form-item label="操作类型" prop="batchType"><el-select v-model="batchForm.batchType" placeholder="选择批量操作" style="width:200px"><el-option label="批量结息" value="SETTLE_INTEREST"/><el-option label="批量兑付" value="REDEEM"/><el-option label="逾期处理" value="OVERDUE"/><el-option label="形态转移" value="TRANSFER_TYPE"/></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="handleBatch">执行</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <template #header><span style="font-weight:600">执行记录</span></template>
      <div class="search-bar">
        <el-select v-model="query.type" placeholder="操作类型" clearable style="width:140px"><el-option label="批量结息" value="SETTLE_INTEREST"/><el-option label="批量兑付" value="REDEEM"/><el-option label="逾期处理" value="OVERDUE"/><el-option label="形态转移" value="TRANSFER_TYPE"/></el-select>
        <el-button type="primary" @click="doSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
      <el-table :data="recordList" class="data-table" style="margin-top:12px">
        <el-table-column prop="batchNo" label="批次号" width="200"/>
        <el-table-column label="操作类型" width="110"><template #default="{ row: r }">{{{'SETTLE_INTEREST':'批量结息','REDEEM':'批量兑付','OVERDUE':'逾期处理','TRANSFER_TYPE':'形态转移'}[r.batchType]||r.batchType}}</template></el-table-column>
        <el-table-column prop="totalCount" label="处理笔数" width="100" align="right"/>
        <el-table-column prop="successCount" label="成功笔数" width="100" align="right"/>
        <el-table-column prop="failCount" label="失败笔数" width="100" align="right"/>
        <el-table-column prop="executeTime" label="执行时间" width="170"/>
        <el-table-column label="状态" width="100"><template #default="{ row: r }"><el-tag size="small" :type="r.status==='SUCCESS'?'success':r.status==='FAILED'?'danger':'warning'" effect="light" style="border:0">{{{'SUCCESS':'成功','FAILED':'失败','PROCESSING':'处理中'}[r.status]||r.status}}</el-tag></template></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage } from 'element-plus'
import { batchSettleInterest, batchRedeem, batchOverdue, batchTransferType, getBatchRecordList } from '../../api/batch'
var batchFormRef=ref(),recordList=ref([])
var batchForm=reactive({batchType:''})
var query=reactive({type:''})
var loadRecords=async(p)=>{try{var r=await getBatchRecordList(p||{});recordList.value=r.data?.records||[]}catch(e){console.error(e)}}
var doSearch=()=>{var p={};if(query.type)p.batchType=query.type;loadRecords(p)}
var resetSearch=()=>{query.type='';loadRecords()}
var handleBatch=async()=>{if(!batchForm.batchType){ElMessage.warning('请选择操作类型');return}try{var fn={'SETTLE_INTEREST':batchSettleInterest,'REDEEM':batchRedeem,'OVERDUE':batchOverdue,'TRANSFER_TYPE':batchTransferType}[batchForm.batchType];var r=await fn({});ElMessage.success('批量操作已提交! 批次号:'+(r.data?.batchNo||''));loadRecords()}catch(e){console.error(e)}}
onMounted(()=>{loadRecords()})
</script>
<style scoped>
.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
.search-bar{display:flex;gap:10px;align-items:center}
</style>
