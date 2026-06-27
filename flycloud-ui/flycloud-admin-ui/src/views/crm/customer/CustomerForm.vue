<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.customer.CustomerForm.ke941d410')" prop="name">
            <el-input
              v-model="formData.name"
              :placeholder="t('auto.views.crm.customer.CustomerForm.kf6b3ea8b')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.customer.CustomerForm.kb805cdaa')" prop="source">
            <el-select
              v-model="formData.source"
              :placeholder="t('auto.views.crm.customer.CustomerForm.ke5ca47c0')"
              class="w-1/1"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_CUSTOMER_SOURCE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.customer.CustomerForm.k9c01ad09')" prop="mobile">
            <el-input
              v-model="formData.mobile"
              :placeholder="t('auto.views.crm.customer.CustomerForm.k55a7cb25')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.customer.CustomerForm.k974d383f')"
            prop="ownerUserId"
          >
            <el-select
              v-model="formData.ownerUserId"
              :disabled="formType !== 'create'"
              class="w-1/1"
            >
              <el-option
                v-for="item in userOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.customer.CustomerForm.kf39c6b62')"
            prop="telephone"
          >
            <el-input
              v-model="formData.telephone"
              :placeholder="t('auto.views.crm.customer.CustomerForm.k902a7fb3')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.customer.CustomerForm.k9ed627bc')" prop="email">
            <el-input
              v-model="formData.email"
              :placeholder="t('auto.views.crm.customer.CustomerForm.kf2dc24de')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.customer.CustomerForm.k68406df3')" prop="wechat">
            <el-input
              v-model="formData.wechat"
              :placeholder="t('auto.views.crm.customer.CustomerForm.k6861083d')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="QQ" prop="qq">
            <el-input
              v-model="formData.qq"
              :placeholder="t('auto.views.crm.customer.CustomerForm.k2b9c2c9f')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.customer.CustomerForm.k7b39ef2d')"
            prop="industryId"
          >
            <el-select
              v-model="formData.industryId"
              :placeholder="t('auto.views.crm.customer.CustomerForm.kc57c87f1')"
              class="w-1/1"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_CUSTOMER_INDUSTRY)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.customer.CustomerForm.kbb7208b8')" prop="level">
            <el-select
              v-model="formData.level"
              :placeholder="t('auto.views.crm.customer.CustomerForm.k5da4f85f')"
              class="w-1/1"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_CUSTOMER_LEVEL)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.customer.CustomerForm.k67d2d797')" prop="areaId">
            <el-cascader
              v-model="formData.areaId"
              :options="areaList"
              :props="defaultProps"
              class="w-1/1"
              clearable
              filterable
              :placeholder="t('auto.views.crm.customer.CustomerForm.k7d1054a7')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.customer.CustomerForm.kab09453d')"
            prop="detailAddress"
          >
            <el-input
              v-model="formData.detailAddress"
              :placeholder="t('auto.views.crm.customer.CustomerForm.k6c79990b')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.customer.CustomerForm.k8e1beb13')"
            prop="contactNextTime"
          >
            <el-date-picker
              v-model="formData.contactNextTime"
              :placeholder="t('auto.views.crm.customer.CustomerForm.k0f0ca712')"
              type="datetime"
              value-format="x"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('common.remark')" prop="remark">
            <el-input
              type="textarea"
              v-model="formData.remark"
              :placeholder="t('auto.views.crm.customer.CustomerForm.k57e709d9')"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.crm.customer.CustomerForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.crm.customer.CustomerForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as CustomerApi from '@/api/crm/customer'
import * as AreaApi from '@/api/system/area'
import { defaultProps } from '@/utils/tree'
import * as UserApi from '@/api/system/user'
import { useUserStore } from '@/store/modules/user'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const areaList = ref([]) // 地区列表
const userOptions = ref<UserApi.UserVO[]>([]) // 用户列表
const formData = ref({
  id: undefined,
  name: undefined,
  contactNextTime: undefined,
  ownerUserId: 0,
  mobile: undefined,
  telephone: undefined,
  qq: undefined,
  wechat: undefined,
  email: undefined,
  areaId: undefined,
  detailAddress: undefined,
  industryId: undefined,
  level: undefined,
  source: undefined,
  remark: undefined
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.crm.customer.CustomerForm.k009abeb7'),
      trigger: 'blur'
    }
  ],
  ownerUserId: [
    {
      required: true,
      message: t('auto.views.crm.customer.CustomerForm.kc40a3652'),
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
      formData.value = await CustomerApi.getCustomer(id)
    } finally {
      formLoading.value = false
    }
  }
  // 获得地区列表
  areaList.value = await AreaApi.getAreaTree()
  // 获得用户列表
  userOptions.value = await UserApi.getSimpleUserList()
  // 默认新建时选中自己
  if (formType.value === 'create') {
    formData.value.ownerUserId = useUserStore().getUser.id
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
    const data = formData.value as unknown as CustomerApi.CustomerVO
    if (formType.value === 'create') {
      await CustomerApi.createCustomer(data)
      message.success(t('common.createSuccess'))
    } else {
      await CustomerApi.updateCustomer(data)
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
    contactNextTime: undefined,
    ownerUserId: 0,
    mobile: undefined,
    telephone: undefined,
    qq: undefined,
    wechat: undefined,
    email: undefined,
    areaId: undefined,
    detailAddress: undefined,
    industryId: undefined,
    level: undefined,
    source: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>
