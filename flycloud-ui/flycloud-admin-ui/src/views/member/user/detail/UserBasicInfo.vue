<template>
  <el-card shadow="never">
    <template #header>
      <slot name="header"></slot>
    </template>
    <el-row>
      <el-col :span="4">
        <ElAvatar shape="square" :size="140" :src="user.avatar || undefined" />
      </el-col>
      <el-col :span="20">
        <el-descriptions :column="2">
          <el-descriptions-item>
            <template #label>
              <descriptions-item-label :label="t('login.username')" icon="ep:user" />
            </template>
            {{ user.name || '空' }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <descriptions-item-label :label="t('system.user.nickname')" icon="ep:user" />
            </template>
            {{ user.name }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('system.user.phone')">
            <template #label>
              <descriptions-item-label :label="t('system.user.phone')" icon="ep:phone" />
            </template>
            {{ user.mobile }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <descriptions-item-label :label="t('profile.user.sex')" icon="fa:mars-double" />
            </template>
            <dict-tag :type="DICT_TYPE.SYSTEM_USER_SEX" :value="user.sex" />
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <descriptions-item-label
                :label="t('auto.views.mall.trade.order.form.OrderUpdateAddressForm.kfe837ba6')"
                icon="ep:location"
              />
            </template>
            {{ user.areaName }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <descriptions-item-label :label="t('extra.k945f63eb')" icon="ep:position" />
            </template>
            {{ user.registerIp }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <descriptions-item-label :label="t('extra.k8483ed13')" icon="fa:birthday-cake" />
            </template>
            {{ user.birthday ? formatDate(user.birthday) : '空' }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <descriptions-item-label
                :label="t('auto.views.member.user.index.k525ad5e2')"
                icon="ep:calendar"
              />
            </template>
            {{ user.createTime ? formatDate(user.createTime) : '空' }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <descriptions-item-label :label="t('extra.k39b4f204')" icon="ep:calendar" />
            </template>
            {{ user.loginDate ? formatDate(user.loginDate) : '空' }}
          </el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
  </el-card>
</template>
<script setup lang="ts">
const { t } = useI18n()
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import * as UserApi from '@/api/member/user'
import { DescriptionsItemLabel } from '@/components/Descriptions/index'

const { user } = defineProps<{ user: UserApi.UserVO }>()
</script>
<style scoped lang="scss">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
