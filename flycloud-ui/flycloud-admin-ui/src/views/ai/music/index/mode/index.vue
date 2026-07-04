<template>
  <ContentWrap class="w-300px h-full mb-[0!important]">
    <el-radio-group v-model="generateMode" class="mb-15px">
      <el-radio-button value="desc">
        {{ t('auto.views.ai.music.index.mode.index.k71bc0142') }}
      </el-radio-button>
      <el-radio-button value="lyric">
        {{ t('auto.views.ai.music.index.mode.index.k3619e1b4') }}
      </el-radio-button>
    </el-radio-group>

    <!-- 描述模式/歌词模式 切换 -->
    <component :is="generateMode === 'desc' ? desc : lyric" ref="modeRef" />

    <el-button type="primary" round class="w-full" @click="generateMusic">
      {{ t('auto.views.ai.music.index.mode.index.k458222e4') }}
    </el-button>
  </ContentWrap>
</template>

<script lang="ts" setup>
import desc from './desc.vue'
import lyric from './lyric.vue'
const { t } = useI18n()
defineOptions({ name: 'Index' })

const emits = defineEmits(['generate-music'])

const generateMode = ref('lyric')

const modeRef = ref<Nullable<{ formData: Recordable }>>(null)

/*
 *@Description: 根据信息生成音乐
 *@MethodAuthor: xiaohong
 *@date: 2025-06-27 16:40:16
 */
function generateMusic() {
  emits('generate-music', { formData: unref(modeRef)?.formData })
}
</script>
