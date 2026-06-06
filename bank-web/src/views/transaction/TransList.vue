<template>
  <div class="page">
    <div class="page-header"><div><h2 class="page-title">交易流水</h2><p class="page-desc">查询历史交易明细</p></div>
      <div class="search-bar">
        <el-input v-model="query.liabilityAccountNo" placeholder="负债账号" clearable style="width:160px"/>
        <el-select v-model="query.transactionType" placeholder="交易类型" clearable style="width:130px"><el-option label="存款" value="DEPOSIT"/><el-option label="取款" value="WITHDRAW"/><el-option label="存款冲正" value="DEPOSIT_CANCEL"/><el-option label="取款冲正" value="WITHDRAW_CANCEL"/></el-select>
        <el-button type="primary" @click="doSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
    </div>
    <el-table :data="list" class="data-table">
      <el-table-column prop="transactionNo" label="流水号" width="200"/>
      <el-table-column prop="liabilityAccountNo" label="负债账号" width="150"/>
      <el-table-column label="交易类型" width="110"><template #default="{ row: r }">{{typeMap[r.transactionType]||r.transactionType}}</template></el-table-column>
      <el-table-column prop="amount" label="金额" width="120" align="right"><template #default="{ row: r }"><span :style="{color:r.transactionType===2001||r.transactionType===2002?'#38a169':'#e53e3e',fontWeight:600}">{{Number(r.amount).toLocaleString('zh-CN',{minFractionDigits:2})}}</span></template></el-table-column>
      <el-table-column prop="balanceBefore" label="前余额" width="120" align="right"/>
      <el-table-column prop="balanceAfter" label="后余额" width="120" align="right"/>
      <el-table-column prop="operateTime" label="操作时间" width="180"/>
      <el-table-column prop="operatorNo" label="操作员" width="110"/>
      <el-table-column label="状态" width="90"><template #default="{ row: r }"><el-tag size="small" :type="r.status===0?'success':'danger'" style="border:0">{{r.status===0?'正常':'已冲正'}}</el-tag></template></el-table-column>
    </el-table>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { getTransactionList } from '../../api/transaction'
var list=ref([]),query=reactive({liabilityAccountNo:'',transactionType:''})
var typeMap={2001:'存款',2002:'取款',3001:'存款冲正',3002:'取款冲正'}
var load=async(p)=>{try{var r=await getTransactionList(p||{});list.value=r.data?.records||[]}catch(e){console.error(e)}}
var doSearch=()=>{var p={};if(query.liabilityAccountNo)p.liabilityAccountNo=query.liabilityAccountNo;if(query.transactionType)p.transactionType=query.transactionType;load(p)}
var resetSearch=()=>{query.liabilityAccountNo='';query.transactionType='';load()}
onMounted(()=>{load()})
</script>
<style scoped>
.page-header{margin-bottom:20px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
.search-bar{display:flex;gap:10px;align-items:center;margin-top:12px}
</style>
