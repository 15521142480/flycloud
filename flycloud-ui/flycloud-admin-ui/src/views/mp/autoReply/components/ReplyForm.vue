<template>
  <div>
    <el-form ref="formRef" :model="replyForm" :rules="rules" label-width="80px">
      <el-form-item
        :label="t('auto.views.mp.autoReply.components.ReplyForm.k15218877')"
        prop="requestMessageType"
        v-if="msgType === MsgType.Message"
      >
        <el-select
          v-model="replyForm.requestMessageType"
          :placeholder="t('auto.views.mp.autoReply.components.ReplyForm.k382f4b55')"
        >
          <template v-for="dict in getDictOptions(DICT_TYPE.MP_MESSAGE_TYPE)" :key="dict.value">
            <el-option
              v-if="RequestMessageTypes.includes(dict.value)"
              :label="dict.label"
              :value="dict.value"
            />
          </template>
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mp.autoReply.components.ReplyTable.k575d0d32')"
        prop="requestMatch"
        v-if="msgType === MsgType.Keyword"
      >
        <el-select v-model="replyForm.requestMatch" :placeholder="t('extra.kc8206777')" clearable>
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.MP_AUTO_REPLY_REQUEST_MATCH)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mp.autoReply.components.ReplyTable.kcc1b21e8')"
        prop="requestKeyword"
        v-if="msgType === MsgType.Keyword"
      >
        <el-input
          v-model="replyForm.requestKeyword"
          :placeholder="t('auto.views.infra.config.ConfigForm.kac962cb9')"
          clearable
        />
      </el-form-item>
      <el-form-item :label="t('extra.k6740d8c7')">
        <WxReplySelect v-model="reply" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import WxReplySelect, { type Reply } from '@/views/mp/components/wx-reply'
import type { FormInstance } from 'element-plus'
import { MsgType } from './types'
import { DICT_TYPE, getDictOptions, getIntDictOptions } from '@/utils/dict'
const { t } = useI18n()
defineOptions({ name: 'ReplyForm' })

const props = defineProps<{
  modelValue: any
  reply: Reply
  msgType: MsgType
}>()

const emit = defineEmits<{
  (e: 'update:reply', v: Reply)
  (e: 'update:modelValue', v: any)
}>()

const reply = computed<Reply>({
  get: () => props.reply,
  set: (val) => emit('update:reply', val)
})

const replyForm = computed<any>({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const formRef = ref<FormInstance | null>(null) // 表单 ref

const RequestMessageTypes = ['text', 'image', 'voice', 'video', 'shortvideo', 'location', 'link'] // 允许选择的请求消息类型

// 表单校验
const rules = {
  requestKeyword: [
    {
      required: true,
      message: t('auto.views.mp.autoReply.components.ReplyForm.k616666bb'),
      trigger: 'blur'
    }
  ],
  requestMatch: [
    {
      required: true,
      message: t('auto.views.mp.autoReply.components.ReplyForm.k522b387f'),
      trigger: 'blur'
    }
  ]
}

defineExpose({
  resetFields: () => formRef.value?.resetFields(),
  validate: async () => formRef.value?.validate()
})
</script>

<style scoped></style>
