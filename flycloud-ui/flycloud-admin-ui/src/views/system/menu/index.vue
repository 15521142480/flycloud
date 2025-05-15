<template>

  <!-- 树型 -->
  <ContentWrap style="height: calc(100vh - 160px)">

    <el-row>
      <el-col :span="12">
        <div style="margin-left: 30px">
          <el-button v-hasPermi="['sys:menu:saveOrUpdate']" type="primary" @click="openForm('create', undefined, 0)">
            新增一级菜单
          </el-button>
        </div>
      </el-col>

      <el-col :span="12">
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          搜索
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
          <el-button v-hasPermi="['sys:menu:saveOrUpdate']" type="primary" :icon="Plus" circle @click="openForm('create', undefined, data.id)" />
          <el-button v-hasPermi="['sys:menu:saveOrUpdate']" type="primary" :icon="Edit" circle  @click="openForm('update', data.id)" />
          <el-switch
            v-model="data.status"
            :disabled="!checkPermi(['sys:menu:enable'])"
            style="margin: 0 10px 0 10px"
            size="large"
            inline-prompt
            :active-value="0"
            :inactive-value="1"
            active-text="已启"
            inactive-text="已禁"
            @change="handleStatusChange(data)"
          />
          <el-button v-hasPermi="['sys:menu:delete']" type="danger" :icon="Delete" circle @click="deleteMenu(data)" />

        </template>
      </el-tree>
    </div>
  </ContentWrap>


  <!-- 表单弹窗：添加/修改 -->
  <MenuForm ref="formRef" @success="getList" />


</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { handleTree } from '@/utils/tree'
import * as MenuApi from '@/api/system/menu'
import {getMenuPageList, getMenuTreeList, MenuVO, updateStatus} from '@/api/system/menu'
import MenuForm from './MenuForm.vue'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
import { CommonStatusEnum } from '@/utils/constants'
import {CirclePlus, Delete, Edit, Plus} from "@element-plus/icons-vue";
import {checkPermi} from "@/utils/permission";

defineOptions({ name: 'SystemMenu' })

const { wsCache } = useCache()
const { t } = useI18n() // 国际化
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
  { label: '一级 1', children: [{ label: '二级 1-1' }] },
  { label: '一级 2', children: [{ label: '二级 2-1' }] }
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
const openForm = (type: string, id?: number, parentId?: number) => {
  formRef.value.open(type, id, parentId)
}

/**
 * 修改状态
 * @param row
 */
const handleStatusChange = async (data: MenuVO) => {
  try {
    // 修改状态的二次确认
    const text = data.status === CommonStatusEnum.ENABLE ? '启用' : '停用'
    await message.confirm('确认要"' + text + '"【' + data.name + '】菜单吗?')
    // 发起修改状态
    await MenuApi.updateStatus(data.id, data.status)
    // 刷新列表
    await getList()
  } catch {
    // 取消后，进行恢复按钮
    data.status =
      data.status === CommonStatusEnum.ENABLE ? CommonStatusEnum.DISABLE : CommonStatusEnum.ENABLE
  }
}

/**
 * 删除菜单
 * @param data
 */
const deleteMenu = async (data: object) => {
  try {
    // 删除的二次确认
    // await message.delConfirm()
    const text = data.children ? '确认删除【' + data.name + '】菜单以及其下所有菜单吗' : '确认删除【' + data.name + '】菜单吗'
    await message.confirm(text)
    // 发起删除
    await MenuApi.deleteMenu(data.id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    handleQuery()
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

/** 刷新菜单缓存按钮操作 */
const refreshMenu = async () => {
  try {
    await message.confirm('即将更新缓存刷新浏览器！', '刷新菜单缓存')
    // 清空，从而触发刷新
    wsCache.delete(CACHE_KEY.USER)
    wsCache.delete(CACHE_KEY.ROLE_ROUTERS)
    // 刷新浏览器
    location.reload()
  } catch {}
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
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
:deep .menu-tree .el-tree-node__content {
  /*  line-height: 50px;*/
  height: 45px;
  font-size: 16px;
  font-weight: bold;
}


</style>
