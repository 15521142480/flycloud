<template>
  <el-form ref="formRef" :model="formData" :rules="rules" label-width="150px">
    <el-row>
      <el-col :span="12">
        <el-form-item
          :label="t('auto.views.infra.codegen.components.GenerateInfoForm.k4f981957')"
          prop="templateType"
        >
          <el-select v-model="formData.templateType">
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_CODEGEN_TEMPLATE_TYPE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item
          :label="t('auto.views.infra.codegen.components.GenerateInfoForm.k8cb22782')"
          prop="frontType"
        >
          <el-select v-model="formData.frontType">
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_CODEGEN_FRONT_TYPE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item
          :label="t('auto.views.infra.codegen.components.GenerateInfoForm.k3af1a4f4')"
          prop="scene"
        >
          <el-select v-model="formData.scene">
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_CODEGEN_SCENE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <template #label>
            <span>
              {{ t('extra.kda19f113') }}
              <el-tooltip
                :content="t('auto.views.infra.codegen.components.GenerateInfoForm.kb3988be5')"
                placement="top"
              >
                <Icon icon="ep:question-filled" />
              </el-tooltip>
            </span>
          </template>
          <el-tree-select
            v-model="formData.parentMenuId"
            :data="menus"
            :props="menuTreeProps"
            check-strictly
            node-key="id"
            :placeholder="t('extra.kfa755c67')"
          />
        </el-form-item>
      </el-col>

      <!--      <el-col :span="12">-->
      <!--        <el-form-item prop="packageName">-->
      <!--          <span slot="label">-->
      <!--            生成包路径-->
      <!--            <el-tooltip content="生成在哪个java包下，例如 com.ruoyi.system" placement="top">-->
      <!--              <i class="el-icon-question"></i>-->
      <!--            </el-tooltip>-->
      <!--          </span>-->
      <!--          <el-input v-model="formData.packageName" />-->
      <!--        </el-form-item>-->
      <!--      </el-col>-->

      <el-col :span="12">
        <el-form-item prop="moduleName">
          <template #label>
            <span>
              {{ t('extra.k322ff093') }}
              <el-tooltip :content="t('extra.kf113775f')" placement="top">
                <Icon icon="ep:question-filled" />
              </el-tooltip>
            </span>
          </template>
          <el-input v-model="formData.moduleName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="businessName">
          <template #label>
            <span>
              {{ t('extra.k4884cc8f') }}
              <el-tooltip :content="t('extra.k03a805da')" placement="top">
                <Icon icon="ep:question-filled" />
              </el-tooltip>
            </span>
          </template>
          <el-input v-model="formData.businessName" />
        </el-form-item>
      </el-col>

      <!--      <el-col :span="12">-->
      <!--        <el-form-item prop="businessPackage">-->
      <!--          <span slot="label">-->
      <!--            业务包-->
      <!--            <el-tooltip content="业务包，自定义二级目录。例如说，我们希望将 dictType 和 dictData 归类成 dict 业务" placement="top">-->
      <!--              <i class="el-icon-question"></i>-->
      <!--            </el-tooltip>-->
      <!--          </span>-->
      <!--          <el-input v-model="formData.businessPackage" />-->
      <!--        </el-form-item>-->
      <!--      </el-col>-->

      <el-col :span="12">
        <el-form-item prop="className">
          <template #label>
            <span>
              {{ t('extra.ka7a7f8c4') }}
              <el-tooltip :content="t('extra.k83014a4e')" placement="top">
                <Icon icon="ep:question-filled" />
              </el-tooltip>
            </span>
          </template>
          <el-input v-model="formData.className" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="classComment">
          <template #label>
            <span>
              {{ t('extra.k0016dce5') }}
              <el-tooltip :content="t('extra.k931448d1')" placement="top">
                <Icon icon="ep:question-filled" />
              </el-tooltip>
            </span>
          </template>
          <el-input v-model="formData.classComment" />
        </el-form-item>
      </el-col>

      <el-col v-if="formData.genType === '1'" :span="24">
        <el-form-item prop="genPath">
          <template #label>
            <span>
              {{ t('extra.k4f356348') }}
              <el-tooltip :content="t('extra.kb5e23fcb')" placement="top">
                <Icon icon="ep:question-filled" />
              </el-tooltip>
            </span>
          </template>
          <el-input v-model="formData.genPath">
            <template #append>
              <el-dropdown>
                <el-button type="primary">
                  {{ t('extra.k8aa6b8f8') }} <i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="formData.genPath = '/'">
                      {{ t('extra.kb2409de3') }}
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>

    <!-- 树表信息 -->
    <el-row v-if="formData.templateType == 2">
      <el-col :span="24">
        <h4 class="form-header">{{ t('extra.kc2369c23') }}</h4>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="treeParentColumnId">
          <template #label>
            <span>
              {{ t('extra.k6f838183') }}
              <el-tooltip :content="t('extra.kb5d06859')" placement="top">
                <Icon icon="ep:question-filled" />
              </el-tooltip>
            </span>
          </template>
          <el-select v-model="formData.treeParentColumnId" :placeholder="t('common.selectText')">
            <el-option
              v-for="(column, index) in props.columns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.id"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="treeNameColumnId">
          <template #label>
            <span>
              {{ t('extra.kc136b47b') }}
              <el-tooltip :content="t('extra.kb2adc48a')" placement="top">
                <Icon icon="ep:question-filled" />
              </el-tooltip>
            </span>
          </template>
          <el-select v-model="formData.treeNameColumnId" :placeholder="t('common.selectText')">
            <el-option
              v-for="(column, index) in props.columns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.id"
            />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <!-- 主表信息 -->
    <el-row v-if="formData.templateType == 15">
      <el-col :span="24">
        <h4 class="form-header">{{ t('extra.k6ee68497') }}</h4>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="masterTableId">
          <template #label>
            <span>
              {{ t('extra.kfc7f2cf7') }}
              <el-tooltip :content="t('extra.kc1f214dd')" placement="top">
                <Icon icon="ep:question-filled" />
              </el-tooltip>
            </span>
          </template>
          <el-select v-model="formData.masterTableId" :placeholder="t('common.selectText')">
            <el-option
              v-for="(table0, index) in tables"
              :key="index"
              :label="table0.tableName + '：' + table0.tableComment"
              :value="table0.id"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="subJoinColumnId">
          <template #label>
            <span>
              {{ t('extra.kfae46df5') }}
              <el-tooltip :content="t('extra.k8f2c5594')" placement="top">
                <Icon icon="ep:question-filled" />
              </el-tooltip>
            </span>
          </template>
          <el-select v-model="formData.subJoinColumnId" :placeholder="t('common.selectText')">
            <el-option
              v-for="(column, index) in props.columns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.id"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="subJoinMany">
          <template #label>
            <span>
              {{ t('extra.k6f6061ac') }}
              <el-tooltip :content="t('extra.kff68907c')" placement="top">
                <Icon icon="ep:question-filled" />
              </el-tooltip>
            </span>
          </template>
          <el-radio-group v-model="formData.subJoinMany" :placeholder="t('common.selectText')">
            <el-radio :value="true">{{ t('extra.k78d5eb01') }}</el-radio>
            <el-radio :value="false">{{ t('extra.kdd7768f4') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { handleTree } from '@/utils/tree'
import * as CodegenApi from '@/api/infra/codegen'
import * as MenuApi from '@/api/system/menu'
import { PropType } from 'vue'
const { t } = useI18n()
defineOptions({ name: 'InfraCodegenGenerateInfoForm' })

const message = useMessage() // 消息弹窗
const props = defineProps({
  table: {
    type: Object as PropType<Nullable<CodegenApi.CodegenTableVO>>,
    default: () => null
  },
  columns: {
    type: Array as unknown as PropType<CodegenApi.CodegenColumnVO[]>,
    default: () => null
  }
})

const formRef = ref()
const formData = ref({
  templateType: null,
  frontType: null,
  scene: null,
  moduleName: '',
  businessName: '',
  className: '',
  classComment: '',
  parentMenuId: null,
  genPath: '',
  genType: '',
  masterTableId: undefined,
  subJoinColumnId: undefined,
  subJoinMany: undefined,
  treeParentColumnId: undefined,
  treeNameColumnId: undefined
})

const rules = reactive({
  templateType: [required],
  frontType: [required],
  scene: [required],
  moduleName: [required],
  businessName: [required],
  businessPackage: [required],
  className: [required],
  classComment: [required],
  masterTableId: [required],
  subJoinColumnId: [required],
  subJoinMany: [required],
  treeParentColumnId: [required],
  treeNameColumnId: [required]
})

const tables = ref([]) // 表定义列表
const menus = ref<any[]>([])
const menuTreeProps = {
  label: 'name'
}

watch(
  () => props.table,
  async (table) => {
    if (!table) return
    formData.value = table as any
    // 加载表列表
    if (table.dataSourceConfigId >= 0) {
      tables.value = await CodegenApi.getCodegenTableList(formData.value.dataSourceConfigId)
    }
  },
  {
    deep: true,
    immediate: true
  }
)

onMounted(async () => {
  try {
    // 加载菜单
    const resp = await MenuApi.getMenusList()
    menus.value = handleTree(resp)
  } catch {}
})

defineExpose({
  validate: async () => unref(formRef)?.validate()
})
</script>
