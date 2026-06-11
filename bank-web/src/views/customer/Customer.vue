<template>
  <div>
    <div class="page-header"><h2 class="page-title">客户管理</h2></div>
    <el-button type="primary" style="margin-bottom: 10px" @click="showAddDialog">新增客户</el-button>
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="customerNo" label="客户号" width="120" />
      <el-table-column prop="customerName" label="客户姓名" width="120" />
      <el-table-column label="证件类型" width="100">
        <template #default="{ row }">
          {{ idTypeMap[row.idType] || ('未知(' + row.idType + ')') }}
        </template>
      </el-table-column>
      <el-table-column prop="idNumber" label="证件号码" width="180" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="address" label="地址" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status===0 ? 'success' : 'danger'">{{ $enumDict.CUSTOMER_STATUS[row.status]||row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="showEditDialog(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="客户姓名" prop="customerName">
          <el-input v-model="form.customerName" />
        </el-form-item>
        <el-form-item label="证件类型" prop="idType">
          <el-select v-model="form.idType" placeholder="请选择证件类型">
            <el-option label="身份证" :value="0" />
            <el-option label="护照" :value="1" />
            <el-option label="军官证" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="证件号码" prop="idNumber">
          <el-input v-model="form.idNumber" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCustomerList, getCustomerByNo, createCustomer, updateCustomer, deleteCustomer } from '../../api/customer'

const idTypeMap = { 0: '身份证', 1: '护照', 2: '军官证' }

const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editCustomerNo = ref('')
const formRef = ref()

const form = reactive({
  customerName: '',
  idType: '',
  idNumber: '',
  phone: '',
  address: ''
})

const rules = {
  customerName: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
  idType: [{ required: true, message: '请选择证件类型', trigger: 'change' }],
  idNumber: [{ required: true, message: '请输入证件号码', trigger: 'blur' }]
}

const dialogTitle = computed(() => isEdit.value ? '编辑客户' : '新增客户')

const loadData = async () => {
  try {
    const res = await getCustomerList()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const resetForm = () => {
  form.customerName = ''
  form.idType = ''
  form.idNumber = ''
  form.phone = ''
  form.address = ''
}

const showAddDialog = () => {
  isEdit.value = false
  editCustomerNo.value = ''
  resetForm()
  dialogVisible.value = true
}

const showEditDialog = async (row) => {
  isEdit.value = true
  editCustomerNo.value = row.customerNo
  try {
    const res = await getCustomerByNo(row.customerNo)
    const data = res.data || row
    form.customerName = data.customerName
    form.idType = data.idType
    form.idNumber = data.idNumber
    form.phone = data.phone
    form.address = data.address
  } catch (error) {
    form.customerName = row.customerName
    form.idType = row.idType
    form.idNumber = row.idNumber
    form.phone = row.phone
    form.address = row.address
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateCustomer({ ...form, customerNo: editCustomerNo.value })
          ElMessage.success('修改成功')
        } else {
          await createCustomer(form)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除客户 ${row.customerName} 吗？`, '提示', { type: 'warning' })
    await deleteCustomer(row.customerNo)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

onMounted(() => {
  loadData()
})
</script>
