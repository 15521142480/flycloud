<template>
  <div class="">
    <Title
      :title="t('auto.views.ai.music.index.mode.lyric.k315d810f')"
      :desc="t('extra.kd0e3c217')"
    >
      <el-input
        v-model="formData.lyric"
        :autosize="{ minRows: 6, maxRows: 6 }"
        resize="none"
        type="textarea"
        maxlength="1200"
        show-word-limit
        :placeholder="t('auto.views.ai.music.index.mode.lyric.k1f6e0b90')"
      />
    </Title>

    <Title :title="t('auto.views.ai.music.index.mode.lyric.k5a2386ff')">
      <el-space class="flex-wrap">
        <el-tag v-for="tag in tags" :key="tag" round class="mb-8px">{{ tag }}</el-tag>
      </el-space>

      <el-button
        :type="showCustom ? 'primary' : 'default'"
        round
        size="small"
        class="mb-6px"
        @click="showCustom = !showCustom"
        >{{ t('extra.k2f1a9300') }}
      </el-button>
    </Title>

    <Title v-show="showCustom" :desc="t('extra.k6e5077a9')" class="-mt-12px">
      <el-input
        v-model="formData.style"
        :autosize="{ minRows: 4, maxRows: 4 }"
        resize="none"
        type="textarea"
        maxlength="256"
        show-word-limit
        :placeholder="t('auto.views.ai.music.index.mode.lyric.k156352d4')"
      />
    </Title>

    <Title :title="t('auto.views.ai.music.index.mode.lyric.kabf19ca4')">
      <el-input
        v-model="formData.name"
        :placeholder="t('auto.views.ai.music.index.mode.lyric.k776c393d')"
      />
    </Title>

    <Title :title="t('auto.views.ai.music.index.mode.lyric.k989d1aff')">
      <el-select
        v-model="formData.version"
        :placeholder="t('auto.views.ai.music.index.mode.lyric.k382f4b55')"
      >
        <el-option
          v-for="item in [
            {
              value: '3',
              label: 'V3'
            },
            {
              value: '2',
              label: 'V2'
            }
          ]"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </Title>
  </div>
</template>

<script lang="ts" setup>
import Title from '../title/index.vue'
const { t } = useI18n()
defineOptions({ name: 'Lyric' })

const tags = ['rock', 'punk', 'jazz', 'soul', 'country', 'kidsmusic', 'pop']

const showCustom = ref(false)

const formData = reactive({
  lyric: '',
  style: '',
  name: '',
  version: ''
})

defineExpose({
  formData
})
</script>
