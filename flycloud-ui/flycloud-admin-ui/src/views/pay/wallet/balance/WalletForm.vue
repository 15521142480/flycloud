<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="800">
    <WalletTransactionList :wallet-id="walletId" />
    <template #footer>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.pay.wallet.balance.WalletForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import WalletTransactionList from '../transaction/WalletTransactionList.vue'
const { t } = useI18n()
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const walletId = ref(0)
/** 打开弹窗 */
const open = async (theWalletId: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('auto.views.pay.wallet.balance.WalletForm.k6c31892f')
  walletId.value = theWalletId
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗
</script>
