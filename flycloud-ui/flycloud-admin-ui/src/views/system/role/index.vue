<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="68px"
    >
      <el-form-item :label="t('auto.views.system.role.index.k3aa1f085')" prop="name">
        <el-input
          v-model="queryParams.name"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.system.role.index.kb7c17b9e')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.role.index.k07f826b6')" prop="code">
        <el-input
          v-model="queryParams.code"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.system.role.index.kc22e9173')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.system.role.index.kdba277df')"
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
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
          :end-placeholder="t('auto.views.system.role.index.kf4b9b2b5')"
          :start-placeholder="t('auto.views.system.role.index.k1f291968')"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k67d60983') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.kac9b7228') }}
        </el-button>
        <el-button
          v-hasPermi="['sys:role:saveOrUpdate']"
          plain
          type="primary"
          @click="openForm('create')"
        >
          <Icon class="mr-5px" icon="ep:plus" />
          {{ t('extra.ka4c9936b') }}
        </el-button>
        <el-button
          v-hasPermi="['sys:role:download']"
          :loading="exportLoading"
          plain
          type="success"
          @click="handleExport"
        >
          <Icon class="mr-5px" icon="ep:download" />
          {{ t('extra.k82cb7a76') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" height="calc(100vh - 355px)">
      <el-table-column
        align="center"
        :label="t('auto.views.system.role.index.k7c187097')"
        prop="id"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.system.role.index.k3aa1f085')"
        prop="name"
        width="170"
      />
      <el-table-column
        :label="t('auto.views.system.role.index.kcb232261')"
        align="center"
        prop="type"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('auto.views.system.role.RoleAssignMenuForm.k07f826b6')"
        prop="code"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.system.role.RoleForm.k22efafba')"
        prop="sort"
        width="90"
      />
      <el-table-column align="center" :label="t('common.remark')" prop="remark" />
      <el-table-column align="center" :label="t('common.status')" prop="status" width="100">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('common.createTime')"
        prop="createTime"
        width="120"
      />
      <el-table-column :width="200" align="center" :label="t('common.operation')">
        <template #default="scope">
          <el-button
            v-hasPermi="['sys:role:saveOrUpdate']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['sys:role:menuPermission']"
            link
            preIcon="ep:basketball"
            :title="t('system.role.menuPermissionTitle')"
            type="primary"
            @click="openAssignMenuForm(scope.row)"
          >
            {{ t('system.role.menuPermissionTitle') }}
          </el-button>
          <el-button
            v-hasPermi="['sys:role:dataPermission']"
            link
            preIcon="ep:coin"
            :title="t('auto.views.system.role.RoleDataPermissionForm.kf3d1827b')"
            type="primary"
            @click="openDataPermissionForm(scope.row)"
          >
            {{ t('auto.views.system.role.RoleDataPermissionForm.kf3d1827b') }}
          </el-button>
          <el-button
            v-hasPermi="['sys:role:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
            {{ t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <RoleForm ref="formRef" @success="getList" />
  <!-- 表单弹窗：菜单权限 -->
  <RoleAssignMenuForm ref="assignMenuFormRef" @success="handleRoleMenuChanged" />
  <!-- 表单弹窗：数据权限 -->
  <RoleDataPermissionForm ref="dataPermissionFormRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as RoleApi from '@/api/system/role'
import RoleForm from './RoleForm.vue'
import RoleAssignMenuForm from './RoleAssignMenuForm.vue'
import RoleDataPermissionForm from './RoleDataPermissionForm.vue'
import { refreshCurrentUserAuthorization } from '@/utils/authorization'
import * as PermissionApi from '@/api/system/permission'
import { useUserStore } from '@/store/modules/user'
const { t } = useI18n()
defineOptions({ name: 'SystemRole' })

const message = useMessage() // 消息弹窗
const userStore = useUserStore()

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  code: '',
  name: '',
  status: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询角色列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await RoleApi.getRolePage(queryParams)
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
const openForm = (type: string, id?: string) => {
  formRef.value.open(type, id)
}

/** 数据权限操作 */
const dataPermissionFormRef = ref()
const openDataPermissionForm = async (row: RoleApi.RoleVO) => {
  dataPermissionFormRef.value.open(row)
}

/** 菜单权限操作 */
const assignMenuFormRef = ref()
const openAssignMenuForm = async (row: RoleApi.RoleVO) => {
  assignMenuFormRef.value.open(row)
}

/**
 * 角色菜单权限保存后刷新角色列表。
 *
 * <p>通过服务端查询当前登录用户的角色 ID，确保判断结果不受浏览器缓存影响。仅当当前用户
 * 拥有本次修改的角色时，才询问是否立即重新加载菜单和页面权限。</p>
 *
 * @param role 已完成菜单权限修改的角色信息
 */
const handleRoleMenuChanged = async (role: { roleId: string; roleName: string }) => {
  await getList()

  const currentUserRoleIds = await PermissionApi.getUserRoleList(userStore.getUser.id)
  const currentUserHasChangedRole = currentUserRoleIds.some(
    (roleId: string | number) => String(roleId) === role.roleId
  )
  if (!currentUserHasChangedRole) {
    return
  }

  try {
    await message.confirm(
      `检测到当前登录用户拥有“${role.roleName}”角色，该角色的菜单权限已修改。是否立即重新加载菜单导航和页面权限？`,
      '刷新菜单导航'
    )
  } catch {
    return
  }

  await refreshCurrentUserAuthorization()
}

/** 删除按钮操作 */
const handleDelete = async (id: string) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await RoleApi.deleteRole(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await RoleApi.exportRole(queryParams)
    download.excel(data, t('auto.views.system.role.index.k32417c86'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
