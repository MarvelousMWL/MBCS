<template>
  <div>
    <div class="page-header"><h2 class="page-title">机构管理</h2></div>
    <el-button type="primary" style="margin-bottom: 10px" @click="showAddDialog">新增机构</el-button>
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="institutionNo" label="机构编号" width="120" />
      <el-table-column prop="institutionName" label="机构名称" width="200" />
      <el-table-column prop="institutionLevel" label="机构级别" width="120">
        <template #default="{ row }">
          <el-tag v-if="row.institutionLevel === 'HEADQUARTERS'" type="danger">总行</el-tag>
          <el-tag v-else-if="row.institutionLevel === 'BRANCH'" type="success">分行</el-tag>
          <el-tag v-else type="warning">支行</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="parentInstitutionNo" label="上级机构" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.normal ? 'success' : 'danger'">{{ row.normal ? '正常' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="showEditDialog(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="机构编号" prop="institutionNo">
          <el-input v-model="form.institutionNo" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="机构名称" prop="institutionName">
          <el-input v-model="form.institutionName" />
        </el-form-item>
        <el-form-item label="机构级别" prop="institutionLevel">
          <el-select v-model="form.institutionLevel" placeholder="请选择机构级别">
            <el-option label="总行" value="HEADQUARTERS" />
            <el-option label="分行" value="BRANCH" />
            <el-option label="支行" value="SUBBRANCH" />
          </el-select>
        </el-form-item>
        <el-form-item label="上级机构编号" prop="parentInstitutionNo">
          <el-input v-model="form.parentInstitutionNo" />
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
import { getInstitutionList, createInstitution, updateInstitution, deleteInstitution } from '../../api/institution'

const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()

const form = reactive({
  institutionNo: '',
  institutionName: '',
  institutionLevel: '',
  parentInstitutionNo: ''
})

const rules = {
  institutionNo: [{ required: true, message: '请输入机构编号', trigger: 'blur' }],
  institutionName: [{ required: true, message: '请输入机构名称', trigger: 'blur' }],
  institutionLevel: [{ required: true, message: '请选择机构级别', trigger: 'change' }]
}

const dialogTitle = computed(() => isEdit.value ? '编辑机构' : '新增机构')

const loadData = async () => {
  try {
    const res = await getInstitutionList()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const resetForm = () => {
  form.institutionNo = ''
  form.institutionName = ''
  form.institutionLevel = ''
  form.parentInstitutionNo = ''
}

const showAddDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  isEdit.value = true
  form.institutionNo = row.institutionNo
  form.institutionName = row.institutionName
  form.institutionLevel = row.institutionLevel
  form.parentInstitutionNo = row.parentInstitutionNo
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateInstitution(form.institutionNo, form)
          ElMessage.success('修改成功')
        } else {
          await createInstitution(form)
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
    await ElMessageBox.confirm(`确定要删除机构 ${row.institutionName} 吗？`, '提示', { type: 'warning' })
    await deleteInstitution(row.institutionNo)
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

<style scoped>
.page-header{margin-bottom:24px}
.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0}
</style>
