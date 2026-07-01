<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" max-height="500px" scroll>
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="160px"
    >
      <el-form-item
        :label="t('auto.views.system.oauth2.client.ClientForm.k090a075b')"
        prop="secret"
      >
        <el-input
          v-model="formData.clientId"
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.k146a2f93')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.oauth2.client.ClientForm.k65f8d895')"
        prop="secret"
      >
        <el-input
          v-model="formData.secret"
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.k3da81e00')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.oauth2.client.ClientForm.k6c19fe01')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.k445d8859')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.oauth2.client.ClientForm.kf93d423d')">
        <UploadImg v-model="formData.logo" directory="sys" :limit="1" />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.oauth2.client.ClientForm.k92dad4cc')">
        <el-input
          v-model="formData.description"
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.k445d8859')"
          type="textarea"
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
        :label="t('auto.views.system.oauth2.client.ClientForm.kaa4907c8')"
        prop="accessTokenValiditySeconds"
      >
        <el-input-number
          v-model="formData.accessTokenValiditySeconds"
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.ka8be7968')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.oauth2.client.ClientForm.k904a9d49')"
        prop="refreshTokenValiditySeconds"
      >
        <el-input-number
          v-model="formData.refreshTokenValiditySeconds"
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.ka8be7968')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.oauth2.client.ClientForm.k3006e441')"
        prop="authorizedGrantTypes"
      >
        <el-select
          v-model="formData.authorizedGrantTypes"
          filterable
          multiple
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.k7564cd28')"
          style="width: 500px"
        >
          <el-option
            v-for="dict in getDictOptions(DICT_TYPE.SYSTEM_OAUTH2_GRANT_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.oauth2.client.ClientForm.k00e778c5')"
        prop="scopes"
      >
        <el-select
          v-model="formData.scopes"
          filterable
          multiple
          allow-create
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.k2e110e3a')"
          style="width: 500px"
        >
          <el-option v-for="scope in formData.scopes" :key="scope" :label="scope" :value="scope" />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.oauth2.client.ClientForm.k8f13730a')"
        prop="autoApproveScopes"
      >
        <el-select
          v-model="formData.autoApproveScopes"
          filterable
          multiple
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.k2e110e3a')"
          style="width: 500px"
        >
          <el-option v-for="scope in formData.scopes" :key="scope" :label="scope" :value="scope" />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.oauth2.client.ClientForm.k3c3c5137')"
        prop="redirectUris"
      >
        <el-select
          v-model="formData.redirectUris"
          allow-create
          filterable
          multiple
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.k68c23ca3')"
          style="width: 500px"
        >
          <el-option
            v-for="redirectUri in formData.redirectUris"
            :key="redirectUri"
            :label="redirectUri"
            :value="redirectUri"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.oauth2.client.ClientForm.k560165a6')"
        prop="authorities"
      >
        <el-select
          v-model="formData.authorities"
          allow-create
          filterable
          multiple
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.k3b039a4c')"
          style="width: 500px"
        >
          <el-option
            v-for="authority in formData.authorities"
            :key="authority"
            :label="authority"
            :value="authority"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.oauth2.client.ClientForm.kc5ca3950')"
        prop="resourceIds"
      >
        <el-select
          v-model="formData.resourceIds"
          allow-create
          filterable
          multiple
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.k5405c1e9')"
          style="width: 500px"
        >
          <el-option
            v-for="resourceId in formData.resourceIds"
            :key="resourceId"
            :label="resourceId"
            :value="resourceId"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.oauth2.client.ClientForm.kb14734ef')"
        prop="additionalInformation"
      >
        <el-input
          v-model="formData.additionalInformation"
          :placeholder="t('auto.views.system.oauth2.client.ClientForm.k613d70a7')"
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.system.oauth2.client.ClientForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.system.oauth2.client.ClientForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getDictOptions, getIntDictOptions } from '@/utils/dict'
import { CommonStatusEnum } from '@/utils/constants'
import * as ClientApi from '@/api/system/oauth2/client'
const { t } = useI18n()
defineOptions({ name: 'SystemOAuth2ClientForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  clientId: undefined,
  secret: undefined,
  name: undefined,
  logo: undefined,
  description: undefined,
  status: CommonStatusEnum.ENABLE,
  accessTokenValiditySeconds: 30 * 60,
  refreshTokenValiditySeconds: 30 * 24 * 60,
  redirectUris: [],
  authorizedGrantTypes: [],
  scopes: [],
  autoApproveScopes: [],
  authorities: [],
  resourceIds: [],
  additionalInformation: undefined
})
const formRules = reactive({
  clientId: [
    {
      required: true,
      message: t('auto.views.system.oauth2.client.ClientForm.kd6a54298'),
      trigger: 'blur'
    }
  ],
  secret: [
    {
      required: true,
      message: t('auto.views.system.oauth2.client.ClientForm.k2de1c4e8'),
      trigger: 'blur'
    }
  ],
  name: [
    {
      required: true,
      message: t('auto.views.system.oauth2.client.ClientForm.kf6f9e61d'),
      trigger: 'blur'
    }
  ],
  logo: [
    {
      required: true,
      message: t('auto.views.system.oauth2.client.ClientForm.k0e5034e1'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.system.oauth2.client.ClientForm.k1318b551'),
      trigger: 'blur'
    }
  ],
  accessTokenValiditySeconds: [
    {
      required: true,
      message: t('auto.views.system.oauth2.client.ClientForm.k2e56aa3e'),
      trigger: 'blur'
    }
  ],
  refreshTokenValiditySeconds: [
    {
      required: true,
      message: t('auto.views.system.oauth2.client.ClientForm.k4b515ea9'),
      trigger: 'blur'
    }
  ],
  redirectUris: [
    {
      required: true,
      message: t('auto.views.system.oauth2.client.ClientForm.kc0939763'),
      trigger: 'blur'
    }
  ],
  authorizedGrantTypes: [
    {
      required: true,
      message: t('auto.views.system.oauth2.client.ClientForm.kda4052ca'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ClientApi.getOAuth2Client(id)
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
    const data = formData.value as unknown as ClientApi.OAuth2ClientVO
    if (formType.value === 'create') {
      await ClientApi.createOAuth2Client(data)
      message.success(t('common.createSuccess'))
    } else {
      await ClientApi.updateOAuth2Client(data)
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
    clientId: undefined,
    secret: undefined,
    name: undefined,
    logo: undefined,
    description: undefined,
    status: CommonStatusEnum.ENABLE,
    accessTokenValiditySeconds: 30 * 60,
    refreshTokenValiditySeconds: 30 * 24 * 60,
    redirectUris: [],
    authorizedGrantTypes: [],
    scopes: [],
    autoApproveScopes: [],
    authorities: [],
    resourceIds: [],
    additionalInformation: undefined
  }
  formRef.value?.resetFields()
}
</script>
