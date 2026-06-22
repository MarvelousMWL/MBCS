<template>
  <el-container style="height:100%">
    <el-aside :width="collapsed?'64px':'240px'" class="sidebar">
      <div class="sidebar-logo">
        <div class="sidebar-logo-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/></svg></div>
        <span class="sidebar-logo-text" v-show="!collapsed">閾惰鏍稿績绯荤粺</span>
      </div>
      <div class="sidebar-menu-wrap">
        <el-menu :default-active="activeMenu" :collapse="collapsed" router class="sidebar-menu">

          <el-menu-item index="/app/home"><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg></el-icon><span>棣栭〉</span></el-menu-item>

          <el-sub-menu index="business" v-if="isVaultUser">
            <template #title><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="3" width="20" height="14" rx="2" ry="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/></svg></el-icon><span>鏌滈潰涓氬姟</span></template>
            <el-menu-item index="/app/institution"><span>鏈烘瀯绠＄悊</span></el-menu-item>
            <el-menu-item index="/app/teller"><span>鏌滃憳绠＄悊</span></el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="product-mod">
            <template #title><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2"/><path d="M3 9h18"/><path d="M9 21V9"/></svg></el-icon><span>浜у搧宸ュ巶</span></template>
            <el-menu-item index="/app/product-factory"><span>浜у搧鍒楄〃</span></el-menu-item>
            <el-menu-item index="/app/product-factory/copy"><span>浜у搧鎷疯礉</span></el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="customer-mod">
            <template #title><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></el-icon><span>瀹㈡埛绠＄悊</span></template>
            <el-menu-item index="/app/customer"><span>瀹㈡埛淇℃伅</span></el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="account-mod">
            <template #title><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="4" width="22" height="16" rx="2" ry="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg></el-icon><span>璐︽埛涓氬姟</span></template>
            <el-menu-item index="/app/account/query"><span>璐︽埛鏌ヨ</span></el-menu-item>
            <el-menu-item-group title="寮€鎴?><template #title><span style="font-size:11px;color:#a0aec0;padding:0 8px">- 寮€鎴?-</span></template>
              <el-menu-item index="/app/account/open-personal"><span>涓汉寮€鎴?/span></el-menu-item>
              <el-menu-item index="/app/account/open-corporate"><span>瀵瑰叕寮€鎴?/span></el-menu-item>
              <el-menu-item index="/app/account/open-liability"><span>寮€绔嬭礋鍊鸿处鎴?/span></el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="閿€鎴?><template #title><span style="font-size:11px;color:#a0aec0;padding:0 8px">- 閿€鎴?-</span></template>
              <el-menu-item index="/app/account/close"><span>璐︽埛閿€鎴?/span></el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="闄愬埗"><template #title><span style="font-size:11px;color:#a0aec0;padding:0 8px">- 闄愬埗 -</span></template>
              <el-menu-item index="/app/account/restriction"><span>闄愬埗鎬昏</span></el-menu-item>
              <el-menu-item index="/app/account/freeze"><span>璐︽埛鍐荤粨</span></el-menu-item>
              <el-menu-item index="/app/account/unfreeze"><span>璐︽埛瑙ｅ喕</span></el-menu-item>
            </el-menu-item-group>
            <el-menu-item index="/app/account/sub-account"><span>瀛愯处鎴风鐞?/span></el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="trans-mod">
            <template #title><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg></el-icon><span>浜ゆ槗绠＄悊</span></template>
            <el-menu-item-group title="浜ゆ槗">
              <el-menu-item index="/app/transaction/deposit"><span>瀛樻浜ゆ槗</span></el-menu-item>
              <el-menu-item index="/app/transaction/withdraw"><span>鍙栨浜ゆ槗</span></el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="鎾ら攢">
              <el-menu-item index="/app/transaction/cancel-deposit"><span>瀛樻鎾ら攢</span></el-menu-item>
              <el-menu-item index="/app/transaction/cancel-withdraw"><span>鍙栨鎾ら攢</span></el-menu-item>
            </el-menu-item-group>
            <el-menu-item index="/app/transaction/list"><span>浜ゆ槗娴佹按</span></el-menu-item>
            <el-menu-item index="/app/transfer"><span>琛屽唴杞处</span></el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="cd-mod">
            <template #title><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="3" width="20" height="18" rx="2"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/></svg></el-icon><span>澶ч瀛樺崟</span></template>
            <el-menu-item index="/app/certificate-deposit/product"><span>浜у搧鏈熸</span></el-menu-item>
            <el-menu-item index="/app/certificate-deposit/subscribe"><span>璁よ喘</span></el-menu-item>
            <el-menu-item index="/app/certificate-deposit/redeem"><span>鍏戜粯</span></el-menu-item>
            <el-menu-item index="/app/certificate-deposit/transfer"><span>杞</span></el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="notice-mod">
            <template #title><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg></el-icon><span>閫氱煡瀛樻</span></template>
            <el-menu-item index="/app/notice-deposit"><span>寮€鎴蜂笌鏀彇</span></el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="batch-mod">
            <template #title><el-icon><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg></el-icon><span>鎵归噺澶勭悊</span></template>
            <el-menu-item index="/app/batch/execute"><span>鎵归噺鎵ц</span></el-menu-item>
          </el-sub-menu>


        </el-menu>
      </div>
      <div class="sidebar-footer" @click="collapsed=!collapsed">
        <el-icon><svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><polyline :points="collapsed?'9 18 15 12 9 6':'15 18 9 12 15 6'"/></svg></el-icon>
        <span v-show="!collapsed">鏀惰捣渚ф爮</span>
      </div>
    </el-aside>
    <el-container>
      <el-header class="topbar">
        <div class="topbar-left"><span class="topbar-title">{{ currentTitle }}</span><span class="topbar-path">{{ route.path }}</span></div>
        <div class="topbar-right"><div class="topbar-user"><div class="topbar-avatar"><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></div><div class="topbar-user-info"><span class="topbar-user-name">{{userStore.tellerName}}</span><span class="topbar-user-role">{{userStore.tellerNo}}</span></div><span class="topbar-divider"></span><el-button text class="topbar-logout" @click="handleLogout">閫€鍑?/el-button></div></div>
      </el-header>
      <el-main class="main-area"><div class="page-content"><router-view/></div></el-main>
    </el-container>
  </el-container>
