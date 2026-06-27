<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="formRules"
    label-width="100px"
    v-loading="formLoading"
  >
    <el-form-item :label="t('auto.views.infra.demo.demo03.inner.components.k364bd1bf')" prop="name">
      <el-input
        v-model="formData.name"
        :placeholder="t('auto.views.infra.demo.demo03.inner.components.k010c1585')"
      />
    </el-form-item>
    <el-form-item
      :label="t('auto.views.infra.demo.demo03.inner.components.k10b965b9')"
      prop="teacher"
    >
      <el-input
        v-model="formData.teacher"
        :placeholder="t('auto.views.infra.demo.demo03.inner.components.k5198d708')"
      />
    </el-form-item>
  </el-form>
</template>
<script setup lang="ts">
import * as Demo03StudentApi from '@/api/infra/demo/demo03/inner'
const { t } = useI18n()
const props = defineProps<{
  studentId: undefined // 学生编号（主表的关联字段）
}>()
const formLoading = ref(false) // 表单的加载中
const formData = ref([])
const formRules = reactive({
  studentId: [
    {
      required: true,
      message: t('auto.views.infra.demo.demo03.inner.components.kd16396dc'),
      trigger: 'blur'
    }
  ],
  name: [
    {
      required: true,
      message: t('auto.views.infra.demo.demo03.inner.components.k46f3776c'),
      trigger: 'blur'
    }
  ],
  teacher: [
    {
      required: true,
      message: t('auto.views.infra.demo.demo03.inner.components.k143f0428'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

/** 监听主表的关联字段的变化，加载对应的子表数据 */
watch(
  () => props.studentId,
  async (val) => {
    // 1. 重置表单
    formData.value = {
      id: undefined,
      studentId: undefined,
      name: undefined,
      teacher: undefined
    }
    // 2. val 非空，则加载数据
    if (!val) {
      return
    }
    try {
      formLoading.value = true
      const data = await Demo03StudentApi.getDemo03GradeByStudentId(val)
      if (!data) {
        return
      }
      formData.value = data
    } finally {
      formLoading.value = false
    }
  },
  { immediate: true }
)

/** 表单校验 */
const validate = () => {
  return formRef.value.validate()
}

/** 表单值 */
const getData = () => {
  return formData.value
}

defineExpose({ validate, getData })
</script>
