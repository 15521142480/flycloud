<template>
  <el-row :gutter="20">
    <!-- 左侧部门树 -->
    <!--    <el-col :span="4" :xs="24">-->
    <!--      <ContentWrap class="h-1/1">-->
    <!--        <DeptTree @node-click="handleDeptNodeClick" />-->
    <!--      </ContentWrap>-->
    <!--    </el-col>-->
    <el-col :span="24" :xs="24">
      <!-- 搜索 -->
      <ContentWrap>
        <el-form
          class="-mb-15px"
          :model="queryParams"
          ref="queryFormRef"
          :inline="true"
          label-width="68px"
        >
          <el-form-item :label="t('system.user.account')" prop="account">
            <el-input
              v-model="queryParams.account"
              :placeholder="t('system.user.accountPlaceholder')"
              clearable
              @keyup.enter="handleQuery"
              class="!w-240px"
            />
          </el-form-item>
          <el-form-item :label="t('system.user.phone')" prop="telephone">
            <el-input
              v-model="queryParams.telephone"
              :placeholder="t('system.user.phonePlaceholder')"
              clearable
              @keyup.enter="handleQuery"
              class="!w-240px"
            />
          </el-form-item>
          <el-form-item :label="t('common.status')" prop="status">
            <el-select
              v-model="queryParams.status"
              :placeholder="t('system.user.statusPlaceholder')"
              clearable
              class="!w-240px"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
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
              type="datetimerange"
              :start-placeholder="t('common.startTimeText')"
              :end-placeholder="t('common.endTimeText')"
              class="!w-240px"
            />
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery"
              ><Icon icon="ep:search" />{{ t('common.search') }}</el-button
            >
            <el-button @click="resetQuery"
              ><Icon icon="ep:refresh" />{{ t('common.reset') }}</el-button
            >
            <el-button
              type="primary"
              plain
              @click="openForm('create')"
              v-hasPermi="['sys:user:saveOrUpdate']"
            >
              <Icon icon="ep:plus" /> {{ t('action.create') }}
            </el-button>
            <el-button
              type="warning"
              plain
              @click="handleImport"
              v-hasPermi="['sys:user:download']"
            >
              <Icon icon="ep:upload" /> {{ t('action.import') }}
            </el-button>
            <el-button
              type="success"
              plain
              @click="handleExport"
              :loading="exportLoading"
              v-hasPermi="['system:user:export']"
            >
              <Icon icon="ep:download" />{{ t('action.export') }}
            </el-button>
          </el-form-item>
        </el-form>
      </ContentWrap>

      <ContentWrap>
        <el-table v-loading="loading" :data="list" height="calc(100vh - 355px)">
          <el-table-column :label="t('system.user.id')" align="center" key="id" prop="id" />
          <el-table-column
            :label="t('system.user.account')"
            align="center"
            prop="account"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            :label="t('system.user.nickname')"
            align="center"
            prop="name"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            :label="t('system.user.role')"
            align="center"
            key="roleNames"
            prop="roleNames"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            :label="t('system.user.dept')"
            align="center"
            key="deptName"
            prop="deptName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            :label="t('system.user.phone')"
            align="center"
            prop="telephone"
            width="120"
          />
          <el-table-column :label="t('common.status')" key="status" width="100">
            <template #default="scope">
              <el-switch
                v-model="scope.row.status"
                :disabled="!checkPermi(['sys:user:enable'])"
                :active-value="0"
                :inactive-value="1"
                @change="handleStatusChange(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column
            :label="t('common.createTime')"
            align="center"
            prop="createTime"
            :formatter="dateFormatter"
            width="180"
          />

          <el-table-column :label="t('common.operation')" align="center" width="220" fixed="right">
            <template #default="scope">
              <el-button
                type="primary"
                link
                @click="openForm('update', scope.row.id)"
                v-hasPermi="['sys:user:saveOrUpdate']"
              >
                <Icon icon="ep:edit" />{{ t('action.update') }}
              </el-button>

              <el-button
                type="primary"
                link
                @click="handleRole(scope.row)"
                v-hasPermi="['sys:user:assignRole']"
              >
                <Icon icon="ep:circle-check" />{{ t('system.user.assignRole') }}
              </el-button>

              <el-button
                type="warning"
                link
                @click="handleResetPwd(scope.row)"
                v-hasPermi="['sys:user:restartPassword']"
              >
                <Icon icon="ep:key" />{{ t('system.user.resetPassword') }}
              </el-button>

              <el-button
                type="danger"
                link
                @click="handleDelete(scope.row)"
                v-hasPermi="['sys:user:delete']"
              >
                <Icon icon="ep:delete" />{{ t('action.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <Pagination
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </ContentWrap>
    </el-col>
  </el-row>

  <!-- 添加或修改用户对话框 -->
  <UserForm ref="formRef" @success="getList" />
  <!-- 用户导入对话框 -->
  <UserImportForm ref="importFormRef" @success="getList" />
  <!-- 分配角色 -->
  <UserAssignRoleForm ref="assignRoleFormRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { checkPermi } from '@/utils/permission'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { CommonStatusEnum } from '@/utils/constants'
import * as UserApi from '@/api/system/user'
import UserForm from './UserForm.vue'
import UserImportForm from './UserImportForm.vue'
import UserAssignRoleForm from './UserAssignRoleForm.vue'
import DeptTree from './DeptTree.vue'

defineOptions({ name: 'SystemUser' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  account: undefined,
  telephone: undefined,
  status: undefined,
  deptId: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await UserApi.getUserPage(queryParams)
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

/** 处理部门被点击 */
const handleDeptNodeClick = async (row) => {
  queryParams.deptId = row.id
  await getList()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 用户导入 */
const importFormRef = ref()
const handleImport = () => {
  importFormRef.value.open()
}

/** 修改用户状态 */
const handleStatusChange = async (row: UserApi.UserVO) => {
  try {
    // 修改状态的二次确认
    const text =
      row.status === CommonStatusEnum.ENABLE
        ? t('system.user.enableAction')
        : t('system.user.disableAction')
    await message.confirm(t('system.user.statusConfirm', { action: text, account: row.account }))
    // 发起修改状态
    await UserApi.updateUserStatus(row.id, row.status)
    // 刷新列表
    await getList()
  } catch {
    // 取消后，进行恢复按钮
    row.status =
      row.status === CommonStatusEnum.ENABLE ? CommonStatusEnum.DISABLE : CommonStatusEnum.ENABLE
  }
}

/** 导出按钮操作 */
const exportLoading = ref(false)
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await UserApi.exportUser(queryParams)
    download.excel(data, t('system.user.exportFileName'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 操作分发 */
const handleCommand = (command: string, row: UserApi.UserVO) => {
  switch (command) {
    case 'handleDelete':
      handleDelete(row.id)
      break
    case 'handleResetPwd':
      handleResetPwd(row)
      break
    case 'handleRole':
      handleRole(row)
      break
    default:
      break
  }
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await UserApi.deleteUser(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 重置密码 */
const handleResetPwd = async (row: UserApi.UserVO) => {
  try {
    // 重置的二次确认
    const result = await message.prompt(
      t('system.user.resetPasswordPrompt', { account: row.account }),
      t('common.reminder')
    )
    const password = result.value
    // 发起重置
    await UserApi.resetUserPwd(row.id, password)
    message.success(t('system.user.resetPasswordSuccess', { password }))
  } catch {}
}

/** 分配角色 */
const assignRoleFormRef = ref()
const handleRole = (row: UserApi.UserVO) => {
  assignRoleFormRef.value.open(row)
}

/** 初始化 */
onMounted(() => {
  getList()
})
</script>
