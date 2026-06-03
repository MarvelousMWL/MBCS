import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
const routes = [
  { path: '/login', name: 'Login', component: () => import('../views/login/Login.vue') },
  {
    path: '/', component: () => import('../views/layout/Layout.vue'), redirect: '/home',
    children: [
      { path: '/home', name: 'Home', component: () => import('../views/home/Home.vue') },
      { path: '/institution', name: 'Institution', component: () => import('../views/institution/Institution.vue') },
      { path: '/teller', name: 'Teller', component: () => import('../views/teller/Teller.vue') },
      { path: '/customer', name: 'Customer', component: () => import('../views/customer/Customer.vue') },
      // 账户查询
      { path: '/account/query', name: 'AccountQuery', component: () => import('../views/account/AccountQuery.vue') },
      // 开户
      { path: '/account/open-personal', name: 'OpenPersonal', component: () => import('../views/account/OpenPersonal.vue') },
      { path: '/account/open-corporate', name: 'OpenCorporate', component: () => import('../views/account/OpenCorporate.vue') },
      { path: '/account/open-liability', name: 'OpenLiability', component: () => import('../views/account/OpenLiability.vue') },
      // 销户
      { path: '/account/close', name: 'AccountClose', component: () => import('../views/account/AccountClose.vue') },
      // 限制总览
      { path: '/account/restriction', name: 'AccountRestriction', component: () => import('../views/account/AccountRestriction.vue') },
      // 冻结/解冻
      { path: '/account/freeze', name: 'AccountFreeze', component: () => import('../views/account/AccountFreeze.vue') },
      { path: '/account/unfreeze', name: 'AccountUnfreeze', component: () => import('../views/account/AccountUnfreeze.vue') },
      // 子账户
      { path: '/account/sub-account', name: 'SubAccount', component: () => import('../views/account/SubAccount.vue') },
      // 交易
      { path: '/transaction/deposit', name: 'Deposit', component: () => import('../views/transaction/Deposit.vue') },
      { path: '/transaction/withdraw', name: 'Withdraw', component: () => import('../views/transaction/Withdraw.vue') },
      { path: '/transaction/cancel-deposit', name: 'CancelDeposit', component: () => import('../views/transaction/CancelDeposit.vue') },
      { path: '/transaction/cancel-withdraw', name: 'CancelWithdraw', component: () => import('../views/transaction/CancelWithdraw.vue') },
      { path: '/transaction/list', name: 'TransList', component: () => import('../views/transaction/TransList.vue') },
    ]
  }
]
const router = createRouter({ history: createWebHistory(), routes })
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.path !== '/login' && !userStore.token) { next('/login') } else { next() }
})
export default router
