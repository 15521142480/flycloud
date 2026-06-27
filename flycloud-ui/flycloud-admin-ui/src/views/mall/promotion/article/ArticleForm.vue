<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="70%">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="110px"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.mall.promotion.article.ArticleForm.kce8733b4')"
            prop="title"
          >
            <el-input
              v-model="formData.title"
              :placeholder="t('auto.views.mall.promotion.article.ArticleForm.k06ba23b7')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.mall.promotion.article.ArticleForm.k8b397ec2')"
            prop="categoryId"
          >
            <el-select
              v-model="formData.categoryId"
              :placeholder="t('auto.views.mall.promotion.article.ArticleForm.k382f4b55')"
            >
              <el-option
                v-for="item in categoryList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.mall.promotion.article.ArticleForm.k4ef2e587')"
            prop="author"
          >
            <el-input
              v-model="formData.author"
              :placeholder="t('auto.views.mall.promotion.article.ArticleForm.ke47f455f')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.mall.promotion.article.ArticleForm.k5c86fcd2')"
            prop="introduction"
          >
            <el-input
              v-model="formData.introduction"
              :placeholder="t('auto.views.mall.promotion.article.ArticleForm.kebc8a8de')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            :label="t('auto.views.mall.promotion.article.ArticleForm.k8ee5b763')"
            prop="picUrl"
          >
            <UploadImg v-model="formData.picUrl" height="80px" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('common.sort')" prop="sort">
            <el-input-number v-model="formData.sort" :min="0" clearable controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
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
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.mall.promotion.article.ArticleForm.kc988a0bd')"
            prop="recommendHot"
          >
            <el-radio-group v-model="formData.recommendHot">
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
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.mall.promotion.article.ArticleForm.k2b0ad34b')"
            prop="recommendBanner"
          >
            <el-radio-group v-model="formData.recommendBanner">
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
        <el-col :span="24">
          <el-form-item
            :label="t('auto.views.mall.promotion.article.ArticleForm.kbe517811')"
            prop="spuId"
          >
            <el-tag v-if="formData.spuId" class="mr-10px">
              {{ spuList.find((item) => item.id === formData.spuId)?.name }}
            </el-tag>
            <el-button @click="spuSelectRef?.open()">{{
              t('auto.views.mall.promotion.article.ArticleForm.kf4d8d03c')
            }}</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="t('auto.views.mall.promotion.article.ArticleForm.k5424b241')">
            <Editor v-model="formData.content" height="150px" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.mall.promotion.article.ArticleForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.mall.promotion.article.ArticleForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
  <SpuSelect ref="spuSelectRef" @confirm="selectSpu" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getBoolDictOptions, getIntDictOptions } from '@/utils/dict'
import * as ArticleApi from '@/api/mall/promotion/article'
import * as ArticleCategoryApi from '@/api/mall/promotion/articleCategory'
import * as ProductSpuApi from '@/api/mall/product/spu'
import { SpuSelect } from '@/views/mall/promotion/components'
const { t } = useI18n()
defineOptions({ name: 'PromotionArticleForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  categoryId: undefined,
  title: undefined,
  author: undefined,
  picUrl: undefined,
  introduction: undefined,
  sort: 0,
  status: 0,
  spuId: 0,
  recommendHot: false,
  recommendBanner: false,
  content: undefined
})
const formRules = reactive({
  categoryId: [
    {
      required: true,
      message: t('auto.views.mall.promotion.article.ArticleForm.k1613b3ed'),
      trigger: 'blur'
    }
  ],
  title: [
    {
      required: true,
      message: t('auto.views.mall.promotion.article.ArticleForm.k9b65199a'),
      trigger: 'blur'
    }
  ],
  picUrl: [
    {
      required: true,
      message: t('auto.views.mall.promotion.article.ArticleForm.k034d1438'),
      trigger: 'blur'
    }
  ],
  sort: [
    {
      required: true,
      message: t('auto.views.mall.promotion.article.ArticleForm.k3218602a'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.mall.promotion.article.ArticleForm.k1318b551'),
      trigger: 'blur'
    }
  ],
  spuId: [
    {
      required: true,
      message: t('auto.views.mall.promotion.article.ArticleForm.k862e6b94'),
      trigger: 'blur'
    }
  ],
  recommendHot: [
    {
      required: true,
      message: t('auto.views.mall.promotion.article.ArticleForm.kca06bef6'),
      trigger: 'blur'
    }
  ],
  recommendBanner: [
    {
      required: true,
      message: t('auto.views.mall.promotion.article.ArticleForm.k2ed24959'),
      trigger: 'blur'
    }
  ],
  content: [
    {
      required: true,
      message: t('auto.views.mall.promotion.article.ArticleForm.kb05f3a56'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref
const spuSelectRef = ref() // 商品和属性选择 Ref
const selectSpu = (spuId: number) => {
  formData.value.spuId = spuId
}
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
      formData.value = await ArticleApi.getArticle(id)
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
    const data = formData.value as unknown as ArticleApi.ArticleVO
    if (formType.value === 'create') {
      await ArticleApi.createArticle(data)
      message.success(t('common.createSuccess'))
    } else {
      await ArticleApi.updateArticle(data)
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
    categoryId: undefined,
    title: undefined,
    author: undefined,
    picUrl: undefined,
    introduction: undefined,
    sort: 0,
    status: 0,
    spuId: 0,
    recommendHot: false,
    recommendBanner: false,
    content: undefined
  }
  formRef.value?.resetFields()
}

const categoryList = ref<ArticleCategoryApi.ArticleCategoryVO[]>([])
const spuList = ref<ProductSpuApi.Spu[]>([])
onMounted(async () => {
  categoryList.value =
    (await ArticleCategoryApi.getSimpleArticleCategoryList()) as ArticleCategoryApi.ArticleCategoryVO[]
  spuList.value = (await ProductSpuApi.getSpuSimpleList()) as ProductSpuApi.Spu[]
})
</script>
