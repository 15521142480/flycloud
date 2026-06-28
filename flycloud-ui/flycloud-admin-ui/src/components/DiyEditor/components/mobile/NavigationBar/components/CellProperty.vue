<template>
  <div class="h-40px flex items-center justify-center">
    <MagicCubeEditor
      v-model="cellList"
      class="m-b-16px"
      :rows="1"
      :cols="cellCount"
      :cube-size="38"
      @hot-area-selected="handleHotAreaSelected"
    />
    <img src="@/assets/imgs/diy/app-nav-bar-mp.png" alt="" class="h-30px w-76px" v-if="isMp" />
  </div>
  <template v-for="(cell, cellIndex) in cellList" :key="cellIndex">
    <template v-if="selectedHotAreaIndex === cellIndex">
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.NavigationBar.components.ke4e46c72')"
        :prop="`cell[${cellIndex}].type`"
      >
        <el-radio-group v-model="cell.type">
          <el-radio value="text">{{
            t('auto.components.DiyEditor.components.mobile.NavigationBar.components.kf4d3dab8')
          }}</el-radio>
          <el-radio value="image">{{
            t('auto.components.DiyEditor.components.mobile.NavigationBar.components.kbe8da62e')
          }}</el-radio>
          <el-radio value="search">{{
            t('auto.components.DiyEditor.components.mobile.NavigationBar.components.k44d7587d')
          }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- 1. 文字 -->
      <template v-if="cell.type === 'text'">
        <el-form-item
          :label="
            t('auto.components.DiyEditor.components.mobile.NavigationBar.components.k163aec91')
          "
          :prop="`cell[${cellIndex}].text`"
        >
          <el-input v-model="cell!.text" maxlength="10" show-word-limit />
        </el-form-item>
        <el-form-item
          :label="
            t('auto.components.DiyEditor.components.mobile.NavigationBar.components.k8ef48860')
          "
          :prop="`cell[${cellIndex}].text`"
        >
          <ColorInput v-model="cell!.textColor" />
        </el-form-item>
      </template>
      <!-- 2. 图片 -->
      <template v-else-if="cell.type === 'image'">
        <el-form-item :label="t('extra.k3b9e2563')" :prop="`cell[${cellIndex}].imgUrl`">
          <UploadImg v-model="cell.imgUrl" :limit="1" height="56px" width="56px">
            <template #tip>{{ t('extra.k2fb19cb9') }}</template>
          </UploadImg>
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.MagicCube.property.k71502205')"
          :prop="`cell[${cellIndex}].url`"
        >
          <AppLinkInput v-model="cell.url" />
        </el-form-item>
      </template>
      <!-- 3. 搜索框 -->
      <template v-else>
        <el-form-item :label="t('extra.k9b14c905')" :prop="`cell[${cellIndex}].placeholder`">
          <el-input v-model="cell.placeholder" maxlength="10" show-word-limit />
        </el-form-item>
        <el-form-item :label="t('extra.k0103eb2d')" :prop="`cell[${cellIndex}].borderRadius`">
          <el-slider
            v-model="cell.borderRadius"
            :max="100"
            :min="0"
            show-input
            input-size="small"
            :show-input-controls="false"
          />
        </el-form-item>
      </template>
    </template>
  </template>
</template>

<script setup lang="ts">
import { NavigationBarCellProperty } from '../config'
import { usePropertyForm } from '@/components/DiyEditor/util'
const { t } = useI18n()
defineOptions({ name: 'NavigationBarCellProperty' })

const props = defineProps<{
  modelValue: NavigationBarCellProperty[]
  isMp: boolean
}>()
const emit = defineEmits(['update:modelValue'])
const { formData: cellList } = usePropertyForm(props.modelValue, emit)
if (!cellList.value) cellList.value = []

// 单元格数量：小程序6个（右侧胶囊按钮占了2个），其它平台8个
const cellCount = computed(() => (props.isMp ? 6 : 8))

// 选中的热区
const selectedHotAreaIndex = ref(0)
const handleHotAreaSelected = (cellValue: NavigationBarCellProperty, index: number) => {
  selectedHotAreaIndex.value = index
  if (!cellValue.type) {
    cellValue.type = 'text'
    cellValue.textColor = '#111111'
  }
}
</script>

<style scoped lang="scss"></style>
