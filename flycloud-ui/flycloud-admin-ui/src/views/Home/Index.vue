<template>
  <div>
    <el-card shadow="never">
      <el-skeleton :loading="loading" animated>

        <el-row :gutter="16" justify="space-between">
          <el-col :xl="12" :lg="12" :md="12" :sm="24" :xs="24">
            <div class="flex items-center">
              <el-avatar :src="getFilePreviewUrl(avatar)" :size="70" class="mr-16px">
                <img src="@/assets/imgs/profile.png" alt="" />
              </el-avatar>
              <div>
                <div class="text-20px">
                  {{ t('workplace.welcome') }} {{ username }} {{ t('workplace.happyDay') }}
                </div>
                <div class="mt-10px text-14px text-gray-500">
                  {{ t('workplace.toady') }}，23℃ - 32℃！
                </div>
              </div>
            </div>
          </el-col>
          <el-col :xl="12" :lg="12" :md="12" :sm="24" :xs="24">
            <div class="h-70px flex items-center justify-end lt-sm:mt-10px">
              <div class="px-8px text-right">
                <div class="mb-16px text-14px text-gray-400">{{ t('workplace.project') }}</div>
                <CountTo
                  class="text-20px"
                  :start-val="0"
                  :end-val="totalSate.project"
                  :duration="2600"
                />
              </div>
              <el-divider direction="vertical" />
              <div class="px-8px text-right">
                <div class="mb-16px text-14px text-gray-400">{{ t('workplace.toDo') }}</div>
                <CountTo
                  class="text-20px"
                  :start-val="0"
                  :end-val="totalSate.todo"
                  :duration="2600"
                />
              </div>
              <el-divider direction="vertical" border-style="dashed" />
              <div class="px-8px text-right">
                <div class="mb-16px text-14px text-gray-400">{{ t('workplace.access') }}</div>
                <CountTo
                  class="text-20px"
                  :start-val="0"
                  :end-val="totalSate.access"
                  :duration="2600"
                />
              </div>
            </div>
          </el-col>
        </el-row>

      </el-skeleton>
    </el-card>
  </div>

  <el-row style="margin-top: 15px">

    <!-- 工作流引擎 -->
    <el-col :span="6">
      <el-card shadow="never" class="project-card">
        <template #header>
          <div class="card-header">
            <span>工作流引擎</span>
          </div>
        </template>
        <div ref="chartRef" v-loading="loading"></div>
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
      <el-card shadow="never" class="project-card">
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
      <el-card shadow="never" class="project-card">
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
      <el-card shadow="never" class="project-card">
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

</template>
<script lang="ts" setup>
import { getFilePreviewUrl } from '@/components/UploadFile/src/useUpload'
// import { set } from 'lodash-es'
const { push } = useRouter()

import { useUserStore } from '@/store/modules/user'
import type { WorkplaceTotal } from './types'
import avatarImg from '@/assets/imgs/avatar.png'
const { t } = useI18n()
defineOptions({ name: 'Home' })
const userStore = useUserStore()
const loading = ref(true)
const avatar = computed(() => userStore.getUser.avatar || avatarImg)
const username = userStore.getUser.name


// 获取统计数
let totalSate = reactive<WorkplaceTotal>({
  project: 0,
  access: 0,
  todo: 0
})

//
const getCount = async () => {
  const data = {
    project: 40,
    access: 2340,
    todo: 10
  }
  totalSate = Object.assign(totalSate, data)
}

//
const routerForward = async (optionType : number) => {
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
  }

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

.project-card {
  height: calc(100vh - 400px);
  display: flex;
  flex-direction: column;
}
.project-card :deep(.el-card__body) {
  flex: 1;
}

.card-header, .card-footer {
  text-align: center;
  font-size: 19px;
  //font-weight: 500;
}

</style>
