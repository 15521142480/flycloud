<template>
  <doc-alert
    :title="t('auto.views.infra.webSocket.index.ke9c3ceb7')"
    url="https://doc.iocoder.cn/websocket/"
  />

  <div class="flex">
    <!-- 左侧：建立连接、发送消息 -->
    <el-card :gutter="12" class="w-1/2" shadow="always">
      <template #header>
        <div class="card-header">
          <span>{{ t('auto.views.infra.webSocket.index.k7328deeb') }}</span>
        </div>
      </template>
      <div class="flex items-center">
        <span class="mr-4 text-lg font-medium"> {{ t('extra.k65026c6a') }} </span>
        <el-tag :color="getTagColor">{{ status }}</el-tag>
      </div>
      <hr class="my-4" />
      <div class="flex">
        <el-input v-model="server" disabled>
          <template #prepend>{{ t('extra.kf562f75c') }}</template>
        </el-input>
        <el-button :type="getIsOpen ? 'danger' : 'primary'" @click="toggleConnectStatus">
          {{ getIsOpen ? '关闭连接' : '开启连接' }}
        </el-button>
      </div>
      <p class="mt-4 text-lg font-medium">{{ t('extra.kebd99cb2') }}</p>
      <hr class="my-4" />
      <el-input
        v-model="sendText"
        :autosize="{ minRows: 2, maxRows: 4 }"
        :disabled="!getIsOpen"
        clearable
        :placeholder="t('extra.kc4651155')"
        type="textarea"
      />
      <el-select v-model="sendUserId" class="mt-4" :placeholder="t('extra.kef45e0ea')">
        <el-option key="" :label="t('extra.kdbe48d46')" value="" />
        <el-option v-for="user in userList" :key="user.id" :label="user.name" :value="user.id" />
      </el-select>
      <el-button :disabled="!getIsOpen" block class="ml-2 mt-4" type="primary" @click="handlerSend">
        {{ t('extra.k3eab8c0b') }}
      </el-button>
    </el-card>
    <!-- 右侧：消息记录 -->
    <el-card :gutter="12" class="w-1/2" shadow="always">
      <template #header>
        <div class="card-header">
          <span>{{ t('extra.k0468263d') }}</span>
        </div>
      </template>
      <div class="max-h-80 overflow-auto">
        <ul>
          <li v-for="msg in messageReverseList" :key="msg.time" class="mt-2">
            <div class="flex items-center">
              <span class="text-primary mr-2 font-medium">{{ t('extra.k286c4a5e') }}</span>
              <span>{{ formatDate(msg.time) }}</span>
            </div>
            <div>
              {{ msg.text }}
            </div>
          </li>
        </ul>
      </div>
    </el-card>
  </div>
</template>
<script lang="ts" setup>
import { formatDate } from '@/utils/formatTime'
import { useWebSocket } from '@vueuse/core'
import { getRefreshToken } from '@/utils/auth'
import { buildSystemWebSocketUrl } from '@/utils/websocket'
import * as UserApi from '@/api/system/user'
const { t } = useI18n()
defineOptions({ name: 'InfraWebSocket' })

const message = useMessage() // 消息弹窗

const server = ref(buildSystemWebSocketUrl(getRefreshToken())) // WebSocket 服务地址
const getIsOpen = computed(() => status.value === 'OPEN') // WebSocket 连接是否打开
const getTagColor = computed(() => (getIsOpen.value ? 'success' : 'red')) // WebSocket 连接的展示颜色

/** 发起 WebSocket 连接 */
const { status, data, send, close, open } = useWebSocket(server.value, {
  autoReconnect: true,
  heartbeat: true
})

/** 监听接收到的数据 */
const messageList = ref([] as { time: number; text: string }[]) // 消息列表
const messageReverseList = computed(() => messageList.value.slice().reverse())
watchEffect(() => {
  if (!data.value) {
    return
  }
  try {
    // 1. 收到心跳
    if (data.value === 'pong') {
      // state.recordList.push({
      //   text: '【心跳】',
      //   time: new Date().getTime()
      // })
      return
    }

    // 2.1 解析 type 消息类型
    const jsonMessage = JSON.parse(data.value)
    const type = jsonMessage.type
    const content = JSON.parse(jsonMessage.content)
    if (!type) {
      message.error(t('auto.views.infra.webSocket.index.k76335b33') + data.value)
      return
    }
    // 2.2 消息类型：demo-message-receive
    if (type === 'demo-message-receive') {
      const single = content.single
      if (single) {
        messageList.value.push({
          text: t('extra.k2fe9be2e', { p0: content.fromUserId, p1: content.text }),
          time: new Date().getTime()
        })
      } else {
        messageList.value.push({
          text: t('extra.k2455ff8a', { p0: content.fromUserId, p1: content.text }),
          time: new Date().getTime()
        })
      }
      return
    }
    // 2.3 消息类型：notice-push
    if (type === 'notice-push') {
      messageList.value.push({
        text: t('extra.k51af183c', { p0: content.title }),
        time: new Date().getTime()
      })
      return
    }
    message.error(t('auto.views.infra.webSocket.index.kc78a33da') + data.value)
  } catch (error) {
    message.error(t('auto.views.infra.webSocket.index.k20eb7224') + data.value)
    console.error(error)
  }
})

/** 发送消息 */
const sendText = ref('') // 发送内容
const sendUserId = ref('') // 发送人
const handlerSend = () => {
  // 1.1 先 JSON 化 message 消息内容
  const messageContent = JSON.stringify({
    text: sendText.value,
    toUserId: sendUserId.value
  })
  // 1.2 再 JSON 化整个消息
  const jsonMessage = JSON.stringify({
    type: 'demo-message-send',
    content: messageContent
  })
  // 2. 最后发送消息
  send(jsonMessage)
  sendText.value = ''
}

/** 切换 websocket 连接状态 */
const toggleConnectStatus = () => {
  if (getIsOpen.value) {
    close()
  } else {
    open()
  }
}

/** 初始化 **/
const userList = ref<any[]>([]) // 用户列表
onMounted(async () => {
  // 获取用户列表
  userList.value = await UserApi.getSimpleUserList()
})
</script>
