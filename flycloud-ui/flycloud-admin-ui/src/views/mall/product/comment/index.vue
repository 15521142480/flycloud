<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item
        :label="t('auto.views.mall.product.comment.index.kfde2b56a')"
        prop="replyStatus"
      >
        <el-select v-model="queryParams.replyStatus" class="!w-240px">
          <el-option :label="t('auto.views.mall.product.comment.index.kae980f6f')" :value="true" />
          <el-option :label="t('auto.views.mall.product.comment.index.k3c9becfc')" :value="false" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.mall.product.comment.index.k47b74133')" prop="spuName">
        <el-input
          v-model="queryParams.spuName"
          :placeholder="t('auto.views.mall.product.comment.index.k5c6bf9b9')"
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.product.comment.index.ka311ed74')"
        prop="userNickname"
      >
        <el-input
          v-model="queryParams.userNickname"
          :placeholder="t('auto.views.mall.product.comment.index.k15762992')"
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.mall.product.comment.index.k8c60a237')" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          :placeholder="t('auto.views.mall.product.comment.index.k703bbdd7')"
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.mall.product.comment.index.k49a47b72')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.mall.product.comment.index.k1f291968')"
          :end-placeholder="t('auto.views.mall.product.comment.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" />
          {{ t('extra.k9e5a77db') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px" />
          {{ t('extra.kd0f3e2f6') }}
        </el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['product:comment:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" />
          {{ t('extra.k0d2b7985') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="false">
      <el-table-column
        :label="t('auto.views.mall.product.comment.index.ke8b034f2')"
        align="center"
        prop="id"
        min-width="80"
      />
      <el-table-column
        :label="t('auto.views.mall.product.comment.index.k751c5853')"
        align="center"
        min-width="400"
      >
        <template #default="scope">
          <div class="row flex items-center gap-x-4px">
            <el-image
              v-if="scope.row.skuPicUrl"
              :src="scope.row.skuPicUrl"
              :preview-src-list="[scope.row.skuPicUrl]"
              class="h-40px w-40px shrink-0"
              preview-teleported
            />
            <div>{{ scope.row.spuName }}</div>
            <el-tag
              v-for="property in scope.row.skuProperties"
              :key="property.propertyId"
              class="mr-10px"
            >
              {{ property.propertyName }}: {{ property.valueName }}
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="用户名称" align="center" prop="userNickname" width="100" />
      <el-table-column label="商品评分" align="center" prop="descriptionScores" width="90" />
      <el-table-column label="服务评分" align="center" prop="benefitScores" width="90" />
      <el-table-column label="评论内容" align="center" prop="content" min-width="210">
        <template #default="scope">
          <p>{{ scope.row.content }}</p>
          <div class="flex justify-center gap-x-4px">
            <el-image
              v-for="(picUrl, index) in scope.row.picUrls"
              :key="index"
              :src="picUrl"
              :preview-src-list="scope.row.picUrls"
              :initial-index="index"
              class="h-40px w-40px"
              preview-teleported
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="回复内容"
        align="center"
        prop="replyContent"
        min-width="250"
        show-overflow-tooltip
      />
      <el-table-column
        label="评论时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180"
      />
      <el-table-column label="是否展示" align="center" width="80px">
        <template #default="scope">
          <el-switch
            v-model="scope.row.visible"
            :active-value="true"
            :inactive-value="false"
            v-hasPermi="['product:comment:update']"
            @change="handleVisibleChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="60px" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="handleReply(scope.row.id)"
            v-hasPermi="['product:comment:update']"
          >
            回复
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <CommentForm ref="formRef" @success="getList" />
  <!-- 回复表单弹窗 -->
  <ReplyForm ref="replyFormRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import * as CommentApi from '@/api/mall/product/comment'
import CommentForm from './CommentForm.vue'
import ReplyForm from './ReplyForm.vue'
const { t } = useI18n()
defineOptions({ name: 'ProductComment' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  replyStatus: null,
  spuName: null,
  userNickname: null,
  orderId: null,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await CommentApi.getCommentPage(queryParams)
    // visible 如果为 null，会导致刷新的时候触发 e-switch 的 change 事件
    data.list.forEach((item) => {
      if (!item.visible) {
        item.visible = false
      }
    })
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
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 回复按钮操作 **/
const replyFormRef = ref()
const handleReply = (id: number) => {
  replyFormRef.value.open(id)
}

/** 显示/隐藏 **/
const handleVisibleChange = async (row: CommentApi.CommentVO) => {
  if (loading.value) {
    return
  }
  let changedValue = row.visible
  try {
    await message.confirm(
      changedValue
        ? t('auto.views.mall.product.comment.index.k0a4a6910')
        : t('auto.views.mall.product.comment.index.k1e3f768b')
    )
    await CommentApi.updateCommentVisible({ id: row.id, visible: changedValue })
    await getList()
  } catch {
    row.visible = !changedValue
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
