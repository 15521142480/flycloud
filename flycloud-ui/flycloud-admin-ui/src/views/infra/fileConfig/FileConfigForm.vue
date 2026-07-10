<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="120px"
    >
      <el-form-item :label="t('auto.views.infra.fileConfig.FileConfigForm.kf0020751')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.k0b5e6a5c')"
        />
      </el-form-item>
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="formData.remark"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.k57e709d9')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.infra.fileConfig.FileConfigForm.k620f816b')"
        prop="storage"
      >
        <el-select
          v-model="formData.storage"
          :disabled="formData.id !== undefined"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.k92c2f199')"
        >
          <el-option
            v-for="dict in getDictOptions(DICT_TYPE.INFRA_FILE_STORAGE)"
            :key="dict.value"
            :label="dict.label"
            :value="parseInt(dict.value)"
          />
        </el-select>
      </el-form-item>
      <!-- DB -->
      <!-- Local / FTP / SFTP -->
      <el-form-item
        v-if="formData.storage >= 10 && formData.storage <= 12"
        :label="t('auto.views.infra.fileConfig.FileConfigForm.kc9530c18')"
        prop="config.basePath"
      >
        <el-input
          v-model="formData.config.basePath"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.kf6d38af8')"
        />
      </el-form-item>
      <el-form-item
        v-if="formData.storage >= 11 && formData.storage <= 12"
        :label="t('auto.views.infra.fileConfig.FileConfigForm.kc5db6b99')"
        prop="config.host"
      >
        <el-input
          v-model="formData.config.host"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.k35bade8f')"
        />
      </el-form-item>
      <el-form-item
        v-if="formData.storage >= 11 && formData.storage <= 12"
        :label="t('auto.views.infra.fileConfig.FileConfigForm.kf33e1f2d')"
        prop="config.port"
      >
        <el-input-number
          v-model="formData.config.port"
          :min="0"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.k58d01da6')"
        />
      </el-form-item>
      <el-form-item
        v-if="formData.storage >= 11 && formData.storage <= 12"
        :label="t('auto.views.infra.fileConfig.FileConfigForm.ka1aaf352')"
        prop="config.username"
      >
        <el-input
          v-model="formData.config.username"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.k713b7382')"
        />
      </el-form-item>
      <el-form-item
        v-if="formData.storage >= 11 && formData.storage <= 12"
        :label="t('auto.views.infra.fileConfig.FileConfigForm.kc839a8ff')"
        prop="config.password"
      >
        <el-input
          v-model="formData.config.password"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.k713b7382')"
        />
      </el-form-item>
      <el-form-item
        v-if="formData.storage === 11"
        :label="t('auto.views.infra.fileConfig.FileConfigForm.k10b501f8')"
        prop="config.mode"
      >
        <el-radio-group v-model="formData.config.mode">
          <el-radio key="Active" value="Active">{{
            t('auto.views.infra.fileConfig.FileConfigForm.kf13757d9')
          }}</el-radio>
          <el-radio key="Passive" value="Passive">{{
            t('auto.views.infra.fileConfig.FileConfigForm.kea085f81')
          }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- S3 -->
      <el-form-item
        v-if="formData.storage === 20"
        :label="t('auto.views.infra.fileConfig.FileConfigForm.kd33819ee')"
        prop="config.endpoint"
      >
        <el-input
          v-model="formData.config.endpoint"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.kf5c43531')"
        />
      </el-form-item>
      <el-form-item
        v-if="formData.storage === 20"
        :label="t('auto.views.infra.fileConfig.FileConfigForm.k81338213')"
        prop="config.bucket"
      >
        <el-input
          v-model="formData.config.bucket"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.k8610b33f')"
        />
      </el-form-item>
      <el-form-item v-if="formData.storage === 20" label="accessKey" prop="config.accessKey">
        <el-input
          v-model="formData.config.accessKey"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.kf7a0d8e5')"
        />
      </el-form-item>
      <el-form-item v-if="formData.storage === 20" label="accessSecret" prop="config.accessSecret">
        <el-input
          v-model="formData.config.accessSecret"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.k20418625')"
        />
      </el-form-item>
      <!-- 通用 -->
      <el-form-item
        v-if="formData.storage === 20"
        :label="t('auto.views.infra.fileConfig.FileConfigForm.k5e197395')"
      >
        <!-- 无需参数校验，所以去掉 prop -->
        <el-input
          v-model="formData.config.domain"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.k4f368565')"
        />
      </el-form-item>
      <el-form-item
        v-else-if="formData.storage"
        :label="t('auto.views.infra.fileConfig.FileConfigForm.k5e197395')"
        prop="config.domain"
      >
        <el-input
          v-model="formData.config.domain"
          :placeholder="t('auto.views.infra.fileConfig.FileConfigForm.k4f368565')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.infra.fileConfig.FileConfigForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.infra.fileConfig.FileConfigForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getDictOptions } from '@/utils/dict'
import * as FileConfigApi from '@/api/infra/fileConfig'
import { FormRules } from 'element-plus'
const { t } = useI18n()
defineOptions({ name: 'InfraFileConfigForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: '',
  storage: 0,
  remark: '',
  config: {} as FileConfigApi.FileClientConfig
})
const formRules = reactive<FormRules>({
  name: [
    {
      required: true,
      message: t('auto.views.infra.fileConfig.FileConfigForm.kc59f7479'),
      trigger: 'blur'
    }
  ],
  storage: [
    {
      required: true,
      message: t('auto.views.infra.fileConfig.FileConfigForm.ke9ad52c6'),
      trigger: 'change'
    }
  ],
  config: {
    basePath: [
      {
        required: true,
        message: t('auto.views.infra.fileConfig.FileConfigForm.k6b5f5111'),
        trigger: 'blur'
      }
    ],
    host: [
      {
        required: true,
        message: t('auto.views.infra.fileConfig.FileConfigForm.k6f02f051'),
        trigger: 'blur'
      }
    ],
    port: [
      {
        required: true,
        message: t('auto.views.infra.fileConfig.FileConfigForm.kb24907e8'),
        trigger: 'blur'
      }
    ],
    username: [
      {
        required: true,
        message: t('auto.views.infra.fileConfig.FileConfigForm.kecb38cb0'),
        trigger: 'blur'
      }
    ],
    password: [
      {
        required: true,
        message: t('auto.views.infra.fileConfig.FileConfigForm.k4d81424b'),
        trigger: 'blur'
      }
    ],
    mode: [
      {
        required: true,
        message: t('auto.views.infra.fileConfig.FileConfigForm.k6cb2717d'),
        trigger: 'change'
      }
    ],
    endpoint: [
      {
        required: true,
        message: t('auto.views.infra.fileConfig.FileConfigForm.k81b61373'),
        trigger: 'blur'
      }
    ],
    bucket: [
      {
        required: true,
        message: t('auto.views.infra.fileConfig.FileConfigForm.kc09095a7'),
        trigger: 'blur'
      }
    ],
    accessKey: [
      {
        required: true,
        message: t('auto.views.infra.fileConfig.FileConfigForm.k62055789'),
        trigger: 'blur'
      }
    ],
    accessSecret: [
      {
        required: true,
        message: t('auto.views.infra.fileConfig.FileConfigForm.kae666ce1'),
        trigger: 'blur'
      }
    ],
    domain: [
      {
        required: true,
        message: t('auto.views.infra.fileConfig.FileConfigForm.kfbb75a63'),
        trigger: 'blur'
      }
    ]
  } as FormRules
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
      formData.value = await FileConfigApi.getFileConfig(id)
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
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as FileConfigApi.FileConfigVO
    if (formType.value === 'create') {
      await FileConfigApi.createFileConfig(data)
      message.success(t('common.createSuccess'))
    } else {
      await FileConfigApi.updateFileConfig(data)
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
    name: '',
    storage: undefined!,
    remark: '',
    config: {} as FileConfigApi.FileClientConfig
  }
  formRef.value?.resetFields()
}
</script>
