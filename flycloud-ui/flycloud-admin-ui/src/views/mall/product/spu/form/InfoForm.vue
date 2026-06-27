<!-- 商品发布 - 基础设置 -->
<template>
  <el-form ref="formRef" :disabled="isDetail" :model="formData" :rules="rules" label-width="120px">
    <el-form-item :label="t('auto.views.mall.product.spu.form.InfoForm.k47b74133')" prop="name">
      <el-input
        v-model="formData.name"
        :autosize="{ minRows: 2, maxRows: 2 }"
        :clearable="true"
        :show-word-limit="true"
        class="w-80!"
        maxlength="64"
        :placeholder="t('auto.views.mall.product.spu.form.InfoForm.k5c6bf9b9')"
        type="textarea"
      />
    </el-form-item>
    <el-form-item
      :label="t('auto.views.mall.product.spu.form.InfoForm.k09482df6')"
      prop="categoryId"
    >
      <el-cascader
        v-model="formData.categoryId"
        :options="categoryList"
        :props="defaultProps"
        class="w-80"
        clearable
        filterable
        :placeholder="t('auto.views.mall.product.spu.form.InfoForm.ke71fcc11')"
      />
    </el-form-item>
    <el-form-item :label="t('auto.views.mall.product.spu.form.InfoForm.k69291cfc')" prop="brandId">
      <el-select
        v-model="formData.brandId"
        class="w-80"
        :placeholder="t('auto.views.mall.product.spu.form.InfoForm.ka5c2affd')"
      >
        <el-option
          v-for="item in brandList"
          :key="item.id"
          :label="item.name"
          :value="item.id as number"
        />
      </el-select>
    </el-form-item>
    <el-form-item :label="t('auto.views.mall.product.spu.form.InfoForm.k76ed45b2')" prop="keyword">
      <el-input
        v-model="formData.keyword"
        class="w-80!"
        :placeholder="t('auto.views.mall.product.spu.form.InfoForm.k3409b6c6')"
      />
    </el-form-item>
    <el-form-item
      :label="t('auto.views.mall.product.spu.form.InfoForm.ke50db553')"
      prop="introduction"
    >
      <el-input
        v-model="formData.introduction"
        :autosize="{ minRows: 2, maxRows: 2 }"
        :clearable="true"
        :show-word-limit="true"
        class="w-80!"
        maxlength="128"
        :placeholder="t('auto.views.mall.product.spu.form.InfoForm.k1ceb80b5')"
        type="textarea"
      />
    </el-form-item>
    <el-form-item :label="t('auto.views.mall.product.spu.form.InfoForm.k35e0b189')" prop="picUrl">
      <UploadImg v-model="formData.picUrl" :disabled="isDetail" height="80px" />
    </el-form-item>
    <el-form-item
      :label="t('auto.views.mall.product.spu.form.InfoForm.k472dbc1b')"
      prop="sliderPicUrls"
    >
      <UploadImgs v-model="formData.sliderPicUrls" :disabled="isDetail" />
    </el-form-item>
  </el-form>
</template>
<script lang="ts" setup>
import { PropType } from 'vue'
import { copyValueToTarget } from '@/utils'
import { propTypes } from '@/utils/propTypes'
import { defaultProps, handleTree } from '@/utils/tree'
import type { Spu } from '@/api/mall/product/spu'
import * as ProductCategoryApi from '@/api/mall/product/category'
import { CategoryVO } from '@/api/mall/product/category'
import * as ProductBrandApi from '@/api/mall/product/brand'
import { BrandVO } from '@/api/mall/product/brand'
const { t } = useI18n()
defineOptions({ name: 'ProductSpuInfoForm' })
const props = defineProps({
  propFormData: {
    type: Object as PropType<Spu>,
    default: () => {}
  },
  isDetail: propTypes.bool.def(false) // 是否作为详情组件
})

const message = useMessage() // 消息弹窗

const formRef = ref() // 表单 Ref
const formData = reactive<Spu>({
  name: '', // 商品名称
  categoryId: undefined, // 商品分类
  keyword: '', // 关键字
  picUrl: '', // 商品封面图
  sliderPicUrls: [], // 商品轮播图
  introduction: '', // 商品简介
  brandId: undefined // 商品品牌
})
const rules = reactive({
  name: [required],
  categoryId: [required],
  keyword: [required],
  introduction: [required],
  picUrl: [required],
  sliderPicUrls: [required],
  brandId: [required]
})

/** 将传进来的值赋值给 formData */
watch(
  () => props.propFormData,
  (data) => {
    if (!data) {
      return
    }
    copyValueToTarget(formData, data)
  },
  {
    immediate: true
  }
)

/** 表单校验 */
const emit = defineEmits(['update:activeName'])
const validate = async () => {
  if (!formRef) return
  try {
    await unref(formRef)?.validate()
    // 校验通过更新数据
    Object.assign(props.propFormData, formData)
  } catch (e) {
    message.error(t('auto.views.mall.product.spu.form.InfoForm.kea3e4b86'))
    emit('update:activeName', 'info')
    throw e // 目的截断之后的校验
  }
}
defineExpose({ validate })

/** 初始化 */
const brandList = ref<BrandVO[]>([]) // 商品品牌列表
const categoryList = ref<CategoryVO[]>([]) // 商品分类树
onMounted(async () => {
  // 获得分类树
  const data = await ProductCategoryApi.getCategoryList({})
  categoryList.value = handleTree(data, 'id')
  // 获取商品品牌列表
  brandList.value = await ProductBrandApi.getSimpleBrandList()
})
</script>
