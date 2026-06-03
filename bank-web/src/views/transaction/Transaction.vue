<template>
  <div>
    <div class="page-header"><h2 class="page-title">交易管理</h2></div>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="存款" name="deposit">
        <el-form ref="depositFormRef" :model="depositForm" :rules="depositRules" label-width="120px" style="max-width: 600px">
          <el-form-item label="客户账号" prop="customerAccountNo">
            <el-input v-model="depositForm.customerAccountNo" @blur="loadDepositSubAccounts" />
          </el-form-item>
          <el-form-item label="子账户" prop="selectedSubAccount" v-if="depositSubAccountList.length > 0">
            <el-select v-model="depositForm.selectedSubAccount" placeholder="请选择子账户" @change="onDepositSubAccountChange">
              <el-option
                v-for="item in depositSubAccountList"
                :key="item.liabilityAccountNo"
                :label="`序号:${item.subAccountSeq} | 负债:${item.liabilityAccountNo}`"
                :value="item"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="负债账号" v-if="depositForm.selectedSubAccount">
            <el-input v-model="depositForm.liabilityAccountNo" disabled />
          </el-form-item>
          <el-form-item label="存款金额" prop="amount">
            <el-input-number v-model="depositForm.amount" :min="0.01" :precision="2" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleDeposit">确认存款</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="取款" name="withdraw">
        <el-form ref="withdrawFormRef" :model="withdrawForm" :rules="withdrawRules" label-width="120px" style="max-width: 600px">
          <el-form-item label="客户账号" prop="customerAccountNo">
            <el-input v-model="withdrawForm.customerAccountNo" @blur="loadWithdrawSubAccounts" />
          </el-form-item>
          <el-form-item label="子账户" prop="selectedSubAccount" v-if="withdrawSubAccountList.length > 0">
            <el-select v-model="withdrawForm.selectedSubAccount" placeholder="请选择子账户" @change="onWithdrawSubAccountChange">
              <el-option
                v-for="item in withdrawSubAccountList"
                :key="item.liabilityAccountNo"
                :label="`序号:${item.subAccountSeq} | 负债:${item.liabilityAccountNo}`"
                :value="item"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="负债账号" v-if="withdrawForm.selectedSubAccount">
            <el-input v-model="withdrawForm.liabilityAccountNo" disabled />
          </el-form-item>
          <el-form-item label="取款金额" prop="amount">
            <el-input-number v-model="withdrawForm.amount" :min="0.01" :precision="2" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleWithdraw">确认取款</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="存款撤销" name="cancel">
        <el-form ref="cancelFormRef" :model="cancelForm" :rules="cancelRules" label-width="120px" style="max-width: 500px">
          <el-form-item label="原交易流水号" prop="originalTransactionNo">
            <el-input v-model="cancelForm.originalTransactionNo" />
          </el-form-item>
          <el-form-item>
            <el-button type="danger" @click="handleCancel">确认撤销</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="取款撤销" name="withdrawCancel">
        <el-form ref="withdrawCancelFormRef" :model="withdrawCancelForm" :rules="withdrawCancelRules" label-width="120px" style="max-width: 500px">
          <el-form-item label="原交易流水号" prop="originalTransactionNo">
            <el-input v-model="withdrawCancelForm.originalTransactionNo" />
          </el-form-item>
          <el-form-item>
            <el-button type="danger" @click="handleWithdrawCancel">确认撤销</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="交易流水" name="list">
        <div style="margin-bottom: 16px; display: flex; flex-wrap: wrap; gap: 12px; align-items: flex-end;">
          <el-form-item label="负债账号" style="margin-bottom: 0">
            <el-input v-model="queryForm.liabilityAccountNo" placeholder="请输入负债账号" clearable style="width: 180px" />
          </el-form-item>
          <el-form-item label="交易类型" style="margin-bottom: 0">
            <el-select v-model="queryForm.transactionType" placeholder="全部" clearable style="width: 140px">
              <el-option label="存款" value="DEPOSIT" />
              <el-option label="取款" value="WITHDRAW" />
              <el-option label="存款冲正" value="DEPOSIT_CANCEL" />
              <el-option label="取款冲正" value="WITHDRAW_CANCEL" />
            </el-select>
          </el-form-item>
          <el-form-item label="日期范围" style="margin-bottom: 0">
            <el-date-picker
              v-model="queryDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
              style="width: 260px"
            />
          </el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="transactionList" border style="width: 100%">
          <el-table-column prop="transactionNo" label="交易流水号" width="200" />
          <el-table-column prop="liabilityAccountNo" label="负债账号" width="120" />
          <el-table-column prop="transactionType" label="交易类型" width="100" />
          <el-table-column prop="amount" label="金额" width="120" />
          <el-table-column prop="balanceBefore" label="交易前余额" width="120" />
          <el-table-column prop="balanceAfter" label="交易后余额" width="120" />
          <el-table-column prop="operatorNo" label="操作员" width="120" />
          <el-table-column prop="operateTime" label="操作时间" width="180" />
          <el-table-column prop="remark" label="备注" />
        </el-table>
        <div style="display: flex; justify-content: flex-end; margin-top: 16px">
          <el-pagination
            v-model:current-page="pagination.page"
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @current-change="loadTransactionList"
            @size-change="onSizeChange"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { deposit, withdraw, cancelDeposit, withdrawCancel, getTransactionList } from '../../api/transaction'
import { getCustomerSubAccountList } from '../../api/subAccount'

const activeTab = ref('list')
const transactionList = ref([])
const depositFormRef = ref()
const withdrawFormRef = ref()
const cancelFormRef = ref()
const withdrawCancelFormRef = ref()
const depositSubAccountList = ref([])
const withdrawSubAccountList = ref([])

