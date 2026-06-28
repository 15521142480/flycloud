<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.mall.trade.brokerage.user.BrokerageUserListDialog.k8e4ab3af')"
    width="75%"
  >
    <ContentWrap>
      <!-- 搜索工作栏 -->
      <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="85px"
      >
        <el-form-item
          :label="t('auto.views.mall.trade.brokerage.user.BrokerageUserListDialog.k31ab92d1')"
          prop="level"
        >
          <el-radio-group v-model="queryParams.level" @change="handleQuery">
            <el-radio-button checked>{{
              t('auto.views.mall.trade.brokerage.user.BrokerageUserListDialog.k778fc8f9')
            }}</el-radio-button>
            <el-radio-button value="1">{{
              t('auto.views.mall.trade.brokerage.user.BrokerageUserListDialog.k8c80ac09')
            }}</el-radio-button>
            <el-radio-button value="2">{{
              t('auto.views.mall.trade.brokerage.user.BrokerageUserListDialog.k0f298d5c')
            }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          :label="t('auto.views.mall.trade.brokerage.user.BrokerageUserListDialog.kd9bbd388')"
          prop="bindUserTime"
        >
          <el-date-picker
            v-model="queryParams.bindUserTime"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            :start-placeholder="
              t('auto.views.mall.trade.brokerage.user.BrokerageUserListDialog.k1f291968')
            "
            :end-placeholder="
              t('auto.views.mall.trade.brokerage.user.BrokerageUserListDialog.kf4b9b2b5')
            "
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
            class="!w-240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery"
            ><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button
          >
          <el-button @click="resetQuery"
            ><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button
          >
        </el-form-item>
      </el-form>
    </ContentWrap>

    <!-- 列表 -->
    <ContentWrap>
      <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
        <el-table-column
          :label="t('auto.views.mall.trade.brokerage.user.BrokerageUserListDialog.kec750ef6')"
          align="center"
          prop="id"
          min-width="80px"
        />
        <el-table-column
          :label="t('auto.views.mall.trade.brokerage.user.BrokerageUserListDialog.k4ceeeb31')"
          align="center"
          prop="avatar"
          width="70px"
        >
          <template #default="scope">
            <el-avatar :src="scope.row.avatar" />
          </template>
        </el-table-column>
        <el-table-column
          :label="t('system.user.nickname')"
          align="center"
          prop="name"
          min-width="80px"
        />
        <el-table-column
          :label="t('extra.ka006ed09')"
          align="center"
          prop="brokerageUserCount"
          min-width="80px"
        />
        <el-table-column
          :label="t('extra.kec878b95')"
          align="center"
          prop="brokerageOrderCount"
          min-width="110px"
        />
        <el-table-column
          :label="t('auto.views.mall.trade.brokerage.user.index.k95774950')"
          align="center"
          prop="brokerageEnabled"
          min-width="80px"
        >
          <template #default="scope">
            <el-tag v-if="scope.row.brokerageEnabled">{{
              t('auto.views.mall.trade.brokerage.user.index.kfbd5b750')
            }}</el-tag>
            <el-tag v-else type="info">{{ t('extra.kadb3d23e') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          :label="t('auto.views.mall.trade.brokerage.user.BrokerageOrderListDialog.kd9bbd388')"
          align="center"
          prop="bindUserTime"
          :formatter="dateFormatter"
          width="180px"
        />
      </el-table>
      <!-- 分页 -->
      <Pagination
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </ContentWrap>
  </Dialog>
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import * as BrokerageUserApi from '@/api/mall/trade/brokerage/user'
/** 推广人列表 */
const { t } = useI18n()
defineOptions({ name: 'BrokerageUserListDialog' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  bindUserId: null,
  level: '',
  bindUserTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 打开弹窗 */
const dialogVisible = ref(false) // 弹窗的是否展示
const open = async (bindUserId: any) => {
  dialogVisible.value = true
  queryParams.bindUserId = bindUserId
  resetQuery()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await BrokerageUserApi.getBrokerageUserPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}
</script>
