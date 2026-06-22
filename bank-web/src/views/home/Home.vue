<template>
  <div class="home-page">
    <div class="home-header">
      <div>
        <h1 class="home-title">工作台</h1>
        <p class="home-desc">欢迎回来，以下是系统运行概览</p>
      </div>
      <div class="home-date">{{ currentDate }}</div>
    </div>
    <div class="home-grid">
      <div class="stat-card" v-for="card in statCards" :key="card.label" :style="{ '--accent': card.color }">
        <div class="stat-icon-wrap">
          <div class="stat-icon-bg"></div>
          <div class="stat-icon"><svg viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="1.5"><path :d="card.icon"/></svg></div>
        </div>
        <div class="stat-body">
          <span class="stat-label">{{ card.label }}</span>
          <span class="stat-value">{{ card.count }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, computed } from 'vue'
import { getInstitutionList } from '../../api/institution'; import { getTellerList } from '../../api/teller'
import { getCustomerList } from '../../api/customer'; import { getLiabilityAccountList } from '../../api/account'

var now2 = new Date()
var weekdays = ['日','一','二','三','四','五','六']
var currentDate = computed(() => now2.getFullYear()+'年'+(now2.getMonth()+1)+'月'+now2.getDate()+'日 星期'+weekdays[now2.getDay()])
var stats = ref({ic:0,tc:0,cc:0,lc:0})
var statCards = ref([])

onMounted(async () => {
  try {
    var [i,t,c,a] = await Promise.all([getInstitutionList(),getTellerList(),getCustomerList(),getLiabilityAccountList()])
    stats.value = {ic:i.data?.length||0,tc:t.data?.length||0,cc:c.data?.length||0,lc:a.data?.length||0}
    statCards.value = [
      {label:'机构总数',count:stats.value.ic,color:'#3182ce',icon:'M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z M9 22V12h6v10'},
      {label:'柜员总数',count:stats.value.tc,color:'#38a169',icon:'M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2 M12 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8'},
      {label:'客户总数',count:stats.value.cc,color:'#dd6b20',icon:'M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2 M9 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8 M23 21v-2a4 4 0 0 0-3-3.9 M16 3.1a4 4 0 0 1 0 7.8'},
      {label:'负债账户',count:stats.value.lc,color:'#d53f8c',icon:'M12 1v22 M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6'}
    ]
  } catch(e) { console.error(e) }
})
</script>
<style scoped>
.home-header{display:flex;align-items:flex-start;justify-content:space-between;margin-bottom:32px}
.home-title{font-size:24px;font-weight:700;color:var(--text-primary);margin:0 0 6px}
.home-desc{font-size:14px;color:var(--text-secondary);margin:0}
.home-date{font-size:14px;color:var(--text-secondary);padding:6px 16px;background:#fff;border-radius:8px;border:1px solid var(--sidebar-border);white-space:nowrap}
.home-grid{display:grid;grid-template-columns:repeat(4,1fr);gap:20px}
.stat-card{background:#fff;border-radius:14px;padding:24px;display:flex;align-items:center;gap:18px;border:1px solid #edf2f7;transition:all .25s cubic-bezier(.4,0,.2,1);cursor:default}
.stat-card:hover{transform:translateY(-4px);box-shadow:0 12px 30px rgba(0,0,0,.06);border-color:var(--accent)}
.stat-icon-wrap{position:relative;width:54px;height:54px;flex-shrink:0}
.stat-icon-bg{position:absolute;inset:0;border-radius:14px;background:var(--accent);opacity:.08}
.stat-icon{position:absolute;inset:0;display:flex;align-items:center;justify-content:center;color:var(--accent)}
.stat-body{display:flex;flex-direction:column;gap:2px}
.stat-label{font-size:14px;color:var(--text-secondary);font-weight:500}
.stat-value{font-size:30px;font-weight:800;color:var(--text-primary);line-height:1.2;letter-spacing:-.5px}
@media(max-width:1200px){.home-grid{grid-template-columns:repeat(2,1fr)}}
</style>