</template>
<script setup>
import { ref, computed } from 'vue'; import { useRouter, useRoute } from 'vue-router'; import { useUserStore } from '../../stores/user'
const router=useRouter(),route=useRoute(),userStore=useUserStore(),collapsed=ref(false),activeMenu=computed(()=>route.path),isVaultUser=computed(()=>userStore.tellerType===1)
var titles={'/app/product-factory':'浜у搧鍒楄〃','/app/product-factory/copy':'浜у搧鎷疯礉','/app/home':'棣栭〉','/app/institution':'鏈烘瀯绠＄悊','/app/teller':'鏌滃憳绠＄悊','/app/customer':'瀹㈡埛绠＄悊','/app/account/query':'璐︽埛鏌ヨ','/app/account/open-personal':'涓汉寮€鎴?,'/app/account/open-corporate':'瀵瑰叕寮€鎴?,'/app/account/open-liability':'寮€绔嬭礋鍊鸿处鎴?,'/app/account/close':'璐︽埛閿€鎴?,'/app/account/restriction':'闄愬埗鎬昏','/app/account/freeze':'璐︽埛鍐荤粨','/app/account/unfreeze':'璐︽埛瑙ｅ喕','/app/account/sub-account':'瀛愯处鎴风鐞?,'/app/transaction/deposit':'瀛樻浜ゆ槗','/app/transaction/withdraw':'鍙栨浜ゆ槗','/app/transaction/cancel-deposit':'瀛樻鎾ら攢','/app/transaction/cancel-withdraw':'鍙栨鎾ら攢','/app/transaction/list':'浜ゆ槗娴佹按','/app/certificate-deposit/product':'浜у搧鏈熸','/app/certificate-deposit/subscribe':'璁よ喘','/app/certificate-deposit/redeem':'鍏戜粯','/app/certificate-deposit/transfer':'杞','/app/notice-deposit':'閫氱煡瀛樻','/app/batch/execute':'鎵归噺鎵ц','/app/transfer':'琛屽唴杞处'}
var currentTitle=computed(()=>titles[route.path]||'宸ヤ綔鍙?)
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