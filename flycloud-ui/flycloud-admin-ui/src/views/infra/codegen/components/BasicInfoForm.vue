<template>
  <el-form ref="formRef" :model="formData" :rules="rules" label-width="120px">
    <el-row>
      <el-col :span="12">
        <el-form-item
          :label="t('auto.views.infra.codegen.components.BasicInfoForm.k4b2d958f')"
          prop="tableName"
        >
          <el-input
            v-model="formData.tableName"
            :placeholder="t('auto.views.infra.codegen.components.BasicInfoForm.k2c2b2339')"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item
          :label="t('auto.views.infra.codegen.components.BasicInfoForm.kdef4ee41')"
          prop="tableComment"
        >
          <el-input
            v-model="formData.tableComment"
            :placeholder="t('auto.views.infra.codegen.components.BasicInfoForm.k601816e1')"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="className">
          <template #label>
            <span>
              {{ t('extra.k44f2fc44') }}
              <el-tooltip
                :content="t('auto.views.infra.codegen.components.BasicInfoForm.kc18743fe')"
                placement="top"
              >
                <Icon class="" icon="ep:question-filled" />
              </el-tooltip>
            </span>
          </template>
          <el-input v-model="formData.className" placeholder="请输入" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="作者" prop="author">
          <el-input v-model="formData.author" placeholder="请输入" />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" :rows="3" type="textarea" />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script lang="ts" setup>
import * as CodegenApi from '@/api/infra/codegen'
import { PropType } from 'vue'
const { t } = useI18n()
defineOptions({ name: 'InfraCodegenBasicInfoForm' })

const props = defineProps({
  table: {
    type: Object as PropType<Nullable<CodegenApi.CodegenTableVO>>,
    default: () => null
  }
})

const formRef = ref()
const formData = ref({
  tableName: '',
  tableComment: '',
  className: '',
  author: '',
  remark: ''
})
const rules = reactive({
  tableName: [required],
  tableComment: [required],
  className: [required],
  author: [required]
})

/** 监听 table 属性，复制给 formData 属性 */
watch(
  () => props.table,
  (table) => {
    if (!table) return
    formData.value = table
  },
  {
    deep: true,
    immediate: true
  }
)

defineExpose({
  validate: async () => unref(formRef)?.validate()
})
</script>