const queryForm = reactive({
  liabilityAccountNo: '',
  transactionType: ''
})
const queryDateRange = ref(null)

const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

const depositForm = reactive({
  customerAccountNo: '',
  selectedSubAccount: null,
  liabilityAccountNo: '',
  amount: 0
})

const withdrawForm = reactive({
  customerAccountNo: '',
  selectedSubAccount: null,
  liabilityAccountNo: '',
  amount: 0
})

const cancelForm = reactive({
  originalTransactionNo: ''
})

const withdrawCancelForm = reactive({
  originalTransactionNo: ''
})

const positiveAmountValidator = (rule, value, callback) => {
  if (value === null || value === undefined || value === '') {
    callback(new Error('请输入金额'))
  } else if (value <= 0) {
    callback(new Error('金额必须大于0'))
  } else {
    callback()
  }
}

const depositRules = {
  customerAccountNo: [{ required: true, message: '请输入客户账号', trigger: 'blur' }],
  selectedSubAccount: [{ required: true, message: '请选择子账户', trigger: 'change' }],
  amount: [{ required: true, validator: positiveAmountValidator, trigger: 'blur' }]
}

const withdrawRules = {
  customerAccountNo: [{ required: true, message: '请输入客户账号', trigger: 'blur' }],
  selectedSubAccount: [{ required: true, message: '请选择子账户', trigger: 'change' }],
  amount: [{ required: true, validator: positiveAmountValidator, trigger: 'blur' }]
}

const cancelRules = {
  originalTransactionNo: [{ required: true, message: '请输入原交易流水号', trigger: 'blur' }]
}

const withdrawCancelRules = {
  originalTransactionNo: [{ required: true, message: '请输入原交易流水号', trigger: 'blur' }]
}

const loadDepositSubAccounts = async () => {
  if (!depositForm.customerAccountNo) {
    depositSubAccountList.value = []
    return
  }
  try {
    const res = await getCustomerSubAccountList(depositForm.customerAccountNo)
    depositSubAccountList.value = (res.data || []).filter(item => item.status === 'NORMAL')
  } catch (error) {
    console.error(error)
    depositSubAccountList.value = []
  }
}

const loadWithdrawSubAccounts = async () => {
  if (!withdrawForm.customerAccountNo) {
    withdrawSubAccountList.value = []
    return
  }
  try {
    const res = await getCustomerSubAccountList(withdrawForm.customerAccountNo)
    withdrawSubAccountList.value = (res.data || []).filter(item => item.status === 'NORMAL')
  } catch (error) {
    console.error(error)
    withdrawSubAccountList.value = []
  }
}

const onDepositSubAccountChange = (val) => {
  depositForm.liabilityAccountNo = val ? val.liabilityAccountNo : ''
}

const onWithdrawSubAccountChange = (val) => {
  withdrawForm.liabilityAccountNo = val ? val.liabilityAccountNo : ''
}

const handleDeposit = async () => {
  await depositFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await deposit({ liabilityAccountNo: depositForm.liabilityAccountNo, amount: depositForm.amount })
        ElMessage.success(`存款成功! 交易流水号: ${res.data?.transactionNo}`)
        depositForm.customerAccountNo = ''
        depositForm.selectedSubAccount = null
        depositForm.liabilityAccountNo = ''
        depositForm.amount = 0
        depositSubAccountList.value = []
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const handleWithdraw = async () => {
  await withdrawFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await withdraw({ liabilityAccountNo: withdrawForm.liabilityAccountNo, amount: withdrawForm.amount })
        ElMessage.success(`取款成功! 交易流水号: ${res.data?.transactionNo}`)
        withdrawForm.customerAccountNo = ''
        withdrawForm.selectedSubAccount = null
        withdrawForm.liabilityAccountNo = ''
        withdrawForm.amount = 0
        withdrawSubAccountList.value = []
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const handleCancel = async () => {
  await cancelFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await cancelDeposit(cancelForm)
        ElMessage.success(`撤销成功! 新交易流水号: ${res.data?.transactionNo}`)
        cancelForm.originalTransactionNo = ''
        loadTransactionList()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const handleWithdrawCancel = async () => {
  await withdrawCancelFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await withdrawCancel(withdrawCancelForm)
        ElMessage.success(`撤销成功! 新交易流水号: ${res.data?.transactionNo}`)
        withdrawCancelForm.originalTransactionNo = ''
        loadTransactionList()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const onSizeChange = () => {
  pagination.page = 1
  loadTransactionList()
}

const handleSearch = () => {
  pagination.page = 1
  loadTransactionList()
}

const handleReset = () => {
  queryForm.liabilityAccountNo = ''
  queryForm.transactionType = ''
  queryDateRange.value = null
  pagination.page = 1
  loadTransactionList()
}

const loadTransactionList = async () => {
  try {
    const params = {
      page: pagination.page,
      size: pagination.size
    }
    if (queryForm.liabilityAccountNo) {
      params.liabilityAccountNo = queryForm.liabilityAccountNo
    }
    if (queryForm.transactionType) {
      params.transactionType = queryForm.transactionType
    }
    if (queryDateRange.value) {
      params.startDate = queryDateRange.value[0]
      params.endDate = queryDateRange.value[1]
    }
    const res = await getTransactionList(params)
    transactionList.value = res.data?.records || []
    pagination.total = res.data?.total || 0
    pagination.page = res.data?.page || 1
    pagination.size = res.data?.size || 20
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadTransactionList()
})
</script>
