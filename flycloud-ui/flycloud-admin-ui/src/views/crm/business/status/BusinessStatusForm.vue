<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item
        :label="t('auto.views.crm.business.status.BusinessStatusForm.k3fe211b1')"
        prop="name"
      >
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.crm.business.status.BusinessStatusForm.k5439e6a6')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.crm.business.status.BusinessStatusForm.k21b65f45')"
        prop="deptIds"
      >
        <template #label>
          <Tooltip
            :message="t('extra.keb7c08a4')"
            :title="t('auto.views.crm.business.status.BusinessStatusForm.k21b65f45')"
          />
        </template>
        <el-tree
          ref="treeRef"
          :data="deptList"
          :props="defaultProps"
          :check-strictly="!checkStrictly"
          node-key="id"
          :placeholder="t('system.user.deptPlaceholder')"
          show-checkbox
        />
      </el-form-item>
      <el-form-item :label="t('extra.k58f4ba99')" prop="statuses">
        <el-table
          border
          style="width: 100%"
          :data="formData.statuses.concat(BusinessStatusApi.DEFAULT_STATUSES)"
        >
          <el-table-column
            align="center"
            :label="t('auto.views.crm.statistics.funnel.components.FunnelBusiness.k4ca39faa')"
            width="70"
          >
            <template #default="scope">
              <el-text v-if="!scope.row.defaultStatus"
                >{{ t('auto.views.crm.statistics.funnel.components.FunnelBusiness.k4ca39faa') }}
                {{ scope.$index + 1 }}</el-text
              >
              <el-text v-else>{{ t('common.doneLabel') }}</el-text>
            </template>
          </el-table-column>
          <el-table-column align="center" :label="t('extra.k4c97de6d')" width="160" prop="name">
            <template #default="{ row }">
              <el-input
                v-if="!row.endStatus"
                v-model="row.name"
                :placeholder="t('extra.k262ebd5e')"
              />
              <el-text v-else>{{ row.name }}</el-text>
            </template>
          </el-table-column>
          <el-table-column width="140" align="center" :label="t('extra.k78327a3b')" prop="percent">
            <template #default="{ row }">
              <el-input-number
                v-if="!row.endStatus"
                v-model="row.percent"
                :placeholder="t('extra.k94e4d53c')"
                controls-position="right"
                :min="0"
                :max="100"
                :precision="2"
                class="!w-1/1"
              />
              <el-text v-else>{{ row.percent }}</el-text>
            </template>
          </el-table-column>
          <el-table-column :label="t('common.operation')" width="110" align="center">
            <template #default="scope">
              <el-button
                v-if="!scope.row.endStatus"
                link
                type="primary"
                @click="addStatus(scope.$index)"
              >
                {{ t('form.add') }}
              </el-button>
              <el-button
                v-if="!scope.row.endStatus"
                link
                type="danger"
                @click="deleteStatusArea(scope.$index)"
                :disabled="formData.statuses.length <= 1"
              >
                {{ t('common.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('extra.k008b8fcb')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.components.AppLinkInput.AppLinkSelectDialog.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
// @ts-nocheck
import * as BusinessStatusApi from '@/api/crm/business/status'
import { defaultProps, handleTree } from '@/utils/tree'
import * as DeptApi from '@/api/system/dept'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的组：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: '',
  deptIds: [],
  statuses: []
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.crm.business.status.BusinessStatusForm.k9dbc1486'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref
const deptList = ref<Tree[]>([]) // 树形结构
const treeRef = ref() // 菜单树组件 Ref
const checkStrictly = ref(true) // 是否严格模式，即父子不关联

/** 打开弹窗 */
const open = async (type: string, id?: string) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await BusinessStatusApi.getBusinessStatus(id)
      treeRef.value.setCheckedKeys(formData.value.deptIds)
      if (formData.value.statuses.length == 0) {
        addStatus()
      }
    } finally {
      formLoading.value = false
    }
  } else {
    addStatus()
  }
  // 加载部门树
  deptList.value = handleTree(await DeptApi.getSimpleDeptList())
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as BusinessStatusApi.BusinessStatusTypeVO
    data.deptIds = treeRef.value.getCheckedKeys(false)
    if (formType.value === 'create') {
      await BusinessStatusApi.createBusinessStatus(data)
      message.success(t('common.createSuccess'))
    } else {
      await BusinessStatusApi.updateBusinessStatus(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  checkStrictly.value = true
  formData.value = {
    id: undefined,
    name: '',
    deptIds: [],
    statuses: []
  }
  treeRef.value?.setCheckedNodes([])
  formRef.value?.resetFields()
}

/** 添加状态 */
const addStatus = () => {
  const data = formData.value
  data.statuses.push({
    name: '',
    percent: undefined
  })
}

/** 删除状态 */
const deleteStatusArea = (index: number) => {
  const data = formData.value
  data.statuses.splice(index, 1)
}
</script>
