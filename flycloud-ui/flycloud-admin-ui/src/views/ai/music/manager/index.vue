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
      <el-form-item :label="t('auto.views.ai.music.manager.index.kec750ef6')" prop="userId">
        <el-select
          v-model="queryParams.userId"
          clearable
          :placeholder="t('auto.views.ai.music.manager.index.kb719fb8a')"
          class="!w-240px"
        >
          <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.music.manager.index.kc1cc63e7')" prop="title">
        <el-input
          v-model="queryParams.title"
          :placeholder="t('auto.views.ai.music.manager.index.kb9e3f511')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.music.manager.index.k29939e96')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.ai.music.manager.index.k9ba3703a')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.AI_MUSIC_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.music.manager.index.kd8c739b9')" prop="generateMode">
        <el-select
          v-model="queryParams.generateMode"
          :placeholder="t('auto.views.ai.music.manager.index.k5a7701c0')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.AI_GENERATE_MODE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.createTime')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.ai.music.manager.index.k1f291968')"
          :end-placeholder="t('auto.views.ai.music.manager.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.music.manager.index.k4c44200c')" prop="publicStatus">
        <el-select
          v-model="queryParams.publicStatus"
          :placeholder="t('auto.views.ai.music.manager.index.kff9fc4c5')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getBoolDictOptions(DICT_TYPE.INFRA_BOOLEAN_STRING)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
        :label="t('auto.views.ai.music.manager.index.k9f42dac6')"
        align="center"
        prop="id"
        width="180"
        fixed="left"
      />
      <el-table-column
        :label="t('auto.views.ai.music.manager.index.kc1cc63e7')"
        align="center"
        prop="title"
        width="180px"
        fixed="left"
      />
      <el-table-column
        :label="t('auto.views.ai.music.manager.index.k9ba763ea')"
        align="center"
        prop="userId"
        width="180"
      >
        <template #default="scope">
          <span>{{ userList.find((item) => item.id === scope.row.userId)?.name }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.ai.music.manager.index.k29939e96')"
        align="center"
        prop="status"
        width="100"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.AI_MUSIC_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.ai.chat.index.components.conversation.k98fd0cbd')"
        align="center"
        prop="model"
        width="180"
      />
      <el-table-column
        :label="t('auto.components.DiyEditor.components.ComponentContainerProperty.k163aec91')"
        align="center"
        width="180"
      >
        <template #default="{ row }">
          <el-link
            v-if="row.audioUrl?.length > 0"
            type="primary"
            :href="row.audioUrl"
            target="_blank"
          >
            {{ t('extra.k95521bb7') }}
          </el-link>
          <el-link
            v-if="row.videoUrl?.length > 0"
            type="primary"
            :href="row.videoUrl"
            target="_blank"
            class="!pl-5px"
          >
            {{ t('auto.components.DiyEditor.components.mobile.Carousel.property.kfa4e33b6') }}
          </el-link>
          <el-link
            v-if="row.imageUrl?.length > 0"
            type="primary"
            :href="row.imageUrl"
            target="_blank"
            class="!pl-5px"
          >
            {{ t('auto.components.DiyEditor.components.mobile.Carousel.property.kb8bb31e1') }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column :label="t('extra.k439f42b7')" align="center" prop="duration" width="100" />
      <el-table-column
        :label="t('auto.views.ai.image.index.components.ImageDetail.k2f0bb123')"
        align="center"
        prop="prompt"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.ai.music.index.mode.lyric.k315d810f')"
        align="center"
        prop="lyric"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.bpm.group.UserGroupForm.k412f54dc')"
        align="center"
        prop="gptDescriptionPrompt"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.ai.music.manager.index.kd8c739b9')"
        align="center"
        prop="generateMode"
        width="100"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.AI_GENERATE_MODE" :value="scope.row.generateMode" />
        </template>
      </el-table-column>
      <el-table-column :label="t('extra.k89a6c742')" align="center" prop="tags" width="180">
        <template #default="scope">
          <el-tag v-for="tag in scope.row.tags" :key="tag" round class="ml-2px">
            {{ tag }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.ai.image.manager.index.k4c44200c')"
        align="center"
        prop="publicStatus"
      >
        <template #default="scope">
          <el-switch
            v-model="scope.row.publicStatus"
            :active-value="true"
            :inactive-value="false"
            @change="handleUpdatePublicStatusChange(scope.row)"
            :disabled="scope.row.status !== AiMusicStatusEnum.SUCCESS"
          />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.bpm.task.todo.index.k017af56c')"
        align="center"
        prop="taskId"
        width="180"
      />
      <el-table-column :label="t('extra.k4604d502')" align="center" prop="errorMessage" />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column :label="t('common.operation')" align="center" width="100" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['ai:music:delete']"
          >
            {{ t('common.delete') }}
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
</template>

<script setup lang="ts">
import { getIntDictOptions, getBoolDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import { MusicApi, MusicVO } from '@/api/ai/music'
import * as UserApi from '@/api/system/user'
import { AiMusicStatusEnum } from '@/views/ai/utils/constants'

/** AI 音乐 列表 */
const { t } = useI18n()
defineOptions({ name: 'AiMusicManager' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<MusicVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userId: undefined,
  title: undefined,
  status: undefined,
  generateMode: undefined,
  createTime: [],
  publicStatus: undefined
})
const queryFormRef = ref() // 搜索的表单
const userList = ref<UserApi.UserVO[]>([]) // 用户列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await MusicApi.getMusicPage(queryParams)
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

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await MusicApi.deleteMusic(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 修改是否发布 */
const handleUpdatePublicStatusChange = async (row: MusicVO) => {
  try {
    // 修改状态的二次确认
    const text = row.publicStatus
      ? t('auto.views.ai.music.manager.index.kdfe5a318')
      : t('auto.views.ai.music.manager.index.k6858674b')
    await message.confirm(
      t('auto.views.ai.music.manager.index.ka4db1fb1') +
        text +
        t('auto.views.ai.music.manager.index.k87a67cb9')
    )
    // 发起修改状态
    await MusicApi.updateMusic({
      id: row.id,
      publicStatus: row.publicStatus
    })
    await getList()
  } catch {
    row.publicStatus = !row.publicStatus
  }
}

/** 初始化 **/
onMounted(async () => {
  getList()
  // 获得用户列表
  userList.value = await UserApi.getSimpleUserList()
})
</script>
