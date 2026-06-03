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
                  <el-input v-model="form.productDescription"/>
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
          <!-- 存期控制 -->
          <el-tab-pane label="存期控制" name="term">
            <div style="margin-bottom:12px"><el-button size="small" @click="termControls.push({termCode:'',termName:'',termDays:null})">添加存期</el-button></div>
            <el-table :data="termControls" size="small">
              <el-table-column label="存期代码" width="160">
                <template #default="{ row: r }"><el-input v-model="r.termCode" placeholder="如 3M"/></template>
              </el-table-column>
              <el-table-column label="存期名称" width="200">
                <template #default="{ row: r }"><el-input v-model="r.termName" placeholder="如 三个月"/></template>
              </el-table-column>
              <el-table-column label="天数" width="120">
                <template #default="{ row: r }"><el-input-number v-model="r.termDays" :min="1" style="width:100%"/></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="termControls.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="termControls.length===0" description="无存期控制配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 开户控制 -->
          <el-tab-pane label="开户控制" name="open">
            <div style="margin-bottom:12px"><el-button size="small" @click="openControls.push({customerAccountGenRule:'',restrictFlag:'0',restrictType:'',priority:0})">添加开户控制</el-button></div>
            <el-table :data="openControls" size="small">
              <el-table-column label="账号生成规则" width="200">
                <template #default="{ row: r }"><el-input v-model="r.customerAccountGenRule"/></template>
              </el-table-column>
              <el-table-column label="限制标志" width="120">
                <template #default="{ row: r }"><el-select v-model="r.restrictFlag" style="width:100%"><el-option label="不限" value="0"/><el-option label="限制" value="1"/></el-select></template>
              </el-table-column>
              <el-table-column label="限制类型" width="200">
                <template #default="{ row: r }"><el-input v-model="r.restrictType"/></template>
              </el-table-column>
              <el-table-column label="优先级" width="100">
                <template #default="{ row: r }"><el-input-number v-model="r.priority" :min="0" style="width:100%"/></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="openControls.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="openControls.length===0" description="无开户控制配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 支取控制 -->
          <el-tab-pane label="支取控制" name="withdraw">
            <div style="margin-bottom:12px"><el-button size="small" @click="withdrawControls.push({minBalance:null,withdrawMethod:''})">添加支取控制</el-button></div>
            <el-table :data="withdrawControls" size="small">
              <el-table-column label="最低余额" width="200">
                <template #default="{ row: r }"><el-input-number v-model="r.minBalance" :min="0" :precision="2" style="width:100%"/></template>
              </el-table-column>
              <el-table-column label="支取方式" width="200">
                <template #default="{ row: r }"><el-input v-model="r.withdrawMethod" placeholder="如 凭证支取"/></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="withdrawControls.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="withdrawControls.length===0" description="无支取控制配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 到期控制 -->
          <el-tab-pane label="到期控制" name="maturity">
            <div style="margin-bottom:12px"><el-button size="small" @click="maturityControls.push({processMethod:'',renewFlag:'0',renewTimes:0})">添加到期控制</el-button></div>
            <el-table :data="maturityControls" size="small">
              <el-table-column label="处理方式" width="220">
                <template #default="{ row: r }"><el-input v-model="r.processMethod" placeholder="如 自动转存"/></template>
              </el-table-column>
              <el-table-column label="续存标志" width="120">
                <template #default="{ row: r }"><el-select v-model="r.renewFlag" style="width:100%"><el-option label="不续存" value="0"/><el-option label="续存" value="1"/></el-select></template>
              </el-table-column>
              <el-table-column label="续存次数" width="120">
                <template #default="{ row: r }"><el-input-number v-model="r.renewTimes" :min="0" style="width:100%"/></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="maturityControls.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="maturityControls.length===0" description="无到期控制配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 过户登记 -->
          <el-tab-pane label="过户登记" name="transfer">
            <div style="margin-bottom:12px"><el-button size="small" @click="formTransfers.push({transferType:'',startDateMethod:'',transferCycle:0,interestFlag:'0',limitAmount:null,dormantToNormalFlag:'0'})">添加过户登记</el-button></div>
            <el-table :data="formTransfers" size="small">
              <el-table-column label="过户类型" width="140">
                <template #default="{ row: r }"><el-input v-model="r.transferType"/></template>
              </el-table-column>
              <el-table-column label="起息方式" width="140">
                <template #default="{ row: r }"><el-input v-model="r.startDateMethod"/></template>
              </el-table-column>
              <el-table-column label="过户周期" width="120">
                <template #default="{ row: r }"><el-input-number v-model="r.transferCycle" :min="0" style="width:100%"/></template>
              </el-table-column>
              <el-table-column label="计息标志" width="120">
                <template #default="{ row: r }"><el-select v-model="r.interestFlag" style="width:100%"><el-option label="不计息" value="0"/><el-option label="计息" value="1"/></el-select></template>
              </el-table-column>
              <el-table-column label="限额" width="140">
                <template #default="{ row: r }"><el-input-number v-model="r.limitAmount" :min="0" :precision="2" style="width:100%"/></template>
              </el-table-column>
              <el-table-column label="不动户转正常" width="150">
                <template #default="{ row: r }"><el-select v-model="r.dormantToNormalFlag" style="width:100%"><el-option label="否" value="0"/><el-option label="是" value="1"/></el-select></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="formTransfers.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="formTransfers.length===0" description="无过户登记配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 渠道控制 -->
          <el-tab-pane label="渠道控制" name="channel">
            <div style="margin-bottom:12px"><el-button size="small" @click="channelControls.push({channelCode:'',channelName:''})">添加渠道</el-button></div>
            <el-table :data="channelControls" size="small">
              <el-table-column label="渠道代码" width="200">
                <template #default="{ row: r }"><el-input v-model="r.channelCode" placeholder="如 CH001"/></template>
              </el-table-column>
              <el-table-column label="渠道名称" width="200">
                <template #default="{ row: r }"><el-input v-model="r.channelName" placeholder="如 柜面"/></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="channelControls.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="channelControls.length===0" description="无渠道控制配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 币种控制 -->
          <el-tab-pane label="币种控制" name="currency">
            <div style="margin-bottom:12px"><el-button size="small" @click="currencyControls.push({currencyCode:'',currencyName:''})">添加币种</el-button></div>
            <el-table :data="currencyControls" size="small">
              <el-table-column label="币种代码" width="200">
                <template #default="{ row: r }"><el-input v-model="r.currencyCode" placeholder="如 156"/></template>
              </el-table-column>
              <el-table-column label="币种名称" width="200">
                <template #default="{ row: r }"><el-input v-model="r.currencyName" placeholder="如 人民币"/></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="currencyControls.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="currencyControls.length===0" description="无币种控制配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 机构控制 -->
          <el-tab-pane label="机构控制" name="institution">
            <div style="margin-bottom:12px"><el-button size="small" @click="institutionControls.push({institutionType:'',institutionCode:''})">添加机构</el-button></div>
            <el-table :data="institutionControls" size="small">
              <el-table-column label="机构类型" width="200">
                <template #default="{ row: r }"><el-input v-model="r.institutionType" placeholder="如 BRANCH"/></template>
              </el-table-column>
              <el-table-column label="机构代码" width="200">
                <template #default="{ row: r }"><el-input v-model="r.institutionCode"/></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="institutionControls.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="institutionControls.length===0" description="无机构控制配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 客户控制 -->
          <el-tab-pane label="客户控制" name="customer">
            <div style="margin-bottom:12px"><el-button size="small" @click="customerControls.push({controlType:'',controlValue:''})">添加客户控制</el-button></div>
            <el-table :data="customerControls" size="small">
              <el-table-column label="控制类型" width="200">
                <template #default="{ row: r }"><el-input v-model="r.controlType" placeholder="如 RISK_LEVEL"/></template>
              </el-table-column>
              <el-table-column label="控制值" width="200">
                <template #default="{ row: r }"><el-input v-model="r.controlValue"/></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="customerControls.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="customerControls.length===0" description="无客户控制配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 凭证控制 -->
          <el-tab-pane label="凭证控制" name="voucher">
            <div style="margin-bottom:12px"><el-button size="small" @click="voucherControls.push({voucherType:'',customerAccountType:''})">添加凭证控制</el-button></div>
            <el-table :data="voucherControls" size="small">
              <el-table-column label="凭证类型" width="200">
                <template #default="{ row: r }"><el-input v-model="r.voucherType" placeholder="如 存折"/></template>
              </el-table-column>
              <el-table-column label="客户账户类型" width="200">
                <template #default="{ row: r }"><el-input v-model="r.customerAccountType"/></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="voucherControls.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="voucherControls.length===0" description="无凭证控制配置" :image-size="60"/>
          </el-tab-pane>


          <!-- 销户控制 -->
          <el-tab-pane label="销户控制" name="close">
            <div style="margin-bottom:12px"><el-button size="small" @click="closeControls.push({closeCtrlMethod:'0',customCtrlMethod:'',earlyCloseCtrl:'0',penaltyType:'0',signCheckMethod:'1',arrearsCheckMethod:'1',closeFundTo:'0',transferAccountNature:'0',currencyCode:'156'})">添加销户控制</el-button></div>
            <el-table :data="closeControls" size="small">
              <el-table-column label="销户控制方法" width="160">
                <template #default="{ row: r }"><el-select v-model="r.closeCtrlMethod" style="width:100%"><el-option label="系统标准" value="0"/><el-option label="自定义" value="1"/></el-select></template>
              </el-table-column>
              <el-table-column label="自定义方式" width="160">
                <template #default="{ row: r }"><el-input v-model="r.customCtrlMethod"/></template>
              </el-table-column>
              <el-table-column label="提前销户控制" width="160">
                <template #default="{ row: r }"><el-select v-model="r.earlyCloseCtrl" style="width:100%"><el-option label="不控制" value="0"/><el-option label="允许提前" value="1"/></el-select></template>
              </el-table-column>
              <el-table-column label="处罚类型" width="140">
                <template #default="{ row: r }"><el-select v-model="r.penaltyType" style="width:100%"><el-option label="新计息方法" value="0"/></el-select></template>
              </el-table-column>
              <el-table-column label="签约检查" width="120">
                <template #default="{ row: r }"><el-select v-model="r.signCheckMethod" style="width:100%"><el-option label="检查" value="1"/></el-select></template>
              </el-table-column>
              <el-table-column label="欠费检查" width="120">
                <template #default="{ row: r }"><el-select v-model="r.arrearsCheckMethod" style="width:100%"><el-option label="检查" value="1"/></el-select></template>
              </el-table-column>
              <el-table-column label="资金去向" width="140">
                <template #default="{ row: r }"><el-select v-model="r.closeFundTo" style="width:100%"><el-option label="不控制" value="0"/><el-option label="转账" value="1"/></el-select></template>
              </el-table-column>
              <el-table-column label="转账账户性质" width="160">
                <template #default="{ row: r }"><el-select v-model="r.transferAccountNature" style="width:100%"><el-option label="同一客户转账" value="0"/></el-select></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="closeControls.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="closeControls.length===0" description="无销户控制配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 存入控制 -->
          <el-tab-pane label="存入控制" name="deposit">
            <div style="margin-bottom:12px"><el-button size="small" @click="depositControls.push({cashDepositFlag:'1',transferDepositFlag:'1',depositCtrlMode:'2',depositCtrlMethod:'0',amountCtrlMode:'0',singleMinAmount:0,singleMaxAmount:0,timesCtrlMode:'0',minDepositTimes:0,maxDepositTimes:0,depositPlanFlag:'0',firstMinAmount:0,firstIncrement:0,retainMaxBalance:0,currencyCode:'156'})">添加存入控制</el-button></div>
            <el-table :data="depositControls" size="small">
              <el-table-column label="现金存入" width="100"><template #default="{ row: r }"><el-select v-model="r.cashDepositFlag" style="width:100%"><el-option label="允许" value="1"/><el-option label="不允许" value="0"/></el-select></template></el-table-column>
              <el-table-column label="转账存入" width="100"><template #default="{ row: r }"><el-select v-model="r.transferDepositFlag" style="width:100%"><el-option label="允许" value="1"/><el-option label="不允许" value="0"/></el-select></template></el-table-column>
              <el-table-column label="控制方式" width="130"><template #default="{ row: r }"><el-select v-model="r.depositCtrlMode" style="width:100%"><el-option label="无条件" value="1"/><el-option label="有条件" value="2"/></el-select></template></el-table-column>
              <el-table-column label="金额控制" width="120"><template #default="{ row: r }"><el-select v-model="r.amountCtrlMode" style="width:100%"><el-option label="不控制" value="0"/><el-option label="控制最小" value="1"/></el-select></template></el-table-column>
              <el-table-column label="单次最小" width="130"><template #default="{ row: r }"><el-input-number v-model="r.singleMinAmount" :precision="2" :min="0" style="width:100%"/></template></el-table-column>
              <el-table-column label="单次最大" width="130"><template #default="{ row: r }"><el-input-number v-model="r.singleMaxAmount" :precision="2" :min="0" style="width:100%"/></template></el-table-column>
              <el-table-column label="首次最小" width="130"><template #default="{ row: r }"><el-input-number v-model="r.firstMinAmount" :precision="2" :min="0" style="width:100%"/></template></el-table-column>
              <el-table-column label="次数控制" width="120"><template #default="{ row: r }"><el-select v-model="r.timesCtrlMode" style="width:100%"><el-option label="不控制" value="0"/><el-option label="控制最大" value="1"/></el-select></template></el-table-column>
              <el-table-column label="最大次数" width="100"><template #default="{ row: r }"><el-input-number v-model="r.maxDepositTimes" :min="0" style="width:100%"/></template></el-table-column>
              <el-table-column label="操作" width="80"><template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="depositControls.splice($index,1)">删除</el-button></template></el-table-column>
            </el-table>
            <el-empty v-if="depositControls.length===0" description="无存入控制配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 利息定义 -->
          <el-tab-pane label="利息定义" name="interest">
            <div style="margin-bottom:12px"><el-button size="small" @click="interestDefinitions.push({interestType:'0',interestFlag:'0',interestBalanceFlag:'0',taxFlag:'0',minInterestAmount:1,interestAdjustMethod:'0',standardInterestMethod:'10',interestPaymentMethod:'0',interestStartMethod:'0',interestFrequency:'D',paymentFrequency:'1QA21E',accrualFrequency:'D',currencyCode:'156'})">添加利息定义</el-button></div>
            <el-table :data="interestDefinitions" size="small">
              <el-table-column label="利息类型" width="120"><template #default="{ row: r }"><el-select v-model="r.interestType" style="width:100%"><el-option label="正利息" value="0"/><el-option label="到期利息" value="1"/></el-select></template></el-table-column>
              <el-table-column label="计息标志" width="100"><template #default="{ row: r }"><el-select v-model="r.interestFlag" style="width:100%"><el-option label="是" value="1"/><el-option label="否" value="0"/></el-select></template></el-table-column>
              <el-table-column label="利息调整" width="130"><template #default="{ row: r }"><el-select v-model="r.interestAdjustMethod" style="width:100%"><el-option label="多段调整" value="0"/><el-option label="积数调整" value="1"/></el-select></template></el-table-column>
              <el-table-column label="计息方法" width="130"><template #default="{ row: r }"><el-select v-model="r.standardInterestMethod" style="width:100%"><el-option label="到期取10" value="10"/><el-option label="活期取00" value="00"/><el-option label="英镑港币12" value="12"/></el-select></template></el-table-column>
              <el-table-column label="支付方式" width="140"><template #default="{ row: r }"><el-select v-model="r.interestPaymentMethod" style="width:100%"><el-option label="定期付息" value="0"/><el-option label="利随本清" value="1"/></el-select></template></el-table-column>
              <el-table-column label="付息频率" width="120"><template #default="{ row: r }"><el-input v-model="r.paymentFrequency"/></template></el-table-column>
              <el-table-column label="最小计息" width="110"><template #default="{ row: r }"><el-input-number v-model="r.minInterestAmount" :precision="2" :min="0" style="width:100%"/></template></el-table-column>
              <el-table-column label="计税" width="80"><template #default="{ row: r }"><el-select v-model="r.taxFlag" style="width:100%"><el-option label="是" value="1"/><el-option label="否" value="0"/></el-select></template></el-table-column>
              <el-table-column label="操作" width="80"><template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="interestDefinitions.splice($index,1)">删除</el-button></template></el-table-column>
            </el-table>
            <el-empty v-if="interestDefinitions.length===0" description="无利息定义配置" :image-size="60"/>
          </el-tab-pane>

          <!-- 利率定义 -->
          <el-tab-pane label="利率定义" name="rate">
            <div style="margin-bottom:12px"><el-button size="small" @click="rateDefinitions.push({liabilityRateType:'0',rateGearMethod:'0',rateCode:'',rateTerm:'D',rateTermFlag:'0',rateDetermineDate:'0',rateDetermineMethod:'0',rateAdjustFrequency:'D',rateChangeAdjustRate:'0',rateChangeAdjustInt:'0',rateUpdateMethod:'0',currencyCode:'156'})">添加利率定义</el-button></div>
            <el-table :data="rateDefinitions" size="small">
              <el-table-column label="利率类型" width="120"><template #default="{ row: r }"><el-select v-model="r.liabilityRateType" style="width:100%"><el-option label="正利率" value="0"/><el-option label="到期利率" value="1"/></el-select></template></el-table-column>
              <el-table-column label="靠档方式" width="130"><template #default="{ row: r }"><el-select v-model="r.rateGearMethod" style="width:100%"><el-option label="挂靠对应档" value="0"/></el-select></template></el-table-column>
              <el-table-column label="利率编号" width="140"><template #default="{ row: r }"><el-input v-model="r.rateCode"/></template></el-table-column>
              <el-table-column label="利率存期" width="110"><template #default="{ row: r }"><el-select v-model="r.rateTerm" style="width:100%"><el-option label="每天" value="D"/></el-select></template></el-table-column>
              <el-table-column label="存期标志" width="150"><template #default="{ row: r }"><el-select v-model="r.rateTermFlag" style="width:100%"><el-option label="产品约定" value="0"/><el-option label="账户约定" value="1"/></el-select></template></el-table-column>
              <el-table-column label="确定日期" width="140"><template #default="{ row: r }"><el-select v-model="r.rateDetermineDate" style="width:100%"><el-option label="开户日" value="0"/><el-option label="结息日" value="1"/></el-select></template></el-table-column>
              <el-table-column label="确定方式" width="140"><template #default="{ row: r }"><el-select v-model="r.rateDetermineMethod" style="width:100%"><el-option label="累计存期" value="0"/><el-option label="开户存期" value="1"/></el-select></template></el-table-column>
              <el-table-column label="调整频率" width="110"><template #default="{ row: r }"><el-select v-model="r.rateAdjustFrequency" style="width:100%"><el-option label="每天" value="D"/></el-select></template></el-table-column>
              <el-table-column label="操作" width="80"><template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="rateDefinitions.splice($index,1)">删除</el-button></template></el-table-column>
            </el-table>
            <el-empty v-if="rateDefinitions.length===0" description="无利率定义配置" :image-size="60"/>
          </el-tab-pane>          <!-- 核算控制 -->
          <el-tab-pane label="核算控制" name="accounting">
            <div style="margin-bottom:12px"><el-button size="small" @click="accountingControls.push({currencyCtrl:'',classifyCtrl:'',termCtrl:''})">添加核算控制</el-button></div>
            <el-table :data="accountingControls" size="small">
              <el-table-column label="币种控制" width="200">
                <template #default="{ row: r }"><el-input v-model="r.currencyCtrl"/></template>
              </el-table-column>
              <el-table-column label="分类控制" width="200">
                <template #default="{ row: r }"><el-input v-model="r.classifyCtrl"/></template>
              </el-table-column>
              <el-table-column label="存期控制" width="200">
                <template #default="{ row: r }"><el-input v-model="r.termCtrl"/></template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row: r, $index }"><el-button size="small" type="danger" link @click="accountingControls.splice($index,1)">删除</el-button></template>
              </el-table-column>
            </el-table>
            <el-empty v-if="accountingControls.length===0" description="无核算控制配置" :image-size="60"/>
          </el-tab-pane>        </el-tabs>

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
var controls=ref([]);var termControls=ref([]);var openControls=ref([]);var withdrawControls=ref([]);var maturityControls=ref([]);var formTransfers=ref([]);var channelControls=ref([]);var currencyControls=ref([]);var institutionControls=ref([]);var customerControls=ref([]);var voucherControls=ref([]);var accountingControls=ref([]);var closeControls=ref([]);var depositControls=ref([]);var interestDefinitions=ref([]);var rateDefinitions=ref([])
var typeMap={'00':'单位活期存款','02':'单位定期存款','04':'国库定期存款','05':'单位通知存款','06':'单位保证金活期','07':'单位保证金定期','09':'单位协议存款','14':'同业存放活期','15':'同业存放定期'}
var form=reactive({productCode:'',productDescription:'',effectiveDate:'',expiryDate:'',productStatus:'0',currentFixedFlag:'0',customerType:'CORPORATE',productType:'TRADITIONAL',depositType:'00',defaultCurrency:'156',withdrawScope:'0',depositScope:'0',cashWithdrawalFlag:'0',transferExchangeFlag:'0',maturityFlag:'0',overdraftFlag:'0',chargeFlag:'0',accountClassifyFlag:'0',channelCtrlMode:'0',currencyCtrlMode:'0',institutionCtrlMode:'0',customerCtrlMode:'0',voucherCtrlMode:'0',termCtrlMode:'0',simpleInterestFlag:'0',settlementFlag:'0',exchangeFlag:'0',autoExchangeSellFlag:'0',balanceSyncFlag:'1',formTransferFlag:'0'})
onMounted(async()=>{try{var r=await getProductList();products.value=r.data||[]}catch(e){}})
var loadSource=async()=>{try{var r=await getProduct(sourceCode.value);source.value=r.data}catch(e){}}
var startCopy=()=>{
  if(!source.value)return
  Object.assign(form,{...source.value,productCode:'',productDescription:''})
  delete form.id;delete form.createdAt;delete form.updatedAt
  var loads=[
    getCfg(sourceCode.value,'controls').then(r=>controls.value=r.data||[]),
    getCfg(sourceCode.value,'term-controls').then(r=>termControls.value=r.data||[]),
    getCfg(sourceCode.value,'open-controls').then(r=>openControls.value=r.data||[]),
    getCfg(sourceCode.value,'withdraw-controls').then(r=>withdrawControls.value=r.data||[]),
    getCfg(sourceCode.value,'maturity-controls').then(r=>maturityControls.value=r.data||[]),
    getCfg(sourceCode.value,'form-transfers').then(r=>formTransfers.value=r.data||[]),
    getCfg(sourceCode.value,'channel-controls').then(r=>channelControls.value=r.data||[]),
    getCfg(sourceCode.value,'currency-controls').then(r=>currencyControls.value=r.data||[]),
    getCfg(sourceCode.value,'institution-controls').then(r=>institutionControls.value=r.data||[]),
    getCfg(sourceCode.value,'customer-controls').then(r=>customerControls.value=r.data||[]),
    getCfg(sourceCode.value,'voucher-controls').then(r=>voucherControls.value=r.data||[]),
    getCfg(sourceCode.value,'accounting-controls').then(r=>accountingControls.value=r.data||[]),
    getCfg(sourceCode.value,'close-controls').then(r=>closeControls.value=r.data||[]),
    getCfg(sourceCode.value,'deposit-controls').then(r=>depositControls.value=r.data||[]),
    getCfg(sourceCode.value,'interest-definitions').then(r=>interestDefinitions.value=r.data||[]),
    getCfg(sourceCode.value,'rate-definitions').then(r=>rateDefinitions.value=r.data||[])
  ]
  Promise.all(loads).catch(()=>{})
  sourceLoaded.value=true;activeTab.value='basic'
}
var handleSave=async()=>{await formRef.value?.validate(async ok=>{if(!ok)return;saving.value=true;try{await createProduct({...form,controls:controls.value,termControls:termControls.value,openControls:openControls.value,withdrawControls:withdrawControls.value,maturityControls:maturityControls.value,formTransfers:formTransfers.value,channelControls:channelControls.value,currencyControls:currencyControls.value,institutionControls:institutionControls.value,customerControls:customerControls.value,voucherControls:voucherControls.value,accountingControls:accountingControls.value,closeControls:closeControls.value,depositControls:depositControls.value,interestDefinitions:interestDefinitions.value,rateDefinitions:rateDefinitions.value});ElMessage.success('拷贝成功！新产品代码: '+form.productCode);sourceLoaded.value=false;sourceCode.value=''}catch(e){}finally{saving.value=false}})}
</script>
<style scoped>
.page-header{margin-bottom:24px}.page-title{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-secondary);margin:0}
</style>






