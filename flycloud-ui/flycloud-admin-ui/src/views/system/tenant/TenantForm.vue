<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="50%">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="80px"
    >
      <el-form-item :label="t('auto.views.system.tenant.TenantForm.kdca5b241')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.system.tenant.TenantForm.kc73394f6')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.tenant.TenantForm.k4c33ed75')" prop="packageId">
        <el-select
          v-model="formData.packageId"
          clearable
          :placeholder="t('auto.views.system.tenant.TenantForm.kb9bedc4d')"
        >
          <el-option
            v-for="item in packageList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.tenant.TenantForm.k2425bd4b')" prop="contactName">
        <el-input
          v-model="formData.contactName"
          :placeholder="t('auto.views.system.tenant.TenantForm.k9feabe27')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.tenant.TenantForm.kef5dfe34')"
        prop="contactMobile"
      >
        <el-input
          v-model="formData.contactMobile"
          :placeholder="t('auto.views.system.tenant.TenantForm.kfd2166a9')"
        />
      </el-form-item>
      <el-form-item
        v-if="formData.id === undefined"
        :label="t('auto.views.system.tenant.TenantForm.ka311ed74')"
        prop="username"
      >
        <el-input
          v-model="formData.username"
          :placeholder="t('auto.views.system.tenant.TenantForm.k15762992')"
        />
      </el-form-item>
      <el-form-item
        v-if="formData.id === undefined"
        :label="t('auto.views.system.tenant.TenantForm.k407b6795')"
        prop="password"
      >
        <el-input
          v-model="formData.password"
          :placeholder="t('auto.views.system.tenant.TenantForm.k323dd2f7')"
          show-password
          type="password"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.tenant.TenantForm.k6a558db6')" prop="accountCount">
        <el-input-number
          v-model="formData.accountCount"
          :min="0"
          controls-position="right"
          :placeholder="t('auto.views.system.tenant.TenantForm.kdfd692f8')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.tenant.TenantForm.k27aefecf')" prop="expireTime">
        <el-date-picker
          v-model="formData.expireTime"
          clearable
          :placeholder="t('auto.views.system.tenant.TenantForm.k7bbb2185')"
          type="date"
          value-format="x"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.tenant.TenantForm.k8ff160ed')" prop="website">
        <el-input
          v-model="formData.website"
          :placeholder="t('auto.views.system.tenant.TenantForm.k8274e357')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.tenant.TenantForm.k5f511c77')" prop="status">
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
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.system.tenant.TenantForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.system.tenant.TenantForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as TenantApi from '@/api/system/tenant'
import { CommonStatusEnum } from '@/utils/constants'
import * as TenantPackageApi from '@/api/system/tenantPackage'
const { t } = useI18n()
defineOptions({ name: 'SystemTenantForm' })
const message = useMessage() // 消息弹窗
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  packageId: undefined,
  contactName: undefined,
  contactMobile: undefined,
  accountCount: undefined,
  expireTime: undefined,
  website: undefined,
  status: CommonStatusEnum.ENABLE,
  // 新增专属
  username: undefined,
  password: undefined
})
const formRules = reactive({
  name: [
    { required: true, message: t('auto.views.system.tenant.TenantForm.k409d3eb2'), trigger: 'blur' }
  ],
  packageId: [
    { required: true, message: t('auto.views.system.tenant.TenantForm.k1db7a238'), trigger: 'blur' }
  ],
  contactName: [
    { required: true, message: t('auto.views.system.tenant.TenantForm.k7e528ae7'), trigger: 'blur' }
  ],
  status: [
    { required: true, message: t('auto.views.system.tenant.TenantForm.k755d7140'), trigger: 'blur' }
  ],
  accountCount: [
    { required: true, message: t('auto.views.system.tenant.TenantForm.k54d52ee5'), trigger: 'blur' }
  ],
  expireTime: [
    { required: true, message: t('auto.views.system.tenant.TenantForm.k18486cd8'), trigger: 'blur' }
  ],
  website: [
    { required: true, message: t('auto.views.system.tenant.TenantForm.kfcb69616'), trigger: 'blur' }
  ],
  username: [
    { required: true, message: t('auto.views.system.tenant.TenantForm.kc3a2f174'), trigger: 'blur' }
  ],
  password: [
    { required: true, message: t('auto.views.system.tenant.TenantForm.k89c7dbc9'), trigger: 'blur' }
  ]
})
const formRef = ref() // 表单 Ref
const packageList = ref([] as TenantPackageApi.TenantPackageVO[]) // 租户套餐

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
      formData.value = await TenantApi.getTenant(id)
    } finally {
      formLoading.value = false
    }
  }
  // 加载套餐列表
  packageList.value = await TenantPackageApi.getTenantPackageList()
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
    const data = formData.value as unknown as TenantApi.TenantVO
    if (formType.value === 'create') {
      await TenantApi.createTenant(data)
      message.success(t('common.createSuccess'))
    } else {
      await TenantApi.updateTenant(data)
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
    packageId: undefined,
    contactName: undefined,
    contactMobile: undefined,
    accountCount: undefined,
    expireTime: undefined,
    website: undefined,
    status: CommonStatusEnum.ENABLE,
    username: undefined,
    password: undefined
  }
  formRef.value?.resetFields()
}
</script>
