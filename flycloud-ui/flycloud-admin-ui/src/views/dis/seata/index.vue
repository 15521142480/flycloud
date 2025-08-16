<template>

  <!-- 树型 -->
  <ContentWrap style="height: calc(100vh - 160px)">

    <el-row>
      <el-col :span="3">
        <div style="margin-left: 30px;margin-top: 35px">
          <el-row>
            <el-col>
              <el-button @click="seataTest(0)" style="margin-bottom: 10px">
                不回滚测试
              </el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-button type="primary" @click="seataTest(1)">
                回滚测试
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-col>

      <el-col :span="20">
        <div style="margin-top: 6px; line-height: 30px">
          不回滚测试为无异常，回滚测试则在最后有异常。
          <br/> 1、分布式事务，采用seata的AT模式方案。
          <br/> 2、测试接口为: 系统服务flycloud-system的 /test/seataTest。
          <br/> 3、测试时请在nacos的application-datasource.yaml检查各系统是否连接不同的数据库，如没有则修改为不同库。
          <br/> 本案例为system-master、bpm-master, 对应库为fly-cloud、fdance_cloud，对应表都为test。
        </div>
      </el-col>
    </el-row>

    <el-divider style="margin: 10px 0" />

  </ContentWrap>



</template>

<script lang="ts" setup>
import * as SeataApi from '@/api/dis/seata'
// import {CirclePlus, Delete, Edit, Plus} from "@element-plus/icons-vue";

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
    const text = isRollback == 0 ? '确认进行【不回滚测试】吗' : '确认进行【回滚测试】吗'
    await message.confirm(text)
    await SeataApi.seataTestApi(isRollback)
    message.success('测试成功');
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
:deep .menu-tree .el-tree-node__content {
  /*  line-height: 50px;*/
  height: 45px;
  font-size: 16px;
  font-weight: bold;
}


</style>
