<template>
  <div>
    <el-card shadow="hover" class="home-overview-card">
      <el-skeleton :loading="loading" animated>
        <div class="home-overview">
          <section class="welcome-panel">
            <div class="welcome-main">
              <el-avatar :src="getFilePreviewUrl(avatar)" :size="58" class="welcome-avatar">
                <img src="@/assets/imgs/profile.png" alt="" />
              </el-avatar>
              <div class="welcome-copy">
                <div class="welcome-title">
                  {{ t('workplace.welcome') }}， {{ username }}
                </div>
                <div class="welcome-weather">{{ t('workplace.toady') }}，23℃ - 32℃！</div>
              </div>
            </div>
            <div class="welcome-stats">
              <div class="welcome-stat">
                <span>{{ t('workplace.project') }}</span>
                <CountTo
                  class="welcome-stat-count"
                  :start-val="0"
                  :end-val="totalSate.project"
                  :duration="2200"
                />
              </div>
              <div class="welcome-stat">
                <span>{{ t('workplace.access') }}</span>
                <CountTo
                  class="welcome-stat-count"
                  :start-val="0"
                  :end-val="totalSate.access"
                  :duration="2200"
                />
              </div>
            </div>
          </section>
          <section class="todo-summary">
            <div class="todo-summary-title">{{ t('workplace.toDo') }}</div>
            <CountTo
              class="todo-summary-count"
              :start-val="0"
              :end-val="totalSate.todo"
              :duration="2200"
            />
          </section>
          <section class="todo-list-panel" :class="{ 'is-single-row': todoDataList.length === 1 }">
            <el-table
              :data="todoDataList"
              :height="todoDataList.length === 1 ? undefined : 98"
              :show-header="false"
              class="todo-table"
              empty-text="暂无代办数据"
            >
              <el-table-column prop="username" min-width="100" show-overflow-tooltip />
              <el-table-column prop="title" min-width="110" show-overflow-tooltip />
              <el-table-column prop="time" min-width="100" align="right" show-overflow-tooltip />
              <el-table-column width="76" align="right">
                <template #default>
                  <el-button link type="primary" class="todo-action" @click="goTodo">去办理</el-button>
                </template>
              </el-table-column>
            </el-table>
          </section>
        </div>
      </el-skeleton>
    </el-card>
  </div>

  <el-row style="margin-top: 15px">

    <!-- 工作流引擎 -->
    <el-col :span="6">
      <el-card shadow="hover" class="project-card">
        <template #header>
          <div class="card-header">
            <span>工作流引擎</span>
          </div>
        </template>
        <div ref="chartRef" v-loading="loading">
          <el-image fit="cover" :src="img_bpm1" class="card-img" @click="imagePreview(img_bpm1)" />
          <el-image fit="cover" :src="img_bpm2" class="card-img" @click="imagePreview(img_bpm2)" />
        </div>
        <template #footer>
          <div class="card-footer">
            <el-button type="primary" @click="routerForward(1)" text bg>
              快捷体验
            </el-button>
          </div>
        </template>
      </el-card>
    </el-col>

    <!-- 商城后台 -->
    <el-col :span="6" style="margin-left: 15px">
      <el-card shadow="hover" class="project-card">
        <template #header>
          <div class="card-header">
            <span>商城后台</span>
          </div>
        </template>
        <div ref="chartRef" v-loading="loading">

        </div>
        <template #footer>
          <div class="card-footer">
            <el-button type="primary" @click="routerForward(2)" text bg>
              快捷体验
            </el-button>
          </div>
        </template>
      </el-card>
    </el-col>

    <!-- 商城移动端 -->
    <el-col :span="5" style="margin-left: 15px">
      <el-card shadow="hover" class="project-card">
        <template #header>
          <div class="card-header">
            <span>商城移动端（h5）</span>
          </div>
        </template>
        <div ref="chartRef" v-loading="loading">

        </div>
        <template #footer>
          <div class="card-footer">
            <el-button type="primary" @click="routerForward(3)" text bg>
              快捷体验
            </el-button>
          </div>
        </template>
      </el-card>
    </el-col>

    <!-- 即时通讯 -->
    <el-col :span="6" style="margin-left: 15px">
      <el-card shadow="hover" class="project-card">
        <template #header>
          <div class="card-header">
            <span>即时通讯</span>
          </div>
        </template>
        <div ref="chartRef" v-loading="loading">

        </div>
        <template #footer>
          <div class="card-footer">
            <el-button type="primary" @click="routerForward(4)" text bg>
              体验1（内嵌）
            </el-button>
            <el-button type="primary" @click="routerForward(5)" text bg>
              体验2（窗口）
            </el-button>
          </div>
        </template>
      </el-card>
    </el-col>
  </el-row>

  <el-row style="margin-top: 15px">
    <el-col :span="24">
      <el-card shadow="hover" class="project-info-card">
        <div class="project-info">
          <section class="project-info-section project-intro">
            <h3>项目介绍</h3>
            <p>飞翔云管理系统</p>
            <p>Spring Cloud 微服务架构</p>
            <p>前后端分离</p>
          </section>

          <section class="project-info-section project-stack">
            <h3>技术栈</h3>
            <p>Spring Boot 3</p>
            <p>Spring Cloud</p>
            <p>Vue 3 + Element Plus</p>
          </section>

          <section class="project-info-section project-repo">
            <h3 style="margin-bottom: 8px;">开源地址</h3>
            <p class="project-repo-link">
              <Icon icon="akar-icons:github-fill" :size="20" />
              <span>https://github.com/15521142480/flycloud</span>
            </p>
            <el-button type="primary" text bg @click="routerForward(9)" style="margin-top: 8px">
              前往仓库 →
            </el-button>
          </section>
        </div>
      </el-card>
    </el-col>
  </el-row>

