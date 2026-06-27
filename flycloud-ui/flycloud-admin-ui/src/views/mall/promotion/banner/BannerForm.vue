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
        <el-col :span="24">
          <el-form-item
            :label="t('auto.views.mall.promotion.banner.BannerForm.k748d7dc7')"
            prop="title"
          >
            <el-input
              v-model="formData.title"
              :placeholder="t('auto.views.mall.promotion.banner.BannerForm.k57b8a602')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            :label="t('auto.views.mall.promotion.banner.BannerForm.kbe8da62e')"
            prop="picUrl"
          >
            <UploadImg v-model="formData.picUrl" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            :label="t('auto.views.mall.promotion.banner.BannerForm.k25d61a1c')"
            prop="url"
          >
            <el-input
              v-model="formData.url"
              :placeholder="t('auto.views.mall.promotion.banner.BannerForm.k6bfca292')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="t('common.sort')" prop="sort">
            <el-input-number v-model="formData.sort" :min="0" clearable controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
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
        </el-col>
        <el-col :span="24">
          <el-form-item
            :label="t('auto.views.mall.promotion.banner.BannerForm.k88c34452')"
            prop="position"
          >
            <el-radio-group v-model="formData.position">
              <el-radio
                v-for="dict in getIntDictOptions(DICT_TYPE.PROMOTION_BANNER_POSITION)"
                :key="dict.value"
                :value="dict.value"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            :label="t('auto.views.mall.promotion.banner.BannerForm.k412f54dc')"
            prop="memo"
          >
            <el-input
              v-model="formData.memo"
              :placeholder="t('auto.views.mall.promotion.banner.BannerForm.kcb869887')"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.mall.promotion.banner.BannerForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.mall.promotion.banner.BannerForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as BannerApi from '@/api/mall/market/banner'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  title: undefined,
  picUrl: undefined,
  status: 0,
  position: 1,
  url: undefined,
  sort: 0,
  memo: undefined
})
const formRules = reactive({
  title: [
    {
      required: true,
      message: t('auto.views.mall.promotion.banner.BannerForm.kc1284045'),
      trigger: 'blur'
    }
  ],
  picUrl: [
    {
      required: true,
      message: t('auto.views.mall.promotion.banner.BannerForm.kf40d58a1'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.mall.promotion.banner.BannerForm.kdbb0aa70'),
      trigger: 'blur'
    }
  ],
  position: [
    {
      required: true,
      message: t('auto.views.mall.promotion.banner.BannerForm.k8ff85847'),
      trigger: 'blur'
    }
  ],
  sort: [
    {
      required: true,
      message: t('auto.views.mall.promotion.banner.BannerForm.k3218602a'),
      trigger: 'blur'
    }
  ],
  url: [
    {
      required: true,
      message: t('auto.views.mall.promotion.banner.BannerForm.k12ada323'),
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
      formData.value = await BannerApi.getBanner(id)
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
    const data = formData.value as unknown as BannerApi.BannerVO
    if (formType.value === 'create') {
      await BannerApi.createBanner(data)
      message.success(t('common.createSuccess'))
    } else {
      await BannerApi.updateBanner(data)
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
    title: undefined,
    picUrl: undefined,
    status: 0,
    position: 1,
    url: undefined,
    sort: 0,
    memo: undefined
  }
  formRef.value?.resetFields()
}
</script>
