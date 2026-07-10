<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="110px"
      v-loading="formLoading"
    >
      <el-form-item
        :label="t('auto.views.bpm.processListener.ProcessListenerForm.k364bd1bf')"
        prop="name"
      >
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.bpm.processListener.ProcessListenerForm.k010c1585')"
        />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.bpm.processListener.ProcessListenerForm.ke4e46c72')"
        prop="type"
      >
        <el-select
          v-model="formData.type"
          :placeholder="t('auto.views.bpm.processListener.ProcessListenerForm.k97f47b78')"
          @change="formData.event = undefined"
        >
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.BPM_PROCESS_LISTENER_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.bpm.processListener.ProcessListenerForm.k550e3280')"
        prop="event"
      >
        <el-select
          v-model="formData.event"
          :placeholder="t('auto.views.bpm.processListener.ProcessListenerForm.k1f1e1ef2')"
        >
          <el-option
            v-for="event in formData.type == 'execution'
              ? ['start', 'end']
              : ['create', 'assignment', 'complete', 'delete', 'update', 'timeout']"
            :label="event"
            :value="event"
            :key="event"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.bpm.processListener.ProcessListenerForm.k5d6dd202')"
        prop="valueType"
      >
        <el-select
          v-model="formData.valueType"
          :placeholder="t('auto.views.bpm.processListener.ProcessListenerForm.kce72e0df')"
        >
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.BPM_PROCESS_LISTENER_VALUE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.bpm.processListener.ProcessListenerForm.kd13531b1')"
        prop="value"
        v-if="formData.type == 'class'"
      >
        <el-input
          v-model="formData.value"
          :placeholder="t('auto.views.bpm.processListener.ProcessListenerForm.k49665ceb')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.bpm.processListener.ProcessListenerForm.k513c1c63')"
        prop="value"
        v-else
      >
        <el-input
          v-model="formData.value"
          :placeholder="t('auto.views.bpm.processListener.ProcessListenerForm.k7634dc77')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.bpm.processListener.ProcessListenerForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.bpm.processListener.ProcessListenerForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, getStrDictOptions, DICT_TYPE } from '@/utils/dict'
import { ProcessListenerApi, ProcessListenerVO } from '@/api/bpm/processListener'
import { CommonStatusEnum } from '@/utils/constants'

/** BPM 流程 表单 */
const { t } = useI18n()
defineOptions({ name: 'ProcessListenerForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  type: undefined,
  status: undefined,
  event: undefined,
  valueType: undefined,
  value: undefined
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.bpm.processListener.ProcessListenerForm.k46f3776c'),
      trigger: 'blur'
    }
  ],
  type: [
    {
      required: true,
      message: t('auto.views.bpm.processListener.ProcessListenerForm.kf0a2b424'),
      trigger: 'change'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.bpm.processListener.ProcessListenerForm.k1318b551'),
      trigger: 'blur'
    }
  ],
  event: [
    {
      required: true,
      message: t('auto.views.bpm.processListener.ProcessListenerForm.kad51b76f'),
      trigger: 'blur'
    }
  ],
  valueType: [
    {
      required: true,
      message: t('auto.views.bpm.processListener.ProcessListenerForm.k9e5998ee'),
      trigger: 'change'
    }
  ],
  value: [
    {
      required: true,
      message: t('auto.views.bpm.processListener.ProcessListenerForm.k1f88f683'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

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
      formData.value = await ProcessListenerApi.getProcessListener(id)
    } finally {
      formLoading.value = false
    }
  }
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
    const data = formData.value as unknown as ProcessListenerVO
    if (formType.value === 'create') {
      await ProcessListenerApi.createProcessListener(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProcessListenerApi.updateProcessListener(data)
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
  formData.value = {
    id: undefined,
    name: undefined,
    type: undefined,
    status: CommonStatusEnum.ENABLE,
    event: undefined,
    valueType: undefined,
    value: undefined
  }
  formRef.value?.resetFields()
}
</script>
