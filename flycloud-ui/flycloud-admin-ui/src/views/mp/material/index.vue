<template>
  <!-- 搜索工作栏 -->
  <ContentWrap>
    <el-form class="-mb-15px" :inline="true" label-width="68px">
      <el-form-item :label="t('auto.views.mp.material.index.ke48fc0ee')" prop="accountId">
        <WxAccountSelect @change="onAccountChanged" />
      </el-form-item>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-tabs v-model="type" @tab-change="onTabChange">
      <!-- tab 1：图片  -->
      <el-tab-pane :name="UploadType.Image">
        <template #label>
          <el-row align="middle">
            <Icon icon="ep:picture" />{{ t('auto.views.mp.material.index.kbe8da62e') }}
          </el-row>
        </template>
        <UploadFile
          v-hasPermi="['mp:material:upload-permanent']"
          directory="mp"
          :type="UploadType.Image"
          @uploaded="getList"
        >
          {{ t('auto.views.mp.components.wx_reply.components.TabImage.kd2ee2686') }}
        </UploadFile>
        <!-- 列表 -->
        <ImageTable :loading="loading" :list="list" @delete="handleDelete" />
        <!-- 分页组件 -->
        <Pagination
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </el-tab-pane>

      <!-- tab 2：语音  -->
      <el-tab-pane :name="UploadType.Voice">
        <template #label>
          <el-row align="middle">
            <Icon icon="ep:microphone" />{{
              t('auto.views.mp.components.wx_material_select.main.k7a73e125')
            }}
          </el-row>
        </template>
        <UploadFile
          v-hasPermi="['mp:material:upload-permanent']"
          directory="mp"
          :type="UploadType.Voice"
          @uploaded="getList"
        >
          {{ t('extra.kd161d68a') }}
        </UploadFile>
        <!-- 列表 -->
        <VoiceTable :list="list" :loading="loading" @delete="handleDelete" />
        <!-- 分页组件 -->
        <Pagination
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </el-tab-pane>

      <!-- tab 3：视频 -->
      <el-tab-pane :name="UploadType.Video">
        <template #label>
          <el-row align="middle">
            <Icon icon="ep:video-play" />
            {{ t('auto.components.DiyEditor.components.mobile.Carousel.property.kfa4e33b6') }}
          </el-row>
        </template>
        <el-button
          v-hasPermi="['mp:material:upload-permanent']"
          type="primary"
          plain
          @click="showCreateVideo = true"
          >{{ t('auto.views.mp.components.wx_reply.components.TabVideo.kcb725a4b') }}</el-button
        >
        <!-- 新建视频的弹窗 -->
        <UploadVideo v-model="showCreateVideo" />
        <!-- 列表 -->
        <VideoTable :list="list" :loading="loading" @delete="handleDelete" />
        <!-- 分页组件 -->
        <Pagination
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </el-tab-pane>
    </el-tabs>
  </ContentWrap>
</template>
<script lang="ts" setup name="MpMaterial">
// @ts-nocheck
import WxAccountSelect from '@/views/mp/components/wx-account-select'
import ImageTable from './components/ImageTable.vue'
import VoiceTable from './components/VoiceTable.vue'
import VideoTable from './components/VideoTable.vue'
import UploadFile from './components/UploadFile.vue'
import UploadVideo from './components/UploadVideo.vue'
import { UploadType } from './components/upload'
import * as MpMaterialApi from '@/api/mp/material'
const { t } = useI18n()
const message = useMessage() // 消息

const type = ref<UploadType>(UploadType.Image) // 素材类型
const loading = ref(false) // 遮罩层
const list = ref<any[]>([]) // 总条数
const total = ref(0) // 数据列表

const accountId = ref(-1)
provide('accountId', accountId)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  accountId: accountId,
  permanent: true
})
const showCreateVideo = ref(false) // 是否新建视频的弹窗

/** 侦听公众号变化 **/
const onAccountChanged = (id: string) => {
  accountId.value = id
  queryParams.accountId = id
  queryParams.pageNum = 1
  getList()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await MpMaterialApi.getMaterialPage({
      ...queryParams,
      type: type.value
    })
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

/** 处理 table 切换 */
const onTabChange = () => {
  // 提前情况数据，避免 tab 切换后显示垃圾数据
  list.value = []
  total.value = 0
  // 从第一页开始查询
  handleQuery()
}

/** 处理删除操作 */
const handleDelete = async (id: string) => {
  await message.confirm(t('auto.views.mp.material.index.k3fa63288'))
  await MpMaterialApi.deletePermanentMaterial(id)
  message.alertSuccess(t('auto.views.mp.material.index.k86e8d12a'))
}
</script>
