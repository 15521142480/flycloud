<template>
  <el-input
    type="textarea"
    :rows="5"
    :placeholder="t('auto.views.mp.components.wx_reply.components.TabText.kac962cb9')"
    v-model="content"
  />
</template>

<script lang="ts" setup>
const { t } = useI18n()
const props = defineProps<{
  modelValue?: string | null
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', v: string | null)
  (e: 'input', v: string | null)
}>()

const content = computed<string | null | undefined>({
  get: () => props.modelValue,
  set: (val: string | null) => {
    emit('update:modelValue', val)
    emit('input', val)
  }
})
</script>
