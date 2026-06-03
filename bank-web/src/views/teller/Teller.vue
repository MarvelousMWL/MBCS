<template>
  <div>
    <div class="page-header"><h2 class="page-title">柜员管理</h2></div>
    <div style="margin-bottom: 20px">
      <el-button type="primary" @click="showAddDialog" v-if="isVaultTeller">新增柜员</el-button>
    </div>
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="tellerNo" label="柜员编号" width="140" />
      <el-table-column prop="tellerName" label="柜员姓名" width="120" />
      <el-table-column prop="institutionNo" label="机构编号" width="120" />
      <el-table-column prop="tellerType" label="柜员类型" width="120">
        <template #default="{ row }">
          <el-tag :type="row.vault ? 'warning' : 'success'">{{ row.vault ? '库管柜员' : '普通柜员' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.normal ? 'success' : 'danger'">{{ row.normal ? '正常' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" width="180" v-if="isVaultTeller">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="showEditDialog(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)" :disabled="row.tellerNo === currentTellerNo">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="柜员编号" prop="tellerNo">
          <el-input v-model="form.tellerNo" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="柜员姓名" prop="tellerName">
          <el-input v-model="form.tellerName" />
        </el-form-item>
        <el-form-item label="机构编号" prop="institutionNo" v-if="!isEdit">
          <el-input v-model="form.institutionNo" />
        </el-form-item>
        <el-form-item label="柜员类型" prop="tellerType">
          <el-select v-model="form.tellerType">
            <el-option label="普通柜员" value="NORMAL" />
            <el-option label="库管柜员" value="VAULT" />
          </el-select>
        </el-form-item>
        <el-form-item label="密码" :rules="isEdit ? [] : [{ required: true, message: '密码不能为空', trigger: 'blur' }]">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-form-item label="状态" v-if="isEdit">
          <el-select v-model="form.status">
            <el-option label="正常" value="NORMAL" />
            <el-option label="停用" value="STOPPED" />
          </el-select>
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
import { ref, computed, onMounted } from 'vue'
import { getTellerList, createTeller, updateTeller, deleteTeller } from '../../api/teller'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '../../stores/user'

const userStore = useUserStore()
const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const form = ref({
  tellerNo: '',
  tellerName: '',
  institutionNo: '',
  tellerType: 'NORMAL',
  password: '',
  status: 'NORMAL'
})

const rules = {
  tellerNo: [{ required: true, message: '请输入柜员编号', trigger: 'blur' }],
  tellerName: [{ required: true, message: '请输入柜员姓名', trigger: 'blur' }],
  institutionNo: [{ required: true, message: '请输入机构编号', trigger: 'blur' }],
  tellerType: [{ required: true, message: '请选择柜员类型', trigger: 'change' }]
}

const currentTellerNo = computed(() => userStore.tellerNo)
const isVaultTeller = computed(() => {
  return tableData.value.some(t => t.tellerNo === currentTellerNo.value && t.vault)
})

const dialogTitle = computed(() => isEdit.value ? '编辑柜员' : '新增柜员')

const loadTellers = async () => {
  try {
    const res = await getTellerList()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    tellerNo: '',
    tellerName: '',
    institutionNo: userStore.institutionNo,
    tellerType: 'NORMAL',
    password: '',
    status: 'NORMAL'
  }
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  isEdit.value = true
  form.value = {
    tellerNo: row.tellerNo,
    tellerName: row.tellerName,
    institutionNo: row.institutionNo,
    tellerType: row.tellerType,
    password: '',
    status: row.status
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          const data = { ...form.value }
          if (!data.password) {
            delete data.password
          }
          await updateTeller(form.value.tellerNo, data)
          ElMessage.success('修改成功')
        } else {
          await createTeller(form.value)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        loadTellers()
      } catch (error) {
        ElMessage.error(error.message || '操作失败')
      }
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除柜员 ${row.tellerName} 吗？`, '提示', {
      type: 'warning'
    })
    await deleteTeller(row.tellerNo)
    ElMessage.success('删除成功')
    loadTellers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

onMounted(() => {
  loadTellers()
})
</script>

<style scoped>
.page-header{margin-bottom:24px}
.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0}
</style>
