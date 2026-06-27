<template>
  <div class="flex flex-col">
    <div class="flex-auto flex overflow-hidden">
      <el-tabs v-model="currentType" class="flex-auto px-[var(--app-content-padding)]">
        <!-- 我的创作 -->
        <el-tab-pane
          v-loading="loading"
          :label="t('auto.views.ai.music.index.list.index.kb1f3a3a5')"
          name="mine"
        >
          <el-row v-if="mySongList.length" :gutter="12">
            <el-col v-for="song in mySongList" :key="song.id" :span="24">
              <songCard :songInfo="song" @play="setCurrentSong(song)" />
            </el-col>
          </el-row>
          <el-empty v-else :description="t('extra.ke30c74e1')" />
        </el-tab-pane>

        <!-- 试听广场 -->
        <el-tab-pane
          v-loading="loading"
          :label="t('auto.views.ai.music.index.list.index.k3eeb2fa4')"
          name="square"
        >
          <el-row v-if="squareSongList.length" v-loading="loading" :gutter="12">
            <el-col v-for="song in squareSongList" :key="song.id" :span="24">
              <songCard :songInfo="song" @play="setCurrentSong(song)" />
            </el-col>
          </el-row>
          <el-empty v-else :description="t('extra.ke30c74e1')" />
        </el-tab-pane>
      </el-tabs>
      <!-- songInfo -->
      <songInfo class="flex-none" />
    </div>
    <audioBar class="flex-none" />
  </div>
</template>

<script lang="ts" setup>
import songCard from './songCard/index.vue'
import songInfo from './songInfo/index.vue'
import audioBar from './audioBar/index.vue'
const { t } = useI18n()
defineOptions({ name: 'Index' })

const currentType = ref('mine')
// loading 状态
const loading = ref(false)
// 当前音乐
const currentSong = ref({})

const mySongList = ref<Recordable[]>([])
const squareSongList = ref<Recordable[]>([])

provide('currentSong', currentSong)

/*
 *@Description: 调接口生成音乐列表
 *@MethodAuthor: xiaohong
 *@Date: 2024-06-27 17:06:44
 */
function generateMusic(formData: Recordable) {
  console.log(formData)
  loading.value = true
  setTimeout(() => {
    mySongList.value = Array.from({ length: 20 }, (_, index) => {
      return {
        id: index,
        audioUrl: '',
        videoUrl: '',
        title: t('auto.views.ai.music.index.list.index.k8b7432ea') + index,
        imageUrl:
          'https://www.carsmp3.com/data/attachment/forum/201909/19/091020q5kgre20fidreqyt.jpg',
        desc: 'Metal, symphony, film soundtrack, grand, majesticMetal, dtrack, grand, majestic',
        date: t('auto.views.ai.music.index.list.index.k07d96261'),
        lyric: t('extra.ka3a5b343')
      }
    })
    loading.value = false
  }, 3000)
}

/*
 *@Description: 设置当前播放的音乐
 *@MethodAuthor: xiaohong
 *@Date: 2024-07-19 11:22:33
 */
function setCurrentSong(music: Recordable) {
  currentSong.value = music
}

defineExpose({
  generateMusic
})
</script>

<style lang="scss" scoped>
:deep(.el-tabs) {
  display: flex;
  flex-direction: column;
  .el-tabs__content {
    padding: 0 7px;
    overflow: auto;
  }
}
</style>
