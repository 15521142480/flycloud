<template>
  <Dialog
    :title="t('auto.views.pay.transfer.CreatePayTransfer.k208ab1da')"
    v-model="dialogVisible"
    width="800px"
  >
    <el-card style="margin-top: 10px">
      <el-descriptions
        :title="t('auto.views.pay.transfer.CreatePayTransfer.ke4635ff9')"
        :column="2"
        border
      >
        <el-descriptions-item :label="t('auto.views.pay.transfer.CreatePayTransfer.k297a5897')">
          {{ typeName }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('auto.views.pay.transfer.CreatePayTransfer.kcab82f68')">
          ￥{{ (transfer.price / 100.0).toFixed(2) }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('auto.views.pay.transfer.CreatePayTransfer.kad69bce0')">
          {{ transfer.userName }}
        </el-descriptions-item>
        <el-descriptions-item
          :label="t('auto.views.pay.transfer.CreatePayTransfer.k6605bf8f')"
          v-if="transfer.type === 1"
        >
          {{ transfer.alipayLogonId }}
        </el-descriptions-item>
        <el-descriptions-item
          :label="t('auto.views.pay.transfer.CreatePayTransfer.kc6eb425b')"
          v-if="transfer.type === 2"
        >
          {{ transfer.openid }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
    <el-card style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>{{ t('auto.views.pay.transfer.CreatePayTransfer.k0be418be') }}</span>
        </div>
      </template>
      <div>
        <el-radio-group v-model="channelCode">
          <el-radio
            value="alipay_pc"
            :disabled="transfer.type === 2 || transfer.type === 3 || transfer.type === 4"
          >
            <img :src="svg_alipay_app" />
          </el-radio>
          <el-radio
            value="wx_app"
            :disabled="transfer.type === 1 || transfer.type === 3 || transfer.type === 4"
          >
            <img :src="svg_wx_app" />
          </el-radio>
        </el-radio-group>
      </div>
    </el-card>
    <el-divider />
    <div style="text-align: right">
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </div>
  </Dialog>
</template>

<script lang="ts" setup>
import * as PayTransferApi from '@/api/pay/transfer'
import { computed, PropType } from 'vue'
import { DICT_TYPE, getDictLabel } from '@/utils/dict'
// 导入图标
import svg_alipay_app from '@/assets/svgs/pay/icon/alipay_app.svg'
import svg_wx_app from '@/assets/svgs/pay/icon/wx_app.svg'
import { yuanToFen } from '@/utils'
const { t } = useI18n()
const message = useMessage() // 消息弹窗
const formLoading = ref(false) // 提交的按钮禁用
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
defineOptions({ name: 'CreatePayTransfer' })

// 提交数据
let submitTransferData: PayTransferApi.TransferVO

const transfer = reactive({
  appId: undefined,
  channelCode: undefined,
  merchantTransferId: undefined,
  type: undefined,
  price: undefined,
  subject: undefined,
  userName: undefined,
  alipayLogonId: undefined,
  openid: undefined
})
const dialogVisible = ref(false)
const typeName = computed(() => {
  return getDictLabel(DICT_TYPE.PAY_TRANSFER_TYPE, transfer.type)
})
const channelCode = computed(() => {
  let channelCode = 'alipay_pc'
  if (transfer.type === 2) {
    channelCode = 'wx_app'
  }
  // TODO 银行卡和钱包 转账待实现
  return channelCode
})

/** 打开弹窗 */
const showPayTransfer = async (payTransfer: PayTransferApi.TransferVO) => {
  dialogVisible.value = true
  submitTransferData = payTransfer
  transfer.merchantTransferId = payTransfer.merchantTransferId
  transfer.price = payTransfer.price
  transfer.userName = payTransfer.userName
  transfer.type = payTransfer.type
  transfer.appId = payTransfer.appId
  transfer.subject = payTransfer.subject
  transfer.alipayLogonId = payTransfer.alipayLogonId
  transfer.openid = payTransfer.openid
}
/** 关闭弹窗 */
const close = async () => {
  dialogVisible.value = false
}
defineExpose({ showPayTransfer, close }) // 提供 showPayTransfer， close 方法，用于打开, 关闭弹窗

const submitForm = async () => {
  // 校验表单
  formLoading.value = true
  try {
    submitTransferData.channelCode = channelCode.value
    await PayTransferApi.createTransfer(submitTransferData)
    message.success(t('auto.views.pay.transfer.CreatePayTransfer.k00cefa46'))
    // 发送操作成功的事件
    emit('success')
    dialogVisible.value = false
  } finally {
    formLoading.value = false
  }
}
</script>

<style lang="scss" scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
