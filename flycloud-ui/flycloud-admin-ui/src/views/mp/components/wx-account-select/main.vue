<template>
  <el-select
    v-model="account.id"
    :placeholder="t('auto.views.mp.components.wx_account_select.main.k6eb9b9d7')"
    class="!w-240px"
    @change="onChanged"
  >
    <el-option v-for="item in accountList" :key="item.id" :label="item.name" :value="item.id" />
  </el-select>
</template>

<script lang="ts" setup>
import * as MpAccountApi from '@/api/mp/account'
import { useTagsViewStore } from '@/store/modules/tagsView'
const { t } = useI18n()
const message = useMessage() // 消息弹窗
const { delView } = useTagsViewStore() // 视图操作
const { push, currentRoute } = useRouter() // 路由

defineOptions({ name: 'WxAccountSelect' })

const account: MpAccountApi.AccountVO = reactive({
  id: -1,
  name: ''
})

const accountList = ref<MpAccountApi.AccountVO[]>([])

const emit = defineEmits<{
  (e: 'change', id: string, name: string)
}>()

const handleQuery = async () => {
  accountList.value = await MpAccountApi.getSimpleAccountList()
  if (accountList.value.length == 0) {
    message.error(t('auto.views.mp.components.wx_account_select.main.k39f2e9bb'))
    delView(unref(currentRoute))
    await push({ name: 'MpAccount' })
    return
  }
  // 默认选中第一个
  if (accountList.value.length > 0) {
    account.id = accountList.value[0].id
    if (account.id) {
      account.name = accountList.value[0].name
      emit('change', account.id, account.name)
    }
  }
}

const onChanged = (id?: string) => {
  const found = accountList.value.find((v) => v.id === id)
  if (account.id) {
    account.name = found ? found.name : ''
    emit('change', account.id, account.name)
  }
}

/** 初始化 */
onMounted(() => {
  handleQuery()
})
</script>
