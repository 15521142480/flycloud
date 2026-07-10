<!-- 跟进记录的添加表单弹窗 -->
<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.crm.followup.FollowUpRecordForm.kea8b3f54')"
    width="50%"
  >
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="120px"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.followup.FollowUpRecordForm.kd1ac0fc8')"
            prop="type"
          >
            <el-select
              v-model="formData.type"
              :placeholder="t('auto.views.crm.followup.FollowUpRecordForm.k813693dc')"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_FOLLOW_UP_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.followup.FollowUpRecordForm.k8e1beb13')"
            prop="nextTime"
          >
            <el-date-picker
              v-model="formData.nextTime"
              :placeholder="t('auto.views.crm.followup.FollowUpRecordForm.k0f0ca712')"
              type="date"
              value-format="x"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            :label="t('auto.views.crm.followup.FollowUpRecordForm.kab264d91')"
            prop="content"
          >
            <el-input v-model="formData.content" :rows="3" type="textarea" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.followup.FollowUpRecordForm.kbe8da62e')"
            prop="picUrls"
          >
            <UploadImgs v-model="formData.picUrls" directory="crm" class="min-w-80px" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.followup.FollowUpRecordForm.k99f6fe6c')"
            prop="fileUrls"
          >
            <UploadFile v-model="formData.fileUrls" directory="crm" class="min-w-80px" />
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.bizType == BizTypeEnum.CRM_CUSTOMER">
          <el-form-item
            :label="t('auto.views.crm.followup.FollowUpRecordForm.k15581973')"
            prop="contactIds"
          >
            <el-button @click="handleOpenContact">
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('extra.k0981929a') }}
            </el-button>
            <FollowUpRecordContactForm :contacts="formData.contacts" />
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.bizType == BizTypeEnum.CRM_CUSTOMER">
          <el-form-item
            :label="t('auto.views.crm.followup.FollowUpRecordForm.k0b77721e')"
            prop="businessIds"
          >
            <el-button @click="handleOpenBusiness">
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('extra.k20f9ccd7') }}
            </el-button>
            <FollowUpRecordBusinessForm :businesses="formData.businesses" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.crm.followup.FollowUpRecordForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.crm.followup.FollowUpRecordForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>

  <!-- 弹窗 -->
  <ContactListModal
    ref="contactTableSelectRef"
    :customer-id="formData.bizId"
    @success="handleAddContact"
  />
  <BusinessListModal
    ref="businessTableSelectRef"
    :customer-id="formData.bizId"
    @success="handleAddBusiness"
  />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { FollowUpRecordApi, FollowUpRecordVO } from '@/api/crm/followup'
import { BizTypeEnum } from '@/api/crm/permission'
import FollowUpRecordBusinessForm from './components/FollowUpRecordBusinessForm.vue'
import FollowUpRecordContactForm from './components/FollowUpRecordContactForm.vue'
import BusinessListModal from '@/views/crm/business/components/BusinessListModal.vue'
import * as BusinessApi from '@/api/crm/business'
import ContactListModal from '@/views/crm/contact/components/ContactListModal.vue'
import * as ContactApi from '@/api/crm/contact'
const { t } = useI18n()
defineOptions({ name: 'FollowUpRecordForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formData = ref({
  bizType: undefined,
  bizId: undefined,
  businesses: [],
  contacts: []
})
const formRules = reactive({
  type: [
    {
      required: true,
      message: t('auto.views.crm.followup.FollowUpRecordForm.kdb22f60b'),
      trigger: 'change'
    }
  ],
  content: [
    {
      required: true,
      message: t('auto.views.crm.followup.FollowUpRecordForm.kb7cfc877'),
      trigger: 'blur'
    }
  ],
  nextTime: [
    {
      required: true,
      message: t('auto.views.crm.followup.FollowUpRecordForm.kbc47324e'),
      trigger: 'blur'
    }
  ]
})

const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (bizType: number, bizId: string) => {
  dialogVisible.value = true
  resetForm()
  formData.value.bizType = bizType
  formData.value.bizId = bizId
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
    const data = {
      ...formData.value,
      contactIds: formData.value.contacts.map((item) => item.id),
      businessIds: formData.value.businesses.map((item) => item.id)
    } as unknown as FollowUpRecordVO
    await FollowUpRecordApi.createFollowUpRecord(data)
    message.success(t('common.createSuccess'))
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 关联联系人 */
const contactTableSelectRef = ref<InstanceType<typeof ContactListModal>>()
const handleOpenContact = () => {
  contactTableSelectRef.value?.open()
}
const handleAddContact = (contactId: [], newContacts: ContactApi.ContactVO[]) => {
  newContacts.forEach((contact) => {
    if (!formData.value.contacts.some((item) => item.id === contact.id)) {
      formData.value.contacts.push(contact)
    }
  })
}

/** 关联商机 */
const businessTableSelectRef = ref<InstanceType<typeof BusinessListModal>>()
const handleOpenBusiness = () => {
  businessTableSelectRef.value?.open()
}
const handleAddBusiness = (businessId: [], newBusinesses: BusinessApi.BusinessVO[]) => {
  newBusinesses.forEach((business) => {
    if (!formData.value.businesses.some((item) => item.id === business.id)) {
      formData.value.businesses.push(business)
    }
  })
}

/** 重置表单 */
const resetForm = () => {
  formRef.value?.resetFields()
  formData.value = {
    bizId: undefined,
    bizType: undefined,
    businesses: [],
    contacts: []
  }
}
</script>
