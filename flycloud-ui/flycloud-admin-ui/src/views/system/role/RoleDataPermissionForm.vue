<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.system.role.RoleDataPermissionForm.kf3d1827b')"
    width="800"
  >
    <el-form ref="formRef" v-loading="formLoading" :model="formData" label-width="80px">
      <el-form-item :label="t('auto.views.system.role.RoleDataPermissionForm.k3aa1f085')">
        <el-tag>{{ formData.name }}</el-tag>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.role.RoleDataPermissionForm.k07f826b6')">
        <el-tag>{{ formData.code }}</el-tag>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.role.RoleDataPermissionForm.ke19818a2')">
        <el-select v-model="formData.dataScope">
          <el-option
            v-for="item in getIntDictOptions(DICT_TYPE.SYSTEM_DATA_SCOPE)"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <el-form-item
      v-if="formData.dataScope === SystemDataScopeEnum.DEPT_CUSTOM"
      :label="t('auto.views.system.role.RoleDataPermissionForm.ke19818a2')"
      label-width="80px"
    >
      <el-card class="w-full h-400px !overflow-y-scroll" shadow="never">
        <template #header>
          {{ t('extra.k1d20ed52') }}
          <el-switch
            v-model="treeNodeAll"
            :active-text="t('extra.k45831684')"
            :inactive-text="t('extra.k246cadca')"
            inline-prompt
            @change="handleCheckedTreeNodeAll()"
          />
          {{ t('extra.kb37608ae') }}
          <el-switch
            v-model="deptExpand"
            :active-text="t('extra.ke4c9999e')"
            :inactive-text="t('extra.k23e5c530')"
            inline-prompt
            @change="handleCheckedTreeExpand"
          />
          {{ t('extra.k882149fd') }}
          <el-switch
            v-model="checkStrictly"
            :active-text="t('extra.k45831684')"
            :inactive-text="t('extra.k246cadca')"
            inline-prompt
          />
        </template>
        <el-tree
          ref="treeRef"
          :check-strictly="!checkStrictly"
          :data="deptOptions"
          :props="defaultProps"
          default-expand-all
          :empty-text="
            t('auto.components.bpmnProcessDesigner.package.penal.task.task_components.k29a4e57b')
          "
          node-key="id"
          show-checkbox
        />
      </el-card>
    </el-form-item>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('extra.k008b8fcb')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.components.AppLinkInput.AppLinkSelectDialog.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
// @ts-nocheck
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { defaultProps, handleTree } from '@/utils/tree'
import { SystemDataScopeEnum } from '@/utils/constants'
import * as RoleApi from '@/api/system/role'
import * as DeptApi from '@/api/system/dept'
import * as PermissionApi from '@/api/system/permission'
const { t } = useI18n()
defineOptions({ name: 'SystemRoleDataPermissionForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formData = reactive({
  id: undefined,
  name: '',
  code: '',
  dataScope: undefined,
  dataScopeDeptIds: []
})
const formRef = ref() // 表单 Ref
const deptOptions = ref<any[]>([]) // 部门树形结构
const deptExpand = ref(true) // 展开/折叠
const treeRef = ref() // 菜单树组件 Ref
const treeNodeAll = ref(false) // 全选/全不选
const checkStrictly = ref(true) // 是否严格模式，即父子不关联

/** 打开弹窗 */
const open = async (row: RoleApi.RoleVO) => {
  dialogVisible.value = true
  resetForm()
  // 加载 Dept 列表。注意，必须放在前面，不然下面 setChecked 没数据节点
  deptOptions.value = handleTree(await DeptApi.getSimpleDeptList())
  // 设置数据
  formData.id = row.id
  formData.name = row.name
  formData.code = row.code
  formData.dataScope = row.dataScope
  await nextTick()
  // 需要在 DOM 渲染完成后，再设置选中状态
  row.dataScopeDeptIds?.forEach((deptId: string): void => {
    treeRef.value.setChecked(deptId, true, false)
  })
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  message.warning(t('auto.views.system.role.RoleDataPermissionForm.ke901250b'))
  // formLoading.value = true
  // try {
  //   const data = {
  //     roleId: formData.id,
  //     dataScope: formData.dataScope,
  //     dataScopeDeptIds:
  //       formData.dataScope !== SystemDataScopeEnum.DEPT_CUSTOM
  //         ? []
  //         : treeRef.value.getCheckedKeys(false)
  //   }
  //   await PermissionApi.assignRoleDataScope(data)
  //   message.success(t('common.updateSuccess'))
  //   dialogVisible.value = false
  //   // 发送操作成功的事件
  //   emit('success')
  // } finally {
  //   formLoading.value = false
  // }
}

/** 重置表单 */
const resetForm = () => {
  // 重置选项
  treeNodeAll.value = false
  deptExpand.value = true
  checkStrictly.value = true
  // 重置表单
  formData.value = {
    id: undefined,
    name: '',
    code: '',
    dataScope: undefined,
    dataScopeDeptIds: []
  }
  treeRef.value?.setCheckedNodes([])
  formRef.value?.resetFields()
}

/** 全选/全不选 */
const handleCheckedTreeNodeAll = () => {
  treeRef.value.setCheckedNodes(treeNodeAll.value ? deptOptions.value : [])
}

/** 展开/折叠全部 */
const handleCheckedTreeExpand = () => {
  const nodes = treeRef.value?.store.nodesMap
  for (let node in nodes) {
    if (nodes[node].expanded === deptExpand.value) {
      continue
    }
    nodes[node].expanded = deptExpand.value
  }
}
</script>
