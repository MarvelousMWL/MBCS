<template>
  <div class="page">
    <div class="page-header">
      <div><h2 class="page-title">产品工厂</h2><p class="page-desc">产品参数配置管理，定义存款产品的属性与规则</p></div>
      <el-button type="primary" @click="openCreate">新建产品</el-button>
    </div>
    <el-card>
      <el-table :data="products" v-loading="loading" stripe>
        <el-table-column prop="productCode" label="产品代码" width="100"/>
        <el-table-column prop="productDescription" label="产品名称" width="160"/>
        <el-table-column label="存款种类" width="110">
          <template #default="{ row: r }">{{ typeMap[r.depositType] || r.depositType }}</template>
        </el-table-column>
        <el-table-column label="类型" width="70">
          <template #default="{ row: r }">{{ r.currentFixedFlag==="0" ? "活期" : "定期" }}</template>
        </el-table-column>
        <el-table-column label="对象" width="80">
          <template #default="{ row: r }">{{ r.customerType==="CORPORATE" ? "对公" : "同业" }}</template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row: r }">
            <el-tag size="small" :type="r.productStatus==='0'?'success':r.productStatus==='1'?'warning':'info'" style="border:0">{{ {0:'正常',1:'待生效',2:'注销'}[r.productStatus] }}</el-tag></template>
        </el-table-column>
        <el-table-column label="生效期间" width="230">
          <template #default="{ row: r }">{{ r.effectiveDate }} ~ {{ r.expiryDate }}</template>
        </el-table-column>
        <el-table-column label="结算户" width="70">
          <template #default="{ row: r }">{{ r.settlementFlag==="1" ? "是" : "否" }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row: r }">
            <el-button size="small" @click="openEdit(r)">编辑</el-button>
            <el-button size="small" type="primary" plain @click="viewDetail(r)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit?'编辑产品':'新建产品'" width="960px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" label-width="120px">
        <el-tabs v-model="activeTab">
          <!-- 基础属性 -->
          <el-tab-pane label="基础属性" name="basic">
            <el-row :gutter="20">
              <el-col :span="12"><el-form-item label="产品代码" prop="productCode" :rules="[{required:true}]">
                <el-input v-model="form.productCode" :disabled="isEdit" placeholder="如 DP001"/></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="产品说明" prop="productDescription" :rules="[{required:true}]">
                <el-input v-model="form.productDescription"/></el-form-item></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12"><el-form-item label="生效日期" prop="effectiveDate" :rules="[{required:true}]">
                <el-date-picker v-model="form.effectiveDate" type="date" style="width:100%"/></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="失效日期" prop="expiryDate" :rules="[{required:true}]">
                <el-date-picker v-model="form.expiryDate" type="date" style="width:100%"/></el-form-item></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8"><el-form-item label="产品状态"><el-select v-model="form.productStatus" style="width:100%">
                <el-option label="正常" value="0"/><el-option label="待生效" value="1"/><el-option label="注销" value="2"/>
              </el-select></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="定活标志"><el-select v-model="form.currentFixedFlag" style="width:100%">
                <el-option label="活期" value="0"/><el-option label="定期" value="1"/>
              </el-select></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="所属对象"><el-select v-model="form.customerType" style="width:100%">
                <el-option label="对公" value="CORPORATE"/><el-option label="同业" value="TRADE"/>
              </el-select></el-form-item></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12"><el-form-item label="存款种类"><el-select v-model="form.depositType" style="width:100%">
                <el-option v-for="(v,k) in typeMap" :key="k" :label="v" :value="k"/>
              </el-select></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="默认币种"><el-input v-model="form.defaultCurrency" placeholder="156"/></el-form-item></el-col>
            </el-row>
          </el-tab-pane>

          <!-- 总控属性 -->
          <el-tab-pane label="总控属性" name="control">
            <el-row :gutter="20">
              <el-col :span="8"><el-form-item label="通兑范围"><el-select v-model="form.withdrawScope" style="width:100%">
                <el-option label="开户机构" value="BRANCH"/><el-option label="分行" value="REGION"/><el-option label="全行" value="ALL"/>
              </el-select></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="通存范围"><el-select v-model="form.depositScope" style="width:100%">
                <el-option label="开户机构" value="BRANCH"/><el-option label="分行" value="REGION"/><el-option label="全行" value="ALL"/>
              </el-select></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="结算户"><el-switch v-model="form.settlementFlag" active-value="1" inactive-value="0"/></el-form-item></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8"><el-form-item label="现金通兑"><el-switch v-model="form.cashExchangeFlag" active-value="1" inactive-value="0"/></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="转账通兑"><el-switch v-model="form.transferExchangeFlag" active-value="1" inactive-value="0"/></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="到期定义"><el-switch v-model="form.maturityFlag" active-value="1" inactive-value="0"/></el-form-item></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8"><el-form-item label="形态转移"><el-switch v-model="form.formTransferFlag" active-value="1" inactive-value="0"/></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="余额同步"><el-switch v-model="form.balanceSyncFlag" active-value="1" inactive-value="0"/></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="允许结汇"><el-switch v-model="form.exchangeFlag" active-value="1" inactive-value="0"/></el-form-item></el-col>
            </el-row>
            <el-divider style="margin:8px 0"/>
            <el-row :gutter="20">
              <el-col :span="8"><el-form-item label="渠道控制"><el-select v-model="form.channelCtrlMode" style="width:100%">
                <el-option label="不控制" value="0"/><el-option label="限制控制" value="1"/><el-option label="排除控制" value="2"/>
              </el-select></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="币种控制"><el-select v-model="form.currencyCtrlMode" style="width:100%">
                <el-option label="不控制" value="0"/><el-option label="限制控制" value="1"/><el-option label="排除控制" value="2"/>
              </el-select></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="机构控制"><el-select v-model="form.institutionCtrlMode" style="width:100%">
                <el-option label="不控制" value="0"/><el-option label="限制控制" value="1"/><el-option label="排除控制" value="2"/>
              </el-select></el-form-item></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8"><el-form-item label="客户控制"><el-select v-model="form.customerCtrlMode" style="width:100%">
                <el-option label="不控制" value="0"/><el-option label="限制控制" value="1"/><el-option label="排除控制" value="2"/>
              </el-select></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="凭证控制"><el-select v-model="form.voucherCtrlMode" style="width:100%">
                <el-option label="不控制" value="0"/><el-option label="限制控制" value="1"/><el-option label="排除控制" value="2"/>
              </el-select></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="存期控制"><el-select v-model="form.termCtrlMode" style="width:100%">
                <el-option label="不定存期" value="0"/><el-option label="产品控制" value="1"/><el-option label="自定义" value="2"/>
              </el-select></el-form-item></el-col>
            </el-row>
          </el-tab-pane>

          <!-- 对象控制 -->
          <el-tab-pane label="对象控制" name="object">
            <div style="margin-bottom:12px"><el-button size="small" @click="controls.push({controlObject:'ACCOUNT_CLASSIFY',currencyRule:'1'})">添加控制项</el-button></div>
            <el-table :data="controls" size="small">
              <el-table-column label="控制对象" width="220"><template #default="{ row: r }"><el-select v-model="r.controlObject" style="width:100%">
                <el-option label="账户分类控制" value="ACCOUNT_CLASSIFY"/><el-option label="存期控制" value="TERM_CONTROL"/>
                <el-option label="利息定义" value="INTEREST_DEF"/><el-option label="存入控制" value="DEPOSIT_CTRL"/><el-option label="支取控制" value="WITHDRAW_CTRL"/>
              </el-select></template></el-table-column>
              <el-table-column label="币种规则" width="220"><template #default="{ row: r }"><el-select v-model="r.currencyRule" style="width:100%">
                <el-option label="不分币种" value="1"/><el-option label="按外币" value="2"/><el-option label="按本币" value="3"/>
                <el-option label="按交易币种" value="4"/><el-option label="指定币种" value="5"/><el-option label="分本外币" value="6"/>
              </el-select></template></el-table-column>
              <el-table-column label="操作" width="80"><template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="controls.splice($index,1)">删除</el-button></template></el-table-column>
            </el-table>
            <el-empty v-if="controls.length===0" description="暂无对象控制配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 详细配置 -->
          <el-tab-pane label="详细配置" name="detail">
            <!-- 存期控制 -->
            <div v-if="form.termCtrlMode!=='0'" class="cfg-section">
              <h4 class="cfg-title">存期控制</h4>
              <el-table :data="termControls" size="small">
                <el-table-column label="存期代码" width="120"><template #default="{ row: r }"><el-input v-model="r.termCode" size="small"/></template></el-table-column>
                <el-table-column label="存期名称" width="150"><template #default="{ row: r }"><el-input v-model="r.termName" size="small"/></template></el-table-column>
                <el-table-column label="存期天数" width="120"><template #default="{ row: r }"><el-input-number v-model="r.termDays" :min="1" size="small" style="width:100%"/></template></el-table-column>
                <el-table-column label="操作" width="80"><template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="termControls.splice($index,1)">删除</el-button></template></el-table-column>
              </el-table>
              <el-button size="small" style="margin-top:8px" @click="termControls.push({termCode:'',termName:'',termDays:30})">添加存期</el-button>
              <el-empty v-if="termControls.length===0" description="未配置存期" :image-size="50"/>
            </div>

            <!-- 开户控制 -->
            <div class="cfg-section">
              <h4 class="cfg-title">开户控制</h4>
              <el-table :data="openControls" size="small">
                <el-table-column label="客户账号生成规则" width="180"><template #default="{ row: r }"><el-select v-model="r.customerAccountGenRule" style="width:100%"><el-option label="不指定" value="0"/><el-option label="指定" value="1"/></el-select></template></el-table-column>
                <el-table-column label="开户限制" width="120"><template #default="{ row: r }"><el-switch v-model="r.restrictFlag" active-value="1" inactive-value="0"/></template></el-table-column>
                <el-table-column label="限制种类" width="150"><template #default="{ row: r }"><el-input v-model="r.restrictType" size="small" placeholder="33-启用控制"/></template></el-table-column>
                <el-table-column label="优先级" width="100"><template #default="{ row: r }"><el-input-number v-model="r.priority" :min="0" size="small" style="width:100%"/></template></el-table-column>
                <el-table-column label="操作" width="80"><template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="openControls.splice($index,1)">删除</el-button></template></el-table-column>
              </el-table>
              <el-button size="small" style="margin-top:8px" @click="openControls.push({customerAccountGenRule:'0',restrictFlag:'0',restrictType:'33',priority:0})">添加开户控制</el-button>
              <el-empty v-if="openControls.length===0" description="未配置开户控制" :image-size="50"/>
            </div>

            <!-- 支取控制 -->
            <div class="cfg-section">
              <h4 class="cfg-title">支取控制</h4>
              <el-table :data="withdrawControls" size="small">
                <el-table-column label="最小留存余额" width="180"><template #default="{ row: r }"><el-input-number v-model="r.minBalance" :min="0" :precision="2" size="small" style="width:100%"/></template></el-table-column>
                <el-table-column label="支取方式" width="200"><template #default="{ row: r }"><el-select v-model="r.withdrawMethod" style="width:100%">
                  <el-option label="全部" value="ALL"/><el-option label="部分" value="PARTIAL"/><el-option label="不可支取" value="NONE"/>
                </el-select></template></el-table-column>
                <el-table-column label="操作" width="80"><template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="withdrawControls.splice($index,1)">删除</el-button></template></el-table-column>
              </el-table>
              <el-button size="small" style="margin-top:8px" @click="withdrawControls.push({minBalance:0,withdrawMethod:'0'})">添加支取控制</el-button>
              <el-empty v-if="withdrawControls.length===0" description="未配置支取控制" :image-size="50"/>
            </div>

            <!-- 到期控制（定期产品） -->
            <div v-if="form.maturityFlag==='1'" class="cfg-section">
              <h4 class="cfg-title">到期控制</h4>
              <el-table :data="maturityControls" size="small">
                <el-table-column label="到期处理方式" width="180"><template #default="{ row: r }"><el-select v-model="r.processMethod" style="width:100%"><el-option label="自动兑付" value="0"/><el-option label="手动处理" value="1"/><el-option label="续存" value="2"/></el-select></template></el-table-column>
                <el-table-column label="自动续存" width="120"><template #default="{ row: r }"><el-switch v-model="r.renewFlag" active-value="1" inactive-value="0"/></template></el-table-column>
                <el-table-column label="续存次数" width="120"><template #default="{ row: r }"><el-input-number v-model="r.renewTimes" :min="0" size="small" style="width:100%"/></template></el-table-column>
                <el-table-column label="操作" width="80"><template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="maturityControls.splice($index,1)">删除</el-button></template></el-table-column>
              </el-table>
              <el-button size="small" style="margin-top:8px" @click="maturityControls.push({processMethod:'0',renewFlag:'0',renewTimes:0})">添加到期控制</el-button>
              <el-empty v-if="maturityControls.length===0" description="未配置到期控制" :image-size="50"/>
            </div>

            <!-- 形态转移（活期产品） -->
            <div v-if="form.formTransferFlag==='1'" class="cfg-section">
              <h4 class="cfg-title">形态转移</h4>
              <el-table :data="formTransfers" size="small">
                <el-table-column label="转移类型" width="150"><template #default="{ row: r }"><el-select v-model="r.transferType" style="width:100%"><el-option label="正常转久悬" value="0"/><el-option label="久悬转营业外" value="1"/></el-select></template></el-table-column>
                <el-table-column label="起始日期方式" width="150"><template #default="{ row: r }"><el-select v-model="r.startDateMethod" style="width:100%"><el-option label="开户日" value="1"/><el-option label="上次结息日" value="2"/><el-option label="最近交易日" value="3"/></el-select></template></el-table-column>
                <el-table-column label="转移周期(天)" width="130"><template #default="{ row: r }"><el-input-number v-model="r.transferCycle" :min="1" size="small" style="width:100%"/></template></el-table-column>
                <el-table-column label="转移后计息" width="100"><template #default="{ row: r }"><el-switch v-model="r.interestFlag" active-value="1" inactive-value="0"/></template></el-table-column>
                <el-table-column label="不动户转正常" width="110"><template #default="{ row: r }"><el-switch v-model="r.dormantToNormalFlag" active-value="1" inactive-value="0"/></template></el-table-column>
                <el-table-column label="操作" width="80"><template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="formTransfers.splice($index,1)">删除</el-button></template></el-table-column>
              </el-table>
              <el-button size="small" style="margin-top:8px" @click="formTransfers.push({transferType:'0',startDateMethod:'3',transferCycle:365,interestFlag:'0',limitAmount:0,dormantToNormalFlag:'1'})">添加形态转移</el-button>
              <el-empty v-if="formTransfers.length===0" description="未配置形态转移" :image-size="50"/>
            </div>
          </el-tab-pane>
        </el-tabs>
        <template #footer>
          <el-button @click="dialogVisible=false">取消</el-button>
          <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
        </template>
      </el-form>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="产品详情" width="750px">
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="产品代码" span="2">{{ detail.productCode }}</el-descriptions-item>
        <el-descriptions-item label="产品名称" span="2">{{ detail.productDescription }}</el-descriptions-item>
        <el-descriptions-item label="存款种类">{{ typeMap[detail.depositType] }}</el-descriptions-item>
        <el-descriptions-item label="定活类型">{{ detail.currentFixedFlag==='0'?'活期':'定期' }}</el-descriptions-item>
        <el-descriptions-item label="所属对象">{{ detail.customerType==='CORPORATE'?'对公':'同业' }}</el-descriptions-item>
        <el-descriptions-item label="产品状态">{{ {0:'正常',1:'待生效',2:'注销'}[detail.productStatus] }}</el-descriptions-item>
        <el-descriptions-item label="生效日期">{{ detail.effectiveDate }}</el-descriptions-item><el-descriptions-item label="失效日期">{{ detail.expiryDate }}</el-descriptions-item>
        <el-descriptions-item label="通兑范围">{{ {BRANCH:'开户机构',REGION:'分行',ALL:'全行'}[detail.withdrawScope] }}</el-descriptions-item>
        <el-descriptions-item label="通存范围">{{ {BRANCH:'开户机构',REGION:'分行',ALL:'全行'}[detail.depositScope] }}</el-descriptions-item>
        <el-descriptions-item label="结算户">{{ detail.settlementFlag==='1'?'是':'否' }}</el-descriptions-item>
        <el-descriptions-item label="形态转移">{{ detail.formTransferFlag==='1'?'是':'否' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getProductList, getProduct, createProduct, updateProduct, getCfg } from '../../api/product'

var products = ref([]), loading = ref(false), dialogVisible = ref(false), detailVisible = ref(false)
var isEdit = ref(false), saving = ref(false), activeTab = ref('basic'), detail = ref(null), formRef = ref(null)

var form = reactive({
  productCode:'', productDescription:'', effectiveDate:'', expiryDate:'', productStatus:'0',
  currentFixedFlag:'0', customerType:'CORPORATE', productType:'TRADITIONAL', depositType:'00',
  defaultCurrency:'156', withdrawScope:'0', depositScope:'0', cashExchangeFlag:'0',
  transferExchangeFlag:'0', maturityFlag:'0', overdraftFlag:'0', chargeFlag:'0', accountClassifyFlag:'0',
  channelCtrlMode:'0', currencyCtrlMode:'0', institutionCtrlMode:'0', customerCtrlMode:'0',
  voucherCtrlMode:'0', termCtrlMode:'0', simpleInterestFlag:'0', settlementFlag:'0',
  exchangeFlag:'0', autoExchangeSellFlag:'0', balanceSyncFlag:'1', formTransferFlag:'0'
})

var controls = ref([]), termControls = ref([]), openControls = ref([]), withdrawControls = ref([])
var maturityControls = ref([]), formTransfers = ref([])
var channelControls = ref([]), currencyControls = ref([]), institutionControls = ref([])
var customerControls = ref([]), voucherControls = ref([]), accountingControls = ref([])

var typeMap = { '00':'单位活期存款','02':'单位定期存款','04':'国库定期存款','05':'单位通知存款','06':'单位保证金活期','07':'单位保证金定期','09':'单位协议存款','14':'同业存放活期','15':'同业存放定期' }

async function loadData() {
  loading.value = true
  try { var r = await getProductList(); products.value = r.data || [] } catch (e) {}
  loading.value = false
}

function resetForm() {
  Object.assign(form, {
    productCode:'', productDescription:'', effectiveDate:'', expiryDate:'', productStatus:'0',
    currentFixedFlag:'0', customerType:'CORPORATE', depositType:'00', defaultCurrency:'156',
    withdrawScope:'0', depositScope:'0', cashExchangeFlag:'0', transferExchangeFlag:'0',
    maturityFlag:'0', overdraftFlag:'0', chargeFlag:'0', accountClassifyFlag:'0',
    channelCtrlMode:'0', currencyCtrlMode:'0', institutionCtrlMode:'0', customerCtrlMode:'0',
    voucherCtrlMode:'0', termCtrlMode:'0', simpleInterestFlag:'0', settlementFlag:'0',
    exchangeFlag:'0', autoExchangeSellFlag:'0', balanceSyncFlag:'1', formTransferFlag:'0'
  })
  controls.value = []; termControls.value = []; openControls.value = []; withdrawControls.value = []
  maturityControls.value = []; formTransfers.value = []; channelControls.value = []
  currencyControls.value = []; institutionControls.value = []; customerControls.value = []
  voucherControls.value = []; accountingControls.value = []
  activeTab.value = 'basic'
}

function openCreate() { resetForm(); isEdit.value = false; dialogVisible.value = true }

async function openEdit(row) {
  resetForm()
  Object.assign(form, row)
  isEdit.value = true
  dialogVisible.value = true
  var code = row.productCode
  async function loadCfg(ep, ref) { try { var r = await getCfg(code, ep); ref.value = r.data || [] } catch (e) {} }
  await loadCfg('controls', controls)
  await loadCfg('term-controls', termControls)
  await loadCfg('open-controls', openControls)
  await loadCfg('withdraw-controls', withdrawControls)
  await loadCfg('maturity-controls', maturityControls)
  await loadCfg('form-transfers', formTransfers)
  await loadCfg('channel-controls', channelControls)
  await loadCfg('currency-controls', currencyControls)
  await loadCfg('institution-controls', institutionControls)
  await loadCfg('customer-controls', customerControls)
  await loadCfg('voucher-controls', voucherControls)
  await loadCfg('accounting-controls', accountingControls)
}

async function viewDetail(row) {
  try { var r = await getProduct(row.productCode); detail.value = r.data; detailVisible.value = true } catch (e) {}
}

function buildPayload() {
  return {
    ...form,
    controls: controls.value,
    termControls: termControls.value,
    openControls: openControls.value,
    withdrawControls: withdrawControls.value,
    maturityControls: maturityControls.value,
    formTransfers: formTransfers.value,
    channelControls: channelControls.value,
    currencyControls: currencyControls.value,
    institutionControls: institutionControls.value,
    customerControls: customerControls.value,
    voucherControls: voucherControls.value,
    accountingControls: accountingControls.value
  }
}

async function handleSave() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch (e) {
    return
  }
  saving.value = true
  try {
    var data = buildPayload()
    if (isEdit.value) {
      await updateProduct(form.productCode, data)
      ElMessage.success('更新成功')
    } else {
      await createProduct(data)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
  saving.value = false
}

onMounted(function () { loadData() })
</script>
<style scoped>
.page-header{margin-bottom:20px;display:flex;justify-content:space-between;align-items:flex-start}
.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}
.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
.cfg-section{border:1px solid #edf2f7;border-radius:8px;padding:16px;margin-bottom:16px}
.cfg-title{font-size:14px;font-weight:600;margin:0 0 12px;color:var(--text-primary);display:flex;align-items:center;gap:8px}
.cfg-title:before{content:'';display:inline-block;width:3px;height:14px;background:#3182ce;border-radius:2px}
</style>


