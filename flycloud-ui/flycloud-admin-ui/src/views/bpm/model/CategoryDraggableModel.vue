<template>
  <div class="flex items-center h-50px">
    <!-- 头部：分类名 -->
    <div class="flex items-center">
      <el-tooltip
        :content="t('auto.views.bpm.model.CategoryDraggableModel.k2980f293')"
        v-if="isCategorySorting"
      >
        <Icon
          :size="22"
          icon="ic:round-drag-indicator"
          class="ml-10px category-drag-icon cursor-move text-#8a909c"
        />
      </el-tooltip>
      <h3 class="ml-20px mr-8px text-18px">{{ categoryInfo.name }}</h3>
      <div class="color-gray-600 text-16px"> ({{ categoryInfo.modelList?.length || 0 }}) </div>
    </div>
    <!-- 头部：操作 -->
    <div class="flex-1 flex" v-if="!isCategorySorting">
      <div
        v-if="categoryInfo.modelList.length > 0"
        class="ml-20px flex items-center"
        :class="[
          'transition-transform duration-300 cursor-pointer',
          isExpand ? 'rotate-180' : 'rotate-0'
        ]"
        @click="isExpand = !isExpand"
      >
        <Icon icon="ep:arrow-down-bold" color="#999" />
      </div>
      <div class="ml-auto flex items-center" :class="isModelSorting ? 'mr-15px' : 'mr-45px'">
        <template v-if="!isModelSorting">
          <el-button
            v-if="categoryInfo.modelList.length > 0"
            link
            type="info"
            class="mr-20px"
            @click.stop="handleModelSort"
          >
            <Icon icon="fa:sort-amount-desc" class="mr-5px" />
            {{ t('extra.k8001152f') }}
          </el-button>
          <el-button v-else link type="info" class="mr-20px" @click.stop="openModelForm('create')">
            <Icon icon="fa:plus" class="mr-5px" />
            {{ t('extra.k4513a049') }}
          </el-button>
          <el-dropdown
            @command="(command) => handleCategoryCommand(command, categoryInfo)"
            placement="bottom"
          >
            <el-button link type="info">
              <Icon icon="ep:setting" class="mr-5px" />
              {{ t('extra.k8c3fa055') }}
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="handleRename">
                  {{ t('auto.views.bpm.model.CategoryDraggableModel.k1cd80fd7') }}
                </el-dropdown-item>
                <el-dropdown-item command="handleDeleteCategory">
                  {{ t('auto.views.bpm.model.CategoryDraggableModel.kbb1753f8') }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button @click.stop="handleModelSortCancel">
            {{ t('auto.components.AppLinkInput.AppLinkSelectDialog.kd54aeadc') }}
          </el-button>
          <el-button type="primary" @click.stop="handleModelSortSubmit">
            {{ t('extra.kcbe89f13') }}
          </el-button>
        </template>
      </div>
    </div>
  </div>
  <!-- 模型列表 -->
  <el-collapse-transition>
    <div v-show="isExpand">
      <el-table
        :class="categoryInfo.name"
        ref="tableRef"
        :header-cell-style="{ backgroundColor: isDark ? '' : '#edeff0', paddingLeft: '10px' }"
        :cell-style="{ paddingLeft: '10px' }"
        :row-style="{ height: '68px' }"
        :data="modelList"
        height="calc(100vh - 320px)"
        row-key="id"
      >
        <el-table-column :label="t('auto.views.bpm.task.copy.index.kf766a7e3')" prop="name">
          <template #default="scope">
            <div class="flex items-center">
              <el-tooltip
                :content="t('auto.components.Draggable.index.k2980f293')"
                v-if="isModelSorting"
              >
                <Icon
                  icon="ic:round-drag-indicator"
                  class="drag-icon cursor-move text-#8a909c mr-10px"
                />
              </el-tooltip>
              <el-image :src="scope.row.icon" class="h-38px w-38px mr-10px rounded" />
              {{ scope.row.name }}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="t('extra.kf9649507')" prop="startUserIds" width="150">
          <template #default="scope">
            <el-text v-if="!scope.row.startUsers || scope.row.startUsers.length === 0">
              {{ t('extra.k0d794240') }}
            </el-text>
            <el-text v-else-if="scope.row.startUsers.length == 1">
              {{ scope.row.startUsers[0].name }}
            </el-text>
            <el-text v-else>
              <el-tooltip
                class="box-item"
                effect="dark"
                placement="top"
                :content="scope.row.startUsers.map((user: any) => user.name).join('、')"
              >
                {{ scope.row.startUsers[0].name }}{{ t('extra.k11ccee3a') }}
                {{ scope.row.startUsers.length }} {{ t('extra.k1a18d432') }}
              </el-tooltip>
            </el-text>
          </template>
        </el-table-column>
        <el-table-column :label="t('extra.k1a946e0f')" prop="formType">
          <template #default="scope">
            <el-button
              v-if="scope.row.formType === BpmModelFormType.NORMAL"
              type="primary"
              link
              @click="handleFormDetail(scope.row)"
            >
              <span>{{ scope.row.formName }}</span>
            </el-button>
            <el-button
              v-else-if="scope.row.formType === BpmModelFormType.CUSTOM"
              type="primary"
              link
              @click="handleFormDetail(scope.row)"
            >
              <span>{{ scope.row.formCustomCreatePath }}</span>
            </el-button>
            <label v-else>{{ t('extra.kaadb29f8') }}</label>
          </template>
        </el-table-column>
        <el-table-column :label="t('extra.kea7f6b97')" prop="deploymentTime" width="225">
          <template #default="scope">
            <div class="flex items-center">
              <span v-if="scope.row.processDefinition" class="w-150px">
                {{ formatDate(scope.row.processDefinition.deploymentTime) }}
              </span>
              <el-tag v-if="scope.row.processDefinition">
                v{{ scope.row.processDefinition.version }}
              </el-tag>
              <el-tag v-else type="warning">{{ t('extra.k647f2223') }}</el-tag>
              <el-tag
                v-if="scope.row.processDefinition?.suspensionState === 2"
                type="warning"
                class="ml-10px"
              >
                {{ t('extra.k69b0f684') }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="t('common.operation')" width="230" fixed="right" align="center">
          <template #default="scope">
            <el-button
              link
              type="primary"
              @click="openModelForm('update', scope.row.id)"
              v-hasPermi="['bpm:manage:model:update']"
              :disabled="!isManagerUser(scope.row)"
            >
              {{ t('extra.k4c512392') }}
            </el-button>
            <el-button
              link
              class="!ml-5px"
              type="primary"
              @click="handleDesign(scope.row)"
              v-hasPermi="['bpm:manage:model:designModel']"
              :disabled="!isManagerUser(scope.row)"
            >
              {{ t('extra.kb08890a6') }}
            </el-button>
            <el-button
              link
              class="!ml-5px"
              type="primary"
              @click="handleDeploy(scope.row)"
              v-hasPermi="['bpm:manage:model:publish']"
              :disabled="!isManagerUser(scope.row)"
            >
              {{ t('extra.kb464b4af') }}
            </el-button>
            <el-dropdown
              class="!align-middle ml-5px"
              @command="(command) => handleModelCommand(command, scope.row)"
              v-hasPermi="[
                'bpm:manage:model:history',
                'bpm:manage:model:enable',
                'bpm:manage:model:delete'
              ]"
            >
              <el-button type="primary" link>{{ t('action.more') }}</el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    command="handleDefinitionList"
                    v-if="checkPermi(['bpm:manage:model:history'])"
                  >
                    {{ t('extra.kc827d8db') }}
                  </el-dropdown-item>
                  <el-dropdown-item
                    command="handleChangeState"
                    v-if="checkPermi(['bpm:manage:model:enable']) && scope.row.processDefinition"
                    :disabled="!isManagerUser(scope.row)"
                  >
                    {{ scope.row.processDefinition.suspensionState === 1 ? '停用' : '启用' }}
                  </el-dropdown-item>
                  <el-dropdown-item
                    type="danger"
                    command="handleDelete"
                    v-if="checkPermi(['bpm:manage:model:delete'])"
                    :disabled="!isManagerUser(scope.row)"
                  >
                    {{ t('common.delete') }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-collapse-transition>

  <!-- 弹窗：重命名分类 -->
  <Dialog :fullscreen="false" class="rename-dialog" v-model="renameCategoryVisible" width="400">
    <template #title>
      <div class="pl-10px font-bold text-18px"> {{ t('extra.kdddf3c2d') }} </div>
    </template>
    <div class="px-30px">
      <el-input v-model="renameCategoryForm.name" />
    </div>
    <template #footer>
      <div class="pr-25px pb-25px">
        <el-button @click="renameCategoryVisible = false">{{
          t('auto.components.AppLinkInput.AppLinkSelectDialog.kd54aeadc')
        }}</el-button>
        <el-button type="primary" @click="handleRenameConfirm">{{
          t('extra.k008b8fcb')
        }}</el-button>
      </div>
    </template>
  </Dialog>

  <!-- 表单弹窗：添加流程模型 -->
  <ModelForm :categoryId="categoryInfo.code" ref="modelFormRef" @success="emit('success')" />
</template>

<script lang="ts" setup>
import ModelForm from './ModelForm.vue'
import { CategoryApi, CategoryVO } from '@/api/bpm/category'
import Sortable from 'sortablejs'
import { propTypes } from '@/utils/propTypes'
import { formatDate } from '@/utils/formatTime'
import * as ModelApi from '@/api/bpm/model'
import * as FormApi from '@/api/bpm/form'
import { setConfAndFields2 } from '@/utils/formCreate'
import { BpmModelFormType, BpmModelType } from '@/utils/constants'
import { checkPermi } from '@/utils/permission'
import { useUserStoreWithOut } from '@/store/modules/user'
import { useAppStore } from '@/store/modules/app'
import { cloneDeep } from 'lodash-es'
const { t } = useI18n()
defineOptions({ name: 'BpmModel' })

const props = defineProps({
  categoryInfo: propTypes.object.def([]), // 分类后的数据
  isCategorySorting: propTypes.bool.def(false) // 是否分类在排序
})
const emit = defineEmits(['success'])
const message = useMessage() // 消息弹窗
const { push } = useRouter() // 路由
const userStore = useUserStoreWithOut() // 用户信息缓存
const isDark = computed(() => useAppStore().getIsDark) // 是否黑暗模式

const isModelSorting = ref(false) // 是否正处于排序状态
const originalData: any = ref([]) // 原始数据
const modelList: any = ref([]) // 模型列表
const isExpand = ref(false) // 是否处于展开状态

/** '更多'操作按钮 */
const handleModelCommand = (command: string, row: any) => {
  switch (command) {
    case 'handleDefinitionList':
      handleDefinitionList(row)
      break
    case 'handleDelete':
      handleDelete(row)
      break
    case 'handleChangeState':
      handleChangeState(row)
      break
    default:
      break
  }
}

/** '分类'操作按钮 */
const handleCategoryCommand = async (command: string, row: any) => {
  switch (command) {
    case 'handleRename':
      renameCategoryForm.value = await CategoryApi.getCategory(row.id)
      renameCategoryVisible.value = true
      break
    case 'handleDeleteCategory':
      await handleDeleteCategory()
      break
    default:
      break
  }
}

/** 删除按钮操作 */
const handleDelete = async (row: any) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ModelApi.deleteModel(row.id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    emit('success')
  } catch {}
}

/** 更新状态操作 */
const handleChangeState = async (row: any) => {
  const state = row.processDefinition.suspensionState
  const newState = state === 1 ? 2 : 1
  try {
    // 修改状态的二次确认
    const id = row.id
    debugger
    const statusState = state === 1 ? t('common.disabled') : t('common.enabled')
    const content =
      t('auto.views.bpm.model.CategoryDraggableModel.k9fc01381') +
      statusState +
      t('auto.views.bpm.model.CategoryDraggableModel.k18795f9e') +
      row.name +
      t('auto.views.bpm.model.CategoryDraggableModel.kc87f4c07')
    await message.confirm(content)
    // 发起修改状态
    await ModelApi.updateModelState(id, newState)
    message.success(statusState + t('common.success'))
    // 刷新列表
    emit('success')
  } catch {}
}

/** 设计流程 */
const handleDesign = (row: any) => {
  if (row.type == BpmModelType.BPMN) {
    push({
      name: 'BpmModelEditor',
      query: {
        modelId: row.id
      }
    })
  } else {
    push({
      name: 'SimpleModelDesign',
      query: {
        modelId: row.id
      }
    })
  }
}

/** 发布流程 */
const handleDeploy = async (row: any) => {
  try {
    // 删除的二次确认
    await message.confirm(t('auto.views.bpm.model.CategoryDraggableModel.k35018faa'))
    // 发起部署
    await ModelApi.deployModel(row.id)
    message.success(t(t('auto.views.bpm.model.CategoryDraggableModel.k48388beb')))
    // 刷新列表
    emit('success')
  } catch {}
}

/** 跳转到指定流程定义列表 */
const handleDefinitionList = (row: any) => {
  push({
    name: 'BpmProcessDefinition',
    query: {
      key: row.key
    }
  })
}

/** 流程表单的详情按钮操作 */
const formDetailVisible = ref(false)
const formDetailPreview = ref({
  rule: [],
  option: {}
})
const handleFormDetail = async (row: any) => {
  if (row.formType == 10) {
    // 设置表单
    const data = await FormApi.getForm(row.formId)
    setConfAndFields2(formDetailPreview, data.conf, data.fields)
    // 弹窗打开
    formDetailVisible.value = true
  } else {
    await push({
      path: row.formCustomCreatePath
    })
  }
}

/** 判断是否可以操作 */
const isManagerUser = (row: any) => {
  const userId = userStore.getUser.id
  return row.managerUserIds && row.managerUserIds.includes(userId)
}

/** 处理模型的排序 **/
const handleModelSort = () => {
  // 保存初始数据
  originalData.value = cloneDeep(props.categoryInfo.modelList)
  isModelSorting.value = true
  initSort()
}

/** 处理模型的排序提交 */
const handleModelSortSubmit = async () => {
  // 保存排序
  const ids = modelList.value.map((item: any) => item.id)
  await ModelApi.updateModelSortBatch(ids)
  // 刷新列表
  isModelSorting.value = false
  message.success(t('auto.views.bpm.model.CategoryDraggableModel.kb04fee6b'))
  emit('success')
}

/** 处理模型的排序取消 */
const handleModelSortCancel = () => {
  // 恢复初始数据
  modelList.value = cloneDeep(originalData.value)
  isModelSorting.value = false
}

/** 创建拖拽实例 */
const tableRef = ref()
const initSort = () => {
  const table = document.querySelector(`.${props.categoryInfo.name} .el-table__body-wrapper tbody`)
  Sortable.create(table, {
    group: 'shared',
    animation: 150,
    draggable: '.el-table__row',
    handle: '.drag-icon',
    // 结束拖动事件
    onEnd: ({ newDraggableIndex, oldDraggableIndex }) => {
      if (oldDraggableIndex !== newDraggableIndex) {
        modelList.value.splice(
          newDraggableIndex,
          0,
          modelList.value.splice(oldDraggableIndex, 1)[0]
        )
      }
    }
  })
}

/** 更新 modelList 模型列表 */
const updateModeList = () => {
  modelList.value = cloneDeep(props.categoryInfo.modelList)
  if (props.categoryInfo.modelList.length > 0) {
    isExpand.value = true
  }
}

/** 重命名弹窗确定 */
const renameCategoryVisible = ref(false)
const renameCategoryForm = ref({
  name: ''
})
const handleRenameConfirm = async () => {
  if (renameCategoryForm.value?.name.length === 0) {
    return message.warning(t('auto.views.bpm.model.CategoryDraggableModel.kc2afb255'))
  }
  // 发起修改
  await CategoryApi.updateCategory(renameCategoryForm.value as CategoryVO)
  message.success(t('auto.views.bpm.model.CategoryDraggableModel.k92d4e6f8'))
  // 刷新列表
  renameCategoryVisible.value = false
  emit('success')
}

/** 删除分类 */
const handleDeleteCategory = async () => {
  try {
    if (props.categoryInfo.modelList.length > 0) {
      return message.warning(t('auto.views.bpm.model.CategoryDraggableModel.k790b6c91'))
    }
    await message.confirm(t('auto.views.bpm.model.CategoryDraggableModel.k670d7aaa'))
    // 发起删除
    await CategoryApi.deleteCategory(props.categoryInfo.id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    emit('success')
  } catch {}
}

/** 添加流程模型弹窗 */
const modelFormRef = ref()
const openModelForm = (type: string, id?: number) => {
  modelFormRef.value.open(type, id)
}

watch(() => props.categoryInfo.modelList, updateModeList, { immediate: true })
watch(
  () => props.isCategorySorting,
  (val) => {
    if (val) isExpand.value = false
  },
  { immediate: true }
)
</script>

<style lang="scss">
.rename-dialog.el-dialog {
  padding: 0 !important;

  .el-dialog__header {
    border-bottom: none;
  }

  .el-dialog__footer {
    border-top: none !important;
  }
}
</style>
<style lang="scss" scoped>
:deep() {
  .el-table__cell {
    overflow: hidden;
    border-bottom: none !important;
  }
}
</style>
