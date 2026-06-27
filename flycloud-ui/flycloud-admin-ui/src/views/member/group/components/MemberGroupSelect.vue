<template>
  <el-select
    v-model="groupId"
    :placeholder="t('auto.views.member.group.components.MemberGroupSelect.k9f7df605')"
    clearable
    class="!w-240px"
  >
    <el-option
      v-for="group in groupOptions"
      :key="group.id"
      :label="group.name"
      :value="group.id"
    />
  </el-select>
</template>
<script lang="ts" setup>
import * as GroupApi from '@/api/member/group'

/** 会员分组选择框 **/
const { t } = useI18n()
defineOptions({ name: 'MemberGroupSelect' })

const props = defineProps({
  /** 下拉框选中值 **/
  modelValue: {
    type: Number,
    default: undefined
  }
})
const emit = defineEmits(['update:modelValue'])

const groupId = computed({
  get() {
    return props.modelValue
  },
  set(value: any) {
    emit('update:modelValue', value)
  }
})

const groupOptions = ref<GroupApi.GroupVO[]>([])

const getList = async () => {
  groupOptions.value = await GroupApi.getSimpleGroupList()
}

/** 初始化 */
onMounted(() => {
  getList()
})
</script>