</template>
<script lang="ts" setup>
import { getFilePreviewUrl } from '@/components/UploadFile/src/useUpload'
import { useUserStore } from '@/store/modules/user'
import type { WorkplaceTotal } from './types'
import { createImageViewer } from '@/components/ImageViewer'
import * as TaskApi from '@/api/bpm/task'

import avatarImg from '@/assets/imgs/avatar.png'
import img_bpm1 from '@/assets/imgs/bpm/bpm_1.png'
import img_bpm2 from '@/assets/imgs/bpm/bpm_2.png'

const { t } = useI18n()
const { push } = useRouter()
defineOptions({ name: 'Home' })

const userStore = useUserStore()
const loading = ref(true)
const avatar = computed(() => userStore.getUser.avatar || avatarImg)
const username = computed(() => userStore.getUser.name)
const taskList = ref<any[]>([])
const todoDataList = ref<any[]>([])

// 获取统计数
let totalSate = reactive<WorkplaceTotal>({
  project: 0,
  access: 0,
  todo: 0
})

// 初始化数据
const getCount = async () => {
  const data = {
    project: 87,
    access: 5270,
    todo: 10
  }
  await getTaskTodoList()
  data.todo = taskList.value.length
  totalSate = Object.assign(totalSate, data)

  todoDataList.value = []
  for (const taskData of taskList.value) {
    const startUser = taskData.processInstance.startUser
    todoDataList.value.push({
      username: startUser.name + (startUser.realName !== null ? ' (' + startUser.realName + ")" : ''),
      title: '请假申请 (' + taskData.processInstance.name + ')',
      time: taskData.createTime,
    })
  }

}

// 获取待办任务数量
const getTaskTodoList = async () => {
  const queryParams = {
    pageNum: 1,
    pageSize: 100
  }

  try {
    const data = await TaskApi.getTaskTodoPage(queryParams)
    taskList.value = data.list || []
  } catch (error) {
    console.log('接口请求出错（TaskApi.getTaskTodoPage）：' + error)
  }
}

// 首页项目卡片快捷跳转
const routerForward = async (optionType: number) => {
  switch (optionType) {
    case 1:
      await push({ path: '/bpm/audit/create' })
      break
    case 2:
      await push({ path: '/mall/product/product' })
      break
    case 3:
      window.open('https://www.laixueshi.cn/mall-app/', '_blank')
      break
    case 4:
      await push({ path: '/im/workbench/conversation' })
      break
    case 5:
      window.open('/#/im/home/conversation', '_blank')
      break
    case 9:
      window.open('https://github.com/15521142480/flycloud', '_blank')
      break
  }
}

// 跳转到待办任务页面
const goTodo = async () => {
  await push({ path: '/bpm/audit/todo' })
}

// 预览首页项目卡片图片
const imagePreview = (imgUrl: string) => {
  createImageViewer({
    urlList: [imgUrl]
  })
}

// 获取全部接口api
const getAllApi = async () => {
  await Promise.all([
    getCount()
  ])
  loading.value = false
}

// 初始化获取全部接口api
getAllApi()

</script>


