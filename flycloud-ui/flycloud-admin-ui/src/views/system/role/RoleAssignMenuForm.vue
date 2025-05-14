<template>
  <ElDialog
    v-model="dialogVisible"
    title="菜单权限"
    width="90%"
    top="3vh"
    style="height:96%; margin-bottom: -80px"
  >

    <el-divider content-position="right" style="margin: -5px 0 20px 0">
      <el-button :disabled="formLoading" type="primary" round @click="submitForm">确 定</el-button>
    </el-divider>

    <el-form ref="formRef" v-loading="formLoading" :model="formData" label-width="80px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="角色名称">
            <el-tag>{{ formData.name }}</el-tag>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="角色标识">
            <el-tag>{{ formData.code }}</el-tag>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="菜单权限">
        <el-card class="w-full" shadow="never">
          <template #header>
            全选/全不选:
            <el-switch
              v-model="treeNodeAll"
              active-text="是"
              inactive-text="否"
              inline-prompt
              @change="handleCheckedTreeNodeAll"
            />
            <span style="margin-left: 20px">全部展开/折叠: </span>
            <el-switch
              v-model="menuExpand"
              active-text="展开"
              inactive-text="折叠"
              inline-prompt
              @change="handleCheckedTreeExpand"
            />
          </template>

          <el-tree
            class="menu-tree"
            ref="treeRef"
            style="height: calc(100vh - 280px); overflow-y: scroll; font-size: 16px; font-weight: bold;"
            :data="treeDataList"
            :props="defaultProps"
            node-key="id"
            default-expand-all
            :expand-on-click-node="false"
          >
            <template #default="{ node, data }">
              <div class="custom-tree-node">

                <span>
                  <Icon :icon="data.icon" style="margin: 10px 12px 0 5px" />
                  {{ node.label }}
                </span>

                <div style="margin-left: 20px">
                  <span v-show="data.buttonPermission" v-for="(item, index) in rolePermissionMap.get(data.id)"  :key="index">
                    <span>{{ item.btnName }}</span>
                    <el-switch
                      v-model="item.checked"
                      style="margin: 0 12px 0 3px"
                      size="large"
                      :active-value="true"
                      :inactive-value="false"
                      @change="handlePermissionChange"
                    />
                  </span>
                </div>
              </div>
            </template>
          </el-tree>
          
        </el-card>
      </el-form-item>
    </el-form>
  </ElDialog>
</template>
<script lang="ts" setup>
import { defaultProps, handleTree } from '@/utils/tree'
import * as RoleApi from '@/api/system/role'
import * as MenuApi from '@/api/system/menu'
import * as PermissionApi from '@/api/system/permission'
import {array} from "vue-types";
import {getRoleMenuTreeList, updateMenuPermission} from "@/api/system/role";

defineOptions({ name: 'SystemRoleAssignMenuForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formData = reactive({
  id: undefined,
  name: '',
  code: '',
  roleMenuPermissionJson: ''
})
const formRef = ref() // 表单 Ref
const treeDataList = ref<any[]>([]) // 菜单树形结构
const menuExpand = ref(true) // 展开/折叠
const treeRef = ref() // 菜单树组件 Ref
const treeNodeAll = ref(false) // 全选/全不选
// const rolePermissionList = ref(false) // 角色拥有的权限列表
const rolePermissionMap = ref(new Map<number, any[]>()); // 角色菜单和按钮权限map


/** 打开弹窗 */
const open = async (row: RoleApi.RoleVO) => {

  // 设置数据
  resetForm()
  formData.id = row.id
  formData.name = row.name
  formData.code = row.code

  dialogVisible.value = true
  // 查询角色菜单列表（里面已返回权限）
  let resData = await RoleApi.getRoleMenuTreeList(row.id)
  treeDataList.value = resData
  handleRoleMenuData(resData)

  // 根据角色拥有的菜单和按钮设置选中
  // formLoading.value = true
  // try {
  //   formData.value.menuIds = await PermissionApi.getRoleMenuList(row.id)
  //   formData.value.menuIds.forEach((menuId: number) => {
  //     treeRef.value.setChecked(menuId, true, false)
  //   })
  // } finally {
  //   formLoading.value = false
  // }
}

/**
 * 处理角色菜单数据（把递归数据拆成普通数据，因为按钮权限是自定义独立的）
 * @param resData
 */
const handleRoleMenuData = (resData: any []) => {

  resData.forEach(item => {
    if (item.buttonPermission) {
      rolePermissionMap.value.set(item.id, JSON.parse(item.buttonPermission))
      if (item.children) {
        handleRoleMenuData(item.children)
      }
    }
  })
}

defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  const roleMenuPermissionList = []
  rolePermissionMap.value.forEach((value, key) => {
    if (value) {
      value.forEach(item => {
        if (item.checked) {
          const roleMenuPermission = {
            menuId: key,
            permission: item.btnPermission
          }
          roleMenuPermissionList.push(roleMenuPermission)
        }
      })
    }
  })
  if (roleMenuPermissionList.length < 1) {
    message.error('请至少选择一项菜单或按钮权限！')
    return
  }
  // 提交请求
  formLoading.value = true
  try {
    const data = {
      id: formData.id,
      roleMenuPermissionJson: JSON.stringify(roleMenuPermissionList)
    }
    await RoleApi.updateMenuPermission(data)
    message.success(t('common.updateSuccess'))
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  // 重置选项
  treeNodeAll.value = false
  menuExpand.value = true
  // 重置表单
  formData.value = {
    id: undefined,
    name: '',
    code: '',
    roleMenuPermissionJson: []
  }
  treeRef.value?.setCheckedNodes([])
  formRef.value?.resetFields()
}


/**
 * 选择权限
 * @param data
 */
const handlePermissionChange = (data) => {
  // let data2 = rolePermissionMap.value.get(1)
  // alert(JSON.stringify(data2))
}


/** 全选/全不选 */
const handleCheckedTreeNodeAll = (flag) => {
  // treeRef.value.setCheckedNodes(treeNodeAll.value ? treeDataList.value : [])
  if (flag) {
    rolePermissionMap.value.forEach((value, key) => {
      if (value) {
        value.forEach(item => {
          item.checked = true
        })
      }
    })
  } else {
    rolePermissionMap.value.forEach((value, key) => {
      if (value) {
        value.forEach(item => {
          item.checked = false
        })
      }
    })
  }
}

/** 展开/折叠全部 */
const handleCheckedTreeExpand = () => {
  const nodes = treeRef.value?.store.nodesMap
  for (let node in nodes) {
    if (nodes[node].expanded === menuExpand.value) {
      continue
    }
    nodes[node].expanded = menuExpand.value
  }
}
</script>

<style lang="scss" scoped>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
/* 增大Element Plus Tree组件的行高 */
:deep .menu-tree .el-tree-node__content {
  /*  line-height: 50px;*/
  height: 40px;
  font-size: 16px;
  font-weight: bold;
}
</style>
