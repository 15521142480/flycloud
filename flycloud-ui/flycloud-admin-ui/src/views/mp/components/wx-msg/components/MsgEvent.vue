<template>
  <div>
    <div v-if="item.event === 'subscribe'">
      <el-tag type="success">{{
        t('auto.views.mp.components.wx_msg.components.MsgEvent.k7ac0d5c9')
      }}</el-tag>
    </div>
    <div v-else-if="item.event === 'unsubscribe'">
      <el-tag type="danger">{{
        t('auto.views.mp.components.wx_msg.components.MsgEvent.kbff57650')
      }}</el-tag>
    </div>
    <div v-else-if="item.event === 'CLICK'">
      <el-tag>{{ t('auto.views.mp.components.wx_msg.components.MsgEvent.kaba076ee') }}</el-tag>
      【{{ item.eventKey }}】
    </div>
    <div v-else-if="item.event === 'VIEW'">
      <el-tag>{{ t('auto.views.mp.components.wx_msg.components.MsgEvent.k449230ce') }}</el-tag>
      【{{ item.eventKey }}】
    </div>
    <div v-else-if="item.event === 'scancode_waitmsg'">
      <el-tag>{{ t('auto.views.mp.components.wx_msg.components.MsgEvent.k29a8c73c') }}</el-tag>
      【{{ item.eventKey }}】
    </div>
    <div v-else-if="item.event === 'scancode_push'">
      <el-tag>{{ t('auto.views.mp.components.wx_msg.components.MsgEvent.k29a8c73c') }}</el-tag>
      【{{ item.eventKey }}】
    </div>
    <div v-else-if="item.event === 'pic_sysphoto'">
      <el-tag>{{ t('auto.views.mp.components.wx_msg.components.MsgEvent.kc7872609') }}</el-tag>
    </div>
    <div v-else-if="item.event === 'pic_photo_or_album'">
      <el-tag>{{ t('auto.views.mp.components.wx_msg.components.MsgEvent.kc0edb9c1') }}</el-tag>
    </div>
    <div v-else-if="item.event === 'pic_weixin'">
      <el-tag>{{ t('auto.views.mp.components.wx_msg.components.MsgEvent.kaffa37d3') }}</el-tag>
    </div>
    <div v-else-if="item.event === 'location_select'">
      <el-tag>{{ t('auto.views.mp.components.wx_msg.components.MsgEvent.ke3a6570e') }}</el-tag>
    </div>
    <div v-else>
      <el-tag type="danger">{{
        t('auto.views.mp.components.wx_msg.components.MsgEvent.ke9246f17')
      }}</el-tag>
    </div>
  </div>
</template>

<script lang="ts" setup>
const { t } = useI18n()
const props = defineProps<{
  item: any
}>()

const item = ref(props.item)
</script>