<style lang="scss" scoped>
.home-overview-card :deep(.el-card__body) {
  padding: 0;
}

.home-overview {
  display: grid;
  grid-template-columns: minmax(0, 40%) minmax(120px, 10%) minmax(0, 50%);
  min-height: 120px;
}

.welcome-panel,
.todo-summary,
.todo-list-panel {
  min-width: 0;
}

.welcome-panel {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 19px 30px 18px;
}

.welcome-main {
  display: flex;
  align-items: center;
  min-width: 0;
}

.welcome-avatar {
  flex: 0 0 auto;
  margin-right: 18px;
}

.welcome-copy {
  min-width: 0;
}

.welcome-title {
  overflow: hidden;
  color: #303133;
  font-size: 20px;
  line-height: 28px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.welcome-weather {
  margin-top: 10px;
  color: #909399;
  font-size: 14px;
  line-height: 20px;
}

.welcome-stats {
  display: flex;
  gap: 28px;
  padding-left: 76px;
  margin-top: 6px;
  color: #606266;
}

.welcome-stat {
  display: inline-flex;
  align-items: baseline;
  gap: 8px;
  font-size: 14px;
}

.welcome-stat-count {
  color: #303133;
  font-size: 20px;
}

.todo-summary {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.todo-summary::before {
  position: absolute;
  top: 30%;
  bottom: 30%;
  left: 0;
  width: 1px;
  content: '';
  background-color: var(--el-border-color-lighter);
}

.todo-summary-title {
  color: #606266;
  font-size: 17px;
  font-weight: 500;
  line-height: 24px;
}

.todo-summary-count {
  margin-top: 14px;
  color: #303133;
  font-size: 26px;
  line-height: 32px;
}

.todo-list-panel {
  display: flex;
  align-items: flex-start;
  padding: 11px 20px 11px 0;
}

.todo-list-panel.is-single-row {
  align-items: center;
}

.todo-action {
  padding: 0;
}

.todo-table {
  width: 100%;
  font-size: 15px;
}

.todo-table :deep(.el-table__cell) {
  padding: 7px 0;
  color: #606266;
}

.todo-table :deep(.el-table__row .el-table__cell:first-child) {
  color: #303133;
}

.todo-table :deep(.el-table__inner-wrapper::before) {
  display: none;
}

.todo-table :deep(.el-scrollbar__bar.is-horizontal) {
  display: none;
}

.project-card {
  height: calc(100vh - 440px);
  display: flex;
  flex-direction: column;
}

.project-card :deep(.el-card__header),
.project-card :deep(.el-card__footer) {
  flex-shrink: 0;
}

.project-card :deep(.el-card__body) {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

.card-header, .card-footer {
  text-align: center;
  font-size: 19px;
  //font-weight: 500;
}
.card-img {
  cursor: pointer;
}

.project-info-card :deep(.el-card__body) {
  padding: 14px 30px 12px;
}

.project-info {
  display: grid;
  grid-template-columns: minmax(0, 33%) minmax(0, 33%) minmax(0, 34%);
  min-height: 80px;
}

.project-info-section {
  position: relative;
  min-width: 0;
  padding: 0 34px;
}

.project-info-section::before {
  position: absolute;
  top: 30%;
  bottom: 30%;
  left: 0;
  width: 1px;
  content: '';
  background-color: var(--el-border-color-lighter);
}

.project-info-section h3 {
  margin: 0 0 4px;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
  line-height: 20px;
}

.project-info-section p {
  margin: 0;
  overflow: hidden;
  color: #606266;
  font-size: 14px;
  line-height: 24px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.project-intro {
  padding-left: 34px;
}

.project-repo {
  padding-right: 20px;
}

.project-repo .el-button {
  margin-top: 4px;
  margin-left: 0;
}

.project-repo-link {
  display: flex;
  align-items: center;
  gap: 8px;
}

.project-repo-link span {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
}

@media (max-width: 992px) {
  .home-overview {
    grid-template-columns: 1fr;
  }

  .todo-summary {
    min-height: 96px;
    border-top: 0;
  }

  .todo-summary::before {
    top: 0;
    right: 25%;
    bottom: auto;
    left: 25%;
    width: auto;
    height: 1px;
  }

  .welcome-stats {
    padding-left: 0;
    margin-top: 18px;
  }

  .project-info {
    grid-template-columns: 1fr;
    gap: 22px;
  }

  .project-info-section {
    padding: 0;
  }

  .project-info-section + .project-info-section::before {
    display: none;
  }
}

</style>
