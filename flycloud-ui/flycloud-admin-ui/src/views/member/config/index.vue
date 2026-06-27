<template>
  <doc-alert
    :title="t('auto.views.member.config.index.k5e432f1a')"
    url="https://doc.iocoder.cn/member/build/"
  />

  <ContentWrap>
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="hideId" v-show="false">
        <el-input v-model="formData.id" />
      </el-form-item>

      <el-tabs>
        <el-tab-pane :label="t('auto.views.member.config.index.kb3e2659e')">
          <el-form-item
            :label="t('auto.views.member.config.index.k34de1129')"
            prop="pointTradeDeductEnable"
          >
            <el-switch v-model="formData.pointTradeDeductEnable" style="user-select: none" />
            <el-text class="w-full" size="small" type="info">{{
              t('auto.views.member.config.index.k51ba1eed')
            }}</el-text>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.member.config.index.k34de1129')"
            prop="pointTradeDeductUnitPrice"
          >
            <el-input-number
              v-model="computedPointTradeDeductUnitPrice"
              :placeholder="t('auto.views.member.config.index.k89ca4fa4')"
              :precision="2"
            />
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.k4f38ad18') }}
            </el-text>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.member.config.index.kd12449c0')"
            prop="pointTradeDeductMaxPrice"
          >
            <el-input-number
              v-model="formData.pointTradeDeductMaxPrice"
              :placeholder="t('auto.views.member.config.index.k8645f546')"
            />
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.k76d68943') }}
            </el-text>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.member.config.index.k3bebc6ff')"
            prop="pointTradeGivePoint"
          >
            <el-input-number
              v-model="formData.pointTradeGivePoint"
              :placeholder="t('auto.views.member.config.index.k3d89d235')"
            />
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.kc09fb5ff') }}
            </el-text>
          </el-form-item>
        </el-tab-pane>
      </el-tabs>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">{{ t('common.save') }}</el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as ConfigApi from '@/api/member/config'
const { t } = useI18n()
defineOptions({ name: 'MemberConfig' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formData = ref({
  id: undefined,
  pointTradeDeductEnable: true,
  pointTradeDeductUnitPrice: 0,
  pointTradeDeductMaxPrice: 0,
  pointTradeGivePoint: 0
})

// 创建一个计算属性，用于将 pointTradeDeductUnitPrice 显示为带两位小数的形式
const computedPointTradeDeductUnitPrice = computed({
  get: () => (formData.value.pointTradeDeductUnitPrice / 100).toFixed(2),
  set: (newValue: number) => {
    formData.value.pointTradeDeductUnitPrice = Math.round(newValue * 100)
  }
})

const formRules = reactive({})
const formRef = ref() // 表单 Ref

/** 修改积分配置 */
const onSubmit = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as ConfigApi.ConfigVO
    await ConfigApi.saveConfig(data)
    message.success(t('common.updateSuccess'))
    dialogVisible.value = false
  } finally {
    formLoading.value = false
  }
}

/** 获得积分配置 */
const getConfig = async () => {
  try {
    const data = await ConfigApi.getConfig()
    if (data === null) {
      return
    }
    formData.value = data
  } finally {
  }
}

onMounted(() => {
  getConfig()
})
</script>
