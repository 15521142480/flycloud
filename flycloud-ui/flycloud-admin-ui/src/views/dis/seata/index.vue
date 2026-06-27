<template>
  <!-- 树型 -->
  <ContentWrap style="height: calc(100vh - 160px)">
    <el-row>
      <el-col :span="3">
        <div style="margin-left: 30px; margin-top: 35px">
          <el-row>
            <el-col>
              <el-button @click="seataTest(0)" style="margin-bottom: 10px">
                {{ t('auto.views.dis.seata.index.k12a6778a') }}
              </el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-button type="primary" @click="seataTest(1)">
                {{ t('auto.views.dis.seata.index.kbed4c98e') }}
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-col>

      <el-col :span="20">
        <div style="margin-top: 6px; line-height: 30px">
          {{ t('extra.kbdeb2eea') }}
          <br />
          {{ t('extra.k2fb71000') }} <br />
          {{ t('extra.kf0f0d734') }} <br />
          {{ t('extra.kd8370bd5') }}
          <br />
          {{ t('extra.k9b3e87cf') }}
        </div>
      </el-col>
    </el-row>

    <el-divider style="margin: 10px 0" />
  </ContentWrap>
</template>

<script lang="ts" setup>
import * as SeataApi from '@/api/dis/seata'
// import {CirclePlus, Delete, Edit, Plus} from "@element-plus/icons-vue";
const { t } = useI18n()
defineOptions({ name: 'SystemMenu' })

// const { wsCache } = useCache()
// const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗
//
// const loading = ref(true) // 列表的加载中
// const list = ref<any>([]) // 列表的数据

/** seata测试 */
const seataTest = async (isRollback: number) => {
  // loading.value = true
  try {
    const text =
      isRollback == 0
        ? t('auto.views.dis.seata.index.kbeb513d1')
        : t('auto.views.dis.seata.index.k62995029')
    await message.confirm(text)
    await SeataApi.seataTestApi(isRollback)
    message.success(t('auto.views.dis.seata.index.k94b6e63d'))
  } finally {
    // loading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  // getList()
})
</script>

<style lang="scss" scoped>
/* 增大Element Plus Tree组件的行高 */
:deep(.menu-tree .el-tree-node__content) {
  /*  line-height: 50px;*/
  height: 45px;
  font-size: 16px;
  font-weight: bold;
}
</style>
