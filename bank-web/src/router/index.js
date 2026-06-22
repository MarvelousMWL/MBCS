import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  // 门户首页（公开）
  { path: '/', name: 'Portal', component: () => import('../views/PortalPage.vue') },
  // 登录（公开）
  { path: '/login', name: 'Login', component: () => import('../views/login/Login.vue') },
  // 柜面系统（需登录）
  {
    path: '/app', component: () => import('../views/layout/Layout.vue'), redirect: '/app/home',
    children: [
      { path: '/app/home', name: 'Home', component: () => import('../views/home/Home.vue') },
      { path: '/app/institution', name: 'Institution', component: () => import('../views/institution/Institution.vue') },
      { path: '/app/teller', name: 'Teller', component: () => import('../views/teller/Teller.vue') },
      { path: '/app/customer', name: 'Customer', component: () => import('../views/customer/Customer.vue') },
      { path: '/app/product-factory', name: 'ProductList', component: () => import('../views/product/ProductList.vue') },
      { path: '/app/product-factory/copy', name: 'ProductCopy', component: () => import('../views/product/ProductCopy.vue') },
      { path: '/app/account/query', name: 'AccountQuery', component: () => import('../views/account/AccountQuery.vue') },
      { path: '/app/account/open-personal', name: 'OpenPersonal', component: () => import('../views/account/OpenPersonal.vue') },
      { path: '/app/account/open-corporate', name: 'OpenCorporate', component: () => import('../views/account/OpenCorporate.vue') },
      { path: '/app/account/open-liability', name: 'OpenLiability', component: () => import('../views/account/OpenLiability.vue') },
      { path: '/app/account/close', name: 'AccountClose', component: () => import('../views/account/AccountClose.vue') },
      { path: '/app/account/restriction', name: 'AccountRestriction', component: () => import('../views/account/AccountRestriction.vue') },
      { path: '/app/account/freeze', name: 'AccountFreeze', component: () => import('../views/account/AccountFreeze.vue') },
      { path: '/app/account/unfreeze', name: 'AccountUnfreeze', component: () => import('../views/account/AccountUnfreeze.vue') },
      { path: '/app/account/sub-account', name: 'SubAccount', component: () => import('../views/account/SubAccount.vue') },
      { path: '/app/transaction/deposit', name: 'Deposit', component: () => import('../views/transaction/Deposit.vue') },
      { path: '/app/transaction/withdraw', name: 'Withdraw', component: () => import('../views/transaction/Withdraw.vue') },
      { path: '/app/transaction/cancel-deposit', name: 'CancelDeposit', component: () => import('../views/transaction/CancelDeposit.vue') },
      { path: '/app/transaction/cancel-withdraw', name: 'CancelWithdraw', component: () => import('../views/transaction/CancelWithdraw.vue') },
      { path: '/app/transaction/list', name: 'TransList', component: () => import('../views/transaction/TransList.vue') },
      { path: '/app/certificate-deposit/product', name: 'CDProduct', component: () => import('../views/certificate-deposit/CDProduct.vue') },
      { path: '/app/certificate-deposit/subscribe', name: 'CDSubscribe', component: () => import('../views/certificate-deposit/CDSubscribe.vue') },
      { path: '/app/certificate-deposit/redeem', name: 'CDRedeem', component: () => import('../views/certificate-deposit/CDRedeem.vue') },
      { path: '/app/certificate-deposit/transfer', name: 'CDTransfer', component: () => import('../views/certificate-deposit/CDTransfer.vue') },
      { path: '/app/batch/execute', name: 'BatchExecute', component: () => import('../views/batch/BatchExecute.vue') },
      { path: '/app/notice-deposit', name: 'NoticeDeposit', component: () => import('../views/notice-deposit/NoticeDeposit.vue') },
      { path: '/app/transfer', name: 'Transfer', component: () => import('../views/transfer/Transfer.vue') },
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  // 公开路由不需要登录
  if (to.path === '/' || to.path === '/login') { next(); return }
  // 柜面路由需要登录
  if (!userStore.token) { next('/login') } else { next() }
})

export default router
