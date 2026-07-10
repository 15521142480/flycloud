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
          <el-form-item :label="t('auto.views.crm.contact.ContactForm.kd6a91bf5')" prop="name">
            <el-input
              v-model="formData.name"
              :placeholder="t('auto.views.crm.contact.ContactForm.k675b3ee2')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.contact.ContactForm.k974d383f')"
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
            :label="t('auto.views.crm.contact.ContactForm.ke941d410')"
            prop="customerId"
          >
            <el-select
              :disabled="formData.customerDefault"
              v-model="formData.customerId"
              :placeholder="t('auto.views.crm.contact.ContactForm.k6bdb05d6')"
              class="w-1/1"
            >
              <el-option
                v-for="item in customerList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.contact.ContactForm.k9c01ad09')" prop="mobile">
            <el-input
              v-model="formData.mobile"
              :placeholder="t('auto.views.crm.contact.ContactForm.k55a7cb25')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.contact.ContactForm.kf39c6b62')" prop="telephone">
            <el-input
              v-model="formData.telephone"
              :placeholder="t('auto.views.crm.contact.ContactForm.k902a7fb3')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.contact.ContactForm.k9ed627bc')" prop="email">
            <el-input
              v-model="formData.email"
              :placeholder="t('auto.views.crm.contact.ContactForm.kf2dc24de')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.contact.ContactForm.k68406df3')" prop="wechat">
            <el-input
              v-model="formData.wechat"
              :placeholder="t('auto.views.crm.contact.ContactForm.k6861083d')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="QQ" prop="qq">
            <el-input
              v-model="formData.qq"
              :placeholder="t('auto.views.crm.contact.ContactForm.k2b9c2c9f')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.contact.ContactForm.ka2a92e50')" prop="post">
            <el-input
              v-model="formData.post"
              :placeholder="t('auto.views.crm.contact.ContactForm.kd73dfab3')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.contact.ContactForm.k20f9b81d')"
            prop="master"
            style="width: 400px"
          >
            <el-radio-group v-model="formData.master">
              <el-radio
                v-for="dict in getBoolDictOptions(DICT_TYPE.INFRA_BOOLEAN_STRING)"
                :key="dict.value"
                :value="dict.value"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.contact.ContactForm.kfe8aa4ef')" prop="sex">
            <el-select
              v-model="formData.sex"
              :placeholder="t('auto.views.crm.contact.ContactForm.k382f4b55')"
              class="w-1/1"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_USER_SEX)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.contact.ContactForm.kcac16f50')" prop="parentId">
            <el-select
              v-model="formData.parentId"
              :placeholder="t('auto.views.crm.contact.ContactForm.kba25fd44')"
              class="w-1/1"
            >
              <el-option
                v-for="item in contactList"
                :key="item.id"
                :disabled="item.id == formData.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.contact.ContactForm.k67d2d797')" prop="areaId">
            <el-cascader
              v-model="formData.areaId"
              :options="areaList"
              :props="defaultProps"
              class="w-1/1"
              clearable
              filterable
              :placeholder="t('auto.views.crm.contact.ContactForm.k7d1054a7')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.contact.ContactForm.kab09453d')"
            prop="detailAddress"
          >
            <el-input
              v-model="formData.detailAddress"
              :placeholder="t('auto.views.crm.contact.ContactForm.k6c79990b')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.contact.ContactForm.k8e1beb13')"
            prop="contactNextTime"
          >
            <el-date-picker
              v-model="formData.contactNextTime"
              :placeholder="t('auto.views.crm.contact.ContactForm.k0f0ca712')"
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
              :placeholder="t('auto.views.crm.contact.ContactForm.k57e709d9')"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.crm.contact.ContactForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.crm.contact.ContactForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as ContactApi from '@/api/crm/contact'
import { DICT_TYPE, getBoolDictOptions, getIntDictOptions } from '@/utils/dict'
import * as UserApi from '@/api/system/user'
import * as CustomerApi from '@/api/crm/customer'
import * as AreaApi from '@/api/system/area'
import { defaultProps } from '@/utils/tree'
import { useUserStore } from '@/store/modules/user'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const areaList = ref([]) // 地区列表
const formData = ref({
  id: undefined,
  name: undefined,
  customerId: undefined,
  contactNextTime: undefined,
  ownerUserId: '0',
  mobile: undefined,
  telephone: undefined,
  qq: undefined,
  wechat: undefined,
  email: undefined,
  areaId: undefined,
  detailAddress: undefined,
  sex: undefined,
  master: false,
  post: undefined,
  parentId: undefined,
  remark: undefined,
  businessId: undefined,
  customerDefault: false
})
const formRules = reactive({
  name: [
    { required: true, message: t('auto.views.crm.contact.ContactForm.kb0f8137f'), trigger: 'blur' }
  ],
  customerId: [
    { required: true, message: t('auto.views.crm.contact.ContactForm.k920199e1'), trigger: 'blur' }
  ],
  ownerUserId: [
    { required: true, message: t('auto.views.crm.contact.ContactForm.kc40a3652'), trigger: 'blur' }
  ]
})
const formRef = ref() // 表单 Ref
const userOptions = ref<UserApi.UserVO[]>([]) // 用户列表
const customerList = ref<CustomerApi.CustomerVO[]>([]) // 客户列表
const contactList = ref<ContactApi.ContactVO[]>([]) // 联系人列表

/** 打开弹窗 */
const open = async (type: string, id?: string, customerId?: string, businessId?: string) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ContactApi.getContact(id)
    } finally {
      formLoading.value = false
    }
  } else {
    if (customerId) {
      formData.value.customerId = customerId
      formData.value.customerDefault = true // 默认客户的选择，不允许变
    }
    // 自动关联 businessId 商机编号
    if (businessId) {
      formData.value.businessId = businessId
    }
  }
  // 获得联系人列表
  contactList.value = await ContactApi.getSimpleContactList()
  // 获得客户列表
  customerList.value = await CustomerApi.getCustomerSimpleList()
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
    const data = formData.value as unknown as ContactApi.ContactVO
    if (formType.value === 'create') {
      await ContactApi.createContact(data)
      message.success(t('common.createSuccess'))
    } else {
      await ContactApi.updateContact(data)
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
    customerId: undefined,
    contactNextTime: undefined,
    ownerUserId: '0',
    mobile: undefined,
    telephone: undefined,
    qq: undefined,
    wechat: undefined,
    email: undefined,
    areaId: undefined,
    detailAddress: undefined,
    sex: undefined,
    master: false,
    post: undefined,
    parentId: undefined,
    remark: undefined,
    businessId: undefined,
    customerDefault: false
  }
  formRef.value?.resetFields()
}
</script>
