<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="formRules"
    v-loading="formLoading"
    label-width="0px"
    :inline-message="true"
  >
    <el-table :data="formData" class="-mt-10px">
      <el-table-column :label="t('common.index')" type="index" width="100" />
      <el-table-column
        :label="t('auto.views.infra.demo.demo03.inner.components.k364bd1bf')"
        min-width="150"
      >
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.name`" :rules="formRules.name" class="mb-0px!">
            <el-input
              v-model="row.name"
              :placeholder="t('auto.views.infra.demo.demo03.inner.components.k010c1585')"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="分数" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.score`" :rules="formRules.score" class="mb-0px!">
            <el-input v-model="row.score" placeholder="请输入分数" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="60">
        <template #default="{ $index }">
          <el-button @click="handleDelete($index)" link>—</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-form>
  <el-row justify="center" class="mt-3">
    <el-button @click="handleAdd" round>+ 添加学生课程</el-button>
  </el-row>
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
  score: [
    {
      required: true,
      message: t('auto.views.infra.demo.demo03.inner.components.ke012aef4'),
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
    formData.value = []
    // 2. val 非空，则加载数据
    if (!val) {
      return
    }
    try {
      formLoading.value = true
      formData.value = await Demo03StudentApi.getDemo03CourseListByStudentId(val)
    } finally {
      formLoading.value = false
    }
  },
  { immediate: true }
)

/** 新增按钮操作 */
const handleAdd = () => {
  const row = {
    id: undefined,
    studentId: undefined,
    name: undefined,
    score: undefined
  }
  row.studentId = props.studentId
  formData.value.push(row)
}

/** 删除按钮操作 */
const handleDelete = (index) => {
  formData.value.splice(index, 1)
}

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
