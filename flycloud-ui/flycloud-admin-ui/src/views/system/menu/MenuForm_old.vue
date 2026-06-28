<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item :label="t('auto.views.system.menu.MenuForm_old.kfafc8d9d')">
        <el-tree-select
          v-model="formData.parentId"
          :data="menuTree"
          :default-expanded-keys="[0]"
          :props="defaultProps"
          check-strictly
          node-key="id"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.menu.MenuForm_old.k2637dede')" prop="name">
        <el-input
          v-model="formData.name"
          clearable
          :placeholder="t('auto.views.system.menu.MenuForm_old.k2bde7388')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.menu.MenuForm_old.k7907c3c5')" prop="type">
        <el-radio-group v-model="formData.type">
          <el-radio-button
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_MENU_TYPE)"
            :key="dict.label"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        v-if="formData.type !== 3"
        :label="t('auto.views.system.menu.MenuForm_old.k4d908302')"
      >
        <IconSelect v-model="formData.icon" clearable />
      </el-form-item>
      <el-form-item
        v-if="formData.type !== 3"
        :label="t('auto.views.system.menu.MenuForm_old.k54faad11')"
        prop="path"
      >
        <template #label>
          <Tooltip
            :message="t('extra.kmenuOldRouteTip')"
            :title="t('auto.views.system.menu.MenuForm_old.k54faad11')"
          />
        </template>
        <el-input
          v-model="formData.path"
          clearable
          :placeholder="t('extra.kmenuOldRoutePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        v-if="formData.type === 2"
        :label="t('extra.kmenuOldComponentPath')"
        prop="component"
      >
        <el-input
          v-model="formData.component"
          clearable
          :placeholder="t('extra.kmenuOldComponentPathPlaceholder')"
        />
      </el-form-item>
      <el-form-item
        v-if="formData.type === 2"
        :label="t('extra.kmenuOldComponentName')"
        prop="componentName"
      >
        <el-input
          v-model="formData.componentName"
          clearable
          :placeholder="t('extra.kmenuOldComponentNamePlaceholder')"
        />
      </el-form-item>
      <el-form-item v-if="formData.type !== 1" :label="t('extra.k4379f002')" prop="permission">
        <template #label>
          <Tooltip
            message="Controller 方法上的权限字符，如：@PreAuthorize(`@ss.hasPermission('system:user:list')`)"
            :title="t('extra.k4379f002')"
          />
        </template>
        <el-input v-model="formData.permission" clearable :placeholder="t('extra.k73497420')" />
      </el-form-item>
      <el-form-item :label="t('system.menu.displaySort')" prop="sort">
        <el-input-number v-model="formData.sort" :min="0" clearable controls-position="right" />
      </el-form-item>
      <el-form-item :label="t('system.menu.menuStatus')" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.label"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        v-if="formData.type !== 3"
        :label="t('system.menu.visibleStatus')"
        prop="visible"
      >
        <template #label>
          <Tooltip
            message="选择隐藏时，路由将不会出现在侧边栏，但仍然可以访问"
            :title="t('system.menu.visibleStatus')"
          />
        </template>
        <el-radio-group v-model="formData.visible">
          <el-radio key="true" :value="true" border>{{ t('system.menu.visible') }}</el-radio>
          <el-radio key="false" :value="false" border>{{ t('system.menu.hidden') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        v-if="formData.type !== 3"
        :label="t('system.menu.alwaysShow')"
        prop="alwaysShow"
      >
        <template #label>
          <Tooltip
            message="选择不是时，当该菜单只有一个子菜单时，不展示自己，直接展示子菜单"
            :title="t('system.menu.alwaysShow')"
          />
        </template>
        <el-radio-group v-model="formData.alwaysShow">
          <el-radio key="true" :value="true" border>{{ t('system.menu.always') }}</el-radio>
          <el-radio key="false" :value="false" border>{{ t('system.menu.notAlways') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        v-if="formData.type === 2"
        :label="t('system.menu.cacheStatus')"
        prop="keepAlive"
      >
        <template #label>
          <Tooltip
            message="选择缓存时，则会被 `keep-alive` 缓存，必须填写「组件名称」字段"
            :title="t('system.menu.cacheStatus')"
          />
        </template>
        <el-radio-group v-model="formData.keepAlive">
          <el-radio key="true" :value="true" border>{{ t('system.menu.cache') }}</el-radio>
          <el-radio key="false" :value="false" border>{{ t('system.menu.noCache') }}</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('extra.k008b8fcb')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.components.AppLinkInput.AppLinkSelectDialog.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as MenuApi from '@/api/system/menu'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
import { CommonStatusEnum, SystemMenuTypeEnum } from '@/utils/constants'
import { defaultProps, handleTree } from '@/utils/tree'
const { t } = useI18n()
defineOptions({ name: 'SystemMenuForm' })

const { wsCache } = useCache()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: '',
  permission: '',
  type: SystemMenuTypeEnum.DIR,
  sort: Number(undefined),
  parentId: 0,
  path: '',
  icon: '',
  component: '',
  componentName: '',
  status: CommonStatusEnum.ENABLE,
  visible: true,
  keepAlive: true,
  alwaysShow: true
})
const formRules = reactive({
  name: [
    { required: true, message: t('auto.views.system.menu.MenuForm_old.k272fb99e'), trigger: 'blur' }
  ],
  sort: [
    { required: true, message: t('auto.views.system.menu.MenuForm_old.k70c49ab5'), trigger: 'blur' }
  ],
  path: [
    { required: true, message: t('auto.views.system.menu.MenuForm_old.kacc757c7'), trigger: 'blur' }
  ],
  status: [
    { required: true, message: t('auto.views.system.menu.MenuForm_old.k1318b551'), trigger: 'blur' }
  ]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number, parentId?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (parentId) {
    formData.value.parentId = parentId
  }
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await MenuApi.getMenu(id)
    } finally {
      formLoading.value = false
    }
  }
  // 获得菜单列表
  await getTree()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    if (
      formData.value.type === SystemMenuTypeEnum.DIR ||
      formData.value.type === SystemMenuTypeEnum.MENU
    ) {
      if (!isExternal(formData.value.path)) {
        if (formData.value.parentId === 0 && formData.value.path.charAt(0) !== '/') {
          message.error(t('auto.views.system.menu.MenuForm_old.k944eff91'))
          return
        } else if (formData.value.parentId !== 0 && formData.value.path.charAt(0) === '/') {
          message.error(t('auto.views.system.menu.MenuForm_old.k53a53a1e'))
          return
        }
      }
    }
    const data = formData.value as unknown as MenuApi.MenuVO
    if (formType.value === 'create') {
      await MenuApi.createMenu(data)
      message.success(t('common.createSuccess'))
    } else {
      await MenuApi.updateMenu(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
    // 清空，从而触发刷新
    wsCache.delete(CACHE_KEY.ROLE_ROUTERS)
  }
}

/** 获取下拉框[上级菜单]的数据  */
const menuTree = ref<Tree[]>([]) // 树形结构
const getTree = async () => {
  menuTree.value = []
  const res = await MenuApi.getMenusList()
  let menu: Tree = { id: 0, name: t('auto.views.system.menu.MenuForm_old.k3eaad510'), children: [] }
  menu.children = handleTree(res)
  menuTree.value.push(menu)
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    name: '',
    permission: '',
    type: SystemMenuTypeEnum.DIR,
    sort: Number(undefined),
    parentId: 0,
    path: '',
    icon: '',
    component: '',
    componentName: '',
    status: CommonStatusEnum.ENABLE,
    visible: true,
    keepAlive: true,
    alwaysShow: true
  }
  formRef.value?.resetFields()
}

/** 判断 path 是不是外部的 HTTP 等链接 */
const isExternal = (path: string) => {
  return /^(https?:|mailto:|tel:)/.test(path)
}
</script>
