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
    <div v-if="resultMsg" style="margin:16px 0;padding:12px 16px;border-radius:8px;background:#f0fff4;border:1px solid #c6f6d5;color:#276749">{{ resultMsg }}</div>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'; import { ElMessage } from 'element-plus'
import { batchSettleInterest, batchRedeem, batchOverdue, batchTransferType } from '../../api/batch'
var batchFormRef=ref(), resultMsg=ref('')
var batchForm=reactive({batchType:''})
var handleBatch=async()=>{
  if(!batchForm.batchType){ElMessage.warning('请选择操作类型');return}
  try{
    var fn={'SETTLE_INTEREST':batchSettleInterest,'REDEEM':batchRedeem,'OVERDUE':batchOverdue,'TRANSFER_TYPE':batchTransferType}[batchForm.batchType]
    var r=await fn({processDate: new Date().toISOString().split('T')[0]})
    resultMsg.value='批量操作已提交，处理日期：' + (r.data?.message || '请查看日志')
    ElMessage.success('批量操作执行成功!')
  }catch(e){
    ElMessage.error('批量操作执行失败: ' + (e.response?.data?.message || e.message))
  }
}
</script>
<style scoped>
.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
</style>