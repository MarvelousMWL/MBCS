<template>
  <div class="page">
    <div class="page-header">
      <div><h2 class="page-title">产品拷贝</h2><p class="page-desc">选择源产品拷贝全部参数，自定义修改后保存为新产品</p></div>
    </div>

    <!-- 第一步：选择源产品 -->
    <el-card v-if="!sourceLoaded" style="max-width:500px">
      <h3 style="margin:0 0 16px;font-size:16px">选择源产品</h3>
      <el-form label-width="90px">
        <el-form-item label="源产品">
          <el-select v-model="sourceCode" filterable placeholder="请选择源产品（支持搜索）" style="width:100%" @change="loadSource">
            <el-option v-for="p in products" :key="p.productCode" :label="p.productCode + ' - ' + p.productName" :value="p.productCode"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="startCopy" :disabled="!sourceCode">开始拷贝</el-button>
        </el-form-item>
      </el-form>
      <el-empty v-if="products.length===0" description="暂无可拷贝的产品" :image-size="80"/>
    </el-card>

    <!-- 第二步：自定义参数 -->
    <div v-if="sourceLoaded">
      <el-alert :title="'正在拷贝: ' + source.productCode + ' - ' + source.productName" type="info" :closable="false" show-icon style="margin-bottom:20px"/>

      <el-form ref="formRef" :model="form" label-width="120px">
        <el-tabs v-model="activeTab">
          <!-- 基础属性 -->
          <el-tab-pane label="基础属性" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="新产品代码" prop="productCode" :rules="[{required:true,message:'请输入产品代码'}]">
                  <el-input v-model="form.productCode" placeholder="如 DP006"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="产品名称" prop="productName" :rules="[{required:true,message:'请输入产品名称'}]">
                  <el-input v-model="form.productName"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="生效日期" prop="effectiveDate" :rules="[{required:true}]">
                  <el-date-picker v-model="form.effectiveDate" type="date" style="width:100%"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="失效日期" prop="expiryDate" :rules="[{required:true}]">
                  <el-date-picker v-model="form.expiryDate" type="date" style="width:100%"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="产品状态"><el-select v-model="form.productStatus" style="width:100%">
                  <el-option label="正常" value="0"/><el-option label="待生效" value="1"/><el-option label="注销" value="2"/>
                </el-select></el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="定活标志"><el-select v-model="form.currentFixedFlag" style="width:100%">
                  <el-option label="活期" value="0"/><el-option label="定期" value="1"/>
                </el-select></el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="所属对象"><el-select v-model="form.customerType" style="width:100%">
                  <el-option label="对公" value="CORPORATE"/><el-option label="同业" value="TRADE"/>
                </el-select></el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="存款种类"><el-select v-model="form.depositType" style="width:100%">
                  <el-option v-for="(v,k) in typeMap" :key="k" :label="v" :value="k"/>
                </el-select></el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="默认币种"><el-input v-model="form.defaultCurrency" placeholder="156"/></el-form-item>
              </el-col>
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
              <el-col :span="8"><el-form-item label="现金通兑"><el-switch v-model="form.cashWithdrawalFlag" active-value="1" inactive-value="0"/></el-form-item></el-col>
              <el-col :span="8"><el-form-item label="转账通兑"><el-switch v-model="form.ttWithdrawalFlag" active-value="1" inactive-value="0"/></el-form-item></el-col>
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
              <el-table-column label="控制对象" width="220">
                <template #default="{ row: r }"><el-select v-model="r.controlObject" style="width:100%">
                  <el-option label="账户分类控制" value="ACCOUNT_CLASSIFY"/><el-option label="存期控制" value="TERM_CONTROL"/>
                  <el-option label="利息定义" value="INTEREST_DEF"/><el-option label="存入控制" value="DEPOSIT_CTRL"/><el-option label="支取控制" value="WITHDRAW_CTRL"/>
                </el-select></template>
              </el-table-column>
              <el-table-column label="币种规则" width="220">
                <template #default="{ row: r }"><el-select v-model="r.currencyRule" style="width:100%">
                  <el-option label="不分币种" value="1"/><el-option label="按外币" value="2"/><el-option label="按本币" value="3"/>
                  <el-option label="按交易币种" value="4"/><el-option label="指定币种" value="5"/><el-option label="分本外币" value="6"/>
                </el-select></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="controls.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="controls.length===0" description="无对象控制配置（从源产品继承）" :image-size="60"/>
          </el-tab-pane>
        </el-tabs>

        <div style="margin-top:24px;display:flex;gap:12px;justify-content:flex-end">
          <el-button @click="sourceLoaded=false;sourceCode=''">返回重选</el-button>
          <el-button type="primary" size="large" @click="handleSave" :loading="saving">保存为新产品</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage } from 'element-plus'
import { getProductList, getProduct, createProduct } from '../../api/product'
import { getCfg } from '../../api/product'
var products=ref([]),source=ref(null),sourceCode=ref(''),sourceLoaded=ref(false),saving=ref(false),activeTab=ref('basic'),formRef=ref(null)
var controls=ref([])
var typeMap={'00':'单位活期存款','02':'单位定期存款','04':'国库定期存款','05':'单位通知存款','06':'单位保证金活期','07':'单位保证金定期','09':'单位协议存款','14':'同业存放活期','15':'同业存放定期'}
var form=reactive({productCode:'',productName:'',effectiveDate:'',expiryDate:'',productStatus:'0',currentFixedFlag:'0',customerType:'CORPORATE',productType:'TRADITIONAL',depositType:'00',defaultCurrency:'156',withdrawScope:'BRANCH',depositScope:'ALL',cashWithdrawalFlag:'0',ttWithdrawalFlag:'0',maturityFlag:'0',overdraftFlag:'0',chargeFlag:'0',accountClassifyFlag:'0',channelCtrlMode:'0',currencyCtrlMode:'0',institutionCtrlMode:'0',customerCtrlMode:'0',voucherCtrlMode:'0',termCtrlMode:'0',simpleInterestFlag:'0',settlementFlag:'0',exchangeFlag:'0',exchangeSellFlag:'0',balanceSyncFlag:'1',formTransferFlag:'0'})
onMounted(async()=>{try{var r=await getProductList();products.value=r.data||[]}catch(e){}})
var loadSource=async()=>{try{var r=await getProduct(sourceCode.value);source.value=r.data}catch(e){}}
var startCopy=()=>{
  if(!source.value)return
  Object.assign(form,{...source.value,productCode:'',productName:''})
  delete form.id;delete form.createdAt;delete form.updatedAt
  getProductControls(sourceCode.value).then(r=>{controls.value=r.data||[]}).catch(()=>{controls.value=[]})
  sourceLoaded.value=true;activeTab.value='basic'
}
var handleSave=async()=>{await formRef.value?.validate(async ok=>{if(!ok)return;saving.value=true;try{await createProduct({...form,controls:controls.value,termControls:termControls.value,openControls:openControls.value,withdrawControls:withdrawControls.value,maturityControls:maturityControls.value,formTransfers:formTransfers.value});ElMessage.success('拷贝成功！新产品代码: '+form.productCode);sourceLoaded.value=false;sourceCode.value=''}catch(e){}finally{saving.value=false}})}
</script>
<style scoped>
.page-header{margin-bottom:24px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
</style>



