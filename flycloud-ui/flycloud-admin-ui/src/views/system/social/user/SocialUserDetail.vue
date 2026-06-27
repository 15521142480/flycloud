<template>
  <Dialog v-model="dialogVisible" :title="t('action.detail')" width="800">
    <el-descriptions :column="1" border>
      <el-descriptions-item
        :label="t('auto.views.system.social.user.SocialUserDetail.kf6445550')"
        min-width="160"
      >
        <dict-tag :type="DICT_TYPE.SYSTEM_SOCIAL_TYPE" :value="detailData.type" />
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.social.user.SocialUserDetail.k90542e0a')"
        min-width="120"
      >
        {{ detailData.name }}
      </el-descriptions-item>
      <el-descriptions
        :label="t('auto.views.system.social.user.SocialUserDetail.k2b78f76f')"
        min-width="120"
      >
        <el-image :src="detailData.avatar" class="h-30px w-30px" />
      </el-descriptions>
      <el-descriptions-item
        :label="t('auto.views.system.social.user.SocialUserDetail.k7bfda319')"
        min-width="120"
      >
        {{ detailData.token }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.social.user.SocialUserDetail.kfbd1a4d1')"
        min-width="120"
      >
        <el-input
          v-model="detailData.rawTokenInfo"
          :autosize="{ maxRows: 20 }"
          :readonly="true"
          type="textarea"
        />
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.social.user.SocialUserDetail.k499d92f5')"
        min-width="120"
      >
        <el-input
          v-model="detailData.rawUserInfo"
          :autosize="{ maxRows: 20 }"
          :readonly="true"
          type="textarea"
        />
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.social.user.SocialUserDetail.k68fce9f5')"
        min-width="120"
      >
        {{ detailData.code }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.social.user.SocialUserDetail.k4997a6a3')"
        min-width="120"
      >
        {{ detailData.state }}
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import * as SocialUserApi from '@/api/system/social/user'
const { t } = useI18n()
const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单的加载中
const detailData = ref({} as SocialUserApi.SocialUserVO) // 详情数据

/** 打开弹窗 */
const open = async (id: number) => {
  dialogVisible.value = true
  // 设置数据
  try {
    detailData.value = await SocialUserApi.getSocialUser(id)
  } finally {
    detailLoading.value = false
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗
</script>
