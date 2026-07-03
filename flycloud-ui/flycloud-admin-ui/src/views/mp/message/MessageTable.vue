<template>
  <div>
    <el-table v-loading="props.loading" :data="props.list">
      <el-table-column
        :label="t('auto.views.mp.message.MessageTable.k98c64dd6')"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column
        :label="t('auto.views.mp.message.MessageTable.k15218877')"
        align="center"
        prop="type"
        width="80"
      />
      <el-table-column
        :label="t('auto.views.mp.message.MessageTable.kdd2e2bfc')"
        align="center"
        prop="sendFrom"
        width="80"
      >
        <template #default="scope">
          <el-tag v-if="scope.row.sendFrom === 1" type="success">{{
            t('auto.views.mp.message.MessageTable.kf3ff5a7d')
          }}</el-tag>
          <el-tag v-else type="info">{{
            t('auto.views.mp.message.MessageTable.ke48fc0ee')
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.mp.message.index.k24afcd30')"
        align="center"
        prop="openid"
        width="300"
      />
      <el-table-column
        :label="t('auto.components.DiyEditor.components.ComponentContainerProperty.k163aec91')"
        prop="content"
      >
        <template #default="scope">
          <!-- 【事件】区域 -->
          <div v-if="scope.row.type === MsgType.Event && scope.row.event === 'subscribe'">
            <el-tag type="success">{{ t('workplace.follow') }}</el-tag>
          </div>
          <div v-else-if="scope.row.type === MsgType.Event && scope.row.event === 'unsubscribe'">
            <el-tag type="danger">{{
              t('auto.views.mp.components.wx_msg.components.MsgEvent.kbff57650')
            }}</el-tag>
          </div>
          <div v-else-if="scope.row.type === MsgType.Event && scope.row.event === 'CLICK'">
            <el-tag>{{
              t('auto.views.mp.components.wx_msg.components.MsgEvent.kaba076ee')
            }}</el-tag>
            【{{ scope.row.eventKey }}】
          </div>
          <div v-else-if="scope.row.type === MsgType.Event && scope.row.event === 'VIEW'">
            <el-tag>{{
              t('auto.views.mp.components.wx_msg.components.MsgEvent.k449230ce')
            }}</el-tag>
            【{{ scope.row.eventKey }}】
          </div>
          <div
            v-else-if="scope.row.type === MsgType.Event && scope.row.event === 'scancode_waitmsg'"
          >
            <el-tag>{{
              t('auto.views.mp.components.wx_msg.components.MsgEvent.k29a8c73c')
            }}</el-tag>
            【{{ scope.row.eventKey }}】
          </div>
          <div v-else-if="scope.row.type === MsgType.Event && scope.row.event === 'scancode_push'">
            <el-tag>{{
              t('auto.views.mp.components.wx_msg.components.MsgEvent.k29a8c73c')
            }}</el-tag>
            【{{ scope.row.eventKey }}】
          </div>
          <div v-else-if="scope.row.type === MsgType.Event && scope.row.event === 'pic_sysphoto'">
            <el-tag>{{
              t('auto.views.mp.components.wx_msg.components.MsgEvent.kc7872609')
            }}</el-tag>
          </div>
          <div
            v-else-if="scope.row.type === MsgType.Event && scope.row.event === 'pic_photo_or_album'"
          >
            <el-tag>{{
              t('auto.views.mp.components.wx_msg.components.MsgEvent.kc0edb9c1')
            }}</el-tag>
          </div>
          <div v-else-if="scope.row.type === MsgType.Event && scope.row.event === 'pic_weixin'">
            <el-tag>{{
              t('auto.views.mp.components.wx_msg.components.MsgEvent.kaffa37d3')
            }}</el-tag>
          </div>
          <div
            v-else-if="scope.row.type === MsgType.Event && scope.row.event === 'location_select'"
          >
            <el-tag>{{
              t('auto.views.mp.components.wx_msg.components.MsgEvent.ke3a6570e')
            }}</el-tag>
          </div>
          <div v-else-if="scope.row.type === MsgType.Event">
            <el-tag type="danger">{{
              t('auto.views.mp.components.wx_msg.components.MsgEvent.ke9246f17')
            }}</el-tag>
          </div>
          <!-- 【消息】区域 -->
          <div v-else-if="scope.row.type === MsgType.Text">{{ scope.row.content }}</div>
          <div v-else-if="scope.row.type === MsgType.Voice">
            <wx-voice-player :url="scope.row.mediaUrl" :content="scope.row.recognition" />
          </div>
          <div v-else-if="scope.row.type === MsgType.Image">
            <a target="_blank" :href="getFilePreviewUrl(scope.row.mediaUrl)">
              <img :src="getFilePreviewUrl(scope.row.mediaUrl)" style="width: 100px" />
            </a>
          </div>
          <div v-else-if="scope.row.type === MsgType.Video || scope.row.type === 'shortvideo'">
            <wx-video-player :url="scope.row.mediaUrl" style="margin-top: 10px" />
          </div>
          <div v-else-if="scope.row.type === MsgType.Link">
            <el-tag>{{
              t('auto.components.DiyEditor.components.mobile.MagicCube.property.k71502205')
            }}</el-tag>
            ：
            <a :href="scope.row.url" target="_blank">{{ scope.row.title }}</a>
          </div>
          <div v-else-if="scope.row.type === MsgType.Location">
            <WxLocation
              :label="scope.row.label"
              :location-y="scope.row.locationY"
              :location-x="scope.row.locationX"
            />
          </div>
          <div v-else-if="scope.row.type === MsgType.Music">
            <WxMusic
              :title="scope.row.title"
              :description="scope.row.description"
              :thumb-media-url="scope.row.thumbMediaUrl"
              :music-url="scope.row.musicUrl"
              :hq-music-url="scope.row.hqMusicUrl"
            />
          </div>
          <div v-else-if="scope.row.type === MsgType.News">
            <WxNews :articles="scope.row.articles" />
          </div>
          <div v-else>
            <el-tag type="danger">{{ t('extra.k6ae181e6') }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('common.operation')"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="emit('send', scope.row.userId)"
            v-hasPermi="['mp:message:send']"
          >
            {{
              t('auto.components.bpmnProcessDesigner.package.designer.plugins.translate.kdc6de3fa')
            }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
  </div>
</template>

<script lang="ts" setup>
import WxVideoPlayer from '@/views/mp/components/wx-video-play'
import WxVoicePlayer from '@/views/mp/components/wx-voice-play'
import WxLocation from '@/views/mp/components/wx-location'
import WxMusic from '@/views/mp/components/wx-music'
import WxNews from '@/views/mp/components/wx-news'
import { getFilePreviewUrl } from '@/components/UploadFile/src/useUpload'
import { dateFormatter } from '@/utils/formatTime'
import { MsgType } from '@/views/mp/components/wx-msg/types'
const { t } = useI18n()
const props = defineProps({
  list: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits<{ (e: 'send', v: number) }>()
</script>
