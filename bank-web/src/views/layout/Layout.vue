<template>
  <el-container style="height:100%">
    <el-aside :width="collapsed?'64px':'240px'" class="sidebar">
      <div class="sidebar-logo">
        <div class="sidebar-logo-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/></svg></div>
        <span class="sidebar-logo-text" v-show="!collapsed">银行核心系统</span>
      </div>
      <div class="sidebar-menu-wrap">
        <el-menu :default-active="activeMenu" :collapse="collapsed" router class="sidebar-menu">

          <el-menu-item index="/home"><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg></el-icon><span>首页</span></el-menu-item>

          <el-sub-menu index="business" v-if="isVaultUser">
            <template #title><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="3" width="20" height="14" rx="2" ry="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/></svg></el-icon><span>柜面业务</span></template>
            <el-menu-item index="/institution"><span>机构管理</span></el-menu-item>
            <el-menu-item index="/teller"><span>柜员管理</span></el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="customer-mod">
            <template #title><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></el-icon><span>客户管理</span></template>
            <el-menu-item index="/customer"><span>客户信息</span></el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="account-mod">
            <template #title><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="4" width="22" height="16" rx="2" ry="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg></el-icon><span>账户业务</span></template>
            <!-- 查询 -->
            <el-menu-item index="/account/query"><span>账户查询</span></el-menu-item>
            <!-- 开户 -->
            <el-menu-item-group title="开 户"><template #title><span style="font-size:11px;color:#a0aec0;padding:0 8px">─ 开 户 ─</span></template>
              <el-menu-item index="/account/open-personal"><span>个人开户</span></el-menu-item>
              <el-menu-item index="/account/open-corporate"><span>对公开户</span></el-menu-item>
              <el-menu-item index="/account/open-liability"><span>开立负债账户</span></el-menu-item>
            </el-menu-item-group>
            <!-- 销户 -->
            <el-menu-item-group title="销 户"><template #title><span style="font-size:11px;color:#a0aec0;padding:0 8px">─ 销 户 ─</span></template>
              <el-menu-item index="/account/close"><span>账户销户</span></el-menu-item>
            </el-menu-item-group>
            <!-- 限制 -->
            <el-menu-item-group title="限 制"><template #title><span style="font-size:11px;color:#a0aec0;padding:0 8px">─ 限 制 ─</span></template>
                            <el-menu-item index="/account/restriction"><span>限制总览</span></el-menu-item><el-menu-item index="/account/freeze"><span>账户冻结</span></el-menu-item>
              <el-menu-item index="/account/unfreeze"><span>账户解冻</span></el-menu-item>
            </el-menu-item-group>
            <!-- 子账户 -->
            <el-menu-item index="/account/sub-account"><span>子账户管理</span></el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="trans-mod">
            <template #title><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg></el-icon><span>交易管理</span></template>
            <el-menu-item-group title="交 易"><template #title><span style="font-size:11px;color:#a0aec0;padding:0 8px">─ 交 易 ─</span></template>
              <el-menu-item index="/transaction/deposit"><span>存款交易</span></el-menu-item>
              <el-menu-item index="/transaction/withdraw"><span>取款交易</span></el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="撤 销"><template #title><span style="font-size:11px;color:#a0aec0;padding:0 8px">─ 撤 销 ─</span></template>
              <el-menu-item index="/transaction/cancel-deposit"><span>存款撤销</span></el-menu-item>
              <el-menu-item index="/transaction/cancel-withdraw"><span>取款撤销</span></el-menu-item>
            </el-menu-item-group>
            <el-menu-item index="/transaction/list"><span>交易流水</span></el-menu-item>
          </el-sub-menu>

        </el-menu>
      </div>
      <div class="sidebar-footer" @click="collapsed=!collapsed">
        <el-icon><svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><polyline :points="collapsed?'9 18 15 12 9 6':'15 18 9 12 15 6'"/></svg></el-icon>
        <span v-show="!collapsed">收起侧栏</span>
      </div>
    </el-aside>
    <el-container>
      <el-header class="topbar">
        <div class="topbar-left"><span class="topbar-title">{{ currentTitle }}</span><span class="topbar-path">{{ route.path }}</span></div>
        <div class="topbar-right"><div class="topbar-user"><div class="topbar-avatar"><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></div><div class="topbar-user-info"><span class="topbar-user-name">{{userStore.tellerName}}</span><span class="topbar-user-role">{{userStore.tellerNo}}</span></div><span class="topbar-divider"></span><el-button text class="topbar-logout" @click="handleLogout">退出</el-button></div></div>
      </el-header>
      <el-main class="main-area"><div class="page-content"><router-view/></div></el-main>
    </el-container>
  </el-container>
