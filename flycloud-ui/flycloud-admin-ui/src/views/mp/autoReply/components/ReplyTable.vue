<template>
  <el-table v-loading="props.loading" :data="props.list">
    <el-table-column
      :label="t('auto.views.mp.autoReply.components.ReplyTable.k1d415d8a')"
      align="center"
      prop="requestMessageType"
      v-if="msgType === MsgType.Message"
    />
    <el-table-column
      :label="t('auto.views.mp.autoReply.components.ReplyTable.kcc1b21e8')"
      align="center"
      prop="requestKeyword"
      v-if="msgType === MsgType.Keyword"
    />
    <el-table-column
      :label="t('auto.views.mp.autoReply.components.ReplyTable.k575d0d32')"
      align="center"
      prop="requestMatch"
      v-if="msgType === MsgType.Keyword"
    >
      <template #default="scope">
        <dict-tag :type="DICT_TYPE.MP_AUTO_REPLY_REQUEST_MATCH" :value="scope.row.requestMatch" />
      </template>
    </el-table-column>
    <el-table-column :label="t('extra.k1fdeeda3')" align="center">
      <template #default="scope">
        <dict-tag :type="DICT_TYPE.MP_MESSAGE_TYPE" :value="scope.row.responseMessageType" />
      </template>
    </el-table-column>
    <el-table-column
      :label="t('auto.views.mall.product.comment.ReplyForm.ka36c787d')"
      align="center"
    >
      <template #default="scope">
        <div v-if="scope.row.responseMessageType === 'text'">{{ scope.row.responseContent }}</div>
        <div v-else-if="scope.row.responseMessageType === 'voice'">
          <WxVoicePlayer v-if="scope.row.responseMediaUrl" :url="scope.row.responseMediaUrl" />
        </div>
        <div v-else-if="scope.row.responseMessageType === 'image'">
          <a target="_blank" :href="scope.row.responseMediaUrl">
            <img :src="scope.row.responseMediaUrl" style="width: 100px" />
          </a>
        </div>
        <div
          v-else-if="
            scope.row.responseMessageType === 'video' ||
            scope.row.responseMessageType === 'shortvideo'
          "
        >
          <WxVideoPlayer
            v-if="scope.row.responseMediaUrl"
            :url="scope.row.responseMediaUrl"
            style="margin-top: 10px"
          />
        </div>
        <div v-else-if="scope.row.responseMessageType === 'news'">
          <WxNews :articles="scope.row.responseArticles" />
        </div>
        <div v-else-if="scope.row.responseMessageType === 'music'">
          <WxMusic
            :title="scope.row.responseTitle"
            :description="scope.row.responseDescription"
            :thumb-media-url="scope.row.responseThumbMediaUrl"
            :music-url="scope.row.responseMusicUrl"
            :hq-music-url="scope.row.responseHqMusicUrl"
          />
        </div>
      </template>
    </el-table-column>
    <el-table-column
      :label="t('common.createTime')"
      align="center"
      prop="createTime"
      :formatter="dateFormatter"
      width="180"
    />
    <el-table-column :label="t('common.operation')" align="center">
      <template #default="scope">
        <el-button
          type="primary"
          link
          @click="emit('on-update', scope.row.id)"
          v-hasPermi="['mp:auto-reply:saveOrUpdate']"
        >
          {{ t('extra.k4c512392') }}
        </el-button>
        <el-button
          type="danger"
          link
          @click="emit('on-delete', scope.row.id)"
          v-hasPermi="['mp:auto-reply:delete']"
        >
          {{ t('common.delete') }}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
<script lang="ts" setup>
import WxVideoPlayer from '@/views/mp/components/wx-video-play'
import WxVoicePlayer from '@/views/mp/components/wx-voice-play'
import WxMusic from '@/views/mp/components/wx-music'
import WxNews from '@/views/mp/components/wx-news'
import { dateFormatter } from '@/utils/formatTime'
import { DICT_TYPE } from '@/utils/dict'
import { MsgType } from './types'
const { t } = useI18n()
const props = defineProps<{
  loading: boolean
  list: any[]
  msgType: MsgType
}>()

const emit = defineEmits<{
  (e: 'on-update', v: number)
  (e: 'on-delete', v: number)
}>()
</script>
