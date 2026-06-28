<template>
  <ElDialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="80%"
    top="4vh"
    style="height: 94%; margin-bottom: -80px"
  >
    <el-divider content-position="right" style="margin: -5px 0 20px 0">
      <el-button :disabled="formLoading" type="primary" round @click="submitForm">{{
        t('auto.views.system.menu.MenuForm.k31f9d856')
      }}</el-button>
    </el-divider>

    <el-row style="margin-left: 15px">
      <el-col :span="14">
        <el-card>
          <template #header>
            <div class="card-header" style="text-align: center">
              <span>{{ t('auto.views.system.menu.MenuForm.k41654e02') }}</span>
            </div>
          </template>
          <el-form
            ref="formRef"
            v-loading="formLoading"
            :model="formData"
            :rules="formRules"
            label-width="100px"
            style="height: calc(100vh - 240px); overflow-y: auto; overflow-x: hidden"
          >
            <el-form-item :label="t('system.menu.parent')">
              <el-tree-select
                v-model="formData.parentId"
                :data="menuTree"
                :default-expanded-keys="[0]"
                :props="defaultProps"
                check-strictly
                node-key="id"
              />
            </el-form-item>
            <el-form-item :label="t('system.menu.name')" prop="name">
              <el-input
                v-model="formData.name"
                clearable
                :placeholder="t('auto.views.mp.menu.components.MenuEditor.k2bde7388')"
              />
            </el-form-item>
            <el-form-item :label="t('system.menu.type')" prop="type">
              <el-radio-group v-model="formData.type">
                <el-radio-button
                  v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_TYPE)"
                  :key="dict.label"
                  :value="dict.value"
                >
                  {{ dict.label }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="t('system.menu.icon')">
              <IconSelect v-model="formData.icon" clearable />
            </el-form-item>
            <el-form-item :label="t('system.role.menuPermissionTitle')" prop="permission">
              <template #label>
                <Tooltip
                  message="访问的菜单权限，如：一级菜单【系统管理】为 sys，其下的二级菜单【用户管理】为 sys:user，如其下还要有第三级则为 sys:user:xxx,与此类推（符号为英文冒号）"
                  :title="t('system.role.menuPermissionTitle')"
                />
              </template>
              <el-input
                v-model="formData.permission"
                clearable
                :placeholder="t('extra.kd2d302a3')"
              />
            </el-form-item>
            <el-form-item :label="t('system.menu.path')" prop="path">
              <template #label>
                <Tooltip
                  message="访问的路由地址，如：`user`。如需外网地址时，则以 `http(s)://` 开头"
                  :title="t('system.menu.path')"
                />
              </template>
              <el-input
                v-model="formData.path"
                clearable
                :placeholder="t('extra.kmenuOldRoutePlaceholder')"
              />
            </el-form-item>
            <el-form-item :label="t('system.menu.component')" prop="component">
              <el-input
                v-model="formData.component"
                clearable
                :placeholder="t('extra.kmenuOldComponentPathPlaceholder')"
              />
            </el-form-item>
            <el-form-item :label="t('system.menu.componentName')" prop="componentName">
              <el-input
                v-model="formData.componentName"
                clearable
                :placeholder="t('extra.kmenuOldComponentNamePlaceholder')"
              />
            </el-form-item>
            <!--            <el-form-item label="权限标识" prop="permission">-->
            <!--              <template #label>-->
            <!--                <Tooltip-->
            <!--                  message="Controller 方法上的权限字符，如：@PreAuthorize(`@ss.hasPermission('system:user:list')`)"-->
            <!--                  title="权限标识"-->
            <!--                />-->
            <!--              </template>-->
            <!--              <el-input v-model="formData.permission" clearable placeholder="请输入权限标识" />-->
            <!--            </el-form-item>-->
            <el-form-item :label="t('system.menu.displaySort')" prop="sort">
              <el-input-number
                v-model="formData.sort"
                :min="0"
                clearable
                controls-position="right"
              />
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
            <el-form-item :label="t('system.menu.visibleStatus')" prop="visible">
              <template #label>
                <Tooltip
                  message="选择隐藏时，路由将不会出现在侧边栏，但仍然可以访问"
                  :title="t('system.menu.visibleStatus')"
                />
              </template>
              <el-radio-group v-model="formData.visible">
                <el-radio key="true" :value="1" border>{{ t('system.menu.visible') }}</el-radio>
                <el-radio key="false" :value="0" border>{{ t('system.menu.hidden') }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="t('system.menu.alwaysShow')" prop="alwaysShow">
              <template #label>
                <Tooltip
                  message="选择不是时，当该菜单只有一个子菜单时，不展示自己，直接展示子菜单"
                  :title="t('system.menu.alwaysShow')"
                />
              </template>
              <el-radio-group v-model="formData.alwaysShow">
                <el-radio key="true" :value="1" border>{{ t('system.menu.always') }}</el-radio>
                <el-radio key="false" :value="0" border>{{ t('system.menu.notAlways') }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="t('system.menu.cacheStatus')" prop="keepAlive">
              <template #label>
                <Tooltip
                  message="选择缓存时，则会被 `keep-alive` 缓存，必须填写「组件名称」字段"
                  :title="t('system.menu.cacheStatus')"
                />
              </template>
              <el-radio-group v-model="formData.keepAlive">
                <el-radio key="true" :value="1" border>{{ t('system.menu.cache') }}</el-radio>
                <el-radio key="false" :value="0" border>{{ t('system.menu.noCache') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <el-col :span="9" style="margin-left: 20px">
        <el-card>
          <template #header>
            <div class="card-header" style="text-align: center">
              <span>{{ t('system.menu.buttonPermission') }}</span>
            </div>
          </template>

          <!--          <div style="height: calc(100vh - 240px); overflow-y:auto; overflow-x:hidden;">-->
          <!--            <div>123</div>-->
          <!--            <div>123</div>-->
          <!--          </div>-->
          <el-button type="primary" plain @click="openBtnForm(false)">
            <!-- v-hasPermi="['system:menu:create']" -->
            <Icon icon="ep:plus" /> {{ t('common.create') }}
          </el-button>
          <el-table
            :data="buttonPermissionList"
            style="width: 100%; height: calc(100vh - 240px); overflow-y: auto; overflow-x: hidden"
          >
            <el-table-column
              fixed
              align="center"
              prop="btnName"
              :label="
                t(
                  'auto.components.bpmnProcessDesigner.package.designer.plugins.translate.k1be7ae4f'
                )
              "
            />
            <el-table-column
              align="center"
              prop="btnPermission"
              :label="t('auto.views.system.oauth2.client.ClientForm.k560165a6')"
            />

            <el-table-column
              fixed="right"
              align="center"
              :label="t('common.operation')"
              min-width="100"
            >
              <template #default="scope">
                <el-button
                  link
                  type="primary"
                  size="small"
                  @click.prevent="openBtnForm(true, scope.row, scope.$index)"
                >
                  {{ t('extra.k4c512392') }}
                </el-button>
                <el-button
                  link
                  type="primary"
                  size="small"
                  @click.prevent="deleteRow(scope.$index)"
                >
                  {{ t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!--  按钮权限弹窗  -->
    <Dialog v-model="btnDialogVisible" :title="btnDialogTitle" width="30%">
      <el-form ref="btnFormRef" :model="btnFormData">
        <el-form-item :label="t('system.menu.buttonName')">
          <el-input v-model="btnFormData.btnName" />
        </el-form-item>
        <el-form-item :label="t('system.menu.buttonPermission')">
          <el-input v-model="btnFormData.btnPermission" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="btnSubmitForm">{{ t('extra.k008b8fcb') }}</el-button>
        <el-button @click="btnDialogVisible = false">{{
          t('auto.components.AppLinkInput.AppLinkSelectDialog.kd54aeadc')
        }}</el-button>
      </template>
    </Dialog>

    <!--    <template #footer>-->
    <!--      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>-->
    <!--      <el-button @click="dialogVisible = false">取 消</el-button>-->
    <!--    </template>-->
  </ElDialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as MenuApi from '@/api/system/menu'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
import { CommonStatusEnum, SystemMenuTypeEnum, SystemTypeEnum } from '@/utils/constants'
import { defaultProps, handleTree } from '@/utils/tree'
const { t } = useI18n()
defineOptions({ name: 'SystemMenuForm' })

const { wsCache } = useCache()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const btnDialogVisible = ref(false) // 按钮弹窗的是否展示
const btnSaveTypeIsUpdate = ref(false) // 按钮保存类型是否修改
const btnSaveUpdateIndex = ref(false) // 按钮保存为修改时操作的下标
const dialogTitle = ref('') // 弹窗的标题
const btnDialogTitle = ref('') // 按钮弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: '',
  permission: '',
  buttonPermission: '',
  type: SystemTypeEnum.TYPE_0,
  sort: Number(undefined),
  parentId: 0,
  path: '',
  icon: '',
  component: '',
  componentName: '',
  status: CommonStatusEnum.ENABLE,
  visible: 1,
  keepAlive: 1,
  alwaysShow: 1
})
const btnFormData = ref({
  btnName: '',
  btnPermission: ''
})
const formRules = reactive({
  name: [
    { required: true, message: t('auto.views.system.menu.MenuForm.k272fb99e'), trigger: 'blur' }
  ],
  sort: [
    { required: true, message: t('auto.views.system.menu.MenuForm.k70c49ab5'), trigger: 'blur' }
  ],
  path: [
    { required: true, message: t('auto.views.system.menu.MenuForm.kacc757c7'), trigger: 'blur' }
  ],
  status: [
    { required: true, message: t('auto.views.system.menu.MenuForm.k1318b551'), trigger: 'blur' }
  ],
  permission: [
    { required: true, message: t('auto.views.system.menu.MenuForm.k3f381f7f'), trigger: 'blur' }
  ]
})

const formRef = ref() // 表单 Ref
const buttonPermissionList = ref<any>([]) // 按钮权限列表

const dialogHeight = window.innerHeight

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
      buttonPermissionList.value = []
      if (formData.value.buttonPermission) {
        buttonPermissionList.value = JSON.parse(formData.value.buttonPermission)
      }
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
    // 按钮权限组装
    if (buttonPermissionList.value) {
      formData.value.buttonPermission = JSON.stringify(buttonPermissionList.value)
    }
    // if (
    //   formData.value.type === SystemMenuTypeEnum.DIR ||
    //   formData.value.type === SystemMenuTypeEnum.MENU
    // ) {
    //   if (!isExternal(formData.value.path)) {
    //     if (formData.value.parentId === 0 && formData.value.path.charAt(0) !== '/') {
    //       message.error('路径必须以 / 开头')
    //       return
    //     } else if (formData.value.parentId !== 0 && formData.value.path.charAt(0) === '/') {
    //       message.error('路径不能以 / 开头')
    //       return
    //     }
    //   }
    // }
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
  let queryParam = {
    searchStatusAll: true
  }
  const res = await MenuApi.getMenuTreeList(queryParam)
  let menu: Tree = { id: 0, name: t('auto.views.system.menu.MenuForm.k3eaad510'), children: [] }
  // menu.children = handleTree(res)
  menu.children = res
  menuTree.value.push(menu)
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    name: '',
    permission: '',
    buttonPermission: '',
    type: SystemTypeEnum.TYPE_0,
    sort: Number(undefined),
    parentId: 0,
    path: '',
    icon: '',
    component: '',
    componentName: '',
    status: CommonStatusEnum.ENABLE,
    visible: 1,
    keepAlive: 1,
    alwaysShow: 1
  }
  formRef.value?.resetFields()
  buttonPermissionList.value = []
}

/** 判断 path 是不是外部的 HTTP 等链接 */
const isExternal = (path: string) => {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * 新增/修改-按钮权限
 * @param isUpdate
 */
const openBtnForm = (isUpdate: boolean, btnData: object, updateIndex: number) => {
  btnSaveTypeIsUpdate.value = isUpdate
  btnDialogTitle.value = isUpdate
    ? t('auto.views.system.menu.MenuForm.k32bacf76')
    : t('auto.views.system.menu.MenuForm.kfbce260e')
  btnFormData.value = {
    btnName: '',
    btnPermission: ''
  }
  if (isUpdate) {
    btnFormData.value = btnData
    btnSaveUpdateIndex.value = updateIndex
  }
  btnDialogVisible.value = true
}

/**
 * 保存按钮权限
 */
const btnSubmitForm = () => {
  if (!btnFormData.value.btnName || !btnFormData.value.btnPermission) {
    message.error(t('auto.views.system.menu.MenuForm.kb68a072c'))
    return
  }
  if (btnSaveTypeIsUpdate.value) {
    buttonPermissionList.value[btnSaveUpdateIndex.value] = btnFormData.value
  } else {
    buttonPermissionList.value.push(btnFormData.value)
  }
  btnDialogVisible.value = false
}

/**
 * 删除按钮权限
 * @param index
 */
const deleteRow = (index: number) => {
  buttonPermissionList.value.splice(index, 1)
}
</script>
