<template>
  <!-- 搜索工作栏 -->
  <ContentWrap>
    <el-row style="margin-left: 15px">
      <el-col :span="17">
        <span style="margin-left: 10px; color: red">
          {{ t('extra.kadf28cb9') }}
          <span style="margin-left: 20px">{{
            t('auto.views.infra.generator.index.k9d073aac')
          }}</span>
        </span>
      </el-col>
    </el-row>

    <el-row style="margin: 5px 20px">
      <el-col :span="10">
        <el-input
          v-model="queryParams.tables"
          :placeholder="t('auto.views.infra.generator.index.kf4eca7ce')"
          clearable
        />
      </el-col>

      <el-col :span="6" style="margin-left: 20px">
        <el-button
          v-hasPermi="['infra:generator:generatorCode']"
          type="primary"
          @click="handleGeneratorCode"
          :loading="loading"
          ><Icon class="mr-5px" icon="ep:plus" /> {{ t('action.generate') }}</el-button
        >
      </el-col>
    </el-row>
  </ContentWrap>

  <!-- 列表 -->
  <!--  <ContentWrap>-->
  <!--    <div>-->

  <!--    </div>-->
  <!--  </ContentWrap>-->
</template>
<script lang="ts" setup>
import * as GeneratorApi from '@/api/infra/generator'
import { object } from 'vue-types'
const { t } = useI18n()
defineOptions({ name: 'Generator' })

const message = useMessage() // 消息弹窗

const loading = ref(false) // 列表的加载中
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10000,
  tables: ''
})

/** 生成代码 */
const handleGeneratorCode = async () => {
  if (!queryParams.tables) {
    message.notifyWarning(t('auto.views.infra.generator.index.k3aca1275'))
    return
  }
  loading.value = true
  try {
    // 删除的二次确认
    await message.confirm(
      t('auto.views.infra.generator.index.k14d0c73d') +
        queryParams.tables +
        t('auto.views.infra.generator.index.k4d4547cf')
    )
    const data = await GeneratorApi.handleGeneratorCode(queryParams.tables)
    message.notifySuccess(
      t('auto.views.infra.generator.index.kf6eb64d2') +
        queryParams.tables +
        t('auto.views.infra.generator.index.k1a0586ff')
    )
  } catch (e) {
    // message.notifyError('生成代码失败！' + e.message)
  } finally {
    loading.value = false
  }
}

/** 初始化 **/
onMounted(async () => {})
</script>
