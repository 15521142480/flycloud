<template>
  <div
    class="h-50px bottom-10 text-14px flex items-center color-#32373c dark:color-#fff font-bold btn-container"
  >
    <!-- 【通过】按钮 -->
    <el-popover
      :visible="popOverVisible.approve"
      placement="top-end"
      :width="420"
      trigger="click"
      v-if="runningTask && isHandleTaskStatus() && isShowButton(OperationButtonType.APPROVE)"
    >
      <template #reference>
        <el-button plain type="success" @click="openPopover('approve')">
          <Icon icon="ep:select" />&nbsp; {{ getButtonDisplayName(OperationButtonType.APPROVE) }}
        </el-button>
      </template>
      <!-- 审批表单 -->
      <div class="flex flex-col flex-1 pt-20px px-20px" v-loading="formLoading">
        <el-form
          label-position="top"
          class="mb-auto"
          ref="formRef"
          :model="genericForm"
          :rules="genericRule"
          label-width="100px"
        >
          <el-card v-if="runningTask?.formId > 0" class="mb-15px !-mt-10px">
            <template #header>
              <span class="el-icon-picture-outline">
                {{ t('extra.k78bf8bef') }}{{ runningTask?.formName }}】
              </span>
            </template>
            <form-create
              v-model="approveForm.value"
              v-model:api="approveFormFApi"
              :option="approveForm.option"
              :rule="approveForm.rule"
            />
          </el-card>
          <el-form-item :label="t('extra.k98a52a9e')" prop="reason">
            <el-input
              v-model="genericForm.reason"
              :placeholder="t('extra.k20bf8923')"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
          <el-form-item>
            <el-button :disabled="formLoading" type="success" @click="handleAudit(true)">
              {{ getButtonDisplayName(OperationButtonType.APPROVE) }}
            </el-button>
            <el-button @click="popOverVisible.approve = false">
              {{ t('common.cancel') }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-popover>

    <!-- 【拒绝】按钮 -->
    <el-popover
      :visible="popOverVisible.reject"
      placement="top-end"
      :width="420"
      trigger="click"
      v-if="runningTask && isHandleTaskStatus() && isShowButton(OperationButtonType.REJECT)"
    >
      <template #reference>
        <el-button class="mr-20px" plain type="danger" @click="openPopover('reject')">
          <Icon icon="ep:close" />&nbsp; {{ getButtonDisplayName(OperationButtonType.REJECT) }}
        </el-button>
      </template>
      <!-- 审批表单 -->
      <div class="flex flex-col flex-1 pt-20px px-20px" v-loading="formLoading">
        <el-form
          label-position="top"
          class="mb-auto"
          ref="formRef"
          :model="genericForm"
          :rules="genericRule"
          label-width="100px"
        >
          <el-card v-if="runningTask?.formId > 0" class="mb-15px !-mt-10px">
            <template #header>
              <span class="el-icon-picture-outline">
                {{ t('extra.k78bf8bef') }}{{ runningTask?.formName }}】
              </span>
            </template>
            <form-create
              v-model="approveForm.value"
              v-model:api="approveFormFApi"
              :option="approveForm.option"
              :rule="approveForm.rule"
            />
          </el-card>
          <el-form-item :label="t('extra.k98a52a9e')" prop="reason">
            <el-input
              v-model="genericForm.reason"
              :placeholder="t('extra.k20bf8923')"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
          <el-form-item>
            <el-button :disabled="formLoading" type="danger" @click="handleAudit(false)">
              {{ getButtonDisplayName(OperationButtonType.REJECT) }}
            </el-button>
            <el-button @click="popOverVisible.reject = false"> {{ t('common.cancel') }} </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-popover>

    <!-- 【抄送】按钮 -->
    <el-popover
      :visible="popOverVisible.copy"
      placement="top-start"
      :width="420"
      trigger="click"
      v-if="runningTask && isHandleTaskStatus() && isShowButton(OperationButtonType.COPY)"
    >
      <template #reference>
        <div @click="openPopover('copy')" class="hover-bg-gray-100 rounded-xl p-6px">
          <Icon :size="14" icon="svg-icon:send" />&nbsp;
          {{ getButtonDisplayName(OperationButtonType.COPY) }}
        </div>
      </template>
      <div class="flex flex-col flex-1 pt-20px px-20px" v-loading="formLoading">
        <el-form
          label-position="top"
          class="mb-auto"
          ref="formRef"
          :model="genericForm"
          :rules="genericRule"
          label-width="100px"
        >
          <el-form-item
            :label="t('auto.components.SimpleProcessDesignerV2.src.consts.kdb9f866c')"
            prop="copyUserIds"
          >
            <el-select
              v-model="genericForm.copyUserIds"
              clearable
              style="width: 100%"
              multiple
              :placeholder="t('extra.k5837c75d')"
            >
              <el-option
                v-for="item in userOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="t('extra.k51cbb39c')" prop="copyReason">
            <el-input
              v-model="genericForm.copyReason"
              clearable
              :placeholder="t('extra.k6eb1aaca')"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item>
            <el-button :disabled="formLoading" type="primary" @click="handleCopy">
              {{ getButtonDisplayName(OperationButtonType.COPY) }}
            </el-button>
            <el-button @click="popOverVisible.copy = false"> {{ t('common.cancel') }} </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-popover>

    <!-- 【转交】按钮 -->
    <el-popover
      :visible="popOverVisible.transfer"
      placement="top-start"
      :width="420"
      trigger="click"
      v-if="runningTask && isHandleTaskStatus() && isShowButton(OperationButtonType.TRANSFER)"
    >
      <template #reference>
        <div @click="openPopover('transfer')" class="hover-bg-gray-100 rounded-xl p-6px">
          <Icon :size="14" icon="fa:share-square-o" />&nbsp;
          {{ getButtonDisplayName(OperationButtonType.TRANSFER) }}
        </div>
      </template>
      <div class="flex flex-col flex-1 pt-20px px-20px" v-loading="formLoading">
        <el-form
          label-position="top"
          class="mb-auto"
          ref="formRef"
          :model="genericForm"
          :rules="genericRule"
          label-width="100px"
        >
          <el-form-item :label="t('extra.k91950f5c')" prop="assigneeUserId">
            <el-select v-model="genericForm.assigneeUserId" clearable style="width: 100%">
              <el-option
                v-for="item in userOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="t('extra.k98a52a9e')" prop="reason">
            <el-input
              v-model="genericForm.reason"
              clearable
              :placeholder="t('extra.k20bf8923')"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item>
            <el-button :disabled="formLoading" type="primary" @click="handleTransfer()">
              {{ getButtonDisplayName(OperationButtonType.TRANSFER) }}
            </el-button>
            <el-button @click="popOverVisible.transfer = false">
              {{ t('common.cancel') }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-popover>

    <!-- 【委派】按钮 -->
    <el-popover
      :visible="popOverVisible.delegate"
      placement="top-start"
      :width="420"
      trigger="click"
      v-if="runningTask && isHandleTaskStatus() && isShowButton(OperationButtonType.DELEGATE)"
    >
      <template #reference>
        <div @click="openPopover('delegate')" class="hover-bg-gray-100 rounded-xl p-6px">
          <Icon :size="14" icon="ep:position" />&nbsp;
          {{ getButtonDisplayName(OperationButtonType.DELEGATE) }}
        </div>
      </template>
      <div class="flex flex-col flex-1 pt-20px px-20px" v-loading="formLoading">
        <el-form
          label-position="top"
          class="mb-auto"
          ref="formRef"
          :model="genericForm"
          :rules="genericRule"
          label-width="100px"
        >
          <el-form-item
            :label="t('auto.views.system.notify.template.NotifyTemplateSendForm.k69eea8b7')"
            prop="delegateUserId"
          >
            <el-select v-model="genericForm.delegateUserId" clearable style="width: 100%">
              <el-option
                v-for="item in userOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="t('extra.k98a52a9e')" prop="reason">
            <el-input
              v-model="genericForm.reason"
              clearable
              :placeholder="t('extra.k20bf8923')"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item>
            <el-button :disabled="formLoading" type="primary" @click="handleDelegate()">
              {{ getButtonDisplayName(OperationButtonType.DELEGATE) }}
            </el-button>
            <el-button @click="popOverVisible.delegate = false">
              {{ t('common.cancel') }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-popover>

    <!-- 【加签】按钮 当前任务审批人为A，向前加签选了一个C，则需要C先审批，然后再是A审批，向后加签B，A审批完，需要B再审批完，才算完成这个任务节点 -->
    <el-popover
      :visible="popOverVisible.addSign"
      placement="top-start"
      :width="420"
      trigger="click"
      v-if="runningTask && isHandleTaskStatus() && isShowButton(OperationButtonType.ADD_SIGN)"
    >
      <template #reference>
        <div @click="openPopover('addSign')" class="hover-bg-gray-100 rounded-xl p-6px">
          <Icon :size="14" icon="ep:plus" />&nbsp;
          {{ getButtonDisplayName(OperationButtonType.ADD_SIGN) }}
        </div>
      </template>
      <div class="flex flex-col flex-1 pt-20px px-20px" v-loading="formLoading">
        <el-form
          label-position="top"
          class="mb-auto"
          ref="formRef"
          :model="genericForm"
          :rules="genericRule"
          label-width="100px"
        >
          <el-form-item :label="t('extra.k700d2573')" prop="addSignUserIds">
            <el-select v-model="genericForm.addSignUserIds" multiple clearable style="width: 100%">
              <el-option
                v-for="item in userOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="t('extra.k98a52a9e')" prop="reason">
            <el-input
              v-model="genericForm.reason"
              clearable
              :placeholder="t('extra.k20bf8923')"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item>
            <el-button :disabled="formLoading" type="primary" @click="handlerAddSign('before')">
              {{ t('extra.k719ffd6e') }}{{ getButtonDisplayName(OperationButtonType.ADD_SIGN) }}
            </el-button>
            <el-button :disabled="formLoading" type="primary" @click="handlerAddSign('after')">
              {{ t('extra.ka83d3df4') }}{{ getButtonDisplayName(OperationButtonType.ADD_SIGN) }}
            </el-button>
            <el-button @click="popOverVisible.addSign = false">
              {{ t('common.cancel') }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-popover>

    <!-- 【减签】按钮 -->
    <el-popover
      :visible="popOverVisible.deleteSign"
      placement="top-start"
      :width="420"
      trigger="click"
      v-if="runningTask?.children.length > 0"
    >
      <template #reference>
        <div @click="openPopover('deleteSign')" class="hover-bg-gray-100 rounded-xl p-6px">
          <Icon :size="14" icon="ep:semi-select" />  {{ t('extra.k30856d25') }}
        </div>
      </template>
      <div class="flex flex-col flex-1 pt-20px px-20px" v-loading="formLoading">
        <el-form
          label-position="top"
          class="mb-auto"
          ref="formRef"
          :model="genericForm"
          :rules="genericRule"
          label-width="100px"
        >
          <el-form-item :label="t('extra.kd730c500')" prop="deleteSignTaskId">
            <el-select v-model="genericForm.deleteSignTaskId" clearable style="width: 100%">
              <el-option
                v-for="item in runningTask.children"
                :key="item.id"
                :label="getDeleteSignUserLabel(item)"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="t('extra.k98a52a9e')" prop="reason">
            <el-input
              v-model="genericForm.reason"
              clearable
              :placeholder="t('extra.k20bf8923')"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item>
            <el-button :disabled="formLoading" type="primary" @click="handlerDeleteSign()">
              {{ t('extra.k30856d25') }}
            </el-button>
            <el-button @click="popOverVisible.deleteSign = false">
              {{ t('common.cancel') }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-popover>

    <!-- 【退回】按钮 -->
    <el-popover
      :visible="popOverVisible.return"
      placement="top-start"
      :width="420"
      trigger="click"
      v-if="runningTask && isHandleTaskStatus() && isShowButton(OperationButtonType.RETURN)"
    >
      <template #reference>
        <div @click="openReturnPopover" class="hover-bg-gray-100 rounded-xl p-6px">
          <Icon :size="14" icon="ep:back" />&nbsp;
          {{ getButtonDisplayName(OperationButtonType.RETURN) }}
        </div>
      </template>
      <div class="flex flex-col flex-1 pt-20px px-20px" v-loading="formLoading">
        <el-form
          label-position="top"
          class="mb-auto"
          ref="formRef"
          :model="genericForm"
          :rules="genericRule"
          label-width="100px"
        >
          <el-form-item :label="t('extra.k3de3e170')" prop="targetTaskDefinitionKey">
            <el-select v-model="genericForm.targetTaskDefinitionKey" clearable style="width: 100%">
              <el-option
                v-for="item in returnList"
                :key="item.taskDefinitionKey"
                :label="item.name"
                :value="item.taskDefinitionKey"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="t('extra.kc71aa01f')" prop="returnReason">
            <el-input
              v-model="genericForm.returnReason"
              clearable
              :placeholder="t('extra.k124f4856')"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item>
            <el-button :disabled="formLoading" type="primary" @click="handleReturn()">
              {{ getButtonDisplayName(OperationButtonType.RETURN) }}
            </el-button>
            <el-button @click="popOverVisible.return = false"> {{ t('common.cancel') }} </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-popover>

    <!--【取消】按钮 这个对应发起人的取消, 只有发起人可以取消 -->
    <el-popover
      :visible="popOverVisible.cancel"
      placement="top-start"
      :width="420"
      trigger="click"
      v-if="
        userId === processInstance?.startUser?.id && !isEndProcessStatus(processInstance?.status)
      "
    >
      <template #reference>
        <div @click="openPopover('cancel')" class="hover-bg-gray-100 rounded-xl p-6px">
          <Icon :size="14" icon="fa:mail-reply" />  {{ t('common.cancel') }}
        </div>
      </template>
      <div class="flex flex-col flex-1 pt-20px px-20px" v-loading="formLoading">
        <el-form
          label-position="top"
          class="mb-auto"
          ref="formRef"
          :model="genericForm"
          :rules="genericRule"
          label-width="100px"
        >
          <el-form-item :label="t('extra.k48ac07ce')" prop="cancelReason">
            <span class="text-#878c93 text-12px">  {{ t('extra.k0aafa353') }}</span>
            <el-input
              v-model="genericForm.cancelReason"
              clearable
              :placeholder="t('extra.k6e7d5f9f')"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item>
            <el-button :disabled="formLoading" type="primary" @click="handleCancel()">
              {{ t('common.cancel') }}
            </el-button>
            <el-button @click="popOverVisible.cancel = false"> {{ t('common.cancel') }} </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-popover>
    <!-- 【再次提交】 按钮-->
    <div
      @click="handleReCreate()"
      class="hover-bg-gray-100 rounded-xl p-6px"
      v-if="
        userId === processInstance?.startUser?.id &&
        isEndProcessStatus(processInstance?.status) &&
        processDefinition?.formType === 10
      "
    >
      <Icon :size="14" icon="ep:refresh" />  {{ t('extra.kb11248e5') }}
    </div>
  </div>
</template>
<script lang="ts" setup>
import { useUserStoreWithOut } from '@/store/modules/user'
import { setConfAndFields2 } from '@/utils/formCreate'
import * as TaskApi from '@/api/bpm/task'
import * as ProcessInstanceApi from '@/api/bpm/processInstance'
import { propTypes } from '@/utils/propTypes'
import {
  OperationButtonType,
  OPERATION_BUTTON_NAME
} from '@/components/SimpleProcessDesignerV2/src/consts'
import { BpmProcessInstanceStatus } from '@/utils/constants'
const { t } = useI18n()
defineOptions({ name: 'ProcessInstanceBtnContainer' })

const router = useRouter() // 路由
const message = useMessage() // 消息弹窗
const { proxy } = getCurrentInstance() as any

const userId = useUserStoreWithOut().getUser.id // 当前登录的编号
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const props = defineProps({
  processInstance: propTypes.object, // 流程实例信息
  processDefinition: propTypes.object, // 流程定义信息
  userOptions: propTypes.any
})

const formLoading = ref(false) // 表单加载中
const popOverVisible = ref({
  approve: false,
  reject: false,
  transfer: false,
  delegate: false,
  addSign: false,
  return: false,
  copy: false,
  cancel: false,
  deleteSign: false
}) // 气泡卡是否展示
const returnList = ref([] as any) // 退回节点

// ========== 审批信息 ==========
const runningTask = ref<any>() // 运行中的任务
const genericForm = ref<any>({}) // 通用表单
const approveForm = ref<any>({}) // 审批通过时，额外的补充信息
const approveFormFApi = ref<any>({}) // approveForms 的 fAPi
const formRef = ref()
const genericRule = reactive({
  reason: [
    {
      required: true,
      message: t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.kb07b5494'),
      trigger: 'blur'
    }
  ],
  returnReason: [
    {
      required: true,
      message: t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.kad746a7d'),
      trigger: 'blur'
    }
  ],
  cancelReason: [
    {
      required: true,
      message: t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.k962a3b95'),
      trigger: 'blur'
    }
  ],
  copyUserIds: [
    {
      required: true,
      message: t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.kd57a604b'),
      trigger: 'change'
    }
  ],
  assigneeUserId: [
    {
      required: true,
      message: t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.kc815a78f'),
      trigger: 'change'
    }
  ],
  delegateUserId: [
    {
      required: true,
      message: t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.k6495510a'),
      trigger: 'change'
    }
  ],
  addSignUserIds: [
    {
      required: true,
      message: t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.k2b5baa3f'),
      trigger: 'change'
    }
  ],
  deleteSignTaskId: [
    {
      required: true,
      message: t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.keba60aa5'),
      trigger: 'change'
    }
  ],
  targetTaskDefinitionKey: [
    {
      required: true,
      message: t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.kc2c67d5a'),
      trigger: 'change'
    }
  ]
}) // 表单校验规则

/** 监听 approveFormFApis，实现它对应的 form-create 初始化后，隐藏掉对应的表单提交按钮 */
watch(
  () => approveFormFApi.value,
  (val) => {
    val?.btn?.show(false)
    val?.resetBtn?.show(false)
  },
  {
    deep: true
  }
)

/** 弹出退回气泡卡 */
const openReturnPopover = async () => {
  returnList.value = await TaskApi.getTaskListByReturn(runningTask.value.id)
  if (returnList.value.length === 0) {
    message.warning(
      t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.kb347049d')
    )
    return
  }
  await openPopover('return')
}

/** 弹出气泡卡 */
const openPopover = async (type: string) => {
  Object.keys(popOverVisible.value).forEach((item) => {
    popOverVisible.value[item] = item === type
  })
  await nextTick()
  formRef.value.resetFields()
}

/** 处理审批通过和不通过的操作 */
const handleAudit = async (pass: boolean) => {
  formLoading.value = true
  try {
    const genericFormRef = proxy.$refs['formRef']
    // 1.2 校验表单
    const elForm = unref(genericFormRef)
    if (!elForm) return
    const valid = await elForm.validate()
    if (!valid) return

    // 2.1 提交审批
    const data = {
      id: runningTask.value.id,
      reason: genericForm.value.reason
    }
    if (pass) {
      // 审批通过，并且有额外的 approveForm 表单，需要校验 + 拼接到 data 表单里提交
      const formCreateApi = approveFormFApi.value
      if (Object.keys(formCreateApi)?.length > 0) {
        await formCreateApi.validate()
        // @ts-ignore
        data.variables = approveForm.value.value
      }
      await TaskApi.approveTask(data)
      popOverVisible.value.approve = false
      message.success(
        t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.kcdc55743')
      )
    } else {
      await TaskApi.rejectTask(data)
      popOverVisible.value.reject = false
      message.success(
        t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.k6666bf90')
      )
    }
    // 2.2 加载最新数据
    reload()
  } finally {
    formLoading.value = false
  }
}

/** 处理抄送 */
const handleCopy = async () => {
  formLoading.value = true
  try {
    const copyFormRef = proxy.$refs['formRef']
    // 1. 校验表单
    const elForm = unref(copyFormRef)
    if (!elForm) return
    const valid = await elForm.validate()
    if (!valid) return
    // 2. 提交抄送
    const data = {
      id: runningTask.value.id,
      reason: genericForm.value.copyReason,
      copyUserIds: genericForm.value.copyUserIds
    }
    await TaskApi.copyTask(data)
    popOverVisible.value.copy = false
    message.success(
      t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.k3c5be5f2')
    )
  } finally {
    formLoading.value = false
  }
}

/** 处理转交 */
const handleTransfer = async () => {
  formLoading.value = true
  try {
    const transferFormRef = proxy.$refs['formRef']
    // 1.1 校验表单
    const elForm = unref(transferFormRef)
    if (!elForm) return
    const valid = await elForm.validate()
    if (!valid) return
    // 1.2 提交转交
    const data = {
      id: runningTask.value.id,
      reason: genericForm.value.reason,
      assigneeUserId: genericForm.value.assigneeUserId
    }

    await TaskApi.transferTask(data)
    popOverVisible.value.transfer = false
    message.success(
      t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.k3c5be5f2')
    )
    // 2. 加载最新数据
    reload()
  } finally {
    formLoading.value = false
  }
}

/** 处理委派 */
const handleDelegate = async () => {
  formLoading.value = true
  try {
    const deletegateFormRef = proxy.$refs['formRef']
    // 1.1 校验表单
    const elForm = unref(deletegateFormRef)
    if (!elForm) return
    const valid = await elForm.validate()
    if (!valid) return
    // 1.2 处理委派
    const data = {
      id: runningTask.value.id,
      reason: genericForm.value.reason,
      delegateUserId: genericForm.value.delegateUserId
    }

    await TaskApi.delegateTask(data)
    popOverVisible.value.delegate = false
    message.success(
      t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.k3c5be5f2')
    )
    // 2. 加载最新数据
    reload()
  } finally {
    formLoading.value = false
  }
}

/** 处理加签 */
const handlerAddSign = async (type: string) => {
  formLoading.value = true
  try {
    const transferFormRef = proxy.$refs['formRef']
    // 1.1 校验表单
    const elForm = unref(transferFormRef)
    if (!elForm) return
    const valid = await elForm.validate()
    if (!valid) return
    // 1.2 提交加签
    const data = {
      id: runningTask.value.id,
      type,
      reason: genericForm.value.reason,
      userIds: genericForm.value.addSignUserIds
    }
    await TaskApi.signCreateTask(data)
    message.success(
      t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.k3c5be5f2')
    )
    popOverVisible.value.addSign = false
    // 2 加载最新数据
    reload()
  } finally {
    formLoading.value = false
  }
}

/** 处理退回 */
const handleReturn = async () => {
  formLoading.value = true
  try {
    const returnFormRef = proxy.$refs['formRef']
    // 1.1 校验表单
    const elForm = unref(returnFormRef)
    if (!elForm) return
    const valid = await elForm.validate()
    if (!valid) return
    // 1.2 提交退回
    const data = {
      id: runningTask.value.id,
      reason: genericForm.value.returnReason,
      targetTaskDefinitionKey: genericForm.value.targetTaskDefinitionKey
    }

    await TaskApi.returnTask(data)
    popOverVisible.value.return = false
    message.success(
      t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.k3c5be5f2')
    )
    // 2 重新加载数据
    reload()
  } finally {
    formLoading.value = false
  }
}

/** 处理取消 */
const handleCancel = async () => {
  formLoading.value = true
  try {
    const cancelFormRef = proxy.$refs['formRef']
    // 1.1 校验表单
    const elForm = unref(cancelFormRef)
    if (!elForm) return
    const valid = await elForm.validate()
    if (!valid) return
    // 1.2 提交取消
    await ProcessInstanceApi.cancelProcessInstanceByStartUser(
      props.processInstance.id,
      genericForm.value.cancelReason
    )
    popOverVisible.value.return = false
    message.success(
      t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.k3c5be5f2')
    )
    // 2 重新加载数据
    reload()
  } finally {
    formLoading.value = false
  }
}

/** 处理再次提交 */
const handleReCreate = async () => {
  // 跳转发起流程界面
  await router.push({
    name: 'BpmProcessInstanceCreate',
    query: { processInstanceId: props.processInstance?.id }
  })
}

/** 获取减签人员标签 */
const getDeleteSignUserLabel = (task: any): string => {
  const deptName = task?.assigneeUser?.deptName || task?.ownerUser?.deptName
  const name = task?.assigneeUser?.name || task?.ownerUser?.name
  return t('extra.k4d11f6e2', { p0: name, p1: deptName })
}
/** 处理减签 */
const handlerDeleteSign = async () => {
  formLoading.value = true
  try {
    const deleteFormRef = proxy.$refs['formRef']
    // 1.1 校验表单
    const elForm = unref(deleteFormRef)
    if (!elForm) return
    const valid = await elForm.validate()
    if (!valid) return
    // 1.2 提交减签
    const data = {
      id: genericForm.value.deleteSignTaskId,
      reason: genericForm.value.reason
    }
    await TaskApi.signDeleteTask(data)
    message.success(
      t('auto.views.bpm.processInstance.detail.ProcessInstanceOperationButton.kd192b1af')
    )
    popOverVisible.value.deleteSign = false
    // 2 加载最新数据
    reload()
  } finally {
    formLoading.value = false
  }
}
/** 重新加载数据 */
const reload = () => {
  emit('success')
}

/** 任务是否为处理中状态 */
const isHandleTaskStatus = () => {
  let canHandle = false
  if (TaskApi.TaskStatusEnum.RUNNING === runningTask.value?.status) {
    canHandle = true
  }
  return canHandle
}

/** 流程状态是否为结束状态 */
const isEndProcessStatus = (status: number) => {
  let isEndStatus = false
  if (
    BpmProcessInstanceStatus.APPROVE === status ||
    BpmProcessInstanceStatus.REJECT === status ||
    BpmProcessInstanceStatus.CANCEL === status
  ) {
    isEndStatus = true
  }
  return isEndStatus
}

/** 是否显示按钮 */
const isShowButton = (btnType: OperationButtonType): boolean => {
  let isShow = true
  if (runningTask.value?.buttonsSetting && runningTask.value?.buttonsSetting[btnType]) {
    isShow = runningTask.value.buttonsSetting[btnType].enable
  }
  return isShow
}

/** 获取按钮的显示名称 */
const getButtonDisplayName = (btnType: OperationButtonType) => {
  let displayName = OPERATION_BUTTON_NAME.get(btnType)
  if (runningTask.value?.buttonsSetting && runningTask.value?.buttonsSetting[btnType]) {
    displayName = runningTask.value.buttonsSetting[btnType].displayName
  }
  return displayName
}

const loadTodoTask = (task: any) => {
  genericForm.value = {}
  approveForm.value = {}
  approveFormFApi.value = {}
  runningTask.value = task
  // 处理 approve 表单.
  if (task && task.formId && task.formConf) {
    const tempApproveForm = {}
    setConfAndFields2(tempApproveForm, task.formConf, task.formFields, task.formVariables)
    approveForm.value = tempApproveForm
  } else {
    approveForm.value = {} // 占位，避免为空
  }
}

defineExpose({ loadTodoTask })
</script>

<style lang="scss" scoped>
:deep(.el-affix--fixed) {
  background-color: var(--el-bg-color);
}

.btn-container {
  > div {
    display: flex;
    margin: 0 8px;
    cursor: pointer;
    align-items: center;

    &:hover {
      color: #6db5ff;
    }
  }
}
</style>