</template>
<script setup>
import { ref, computed } from 'vue'; import { useRouter, useRoute } from 'vue-router'; import { useUserStore } from '../../stores/user'
const router=useRouter(),route=useRoute(),userStore=useUserStore(),collapsed=ref(false),activeMenu=computed(()=>route.path),isVaultUser=computed(()=>userStore.tellerType==='VAULT')
var titles={'/home':'首页','/institution':'机构管理','/teller':'柜员管理','/customer':'客户管理','/account/query':'账户查询','/account/open-personal':'个人开户','/account/open-corporate':'对公开户','/account/open-liability':'开立负债账户','/account/close':'账户销户','/account/restriction':'限制总览','/account/freeze':'账户冻结','/account/unfreeze':'账户解冻','/account/sub-account':'子账户管理','/transaction/deposit':'存款交易','/transaction/withdraw':'取款交易','/transaction/cancel-deposit':'存款撤销','/transaction/cancel-withdraw':'取款撤销','/transaction/list':'交易流水'}
var currentTitle=computed(()=>titles[route.path]||'工作台')
const handleLogout=()=>{userStore.logout();router.push('/login')}
</script>
<style scoped>
.sidebar{background:var(--sidebar-bg);border-right:1px solid var(--sidebar-border);display:flex;flex-direction:column;transition:width .25s cubic-bezier(.4,0,.2,1);overflow:hidden;z-index:10}
.sidebar-logo{height:64px;display:flex;align-items:center;padding:0 16px;gap:10px;flex-shrink:0}
.sidebar-logo-icon{width:34px;height:34px;border-radius:10px;background:linear-gradient(135deg,#3182ce,#2b6cb0);display:flex;align-items:center;justify-content:center;color:#fff;flex-shrink:0;box-shadow:0 2px 8px rgba(49,130,206,.3)}
.sidebar-logo-icon svg{width:18px;height:18px}
.sidebar-logo-text{font-size:17px;font-weight:700;color:var(--text-primary);letter-spacing:.5px;white-space:nowrap}
.sidebar-menu-wrap{flex:1;overflow-y:auto;overflow-x:hidden;padding:8px 12px}
.sidebar-menu-wrap::-webkit-scrollbar{width:0}
.sidebar-menu{border-right:none!important;background:transparent!important}
.sidebar-menu:not(.el-menu--collapse){width:100%}
.sidebar-menu .el-menu-item,.sidebar-menu .el-sub-menu__title{height:40px;line-height:40px;color:var(--sidebar-text)!important;margin:1px 0;border-radius:8px;transition:all .15s;font-size:14px}
.sidebar-menu .el-menu-item:hover,.sidebar-menu .el-sub-menu__title:hover{background:var(--sidebar-hover-bg)!important;color:var(--sidebar-active)!important}
.sidebar-menu .el-menu-item.is-active{background:var(--sidebar-active-bg)!important;color:var(--sidebar-active)!important;font-weight:600}
.sidebar-menu .el-menu-item .el-icon,.sidebar-menu .el-sub-menu__title .el-icon{color:inherit;width:22px;display:flex;align-items:center;justify-content:center;margin-right:8px;flex-shrink:0}
.sidebar-menu .el-menu-item span,.sidebar-menu .el-sub-menu__title span{font-size:13px}
.sidebar-menu .el-sub-menu .el-menu{background:transparent!important}
.sidebar-menu .el-sub-menu .el-menu .el-menu-item{padding-left:50px!important;height:34px;line-height:34px;font-size:13px;margin:0}
.sidebar-menu .el-sub-menu .el-menu .el-menu-item.is-active{background:var(--sidebar-active-bg)!important}
.sidebar-menu .el-menu-item-group__title{padding:0!important}
.sidebar-footer{height:40px;display:flex;align-items:center;padding:0 16px;gap:8px;border-top:1px solid var(--sidebar-border);cursor:pointer;color:var(--sidebar-text-secondary);font-size:13px;transition:color .2s;flex-shrink:0}
.sidebar-footer:hover{color:var(--sidebar-text)}
.topbar{background:var(--header-bg);border-bottom:1px solid var(--header-border);display:flex;align-items:center;justify-content:space-between;height:64px;padding:0 28px}
.topbar-left{display:flex;align-items:baseline;gap:16px}
.topbar-title{font-size:18px;font-weight:600;color:var(--text-primary)}
.topbar-path{font-size:12px;color:var(--text-secondary);font-family:'Courier New',monospace}
.topbar-right{display:flex;align-items:center}
.topbar-user{display:flex;align-items:center;gap:10px}
.topbar-avatar{width:34px;height:34px;border-radius:50%;background:#ebf4ff;color:#3182ce;display:flex;align-items:center;justify-content:center}
.topbar-user-info{display:flex;flex-direction:column;line-height:1.2}
.topbar-user-name{font-size:14px;font-weight:600;color:var(--text-primary)}
.topbar-user-role{font-size:12px;color:var(--text-secondary)}
.topbar-divider{width:1px;height:20px;background:var(--sidebar-border);margin:0 6px}
.topbar-logout{color:var(--text-secondary)!important;font-size:13px;padding:4px 12px}
.topbar-logout:hover{color:#e53e3e!important;background:#fff5f5;border-radius:6px}
.main-area{background:var(--main-bg);padding:0}
.page-content{padding:28px;min-height:100%}
</style>
