<template>
  <el-table :data="formData" :show-overflow-tooltip="true" :stripe="true" height="120">
    <el-table-column
      :label="t('auto.views.crm.followup.components.FollowUpRecordBusinessForm.k84b59248')"
      fixed="left"
      align="center"
      prop="name"
    />
    <el-table-column
      :label="t('auto.views.crm.followup.components.FollowUpRecordBusinessForm.k670248ff')"
      align="center"
      prop="totalPrice"
      :formatter="erpPriceTableColumnFormatter"
    />
    <el-table-column
      :label="t('auto.views.crm.followup.components.FollowUpRecordBusinessForm.ke941d410')"
      align="center"
      prop="customerName"
    />
    <el-table-column
      :label="t('auto.views.crm.followup.components.FollowUpRecordBusinessForm.k949cacee')"
      align="center"
      prop="statusTypeName"
    />
    <el-table-column
      :label="t('auto.views.crm.followup.components.FollowUpRecordBusinessForm.kc0a20f9a')"
      align="center"
      prop="statusName"
    />
    <el-table-column align="center" fixed="right" :label="t('common.operation')" width="80">
      <template #default="{ $index }">
        <el-button link type="danger" @click="handleDelete($index)">
          {{
            t('auto.views.crm.followup.components.FollowUpRecordBusinessForm.k2f752c00')
          }}</el-button
        >
      </template>
    </el-table-column>
  </el-table>
</template>

<script lang="ts" setup>
import { erpPriceTableColumnFormatter } from '@/utils'
const { t } = useI18n()
const props = defineProps<{
  businesses: undefined
}>()
const formData = ref([])

/** 初始化商机列表 */
watch(
  () => props.businesses,
  async (val) => {
    formData.value = val
  },
  { immediate: true }
)

/** 删除按钮操作 */
const handleDelete = (index: number) => {
  formData.value.splice(index, 1)
}
</script>
