<template>
  <!-- 树型 -->
  <ContentWrap style="height: calc(100vh - 160px)">
    <el-row>
      <el-col :span="12">
        <div style="margin-left: 30px">
          <el-button
            v-hasPermi="['sys:menu:saveOrUpdate']"
            type="primary"
            @click="openForm('create', undefined, 0)"
          >
            {{ t('extra.k91f7e670') }}
          </el-button>
        </div>
      </el-col>

      <el-col :span="12">
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k0f82221e') }}
        </el-button>
      </el-col>
    </el-row>

    <el-divider style="margin: 10px 0" />
    <div>
      <el-tree
        class="menu-tree"
        style="height: calc(100vh - 240px); overflow-y: scroll"
        :data="list"
        :props="props"
        v-loading="loading"
      >
        <template #default="{ node, data }">
          <Icon :icon="data.icon" style="margin: 0 12px 0 8px" />
          <span>{{ node.label }}</span>

          <span style="margin-left: 50%"></span>
          <el-button
            v-hasPermi="['sys:menu:saveOrUpdate']"
            type="primary"
            :icon="Plus"
            circle
            @click="openForm('create', undefined, data.id)"
          />
          <el-button
            v-hasPermi="['sys:menu:saveOrUpdate']"
            type="primary"
            :icon="Edit"
            circle
            @click="openForm('update', data.id)"
          />
          <el-switch
            v-model="data.status"
            :disabled="!checkPermi(['sys:menu:enable'])"
            style="margin: 0 10px 0 10px"
            size="large"
            inline-prompt
            :active-value="0"
            :inactive-value="1"
            :active-text="t('extra.kf5e3b8ac')"
            :inactive-text="t('extra.k1a41240c')"
            @change="handleStatusChange(data)"
          />
          <el-button
            v-hasPermi="['sys:menu:delete']"
            type="danger"
            :icon="Delete"
            circle
            @click="deleteMenu(data)"
          />
        </template>
      </el-tree>
    </div>
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <MenuForm ref="formRef" @success="handleMenuChanged" />
</template>

<script lang="ts" setup>
// @ts-nocheck
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { handleTree } from '@/utils/tree'
import * as MenuApi from '@/api/system/menu'
import { getMenuPageList, getMenuTreeList, MenuVO, updateStatus } from '@/api/system/menu'
import MenuForm from './MenuForm.vue'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
import { CommonStatusEnum } from '@/utils/constants'
import { CirclePlus, Delete, Edit, Plus } from '@element-plus/icons-vue'
import { checkPermi } from '@/utils/permission'
import { refreshCurrentUserAuthorization } from '@/utils/authorization'
const { t } = useI18n()
defineOptions({ name: 'SystemMenu' })

const { wsCache } = useCache()
const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<any>([]) // 列表的数据
const queryParams = reactive({
  name: undefined,
  status: undefined,
  searchStatusAll: true
})
const queryFormRef = ref() // 搜索的表单
const isExpandAll = ref(false) // 是否展开，默认全部折叠
const refreshTable = ref(true) // 重新渲染表格状态

const treeHeight = ref(0)
const props = {
  value: 'id',
  label: 'name',
  children: 'children'
}

const treeData = ref([
  {
    label: t('auto.views.system.menu.index.kf6aecd5f'),
    children: [{ label: t('auto.views.system.menu.index.k52adfdba') }]
  },
  {
    label: t('auto.views.system.menu.index.k0bb97c90'),
    children: [{ label: t('auto.views.system.menu.index.k389b0adf') }]
  }
])

/** 查询列表 */
const getList = async () => {
  loading.value = true
  treeHeight.value = window.innerHeight
  try {
    const data = await MenuApi.getMenuTreeList(queryParams)
    list.value = data
    // list.value = handleTree(data)
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: string, parentId?: string) => {
  formRef.value.open(type, id, parentId)
}

/**
 * 修改状态
 * @param row
 */
const handleStatusChange = async (data: MenuVO) => {
  try {
    // 修改状态的二次确认
    const text =
      data.status === CommonStatusEnum.ENABLE ? t('common.enabled') : t('common.disabled')
    await message.confirm(
      t('auto.views.system.menu.index.ka4db1fb1') +
        text +
        '"【' +
        data.name +
        t('auto.views.system.menu.index.k057bc7b9')
    )
    // 发起修改状态
    await MenuApi.updateStatus(data.id, data.status)
    // 刷新列表
    await getList()
  } catch {
    // 取消后，进行恢复按钮
    data.status =
      data.status === CommonStatusEnum.ENABLE ? CommonStatusEnum.DISABLE : CommonStatusEnum.ENABLE
    return
  }
  await refreshCurrentUserAuthorization()
}

/**
 * 删除菜单
 * @param data
 */
const deleteMenu = async (data: object) => {
  try {
    // 删除的二次确认
    // await message.delConfirm()
    const text = data.children
      ? t('auto.views.system.menu.index.k8fb3140d') +
        data.name +
        t('auto.views.system.menu.index.ke8421874')
      : t('auto.views.system.menu.index.k8fb3140d') +
        data.name +
        t('auto.views.system.menu.index.k8be4905e')
    await message.confirm(text)
    // 发起删除
    await MenuApi.deleteMenu(data.id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await handleMenuChanged()
  } catch {}
}

/** 展开/折叠操作 */
const toggleExpandAll = () => {
  refreshTable.value = false
  isExpandAll.value = !isExpandAll.value
  nextTick(() => {
    refreshTable.value = true
  })
}

/**
 * 菜单新增、修改、删除后刷新菜单树，并重新获取当前登录用户的导航权限。
 */
const handleMenuChanged = async () => {
  await getList()
  await refreshCurrentUserAuthorization()
}

/** 刷新菜单缓存按钮操作 */
const refreshMenu = async () => {
  try {
    await message.confirm(
      t('auto.views.system.menu.index.k42ce1004'),
      t('auto.views.system.menu.index.k9476c72d')
    )
    // 清空，从而触发刷新
    wsCache.delete(CACHE_KEY.USER)
    wsCache.delete(CACHE_KEY.ROLE_ROUTERS)
    // 刷新浏览器
    location.reload()
  } catch {}
}

/** 删除按钮操作 */
const handleDelete = async (id: string) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await MenuApi.deleteMenu(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 开启/关闭菜单的状态 */
const menuStatusUpdating = ref({}) // 菜单状态更新中的 menu 映射。key：菜单编号，value：是否更新中
// const handleStatusChanged = async (menu: MenuVO, val: number) => {
//   // 1. 标记 menu.id 更新中
//   menuStatusUpdating.value[menu.id] = true
//   try {
//     // 2. 发起更新状态
//     menu.status = val
//     await MenuApi.updateMenu(menu)
//   } finally {
//     // 3. 标记 menu.id 更新完成
//     menuStatusUpdating.value[menu.id] = false
//   }
// }

/** 初始化 **/
onMounted(() => {
  getList()
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
