<template>
  <el-table :data="props.list" stripe border v-loading="props.loading" style="margin-top: 10px">
    <el-table-column
      :label="t('auto.views.mp.material.components.VoiceTable.k9f42dac6')"
      align="center"
      prop="mediaId"
    />
    <el-table-column
      :label="t('auto.views.mp.material.components.VoiceTable.k1275f6fe')"
      align="center"
      prop="name"
    />
    <el-table-column
      :label="t('auto.views.mp.material.components.VoiceTable.k7a73e125')"
      align="center"
    >
      <template #default="scope">
        <WxVoicePlayer v-if="scope.row.url" :url="scope.row.url" />
      </template>
    </el-table-column>
    <el-table-column
      :label="t('extra.kcae25527')"
      align="center"
      prop="createTime"
      :formatter="dateFormatter"
      width="180"
    >
      <template #default="scope">
        <span>{{ scope.row.createTime }}</span>
      </template>
    </el-table-column>
    <el-table-column
      :label="t('common.operation')"
      align="center"
      class-name="small-padding fixed-width"
    >
      <template #default="scope">
        <el-button type="primary" link @click="emit('delete', scope.row.id)">
          <Icon icon="ep:download" />{{ t('auto.views.infra.file.index.k2b9d0131') }}
        </el-button>
        <el-button
          type="primary"
          link
          @click="emit('delete', scope.row.id)"
          v-hasPermi="['mp:material:delete']"
        >
          <Icon icon="ep:delete" />{{ t('common.delete') }}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script lang="ts" setup>
import WxVoicePlayer from '@/views/mp/components/wx-voice-play'
import { dateFormatter } from '@/utils/formatTime'
const { t } = useI18n()
const props = defineProps<{
  list: any[]
  loading: boolean
}>()

const emit = defineEmits<{
  (e: 'delete', v: number)
}>()
</script>
