<template>
  <el-table ref="dragTable" :data="formData" :max-height="tableHeight" row-key="columnId">
    <el-table-column
      :show-overflow-tooltip="true"
      :label="t('auto.views.infra.codegen.components.ColumInfoForm.k24d998a8')"
      min-width="10%"
      prop="columnName"
    />
    <el-table-column
      :label="t('auto.views.infra.codegen.components.ColumInfoForm.k805453de')"
      min-width="10%"
    >
      <template #default="scope">
        <el-input v-model="scope.row.columnComment" />
      </template>
    </el-table-column>
    <el-table-column
      :show-overflow-tooltip="true"
      :label="t('extra.kacdfced1')"
      min-width="10%"
      prop="dataType"
    />
    <el-table-column :label="t('extra.kcdb88772')" min-width="11%">
      <template #default="scope">
        <el-select v-model="scope.row.javaType">
          <el-option label="Long" value="Long" />
          <el-option label="String" value="String" />
          <el-option label="Integer" value="Integer" />
          <el-option label="Double" value="Double" />
          <el-option label="BigDecimal" value="BigDecimal" />
          <el-option label="LocalDateTime" value="LocalDateTime" />
          <el-option label="Boolean" value="Boolean" />
        </el-select>
      </template>
    </el-table-column>
    <el-table-column :label="t('extra.k8309e4b0')" min-width="10%">
      <template #default="scope">
        <el-input v-model="scope.row.javaField" />
      </template>
    </el-table-column>
    <el-table-column :label="t('extra.k9bdb07e7')" min-width="4%">
      <template #default="scope">
        <el-checkbox v-model="scope.row.createOperation" false-value="false" true-value="true" />
      </template>
    </el-table-column>
    <el-table-column :label="t('common.edit')" min-width="4%">
      <template #default="scope">
        <el-checkbox v-model="scope.row.updateOperation" false-value="false" true-value="true" />
      </template>
    </el-table-column>
    <el-table-column :label="t('extra.k3712972d')" min-width="4%">
      <template #default="scope">
        <el-checkbox
          v-model="scope.row.listOperationResult"
          false-value="false"
          true-value="true"
        />
      </template>
    </el-table-column>
    <el-table-column :label="t('common.search')" min-width="4%">
      <template #default="scope">
        <el-checkbox v-model="scope.row.listOperation" false-value="false" true-value="true" />
      </template>
    </el-table-column>
    <el-table-column :label="t('extra.k55520398')" min-width="10%">
      <template #default="scope">
        <el-select v-model="scope.row.listOperationCondition">
          <el-option label="=" value="=" />
          <el-option label="!=" value="!=" />
          <el-option label=">" value=">" />
          <el-option label=">=" value=">=" />
          <el-option label="<" value="<>" />
          <el-option label="<=" value="<=" />
          <el-option label="LIKE" value="LIKE" />
          <el-option label="BETWEEN" value="BETWEEN" />
        </el-select>
      </template>
    </el-table-column>
    <el-table-column :label="t('extra.k94c249f5')" min-width="5%">
      <template #default="scope">
        <el-checkbox v-model="scope.row.nullable" false-value="false" true-value="true" />
      </template>
    </el-table-column>
    <el-table-column :label="t('extra.ka5d83383')" min-width="12%">
      <template #default="scope">
        <el-select v-model="scope.row.htmlType">
          <el-option :label="t('extra.k5ac57ce6')" value="input" />
          <el-option :label="t('extra.kd2362dca')" value="textarea" />
          <el-option
            :label="t('auto.components.FormCreate.src.config.selectRule.k2c2c1ef2')"
            value="select"
          />
          <el-option :label="t('form.radio')" value="radio" />
          <el-option :label="t('extra.kdb98f889')" value="checkbox" />
          <el-option :label="t('extra.k56bc8c71')" value="datetime" />
          <el-option :label="t('extra.k6bfb9bb2')" value="imageUpload" />
          <el-option
            :label="t('auto.components.FormCreate.src.config.useUploadFileRule.k39c04b3d')"
            value="fileUpload"
          />
          <el-option :label="t('extra.k791edf1c')" value="editor" />
        </el-select>
      </template>
    </el-table-column>
    <el-table-column :label="t('system.dict.typeCode')" min-width="12%">
      <template #default="scope">
        <el-select
          v-model="scope.row.dictType"
          clearable
          filterable
          :placeholder="t('common.selectText')"
        >
          <el-option
            v-for="dict in dictOptions"
            :key="dict.id"
            :label="dict.name"
            :value="dict.type"
          />
        </el-select>
      </template>
    </el-table-column>
    <el-table-column :label="t('extra.k3f258f5e')" min-width="10%">
      <template #default="scope">
        <el-input v-model="scope.row.example" />
      </template>
    </el-table-column>
  </el-table>
</template>
<script lang="ts" setup>
import { PropType } from 'vue'
import * as CodegenApi from '@/api/infra/codegen'
import * as DictDataApi from '@/api/system/dict/dict.type'
const { t } = useI18n()
defineOptions({ name: 'InfraCodegenColumInfoForm' })

const props = defineProps({
  columns: {
    type: Array as unknown as PropType<CodegenApi.CodegenColumnVO[]>,
    default: () => null
  }
})

const formData = ref<CodegenApi.CodegenColumnVO[]>([])
const tableHeight = document.documentElement.scrollHeight - 350 + 'px'

/** 查询字典下拉列表 */
const dictOptions = ref<DictDataApi.DictTypeVO[]>()
const getDictOptions = async () => {
  dictOptions.value = await DictDataApi.getSimpleDictTypeList()
}

watch(
  () => props.columns,
  (columns) => {
    if (!columns) return
    formData.value = columns
  },
  {
    deep: true,
    immediate: true
  }
)

onMounted(async () => {
  await getDictOptions()
})
</script>
